/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.davidellner.shottracker.utilities.DbAccess;
import com.davidellner.shottracker.utilities.DbUtilities;
import com.davidellner.shottracker.utilities.PolygonProcessor;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;

/**
 *
 * @author davidellner
 */
public class Tester {
    
    public static void main(String[] args){
        try {
            int polyCorners = 4;
            float[] polyX = {0, 0, 50, 50};
            float[] polyY = {0, 50, 50, 0};
            PolygonProcessor p = new PolygonProcessor(polyCorners, polyX, polyY);
            
            
            
          JSONArray fullAttemptJSON = DbAccess.getAttemptAggregationJSONByPlayerAndLocation("Sidney", "Crosby", "PIT", 0, 0, 40, 40, p);
       
        System.out.println(fullAttemptJSON);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
