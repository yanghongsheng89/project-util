package com.yang.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConnectionUtil<T> {

    private static Properties resource = ResourceUtil.getProperty("resource");

    private Logger logger = Logger.getLogger(this.getClass().getName());

    static {
        try {
            Class.forName(resource.getProperty("driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private Connection getConnection() throws SQLException {

        return DriverManager.getConnection(resource.getProperty("url"),resource);
    }

    private void close(Connection conn, PreparedStatement preparedStatement, ResultSet resultSet ){
        if (conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                if (preparedStatement != null){
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }finally {
                        if (resultSet != null){
                            try {
                                resultSet.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
    public int update(String sql,Object...obj) throws SQLException{
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        if (obj != null) {
            for (int i = 0; i < obj.length; i++) {
                preparedStatement.setObject(i+1,obj[i]);
            }
        }
        int rows = preparedStatement.executeUpdate();
        close(connection,preparedStatement,null);
        return rows;
    }

    public int save(T t)  throws SQLException{
        Connection connection = getConnection();
        String className = t.getClass().getName();
        byte[] byteClassName = className.substring(className.lastIndexOf(".") + 1).getBytes();
        byteClassName[0] += 32;
        StringBuilder buffer = new StringBuilder();
        buffer.append("insert into ").
                append(new String(byteClassName))
                .append("(");

        Method[] declaredMethods = t.getClass().getDeclaredMethods();
        Field[] declaredFields = t.getClass().getDeclaredFields();
        List<String> listKey = new ArrayList<>();
        Map<String,String> nameSet =  new HashMap<>();
        for (Field declaredField : declaredFields) {
            String name = declaredField.getName();
            byte[] bytes = name.getBytes();
            bytes[0] -= 32;
            nameSet.put("get" + new String(bytes), name);
            listKey.add(name);
        }
        Map<String,Object> parameters = new HashMap<>();

        for (Method declaredMethod : declaredMethods) {
            String methodName = declaredMethod.getName();
            if (nameSet.containsKey(methodName)) {
                try {
                    Object invoke = declaredMethod.invoke(t);
                    if (invoke != null){
                        parameters.put(nameSet.get(methodName), invoke);
                    }else {
                        listKey.remove(nameSet.get(methodName));
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();
        for (String s : listKey) {
            sb.append(s).append(",");
            sb1.append("?,");
        }
        buffer.append(sb.subSequence(0,sb.lastIndexOf(",")))
                .append(")values(")
                .append(sb1.subSequence(0,sb1.lastIndexOf(","))).append(")");
        logger.log(Level.CONFIG,buffer.toString());
        System.err.println(buffer.toString());
        PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
        for (int i = 0; i < listKey.size(); i++) {
            preparedStatement.setObject(i+1,parameters.get(listKey.get(i)));
        }

        int rows = preparedStatement.executeUpdate();
        close(connection,preparedStatement,null);
        return rows;
    }

    public List<T> query(String sql,Class<T> clazz, Object...obj) throws SQLException{
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        if (obj != null) {
            for (int i = 0; i < obj.length; i++) {
                preparedStatement.setObject(i+1,obj[i]);
            }
        }

        ResultSet resultSet = preparedStatement.executeQuery();

        List<T> list = new ArrayList<>();
        Set<String> columnLabelSet = new HashSet<>();

        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            String columnLabel = resultSetMetaData.getColumnLabel(i + 1);
            columnLabelSet.add(columnLabel);
        }

        Field[] fields = clazz.getDeclaredFields();
        Method[] methods = clazz.getDeclaredMethods();

        Map<String,String> map = new HashMap<>();
        getFieldMap(fields, map);
        List<Method> methodList = new ArrayList<>();
        for (Method method : methods) {
            String name = method.getName();
            if (map.containsKey(name)){
                methodList.add(method);
            }
        }
        while (resultSet.next()){
            T t = getT(resultSet,methodList.toArray(new Method[]{}),map, clazz,columnLabelSet);
            list.add(t);
        }
        close(connection,preparedStatement,resultSet);
        return list;
    }

    private void getFieldMap(Field[] fields, Map<String, String> map) {
        for (Field field : fields) {
            String fieldName = field.getName();
            byte[] bytes = fieldName.getBytes();
            bytes[0] -= 32;
            map.put("set"+new String(bytes),fieldName);
        }
    }

    public T queryOne(String sql,Class<T> clazz, Object...obj) throws SQLException{
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        if (obj != null) {
            for (int i = 0; i < obj.length; i++) {
                preparedStatement.setObject(i+1,obj[i]);
            }
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        T t = null;
        if (resultSet.next()&&resultSet.isFirst()&&resultSet.isLast()){

            Set<String> columnLabelSet = new HashSet<>();

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                String columnLabel = resultSetMetaData.getColumnLabel(i + 1);
                columnLabelSet.add(columnLabel);
            }
            Field[] fields = clazz.getDeclaredFields();
            Method[] methods = clazz.getDeclaredMethods();

            Map<String,String> map = new HashMap<>();
            getFieldMap(fields, map);
            List<Method> methodList = new ArrayList<>();
            for (Method method : methods) {
                String name = method.getName();
                if (map.containsKey(name)){
                    methodList.add(method);
                }
            }
            t = getT(resultSet,methodList.toArray(new Method[]{}),map, clazz,columnLabelSet);
        }else if (resultSet.isFirst() &&(!resultSet.isLast())){
            close(connection,preparedStatement,resultSet);
            throw new RuntimeException("多于一个值");
        }
        close(connection,preparedStatement,resultSet);
        return t;
    }

    private T getT(ResultSet resultSet,Method[] methods ,Map<String,String> map, Class<T> aClass,Set<String> columnLabelSet) throws SQLException {
        T t = null;
        try {
            t = aClass.newInstance();

            for (Method method : methods) {
                String name = method.getName();
                if (map.containsKey(name)&&columnLabelSet.contains(map.get(name))){
                    method.invoke(t,resultSet.getObject(map.get(name)));
                }
            }
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }


}