package com.upgrad.FoodOrderingApp.service.common;

import org.springframework.stereotype.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  This is utility class
 */
@Component
public class Utility {
    /**
     *  This method validates if rating return by user is valid or not..
     * @param cutomerRating
     * @return
     */
    public boolean isValidRating(String cutomerRating){
        if(cutomerRating.equals("5.0")){
            return true;
        }
        Pattern p = Pattern.compile("[1-4].[0-9]");
        Matcher m = p.matcher(cutomerRating);
        return (m.find() && m.group().equals(cutomerRating));
    }

}

