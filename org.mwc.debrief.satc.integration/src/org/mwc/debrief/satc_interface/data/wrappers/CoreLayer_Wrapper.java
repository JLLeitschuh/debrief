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
package org.mwc.debrief.satc_interface.data.wrappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import org.eclipse.core.runtime.Status;

import MWC.GUI.CanvasType;
import MWC.GUI.Editable;
import MWC.GUI.ExcludeFromRightClickEdit;
import MWC.GUI.Layer;
import MWC.GUI.Plottable;
import MWC.GUI.Plottables.IteratorWrapper;
import MWC.GenericData.WorldArea;
import MWC.GenericData.WorldLocation;
import MWC.Utilities.TextFormatting.FormatRNDateTime;

import com.planetmayo.debrief.satc.model.contributions.BaseContribution;
import com.planetmayo.debrief.satc.model.contributions.CoreMeasurementContribution;
import com.planetmayo.debrief.satc.model.contributions.CoreMeasurementContribution.CoreMeasurement;
import com.planetmayo.debrief.satc_rcp.SATC_Activator;

@SuppressWarnings("rawtypes")
abstract public class CoreLayer_Wrapper<Contribution extends CoreMeasurementContribution<?>, 
  Measurement extends CoreMeasurementContribution.CoreMeasurement,
  Wrapper extends CoreLayer_Wrapper.CoreMeasurementWrapper> extends ContributionWrapper implements Layer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/** the set of measurements that we strore 
	 * 
	 */
	protected Collection<Editable> _myElements;


	public CoreLayer_Wrapper(BaseContribution contribution)
	{
		super(contribution);
	}

	@Override
	final public void add(Editable point)
	{
		SATC_Activator.log(Status.ERROR,
				"Should not be adding items to this layer", null);
	}
	

	@Override
	public Enumeration<Editable> elements()
	{
		if (_myElements == null)
		{
			// wrap the measurements
			_myElements = new ArrayList<Editable>();

			final CoreMeasurementContribution<Measurement> bmc =  getBMC();
			final ArrayList<Measurement> meas = bmc.getMeasurements();
			final Iterator<Measurement> iter = meas.iterator();
			while (iter.hasNext())
			{
				final CoreMeasurement thisM = iter.next();
				addThis(thisM);
			}
		}

		return new IteratorWrapper(_myElements.iterator());
	}

	
	/** wrap this object appropriately, and store it
	 * 
	 * @param thisM
	 */
	abstract protected void addThis(CoreMeasurement thisM);

	@Override
	final public void append(Layer other)
	{
	}

	@Override
	final public void exportShape()
	{
	}

	@SuppressWarnings("unchecked")
	protected CoreMeasurementContribution<Measurement> getBMC()
	{
		return  (CoreMeasurementContribution<Measurement>) super.getContribution();
	}

	@Override
	final public int getLineThickness()
	{
		return 0;
	}

	@Override
	final public boolean hasEditor()
	{
		return true;
	}
	

	@Override
	final public boolean hasOrderedChildren()
	{
		return true;
	}

	@Override
	final public void removeElement(Editable point)
	{

	}

	@Override
	final public void setName(String val)
	{
		super.getContribution().setName(val);
	}

	public int size()
	{
		return getBMC().getNumObservations();
	}

	abstract public class CoreMeasurementWrapper implements Plottable, ExcludeFromRightClickEdit
	{

		protected final CoreMeasurement _myMeas;

		public CoreMeasurementWrapper(CoreMeasurement measurement)
		{
			_myMeas = measurement;
		}

		@Override
		public int compareTo(Plottable arg0)
		{
			return 0;
		}

		public Boolean getActive()
		{
			return _myMeas.isActive();
		}

		@Override
		public WorldArea getBounds()
		{
			return null;
		}

		@Override
		public String getName()
		{
			return FormatRNDateTime.toString(_myMeas.getDate().getTime());
		}

		@Override
		public boolean getVisible()
		{
			return getActive();
		}

		@Override
		public boolean hasEditor()
		{
			return true;
		}

		@Override
		public void paint(CanvasType dest)
		{
		
		}

		@Override
		public double rangeFrom(WorldLocation other)
		{
			return INVALID_RANGE;
		}

		public void setActive(Boolean active)
		{
			_myMeas.setActive(active);
		
			// fire hard constraints changed
			getContribution().fireHardConstraintsChange();
		}

		@Override
		public void setVisible(boolean val)
		{
			setActive(val);
		}

		@Override
		public String toString()
		{
			return getName();
		}
		
	}
}
