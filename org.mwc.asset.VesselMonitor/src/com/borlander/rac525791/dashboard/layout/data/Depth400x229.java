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

package com.borlander.rac525791.dashboard.layout.data;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import com.borlander.rac525791.dashboard.layout.ControlUIModel;

class Depth400x229 implements ControlUIModel {
	
	public Point getControlCenter() {
		return new Point(320, 147);
	}

	public Dimension getUnitsAndMultipliersSize() {
		return new Dimension(34, 13);
	}

	public Point getUnitsPosition() {
		return new Point(-35, 21);
	}

	public int getRedSectorRadius() {
		return 51;
	}
	
	public Dimension getZeroMark() {
		return new Dimension(-35, -45);
	}

	public Dimension getMaximumMark() {
		return new Dimension(14, 55);
	}
	
	public boolean isFullCircleMapped() {
		return false;
	}
	
	public Dimension getValueTextSize() {
		return new Dimension(23, 21);
	}
	
	public Point getValueTextPosition() {
		return new Point(285 - 320, 137 - 147);
	}

}
