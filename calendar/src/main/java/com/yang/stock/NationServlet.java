package com.yang.stock;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yang.util.ConnectionUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class NationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        String m = req.getParameter("m");

        ConnectionUtil<Nation> connectionUtil = new ConnectionUtil<>();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        try {
            if ("listAll".equals(m)){
                resp.getWriter().append( gson.toJson(connectionUtil.query("select * from nation ",Nation.class)));
            }else if ("byName".equals(m)){
                String nationName = req.getParameter("nationName");
                resp.getWriter().append(gson.toJson(connectionUtil.query("select * from nation where notionName like '%?%'",Nation.class,nationName)));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}
