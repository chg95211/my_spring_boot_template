package com.zsk.template.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-16 22:57
 **/
public class DateUtil
{
    private DateUtil()
    {
    }

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String formatDate2Str(Date date, String formmat)
    {
        SimpleDateFormat format = new SimpleDateFormat(formmat);
        return format.format(date);
    }

    public static String formatDate2Str(Date date)
    {
        return formatDate2Str(date, DEFAULT_DATE_FORMAT);
    }


    public static Date formatString2Date(String dateStr, String format)
    {
        SimpleDateFormat formater = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        Date date = null;
        try
        {
            date = formater.parse(dateStr);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return date;
    }

    public static Date formatString2Date(String dateStr)
    {
        return formatString2Date(dateStr, DEFAULT_DATE_FORMAT);
    }

    public static String formatLocalDateTime2Str(LocalDateTime localDateTime, String format)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return formatter.format(localDateTime);
    }

    public static String formatLocalDateTime2Str(LocalDateTime localDateTime)
    {
        return formatLocalDateTime2Str(localDateTime, DEFAULT_DATE_FORMAT);
    }

    public static LocalDateTime formatString2LocalDateTime(String dateStr, String format)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(dateStr,formatter);
    }

    public static LocalDateTime formatString2LocalDateTime(String dateStr)
    {
        return formatString2LocalDateTime(dateStr,DEFAULT_DATE_FORMAT);
    }


    public static Date localDateTime2Date(LocalDateTime dateTime)
    {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime date2LocalDateTime(Date date)
    {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }


    public static Instant date2Instant(Date date)
    {
        return date.toInstant();
    }


    public static Date instant2Date(Instant instant)
    {
        return Date.from(instant);
    }

    public static LocalDateTime instant2LocalDateTime(Instant instant)
    {
        return LocalDateTime.ofInstant(instant,  ZoneId.systemDefault());
    }

    public static Instant localDateTime2Instant(LocalDateTime dateTime)
    {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    public static Duration durationBetweenInstant(Instant instant1, Instant instant2)
    {
        return Duration.between(instant1, instant2);
    }

    public static Period periodBetweenInstant(LocalDate date1, LocalDate date2)
    {
        return Period.between(date1,date2);
    }

    public static LocalDate plus(LocalDate date, int day)
    {
        return date.plus(day, ChronoUnit.DAYS);
    }

    public static LocalDate minus(LocalDate date, int day)
    {
        return date.minus(day, ChronoUnit.DAYS);
    }

    public static String timestampToLocalDateTimeStr(Timestamp value)
    {
       return formatLocalDateTime2Str(value.toLocalDateTime());
    }
}
