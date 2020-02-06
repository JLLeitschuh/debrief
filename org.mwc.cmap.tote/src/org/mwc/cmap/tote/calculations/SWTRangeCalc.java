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

package org.mwc.cmap.tote.calculations;

import java.text.NumberFormat;

import Debrief.Tools.Tote.*;
import MWC.GenericData.HiResDate;
import MWC.GenericData.Watchable;
import MWC.Utilities.TextFormatting.DebriefFormatDateTime;

/**
 * @author ian.mayo
 *
 */
public class SWTRangeCalc implements toteCalculation
{

	public String update(final Watchable primary, final Watchable secondary, final HiResDate thisTime)
	{
		return "updated at:" + DebriefFormatDateTime.toStringHiRes(thisTime);
	}

	public double calculate(final Watchable primary, final Watchable secondary, final HiResDate thisTime)
	{
		return -1;
	}

	public void setPattern(final NumberFormat format)
	{
	
	}

	public String getTitle()
	{
		return "calc test:range";
	}

	public String getUnits()
	{
		return "n/a";
	}

	public boolean isWrappableData()
	{
		return false;
	}

}
