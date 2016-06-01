/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.davidellner.shottracker.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author davidellner
 */
public class StringUtilities {
    /**
     * Takes current date, separates month, day, and year with underscores and adds a file extension
     * @param extension - Extension of a file, usually a .txt
     * @return File name
     */
    public static String dateToFilename(String extension) {
        Date tempDate = new Date();
        return tempDate.getMonth() + "_" + tempDate.getDay() + "_" + tempDate.getYear() + "." + extension;
    }
    
    /**
    * Checks if a String varialbe consists only of numbers.  For example, if a String variable contains "1234" (only numbers),
    * this method will return "true".  If a String variable contains even one non-numeric character - "123x4" - this method
    * will return false. 
    * @param str - String variable - we need to check if it can be converted to a number without an error
    * @return boolean (true/false)
     */
    public static boolean isNumeric(String str) {
        try {
		double d = Double.parseDouble(str);
	} 
	catch (NumberFormatException nfe) {
		return false;
	}
	return true;
    }

    
    /**
     * Replaces dangerous characters in SQL strings to prevent injections
     * @param data - a string parameter used to concatenate SQL queries
     * @return String with dangerous characters escaped or replaced
     */
    public static String cleanMySqlInsert(String data) {
        String cleanData = data.replace("  ", " ");
        //cleanData = cleanData.replace("\\", "\\\\");
        cleanData = cleanData.replace("'", "\\'");
        return cleanData;
    }

    /**
     * Encodes password using MD5 encryption
     * @param password - user-provided password
     * @return encoded string
     */
    public static String encodePassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());

            byte byteData[] = md.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(StringUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    /**
     * Checks if a UUID (a globally unique identifier - GUID) is in correct and valid format
     * @param guid - a UUID in string format - the value that we need to validate
     * @return  true if valid, false if invalid
     */
    public static boolean isValidGUID(String guid){
        UUID uuid = UUID.fromString(guid);
        if(guid.equals(uuid.toString())){
            return true;
        }
        return false;
    }
    
}
