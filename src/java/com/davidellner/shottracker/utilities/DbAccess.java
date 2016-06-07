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
        } finally{
            db.close();
        }
        
        return json;
    }
    private static String getFullAttemptSummary(){
        String sql = "SELECT * FROM nhl.attemptlocationview";
        return sql;
    }
    
    public static JSONArray getAttemptSummaryJSONForPlayer(String firstName, String lastName, String team){
        DbUtilities db = new DbUtilities();
        JSONArray json = null;
        try {
            json = db.getJsonDataTable(getAttemptSummaryByPlayer(firstName, lastName, team));
        } catch (SQLException ex) {
            ErrorLogger.log(ex.toString());
        } catch (JSONException ex) {
            ErrorLogger.log(ex.toString());
        } finally{
            db.close();
        }
        return json;
    }
    
    private static String getAttemptSummaryByPlayer(String firstName, String lastName, String team){
        return "SELECT eventtype, shottype, x, y FROM nhl.attemptlocationview WHERE firstname = '" + firstName + "' AND lastname = '" + lastName + "' AND teamname = '" + team + "'";
    }
    
    public static ResultSet getAttemptSummaryResultSetByPlayerAndLocation(String firstName, String lastName, String team, int minx, int miny, int maxx, int maxy){
    
        DbUtilities db = new DbUtilities();
        ResultSet rs = null;
        try{
            rs = db.getResultSet(getAttemptSummaryByPlayerAndLocation(firstName, lastName, team, minx, miny, maxx, maxy));
            
        } catch (SQLException ex) {
           ErrorLogger.log(ex.getMessage());
        } finally {
            //db.close();
        }
      return rs;
    }
    
    
    private static String getAttemptSummaryByPlayerAndLocation(
            String firstName, String lastName, String team, int minx, int miny, int maxx, int maxy){
        return getAttemptSummaryByPlayer(firstName, lastName, team) 
                + " AND x >= " + minx
                + " AND y >= " + miny
                + " AND x <= " + maxx
                + " AND y <= " + maxy;
    }
    
    
}
