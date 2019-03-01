package com.yang.test;

import com.yang.event.Event;
import com.yang.util.ConnectionUtil;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class ConnectionUtilTest {

    ConnectionUtil<Event> eventConnectionUtil = new ConnectionUtil<>();
    @Test
    public void testEventSave(){
//        try {
//
//            Event event = new Event();
//            event.setTitle("util title");
//            event.setRemark("util remark");
//            event.setCreateBy("admin");
//
//            for (int i=0;i<100;i++){
//                long timeMillis = System.currentTimeMillis();
//                Timestamp timestamp = new Timestamp(timeMillis);
//                Timestamp end = new Timestamp(timeMillis+1000*60*60*(i+1));
//                event.setCreateAt(timestamp);
//                event.setStart(timestamp);
//                event.setEnd(end);
//                eventConnectionUtil.save(event);
//                Thread.sleep(1000);
//            }
//            // create table  event(createAt datetime,createBy varchar(64),title varchar(128),remark varchar(1024),start datetime,end datetime,id bigint primary key auto_increment)
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }


    @Test
    public void testQueryOne(){
        try {
            Event event = eventConnectionUtil.queryOne("select * from event where id= ?",Event.class,400);
            if (event != null){
                System.err.println(event.getTitle());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testQueryList(){
        try {
            List<Event> eventList = eventConnectionUtil.query("select * from event",Event.class);
            System.err.println(eventList.size());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}