/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.davidellner.shottracker.utilities;

/**
 *
 * @author davidellner
 */
public class QueryFailureException extends Exception {
    /**
     *
     * @param s
     */
    public QueryFailureException(String s){
        
        super(s);
    
    }
    
}
