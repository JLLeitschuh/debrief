/*******************************************************************************
 * Debrief - the Open Source Maritime Analysis Application
 * http://debrief.info
 *  
 * (C) 2000-2020, Deep Blue C Technology Ltd
 *  
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html)
 *  
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *******************************************************************************/

package com.borlander.rac525791.dashboard;

import org.eclipse.draw2d.geometry.Dimension;

import com.borlander.rac525791.dashboard.rotatable.AngleMapper;

public class ArcAngleMapper implements AngleMapper {
	
	private final int myFirstValue;
	private final int mySecondValue;
	private final boolean mySelectBiggestSector;
	
	private double myFirstAngle;
	private double mySecondAngle;
	
	public ArcAngleMapper(int minValue, int maxValue, boolean selectBiggestSector){
		myFirstValue = minValue;
		mySecondValue = maxValue;
		mySelectBiggestSector = selectBiggestSector;
	}
	
	public double computeAngle(double value){
		double alpha = (value - myFirstValue) / (mySecondValue - myFirstValue);
		return myFirstAngle + alpha * (mySecondAngle - myFirstAngle);
	}
	
	public void setAnglesRange(Dimension minDirection, Dimension maxDirection) {
		double firstAngle = Math.atan2(minDirection.height, minDirection.width);
		double secondAngle = Math.atan2(maxDirection.height, maxDirection.width);
		
		boolean isOnBiggestSector = (secondAngle - firstAngle > Math.PI);
		if (isOnBiggestSector != mySelectBiggestSector){
			if (firstAngle > secondAngle){
				secondAngle += Math.PI * 2;
			} else {
				secondAngle -= Math.PI * 2;
			}
		}
		myFirstAngle = firstAngle;
		mySecondAngle = secondAngle;
	}

}
