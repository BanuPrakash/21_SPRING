package com.adobe.rentalapp.util;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateUtil {
    public Date dateFromString(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            return sdf.parse(strDate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  null;
    }
}
