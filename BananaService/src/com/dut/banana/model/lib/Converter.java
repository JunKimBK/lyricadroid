package com.dut.banana.model.lib;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public final class Converter {
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
    
    public static Date stringToDate(String dateInString) {
    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    	Date date = null;
    	try {
			date = formatter.parse(dateInString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return date;
    }
    
    public static String dateToString(Date date) {
    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    	return formatter.format(date);
    }
}