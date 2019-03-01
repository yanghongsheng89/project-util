package com.yang.test;

import com.yang.event.Event;
import com.yang.util.ConnectionUtil;
import com.yang.util.EventConst;
import com.yang.util.ResourceUtil;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.Calendars;
import net.fortuna.ical4j.util.UidGenerator;
import net.fortuna.ical4j.util.Uris;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ICSTest {



    @Test
    public void test01(){
//        try {
//            importFile("HOLIDAY_URL_CN");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParserException e) {
//            e.printStackTrace();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void test02(){
//        try {
//            importFile("HOLIDAY_URL_US");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParserException e) {
//            e.printStackTrace();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }


    @Test
    public void exportFile() {
        try {
            // 创建一个时区（TimeZone）
            TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
            TimeZone timezone = registry.getTimeZone("Asia/Shanghai");
            VTimeZone tz = timezone.getVTimeZone();

            // 创建日历
            Calendar calendar = new Calendar();
            calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
            calendar.getProperties().add(Version.VERSION_2_0);
            calendar.getProperties().add(CalScale.GREGORIAN);

            // 时间主题
            String summary = "重复事件测试";
            // 开始时间
            DateTime start = new DateTime(1478016000000l);
            // 开始时间转换为UTC时间（UTC ＋ 时区差 ＝ 本地时间 ）
            start.setUtc(true);
            // 结束时间
            DateTime end = new DateTime(1478016000000l);
            // 结束时间设置成UTC时间（UTC ＋ 时区差 ＝ 本地时间 ）
            end.setUtc(true);
            // 新建普通事件
            // VEvent event = new VEvent(start, end, summary);
            // 定义全天事件（注意默认是UTC时间）
            VEvent event = new VEvent(new Date(1478016000000l), new Date(1478016000000l), summary);
            event.getProperties().add(new Location("南京堵路"));
            // 生成唯一标示
            event.getProperties().add(new Uid(new UidGenerator("iCal4j").generateUid().getValue()));
            // 添加时区信息
            event.getProperties().add(tz.getTimeZoneId());
            // 添加邀请者
            Attendee dev1 = new
                    Attendee(URI.create("mailto:dev1@mycompany.com"));
            dev1.getParameters().add(Role.REQ_PARTICIPANT);
            dev1.getParameters().add(new Cn("Developer 1"));
            event.getProperties().add(dev1);
            // 重复事件
            Recur recur = new Recur(Recur.WEEKLY, Integer.MAX_VALUE);
            recur.getDayList().add(WeekDay.MO);
            recur.getDayList().add(WeekDay.TU);
            recur.getDayList().add(WeekDay.WE);
            recur.getDayList().add(WeekDay.TH);
            recur.getDayList().add(WeekDay.FR);
            RRule rule = new RRule(recur);
            event.getProperties().add(rule);
            // 提醒,提前10分钟
            VAlarm valarm = new VAlarm(new Dur(0, 0, -10, 0));
            valarm.getProperties().add(new Summary("Event Alarm"));
            valarm.getProperties().add(Action.DISPLAY);
            valarm.getProperties().add(new Description("Progress Meeting at 9:30am"));
            // 将VAlarm加入VEvent
            event.getAlarms().add(valarm);
            // 添加事件
            calendar.getComponents().add(event);
            // 验证
            calendar.validate();
//            FileOutputStream fout = new FileOutputStream("D://2.ics");
//            CalendarOutputter outputter = new CalendarOutputter();
//            outputter.output(calendar, fout);
            System.err.println(calendar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void importFile(String key) throws IOException, ParserException, URISyntaxException, SQLException {


        URI uri = Uris.create(ResourceUtil.getProperty("resource").getProperty(key));
        Calendar calendar = Calendars.load(uri.toURL());
//        FileInputStream fis = new FileInputStream(ResourceUtil.getProperty("resource").getProperty("holidayURL"));
//        CalendarBuilder build = new CalendarBuilder();
//        Calendar calendar = build.build(fis);
        for (Iterator i = calendar.getComponents(Component.VEVENT).iterator(); i.hasNext();) {
            VEvent event = (VEvent) i.next();
            Event e = new Event();
            e.setColor(EventConst.COLOR_HOLIDAY);
            e.setCategoryId(EventConst.CATEGORY_ID_1);
            e.setStart(new Timestamp(event.getStartDate().getDate().getTime()));
            // 开始时间
            System.out.println("开始时间：" + event.getStartDate().getValue());
            // 结束时间
            e.setEnd(new Timestamp(event.getEndDate().getDate().getTime()));
            System.out.println("结束时间：" + event.getEndDate().getValue());
            if (null != event.getProperty("DTSTART")) {
                ParameterList parameters = event.getProperty("DTSTART").getParameters();
                if (null != parameters.getParameter("VALUE")) {
                    System.out.println(parameters.getParameter("VALUE").getValue());
                }
            }
            // 主题
            System.out.println("主题：" + event.getSummary().getValue());
            e.setTitle(event.getSummary().getValue());
            // 地点
            if (null != event.getLocation()) {
                System.out.println("地点：" + event.getLocation().getValue());
            }
            // 描述
            if (null != event.getDescription()) {
                System.out.println("描述：" + event.getDescription().getValue());
            }
            e.setRemark("");

            // 创建时间
            if (null != event.getCreated()) {
                System.out.println("创建时间：" + event.getCreated().getValue());
            }
            e.setCreateAt(new Timestamp(event.getCreated().getDate().getTime()));
            // 最后修改时间
            if (null != event.getLastModified()) {
                System.out.println("最后修改时间：" + event.getLastModified().getValue());
            }
            e.setCreateBy("admin");
            // 重复规则
            if (null != event.getProperty("RRULE")) {
                System.out.println("RRULE:" + event.getProperty("RRULE").getValue());
            }
            // 提前多久提醒
            for (Iterator alrams = event.getAlarms().iterator(); alrams.hasNext();) {
                VAlarm alarm = (VAlarm) alrams.next();
                Pattern p = Pattern.compile("[^0-9]");
                String aheadTime = alarm.getTrigger().getValue();
                Matcher m = p.matcher(aheadTime);
                int timeTemp = Integer.valueOf(m.replaceAll("").trim());
                if (aheadTime.endsWith("W")) {
                    System.out.println("提前多久：" + timeTemp + "周");
                } else if (aheadTime.endsWith("D")) {
                    System.out.println("提前多久：" + timeTemp + "天");
                } else if (aheadTime.endsWith("H")) {
                    System.out.println("提前多久：" + timeTemp + "小时");
                } else if (aheadTime.endsWith("M")) {
                    System.out.println("提前多久：" + timeTemp + "分钟");
                } else if (aheadTime.endsWith("S")) {

                    System.out.println("提前多久：" + timeTemp + "秒");
                }
            }
            // 邀请人
            if (null != event.getProperty("ATTENDEE")) {
                ParameterList parameters = event.getProperty("ATTENDEE").getParameters();
                System.out.println(event.getProperty("ATTENDEE").getValue().split(":")[1]);
                System.out.println(parameters.getParameter("PARTSTAT").getValue());
            }
            ConnectionUtil<Event> connectionUtil = new ConnectionUtil<>();
            connectionUtil.save(e);
            System.out.println("----------------------------");
        }
    }
}