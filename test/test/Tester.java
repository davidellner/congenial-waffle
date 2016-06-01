/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.davidellner.shottracker.utilities.DbAccess;
import com.davidellner.shottracker.utilities.DbUtilities;
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
        
        
        
        JSONArray fullAttemptJSON = DbAccess.getFullAttemptJSON();
        System.out.println(fullAttemptJSON.toString());
    }
}
