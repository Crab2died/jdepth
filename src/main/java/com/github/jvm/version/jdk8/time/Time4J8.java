package com.github.jvm.version.jdk8.time;

import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * JDK1.8 日期API
 * <p>
 * java 8 给出的日期API都是不可变且线程安全的，这一点完全不同于{@link Date}的
 *
 * @author : Crab2Died
 * 2018/03/15  15:53:52
 */
public class Time4J8 {

    @Test
    public void localTime() {

        // 当前时间
        LocalTime localTime = LocalTime.now();
        System.out.println("Now the time is " + localTime);
        // 2小时后时间
        System.out.println("After 2 hours is " + localTime.plusHours(2));
    }

    @Test
    public void localDate() {

        // 当前日期
        LocalDate localDate = LocalDate.now();
        System.out.println("Today is " + localDate);
        // 年
        int year = localDate.getYear();
        // 月
        int month = localDate.getMonthValue();
        // 日
        int day = localDate.getDayOfMonth();
        System.out.printf("Year: %d, Month: %d, Day: %d \n", year, month, day);

        // 判断闰年
        if (localDate.isLeapYear()) {
            System.out.println("this year is leap year");
        } else {
            System.out.println("this year is not leap year");
        }


        // 指定日期
        LocalDate birthday = LocalDate.of(1999, 9, 9);
        System.out.println("your birthday is " + birthday);

        System.out.println("After 2 years is " + localDate.plusYears(2));

        // 指定Note
        LocalDate note = LocalDate.of(2020, 1, 1);
        // 日期判断
        if (localDate.equals(note)) {
            System.out.println("today is important for you!");
        } else {
            System.out.println("today is not your important day ^.^");
        }
        // 之前
        if (localDate.isBefore(note)) {
            System.out.println("your important moment has not arrived yet.");
        }
    }

    @Test
    public void monthDay() {

        // 生日，月-日
        MonthDay birthday = MonthDay.of(3, 15);

        // 比较判断
        if (birthday.equals(MonthDay.now())) {
            System.out.println("Happy birthday to you!");
        } else {
            System.out.println("hello today is good day");
        }

    }

    @Test
    public void clock() {

        Clock clockUTC = Clock.systemUTC();
        System.out.println("Zone is " + clockUTC.getZone() + ", system millis is " + clockUTC.millis());

        Clock clockDefault = Clock.systemDefaultZone();
        System.out.println("Zone is " + clockDefault.getZone() + ", system millis is " + clockDefault.millis());
    }

    @Test
    public void instant() {

        // 当前时间戳
        Instant now = Instant.now();
        System.out.println("Now is " + now);

        // Instant 转 Date
        Date date = Date.from(now);
        // Date 转 Instant
        Instant date2 = date.toInstant();

        System.out.println(date + " <=> " + date2);
    }

    @Test
    public void formatTime() {

        // 格式化
        LocalDate date = LocalDate.parse("20180312", DateTimeFormatter.BASIC_ISO_DATE);
        System.out.println("20180315 format to date " + date);

        // 自定义格式化
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
        LocalDate formatDate = LocalDate.parse("五月 12 2017", formatter);
        System.out.println("05 12 2017 format to date " + formatDate);

        LocalDateTime dateTime = LocalDateTime.parse(
                "18:10:11 02-12-2016",
                DateTimeFormatter.ofPattern("HH:mm:ss MM-dd-yyyy")
        );
        System.out.println(dateTime);

        // 时间格式化为字符串
        String dateStr = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        System.out.println(dateStr);
    }
}
