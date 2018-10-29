package com.github;


import com.github.jscript.scala.IpV4IntUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class MAIN {

    public static void main(String... args) throws ParseException {
        System.out.println(IpV4IntUtil.int2Ip(113546789));

        String date = "2018-10-09";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
        System.out.println(sdf1.parse(date).getTime());

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
       // sdf2.setTimeZone(TimeZone.getTimeZone("UTC"));
        System.out.println(sdf2.parse(date).getTime());

        LocalDate dateTime = LocalDate.parse(
                "2018-10-09",
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
        );
        ZonedDateTime zoneDate = ZonedDateTime.of(dateTime, LocalTime.of(0, 0, 0), ZoneId.of("Asia/Shanghai"));
       // ZonedDateTime zoneDate = dateTime.(ZoneId.of("Asia/Shanghai"));
        System.out.println(zoneDate.toInstant().toEpochMilli());
//        System.out.println(dateTime.toInstant(ZoneOffset.of("Asia/Shanghai")));
//        System.out.println(dateTime.toInstant(ZoneOffset.of("UTC")));

    }

}
