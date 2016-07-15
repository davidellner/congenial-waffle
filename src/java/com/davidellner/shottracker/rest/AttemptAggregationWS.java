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
@WebServlet(name = "attemptaggregationjson", urlPatterns = {"/rest/attemptaggregationjson"})
public class AttemptAggregationWS extends HttpServlet {
    
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
                int minx = 10000;   //Integer.parseInt(request.getParameter("minx"));
                int miny = 10000;   //Integer.parseInt(request.getParameter("miny"));
                int maxx = -10000;  //Integer.parseInt(request.getParameter("maxx"));
                int maxy = -10000;  //Integer.parseInt(request.getParameter("maxy"));
 
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
                    
                    //set max variables
                    if(x < minx) minx = x;
                    if(x > maxx) maxx = x;
                    if(y < miny) miny = y;
                    if(y > maxy) maxy = y;
                    
                    tx[(i / 2)] = x;
                    ty[(i / 2)] = y;
                    p = new Point(x, y);
                    //System.out.println(p);
                    coordinates.add(p);
                    
                }
                //System.out.println("minx: " + minx + ", maxx: " + maxx + ", miny: " + miny + ", maxy: " + maxy);
                
                PolygonProcessor polygon = new PolygonProcessor((coordinates.size()-1), tx, ty);
                //ResultSet rs = DbAccess.getAttemptSummaryResultSetByPlayerAndLocation(firstName, lastName, team, minx, miny, maxx, maxy);
                //need to refactor this so we can close the db connection. db access shouldn't return a resultset
                attempts = DbAccess.getAttemptAggregationJSONByPlayerAndLocation(firstName, lastName, team, minx, miny, maxx, maxy, polygon);
                
           }
           
           StringWriter sw = new StringWriter();
           attempts.write(sw);
           out.println(sw);
           } catch (JSONException | SQLException ex) {
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