/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.davidellner.shottracker.core;

import java.util.ArrayList;

/**
 *
 * @author davidellner
 */
public class ShotAttempts {
    
    private ArrayList<ShotAttempt> blocks;
    private ArrayList<ShotAttempt> misses;
    private ArrayList<ShotAttempt> goals;
    private ArrayList<ShotAttempt> shots;
    
    public ShotAttempts(){
         this.blocks = new ArrayList();
         this.misses = new ArrayList();
         this.goals = new ArrayList();
         this.shots = new ArrayList();
    }
    
    public void add(ShotAttempt sh){
        
        if(sh.getAttempttype().equals(ShotAttempt.ATTEMPT_TYPE_SHOT)){
            this.shots.add(sh);
        } else if(sh.getAttempttype().equals(ShotAttempt.ATTEMPT_TYPE_BLOCK)){
            this.blocks.add(sh);
        } else if(sh.getAttempttype().equals(ShotAttempt.ATTEMPT_TYPE_MISS)){
            this.misses.add(sh);
        } else if(sh.getAttempttype().equals(ShotAttempt.ATTEMPT_TYPE_GOAL)){
            this.goals.add(sh);
        } 
        
    }
    
}
