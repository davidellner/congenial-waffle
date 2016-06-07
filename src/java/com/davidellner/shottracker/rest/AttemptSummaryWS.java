/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.davidellner.shottracker.rest;

import com.davidellner.shottracker.utilities.DbAccess;
import com.davidellner.shottracker.utilities.ErrorLogger;
import com.davidellner.shottracker.utilities.PolygonProcessor;
import java.awt.Point;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 *
 * @author davidellner
 */
@WebServlet(name = "attemptsummaryjson", urlPatterns = {"/rest/attemptsummaryjson"})
public class AttemptSummaryWS extends HttpServlet {
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
           String firstName = request.getParameter("firstName");
           String lastName = request.getParameter("lastName");
           String team = request.getParameter("team");
           
           
           
           
           JSONArray attempts = null;
           if(firstName == null || lastName == null || team  == null){
               //what do?
               attempts = null;
           } else {
               //attempts = DbAccess.getAttemptSummaryJSONForPlayer(firstName, lastName, team);
               attempts = new JSONArray();
               //tracking min/max x and y will reduce the amount of effort needed to search through each shot
                int minx = Integer.parseInt(request.getParameter("minx"));
                int miny = Integer.parseInt(request.getParameter("miny"));
                int maxx = Integer.parseInt(request.getParameter("maxx"));
                int maxy = Integer.parseInt(request.getParameter("maxy"));

                //need error handling

                String c = request.getParameter("coords");
                String[] coords = c.split(",");

                ArrayList<Point> coordinates = new ArrayList();
                Point p;
                int x;
                int y;
                float[] tx = new float[coords.length];
                float[] ty = new float[coords.length];
                
                for(int i = 0; i < coords.length; i = i + 2){
                    
                    x = Integer.parseInt(coords[i].replace("\"", ""));
                    y = Integer.parseInt(coords[i + 1].replace("\"", ""));
                    
                    tx[(i / 2)] = x;
                    ty[(i / 2)] = y;
                    p = new Point(x, y);
                    System.out.println(p);
                    coordinates.add(p);
                    
                }
                
                PolygonProcessor polygon = new PolygonProcessor((coordinates.size()-1), tx, ty);
                ResultSet rs = DbAccess.getAttemptSummaryResultSetByPlayerAndLocation(firstName, lastName, team, minx, miny, maxx, maxy);
                //need to refactor this so we can close the db connection. db access shouldn't return a resultset
                
                String eventtype;
                String shottype;
                int shotx;
                int shoty;
                JSONObject json;
                
                while(rs.next()){
                    shotx = rs.getInt("x");
                    shoty = rs.getInt("y");
                    
                    if(polygon.isPointInPolygon(shotx, shoty)){
                        json = new JSONObject();
                        json.put("type", rs.getString("eventtype"));
                        json.put("shottype", rs.getString("shottype"));
                        json.put("x", shotx);
                        json.put("y", shoty);
                        attempts.put(json);
                    }
                }
           }
           
           StringWriter sw = new StringWriter();
           attempts.write(sw);
           out.println(sw);
           } catch (JSONException ex) {
            ErrorLogger.log(ex.toString());
        } catch (SQLException ex) {
            ErrorLogger.log(ex.toString());
        }
        } 
     /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
