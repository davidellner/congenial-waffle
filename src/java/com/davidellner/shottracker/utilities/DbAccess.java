/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.davidellner.shottracker.utilities;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author davidellner
 * How to use this class: Add a method to return each 
 */
public class DbAccess {
    public static final String DB_EVENT_TYPE_MISS = "MISS";
    public static final String DB_EVENT_TYPE_GOAL = "GOAL";
    public static final String DB_EVENT_TYPE_BLOCK = "BLOCK";
    public static final String DB_EVENT_TYPE_SHOT = "SHOT";
    
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
    
    public static JSONArray getAttemptSummaryJSONByPlayerAndLocation(String firstName, String lastName, String team, int minx, int miny, int maxx, int maxy, PolygonProcessor polygon) throws SQLException{
                JSONArray attempts = new JSONArray();
                ResultSet rs = DbAccess.getAttemptSummaryResultSetByPlayerAndLocation(firstName, lastName, team, minx, miny, maxx, maxy);
                //need to refactor this so we can close the db connection. db access shouldn't return a resultset
                
                String eventtype;
                String shottype;
                int shotx;
                int shoty;
                int miss;
                int shot;
                int block;
                JSONObject json;
                
                while(rs.next()){
                    shotx = rs.getInt("x");
                    shoty = rs.getInt("y");
                    
                    if(polygon.isPointInPolygon(shotx, shoty)){
                        try {
                            json = new JSONObject();
                            json.put("type", rs.getString("eventtype"));
                            json.put("shottype", rs.getString("shottype"));
                            json.put("x", shotx);
                            json.put("y", shoty);
                            attempts.put(json);
                        } catch (JSONException ex) {
                            ErrorLogger.log(ex.toString());
                        }
                    }
                }
         return attempts;    
    }
    public static JSONArray getAttemptAggregationJSONByPlayerAndLocation(String firstName, String lastName, String team, int minx, int miny, int maxx, int maxy, PolygonProcessor polygon) throws SQLException{
                JSONArray attempts = new JSONArray();
                ResultSet rs = DbAccess.getAttemptSummaryResultSetByPlayerAndLocation(firstName, lastName, team, minx, miny, maxx, maxy);
                //need to refactor this so we can close the db connection. db access shouldn't return a resultset
                
                String eventtype;
                String shottype;
                int shotx;
                int shoty;
                JSONObject json;
                
                
                int blocks = 0;
                int misses = 0;
                int shots = 0;
                int goals = 0;
                
                int wrist = 0;
                int snap = 0;
                int slap = 0;
                int tip = 0;
                
                int debug = 0;
                int backhand = 0;
                while(rs.next()){
                    shotx = rs.getInt("x");
                    shoty = rs.getInt("y");
                    debug++;
                    if(polygon.isPointInPolygon(shotx, shoty)){
                        try {
                            
                            eventtype = rs.getString("eventtype");
                            shottype = rs.getString("shottype");
                            
                            switch(eventtype){
                                case DB_EVENT_TYPE_MISS: misses++; break;
                                case DB_EVENT_TYPE_SHOT: shots++; break;
                                case DB_EVENT_TYPE_BLOCK: blocks++; break;
                                case DB_EVENT_TYPE_GOAL: goals++; break;
                            }

                        } catch (Exception ex) {
                            ErrorLogger.log(ex.toString());
                        }
                    }
                    else{
                        //debug
                        
                        //System.out.println("not in polygon: x = " + shotx + " and y = " + shoty);
                    }
                }
                //System.out.println(debug);
               JSONObject obj = new JSONObject();
        try {
            obj.put(DB_EVENT_TYPE_SHOT, shots)
               .put(DB_EVENT_TYPE_MISS, misses)
               .put(DB_EVENT_TYPE_BLOCK, blocks)
               .put(DB_EVENT_TYPE_GOAL, goals);
            
            //build json array. TODO: add more data to array
            
            attempts.put(obj);
        } catch (JSONException ex) {
            ErrorLogger.log(ex.toString());
        }
         return attempts;    
    }
    public static ResultSet getAttemptSummaryResultSetByPlayerAndLocation(String firstName, String lastName, String team, int minx, int miny, int maxx, int maxy){
    
        DbUtilities db = new DbUtilities();
        ResultSet rs = null;
        try{
            rs = db.getResultSet(getAttemptSummaryByPlayerAndLocation(firstName, lastName, team, minx, miny, maxx, maxy));
            //System.out.println(getAttemptSummaryByPlayerAndLocation(firstName, lastName, team, minx, miny, maxx, maxy));
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
