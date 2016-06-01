/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.davidellner.shottracker.utilities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author davidellner
 * How to use this class: Add a method to return each 
 */
public class DbAccess {
    
    public static JSONArray getFullAttemptJSON(){
        JSONArray json = new JSONArray();
        DbUtilities db = new DbUtilities();
        
        try {
            ResultSet rs = db.getResultSet(getFullAttemptSummary());
            JSONObject jsonobj = null;
            while(rs.next()){
                jsonobj = new JSONObject();
                jsonobj.put("playername", (rs.getString("firstname")+ "_"+ rs.getString("lastname")));
                jsonobj.put("eventtype", rs.getString("eventtype"));
                jsonobj.put("team", rs.getString("teamname"));
                jsonobj.put("shottype", rs.getString("shottype"));
                jsonobj.put("x", rs.getInt("x"));
                jsonobj.put("y", rs.getInt("y"));
                json.put(jsonobj);
            }
            
        } catch (SQLException | JSONException ex) {
            ErrorLogger.log(ex.toString());
        }
        
        return json;
    }
    private static String getFullAttemptSummary(){
        String sql = "SELECT * FROM nhl.attemptlocationview";
        return sql;
    }
    
    
}