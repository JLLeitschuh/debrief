
package org.mwc.debrief.satc_interface.data.wrappers;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import com.planetmayo.debrief.satc.model.contributions.BaseContribution;

public class StraightLegWrapper extends ContributionWrapper
{

	public static class StraightLegInfo extends EditorType
	{

		public StraightLegInfo(StraightLegWrapper data)
		{
			super(data, "Straight Leg", "Straight Leg");
		}

		public PropertyDescriptor[] getPropertyDescriptors()
		{
			try
			{
				final PropertyDescriptor[] res =
				{ 
						prop("Name", "the Name of this leg", FORMAT),
						displayProp("_Start", "Start", "the start date of this leg", FORMAT),
						prop("End", "the finish date of this leg", FORMAT)
				};

				return res;
			}
			catch (final IntrospectionException e)
			{
				return super.getPropertyDescriptors();
			}
		}

	}

	public StraightLegWrapper(BaseContribution contribution)
	{
		super(contribution);
	}

	@Override
	public boolean hasEditor()
	{
		return true;
	}

	@Override
	public EditorType getInfo()
	{
		if (_myEditor == null)
			_myEditor = new StraightLegInfo(this);

		return _myEditor;
	}

}
