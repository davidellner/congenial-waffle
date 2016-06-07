/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.davidellner.shottracker.core;

/**
 *
 * @author davidellner
 */
public class ShotAttempt {
    
    public static final String ATTEMPT_TYPE_SHOT = "SHOT";
    public static final String ATTEMPT_TYPE_BLOCK = "BLOCK";
    public static final String ATTEMPT_TYPE_MISS = "MISS";
    public static final String ATTEMPT_TYPE_GOAL = "GOAL";
    
    private String attempttype;
    private String shottype;
    private int x;
    private int y;

    public ShotAttempt(String attempttype, String shottype, int x, int y) {
        this.attempttype = attempttype;
        this.shottype = shottype;
        this.x = x;
        this.y = y;
    }
    
    /**
     * @return the attempttype
     */
    public String getAttempttype() {
        return attempttype;
    }

    /**
     * @param attempttype the attempttype to set
     */
    public void setAttempttype(String attempttype) {
        this.attempttype = attempttype;
    }

    /**
     * @return the shottype
     */
    public String getShottype() {
        return shottype;
    }

    /**
     * @param shottype the shottype to set
     */
    public void setShottype(String shottype) {
        this.shottype = shottype;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }
    
}
