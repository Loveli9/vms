package com.icss.mvp.util;

import java.util.List;

import com.icss.mvp.entity.ProjectStatus;
import com.icss.mvp.entity.ProjectStatusLighting;

public enum LightUp {
    RED("red"),
    YELLOW("yellow"),
    GREEN("green"),
    NO("no");
    private String color;
    LightUp(String color) {
        this.color = color;
    }
    public static LightUp fromColor(String color){
        for (LightUp lightUp : LightUp.values()) {
            if (lightUp.getColor().equals(color)){
                return lightUp;
            }
        }
        return NO;
    }
    public String getColor() {
        return color;
    }
    
    public static ProjectStatusLighting getColors(List<Integer> vals) {
    	ProjectStatusLighting list = new ProjectStatusLighting();
    	Integer red = 0;
		Integer yellow = 0;
		Integer green = 0;
		Integer gray = 0;
		for (int i=0;i<vals.size();i++) {
			Integer val = vals.get(i);
			if(val == 0) {
				gray++;
			}else if(val>=80) {
				green++;
			}else if(val<70) {
				red++;
			}else {
				yellow++;
			}
		}
		list.setRed(red);
		list.setYellow(yellow);
		list.setGreen(green);
		list.setGray(gray);
    	return list;
    }
}
