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

package org.mwc.cmap.gridharness.data.samples;

import java.awt.Color;

import org.mwc.cmap.gridharness.data.WorldLocation;
import org.mwc.cmap.gridharness.data.WorldSpeed2;

import MWC.GUI.TimeStampedDataItem;
import MWC.GenericData.HiResDate;

/**
 * @author Administrator
 * 
 */
public class Position implements TimeStampedDataItem {

	private HiResDate _myTime;

	private double _course;

	private WorldSpeed2 _speed;

	private WorldLocation _location = WorldLocation.NULL;
	
	private Color _color;

	public Position() {
	}

	public Position(final HiResDate time, final double latitude, 
			final double longitude, final double course, final WorldSpeed2 speed, Color color) {
		_myTime = time;
		_course = course;
		_location = new WorldLocation(latitude, longitude);
		_speed = speed;
		_color = color;
	}

	public String toString() {
		return "Position:" + _myTime.toString();
	}

	public HiResDate getDTG() {
		return _myTime;
	}

	public HiResDate getTime() {
		return _myTime;
	}

	public void setTime(final HiResDate time) {
		_myTime = time;
	}

	public double getLatitude() {
		return _location.getLatitude();
	}

	public void setLatitude(final double latitude) {
		this._location = new WorldLocation(latitude, _location.getLongitude());
	}

	public double getLongitude() {
		return _location.getLongitude();
	}

	public void setLongitude(final double _longitude) {
		this._location = new WorldLocation(_location.getLatitude(), _longitude);
	}

	public void setLocation(final WorldLocation location) {
		this._location = location;
	}

	public WorldLocation getLocation() {
		return this._location;
	}

	public double getCourse() {
		return _course;
	}

	public void setCourse(final double _course) {
		this._course = _course;
	}

	public WorldSpeed2 getSpeed() {
		return _speed;
	}

	public void setSpeed(final WorldSpeed2 _speed) {
		this._speed = _speed;
	}

	public void setDTG(final HiResDate newTime) {
		this._myTime = newTime;
	}

	public void setColor(Color color)
	{
		_color = color;
	}
	
	@Override
	public Color getColor()
	{
		return _color;
	}

}
