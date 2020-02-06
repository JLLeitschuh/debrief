
package org.mwc.cmap.gridharness.data.samples;

import java.awt.Color;

import org.mwc.cmap.gridharness.data.WorldDistance2;

import MWC.GUI.TimeStampedDataItem;
import MWC.GenericData.HiResDate;


/**
 * @author Administrator
 * 
 */
public class Observation implements TimeStampedDataItem {

	private HiResDate _myTime;

	private double _bearing;

	private WorldDistance2 _range;

	private Color _color;

	public Observation() {
	}

	public Observation(final HiResDate dtg, final double bearing, final WorldDistance2 range, final Color color) {
		_myTime = dtg;
		_bearing = bearing;
		_range = range;
		_color = color;
	}

	public String toString() {
		return "Observation:" + _myTime.toString();
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

	public double getBearing() {
		return _bearing;
	}

	public void setBearing(final double _bearing) {
		this._bearing = _bearing;
	}

	public WorldDistance2 getRange() {
		return _range;
	}

	public void setRange(final WorldDistance2 _range) {
		this._range = _range;
	}

	public void setDTG(final HiResDate newTime) {
		this._myTime = newTime;
	}
	
	public void setColor(final Color color)
	{
		_color = color;
	}

	@Override
	public Color getColor()
	{
		return _color;
	}


}
