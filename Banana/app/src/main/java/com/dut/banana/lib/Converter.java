package com.dut.banana.lib;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

public final class Converter {
    private static final String ARRAY_SPERATOR = ",";

    public static String toCondition(Object obj) {
        if (obj != null) {
            return "'%" + obj.toString() + "%'";
        } else {
            return "'%'";
        }
    }

    public static String toParam(Object obj) {
        if (obj != null)  {
            return "'" + obj.toString().replace("'", "''") + "'";
        } else {
            return "null";
        }
    }

    public static String showListObj(List listObj) {
        if (listObj != null) {
            return "" + listObj.size();
        } else {
            return "null";
        }
    }

    public static String showObj(Object obj) {
        if (obj != null) {
            return obj.toString();
        } else {
            return "null";
        }
    }

    public static List<String> stringToList(String str) {
        List<String> list = new ArrayList();
        if (null == str)
            return null;
        StringTokenizer tokenizer = new StringTokenizer(str, ARRAY_SPERATOR);
        while (tokenizer.hasMoreTokens())
            list.add(tokenizer.nextToken());
        return list;
    }

    public static long[] stringToListLongs(String str) {
        List<Long> listLong = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(str, ARRAY_SPERATOR);
        while (tokenizer.hasMoreTokens())
            listLong.add(new Long(tokenizer.nextToken()));
        long[] lstl;
        lstl = new long[listLong.size()];
        int i = 0;
        for (Long l : listLong) {
            lstl[i] = Long.valueOf(l);
            ++i;
        }
        return lstl;
    }

    public static String listToString(long[] list) {
        StringBuilder builder = new StringBuilder();
        for (long l : list) {
            builder.append(l);
            builder.append(ARRAY_SPERATOR);
        }
        return builder.toString();
    }
}