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
import java.util.List;

public class StockServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);
        ConnectionUtil<Stock> connectionUtil = new ConnectionUtil<>();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        try {
            List<Stock> stockList = connectionUtil.query("select * from stock where code regexp '^sh6|^sz0'", Stock.class);
            String stockListJson = gson.toJson(stockList);
            resp.getWriter().append(stockListJson);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
