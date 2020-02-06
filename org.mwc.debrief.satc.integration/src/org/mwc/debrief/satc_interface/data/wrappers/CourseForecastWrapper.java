
package org.mwc.debrief.satc_interface.data.wrappers;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import com.planetmayo.debrief.satc.model.contributions.CourseForecastContribution;

public class CourseForecastWrapper extends ContributionWrapper
{

	public static class CourseInfo extends EditorType
	{

		public CourseInfo(CourseForecastWrapper data)
		{
			super(data, "Course Forecast", "CourseForecast");
		}

		public PropertyDescriptor[] getPropertyDescriptors()
		{
			try
			{
				final PropertyDescriptor[] res =
				{ 
						prop("Name", "the Name of this leg", FORMAT),
						displayProp("_Start", "Start date", "the start date of this leg", FORMAT),
						displayProp("End", "End date", "the finish date of this leg", FORMAT),
						displayProp("MinCourse", "Minimum course", "the minimum course", SPATIAL),
						displayProp("MaxCourse", "Maximum course", "the maximum course", SPATIAL)
						
				};

				return res;
			}
			catch (final IntrospectionException e)
			{
				return super.getPropertyDescriptors();
			}
		}

	}

	private final CourseForecastContribution _courseF;

	public CourseForecastWrapper(CourseForecastContribution contribution)
	{
		super(contribution);
		_courseF = contribution;
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
			_myEditor = new CourseInfo(this);

		return _myEditor;
	}
	
	
	public double getMinCourse()
	{
		return (int) Math.toDegrees(_courseF.getMinCourse());
	}

	public void setMinCourse(double minCourse)
	{
		_courseF.setMinCourse(Math.toRadians(minCourse));
	}

	public double getMaxCourse()
	{
		return (int) Math.toDegrees(_courseF.getMaxCourse());
	}

	public void setMaxCourse(double maxCourse)
	{
		_courseF.setMaxCourse(Math.toRadians(maxCourse));
	}

}
