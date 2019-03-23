package com.yang.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yang.util.ConnectionUtil;
import com.yang.util.EventConst;
import com.yang.util.ResourceUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EventsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String m = req.getParameter("m");

        ConnectionUtil<Event> connectionUtil = new ConnectionUtil<>();

        String start = req.getParameter("start");
        String end = req.getParameter("end");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        String id = req.getParameter("id");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        PrintWriter writer = resp.getWriter();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if ("list".equals(m)){
            try {
                List<Event> events = connectionUtil.query("select * from event where not (start > ? or end < ?) and controlBit&?=?", Event.class, end, start, EventConst.SHOW_STATUS,EventConst.SHOW_STATUS);
                Properties dayList = ResourceUtil.getProperty("dayList");
                for (Object key : dayList.keySet()) {
                    Event e = new Event();
                    e.setCategoryId(EventConst.CATEGORY_ID_1);
                    e.setColor(EventConst.COLOR_HOLIDAY);
                    e.setTitle((String)dayList.get(key));
                    e.setRemark((String)dayList.get(key));
                    String year = start.substring(0,4);
                    String startDate = year+"-"+key+" 00:00:00";
                    String endDate = year+"-"+key+" 23:59:59";
                    Date parse = simpleDateFormat.parse(startDate);
                    e.setStart(new Timestamp(parse.getTime()));
                    e.setEnd(new Timestamp(simpleDateFormat.parse(endDate).getTime()));
                    e.setControlBit(EventConst.SHOW_STATUS);
                    events.add(e);
                    e.setId(-1l);
                }
                writer.append(gson.toJson(events));
            } catch (SQLException | ParseException e) {
                e.printStackTrace();
            }
        }else if("add".equals(m)){
            Event event = new Event();
            event.setRemark(req.getParameter("remark"));
            event.setTitle(req.getParameter("title"));
            long l = System.currentTimeMillis();
            event.setCreateAt(new Timestamp(l));
            event.setCreateBy("admin");
            event.setCategoryId(EventConst.CATEGORY_ID_2);
            try {
                event.setStart(new Timestamp(simpleDateFormat.parse(start).getTime()));
                event.setEnd(new Timestamp(simpleDateFormat.parse(end).getTime()));
                connectionUtil.save(event);
                writer.append("{\"result\":0}");
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }else if ("del".equals(m)){
            try {
                connectionUtil.update("update event set controlBit = controlBit&? where id = ?",Integer.MAX_VALUE-EventConst.SHOW_STATUS,id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            writer.append("{\"result\":0}");
        }else if ("byId".equals(m)){
            try {
                Event event = connectionUtil.queryOne("select * from event where id = ?", Event.class, id);
                writer.append(gson.toJson(event));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if ("mod".equals(m)){
            try {
                connectionUtil.update("update event set title=?,remark=?,color=?,start=?,end=? where id = ?",req.getParameter("title"),req.getParameter("remark"),EventConst.COLOR_MOD,new Timestamp(simpleDateFormat.parse(start).getTime()),new Timestamp(simpleDateFormat.parse(end).getTime()),id);
                writer.append("{\"result\":0}");
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else if ("don".equals(m)){
            try {
                connectionUtil.update("update event set color = ? where id = ?",EventConst.COLOR_DON,id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            writer.append("{\"result\":0}");
        }

    }

}