/*
 *    Debrief - the Open Source Maritime Analysis Application
 *    http://debrief.info
 *
 *    (C) 2000-2014, PlanetMayo Ltd
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the Eclipse Public License v1.0
 *    (http://www.eclipse.org/legal/epl-v10.html)
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 */
package MWC.TacticalData.GND;

import java.awt.Color;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import MWC.GUI.CanvasType;
import MWC.GUI.Griddable;
import MWC.GUI.Griddable.HasNonBeanPropertyDescriptors;
import MWC.GUI.Plottable;
import MWC.GUI.TimeStampedDataItem;
import MWC.GUI.Tools.SubjectAction;
import MWC.GenericData.HiResDate;
import MWC.GenericData.WorldArea;
import MWC.GenericData.WorldLocation;

public class GDataItem implements Plottable, TimeStampedDataItem,
		HasNonBeanPropertyDescriptors
{
	public static final String LOCATION = "Location";

	
	public static interface Setter
	{
		public void setValue(String name, Object value);
	}
	
	/** the griddable dataset for this class
	 * 
	 * @author ian
	 *
	 */
	public final class GDataInfo extends Griddable
	{

		private NonBeanPropertyDescriptor[] _myGridProps;
		private PropertyDescriptor[] _myInfoPropertyDescriptors;

		public GDataInfo(final GDataItem data)
		{
			super(data, data.getName(), "GND item:" + data.getName());
		}

		@Override
		public final String getDisplayName()
		{
			return getData().toString();
		}

		@Override
		public PropertyDescriptor[] getGriddablePropertyDescriptors()
		{
			return new PropertyDescriptor[0];
		}

		public final BeanInfo[] getAdditionalBeanInfo()
		{
			return null;
		}

		public final PropertyDescriptor[] getPropertyDescriptors()
		{
			if (_myInfoPropertyDescriptors == null)
			{
				try
				{
					_myInfoPropertyDescriptors = new PropertyDescriptor[]
					{ prop("Visible", "whether the whole fix is visible", VISIBILITY) };
				}
				catch (final IntrospectionException ee)
				{
					ee.printStackTrace();
				}
			}
			return _myInfoPropertyDescriptors;
		}

		public final MethodDescriptor[] getMethodDescriptors()
		{
			return null;
		}

		public final SubjectAction[] getUndoableActions()
		{
			return null;
		}

		@Override
		public NonBeanPropertyDescriptor[] getNonBeanGriddableDescriptors()
		{
			final GDataItem gt = (GDataItem) this.getData();
			if (_myGridProps == null)
			{
				final Set<String> keys = gt._myData.keySet();
				final Iterator<String> iter = keys.iterator();
				final ArrayList<NonBeanPropertyDescriptor> list = new ArrayList<NonBeanPropertyDescriptor>();
				while (iter.hasNext())
				{
					String field = (String) iter.next();
					
					// skip the LON field
					if(field.equals(GDataset.LON))
						continue;
					
					// also skip the time field, we handle it separately
					if(field.equals(GDataset.TIME))
						continue;
					// if it's the LAT - convert to location
					if(field.equals(GDataset.LAT))
						field = LOCATION;
					
					final String theField = field;
					
					final NonBeanPropertyDescriptor newItem = new NonBeanPropertyDescriptor()
					{

						@Override
						public String getFieldName()
						{
							return theField;
						}

						@Override
						public Class<?> getDataType()
						{
							Class<?> res;
							if (theField.equals(GDataset.TIME))
								res = Date.class;
							else if (theField.equals(LOCATION))
								res = WorldLocation.class;
							else
								res = Double.class;

							return res;
						}

						@Override
						public HasNonBeanPropertyDescriptors getDataObject()
						{
							return gt;
						}
					};
					list.add(newItem);
				}
				_myGridProps = list.toArray(new NonBeanPropertyDescriptor[]
				{ null });
			}
			return _myGridProps;
		}

	}

	private final HashMap<String, Object> _myData;
	private final String _name;
	private final Setter _setter;
	private final Color _color;

	public GDataItem(final String name, final HashMap<String, Object> fields, final Setter setter,
			Color color)
	{
		_myData = fields;
		_name = name;
		_setter = setter;
		_color = color;
	}

	@Override
	public String getName()
	{
		return _name;
	}

	@Override
	public boolean hasEditor()
	{
		return true;
	}

	@Override
	public EditorType getInfo()
	{
		return new GDataInfo(this);
	}

  @Override
  public int compareTo(final Plottable o)
  {
    int res = 0;
    if (o instanceof GDataItem)
    {
      final GDataItem other = (GDataItem) o;
      res = this.getTime().compareTo(other.getTime());
    }
    
    // have we sorted? 
    if (res == 0)
    {
    	// nope, try the hashcode, as a fallback
    	res = Integer.valueOf(this.hashCode()).compareTo(
    			Integer.valueOf(o.hashCode()));
    }

    return res;
  }


	public HiResDate getTime()
	{
		HiResDate res = null;
		if (_myData.get("time") != null)
		{
			final Date date = (Date) _myData.get("time");
			res = new HiResDate(date);
		}
		return res;
	}

	@Override
	public void paint(final CanvasType dest)
	{
	}

	@Override
	public WorldArea getBounds()
	{
		return null;
	}

	@Override
	public boolean getVisible()
	{
		return true;
	}

	@Override
	public void setVisible(final boolean val)
	{
	}

	@Override
	public double rangeFrom(final WorldLocation other)
	{
		return Plottable.INVALID_RANGE;
	}

	@Override
	public HiResDate getDTG()
	{
		return getTime();
	}

	@Override
	public void setDTG(final HiResDate date)
	{
		// TODO: somehow, handle this!
	}

	public Object getValue(final String fieldName)
	{
		final Object res;
		// special case, is it hte lcoation?
		if(fieldName.equals(LOCATION))
		{
			final WorldLocation loc = new WorldLocation((Double)_myData.get(GDataset.LAT),(Double) _myData.get(GDataset.LON), 0d);
			final Double elevation = (Double) _myData.get(GDataset.ELEVATION);
			if(elevation != null)
				loc.setDepth(-elevation.doubleValue());
			
			res = loc;
		}
		else
			res =_myData.get(fieldName);
		
		return res;
	}

	@Override
	public void setValue(final String fieldName, final Object newVal)
	{
		_myData.put(fieldName, newVal);
		_setter.setValue(fieldName, newVal);
	}


	/** deep copy constructor
	 * 
	 * @return
	 */
	public TimeStampedDataItem makeCopy()
	{
		final HashMap<String, Object> res =new HashMap<String,Object>(_myData);
		return new GDataItem(_name, res, _setter, _color);
	}

	@Override
	public Color getColor()
	{
		return _color;
	}

}