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

package com.borlander.rac525791.dashboard.rotatable;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

public class RedSector extends Shape {
	private static final Color RED_BACKGROUND = new Color(null, 255, 63, 63);
	private static final int ALPHA = 150;
	private final Point myCenter = new Point();
	private final AngleMapper myAngleMapper;
	private int myActualValue;
	private int myDemandedValue;
	private int myRadius;
	private final boolean mySelectSmallestSector;

	public RedSector(AngleMapper angleMapper, boolean selectSmallestSector){
		mySelectSmallestSector = selectSmallestSector;
		myAngleMapper = angleMapper;
		setBackgroundColor(RED_BACKGROUND);
	}
	
	public void setCenterLocation(Point center){
		myCenter.setLocation(center);
		updateBounds();
	}
	
	public void setRadius(int radius){
		myRadius = radius;
		updateBounds();
	}
	
	public void setActualValue(int actualValue){
		myActualValue = actualValue;
	}
	
	public void setDemandedValue(int demandedValue){
		myDemandedValue = demandedValue;
	}
	
	@Override
	protected void outlineShape(Graphics g) {
		//
	}
	
	@Override
	protected void fillShape(Graphics g) {
		if (myActualValue == myDemandedValue){
			return;
		}
		
		g.pushState();
		g.setAlpha(ALPHA);
		
		double actualAngle = myAngleMapper.computeAngle(myActualValue);
		double demandedAngle = myAngleMapper.computeAngle(myDemandedValue);
		
		if (mySelectSmallestSector && Math.abs(actualAngle - demandedAngle) > Math.PI){
			if (actualAngle < demandedAngle){
				actualAngle += 2 * Math.PI;
			} else {
				demandedAngle += 2 * Math.PI;
			}
		}
		int startDegrees = toDegrees(Math.min(actualAngle, demandedAngle));
		int lengthDegrees = toDegrees(Math.abs(actualAngle - demandedAngle));
		
		startDegrees *= -1; //g.fillArc() counts angles counter-clockwise but AngleMapper -- clockwise
		lengthDegrees *= -1; //see above

		g.fillArc(myCenter.x - myRadius, myCenter.y - myRadius, myRadius * 2, myRadius * 2, startDegrees, lengthDegrees);
		
		g.popState();
	}
	
	private void updateBounds(){
		setBounds(new Rectangle(myCenter.x - myRadius, myCenter.y - myRadius, myRadius * 2, myRadius * 2));
	}
	
	private static int toDegrees(double angle){
		return (int)(angle * 180 / Math.PI);
	}

}
