/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.davidellner.shottracker.utilities;

import java.awt.Point;

/**
 *
 * @author davidellner
 */
public class PolygonProcessor {

    
public static void main(String[] args){
    int polyCorners = 4;
    float[] polyX = {0, 0, 50, 50, 0};
    float[] polyY = {0, 50, 50, 0, 0};
    PolygonProcessor p = new PolygonProcessor(polyCorners, polyX, polyY);
    
    System.out.println(p.isPointInPolygon(50, 30));
}
public PolygonProcessor(int polyCorners, float[] polyX, float[] polyY){
    this.polyCorners = polyCorners;
    this.polyX = polyX;
    this.polyY = polyY;
    this.constant = new float[this.polyX.length];
    this.multiple = new float[this.polyY.length];
    this.precalc_values();
    
}
//  Globals which should be set before calling these functions:
//
//  int    polyCorners  =  how many corners the polygon has (no repeats)
//  float  polyX[]      =  horizontal coordinates of corners
//  float  polyY[]      =  vertical coordinates of corners
//  float  x, y         =  point to be tested
//
//  The following global arrays should be allocated before calling these functions:
//
//  float  constant[] = storage for precalculated constants (same size as polyX)
//  float  multiple[] = storage for precalculated multipliers (same size as polyX)
//
//  (Globals are used in this example for purposes of speed.  Change as
//  desired.)
//
//  USAGE:
//  Call precalc_values() to initialize the constant[] and multiple[] arrays,
//  then call pointInPolygon(x, y) to determine if the point is in the polygon.
//
//  The function will return YES if the point x,y is inside the polygon, or
//  NO if it is not.  If the point is exactly on the edge of the polygon,
//  then the function may return YES or NO.
//
//  Note that division by zero is avoided because the division is protected
//  by the "if" clause which surrounds it.
int polyCorners;
float[] polyX;
float[] polyY;


float[] constant;
float[] multiple;

private void precalc_values() {

  int i;
  int j=polyCorners-1 ;

  for(i=0; i<polyCorners; i++) {
    if(polyY[j]==polyY[i]) {
      constant[i]=polyX[i];
      multiple[i]=0; }
    else {
      constant[i]=polyX[i]-(polyY[i]*polyX[j])/(polyY[j]-polyY[i])+(polyY[i]*polyX[i])/(polyY[j]-polyY[i]);
      multiple[i]=(polyX[j]-polyX[i])/(polyY[j]-polyY[i]); }
    j=i; }}

public boolean isPointInPolygon(int x, int y) {

  int   i, j=polyCorners-1 ;
  boolean  oddNodes = false;

  for (i=0; i<polyCorners; i++) {
    if ((polyY[i]< y && polyY[j]>=y
    ||   polyY[j]< y && polyY[i]>=y)) {
      oddNodes^=(y*multiple[i]+constant[i]<x); }
    j=i; }

  return oddNodes; }

public boolean isPointInPolygon(Point p){
    
    return this.isPointInPolygon(p.x, p.y);

}
}
