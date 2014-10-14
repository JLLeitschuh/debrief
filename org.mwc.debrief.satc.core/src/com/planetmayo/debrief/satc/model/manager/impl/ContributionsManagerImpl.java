/*
 *    Debrief - the Open Source Maritime Analysis Application
 *    http://debrief.info
 *
 *    (C) 2000-2014, PlanetMayo Ltd
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package com.planetmayo.debrief.satc.model.manager.impl;

import java.util.ArrayList;
import java.util.List;

import com.planetmayo.debrief.satc.model.contributions.ATBForecastContribution;
import com.planetmayo.debrief.satc.model.contributions.BaseContribution;
import com.planetmayo.debrief.satc.model.contributions.ContributionBuilder;
import com.planetmayo.debrief.satc.model.contributions.CourseAnalysisContribution;
import com.planetmayo.debrief.satc.model.contributions.CourseForecastContribution;
import com.planetmayo.debrief.satc.model.contributions.LocationAnalysisContribution;
import com.planetmayo.debrief.satc.model.contributions.LocationForecastContribution;
import com.planetmayo.debrief.satc.model.contributions.SpeedAnalysisContribution;
import com.planetmayo.debrief.satc.model.contributions.SpeedForecastContribution;
import com.planetmayo.debrief.satc.model.contributions.StraightLegForecastContribution;
import com.planetmayo.debrief.satc.model.manager.IContributionsManager;

public class ContributionsManagerImpl implements IContributionsManager
{	
	private List<ContributionBuilder> availableContributions;

	@Override
	public synchronized List<ContributionBuilder> getAvailableContributions()
	{
		if (availableContributions == null)
		{
			availableContributions = new ArrayList<ContributionBuilder>();
			availableContributions.add(new ContributionBuilder("Course Forecast")
			{

				@Override
				public BaseContribution create()
				{
					return new CourseForecastContribution();
				}
			});
			availableContributions.add(new ContributionBuilder("Speed Forecast")
			{
				@Override
				public BaseContribution create()
				{
					return new SpeedForecastContribution();
				}
			});
			availableContributions.add(new ContributionBuilder("Location Forecast")
			{
				@Override
				public BaseContribution create()
				{
					return new LocationForecastContribution();
				}
			});
			availableContributions.add(new ContributionBuilder("Location Analysis")
			{
				@Override
				public BaseContribution create()
				{
					return new LocationAnalysisContribution();
				}
			});
			availableContributions.add(new ContributionBuilder("Speed Analysis")
			{
				@Override
				public BaseContribution create()
				{
					return new SpeedAnalysisContribution();
				}
			});
			availableContributions.add(new ContributionBuilder("Course Analysis")
			{
				@Override
				public BaseContribution create()
				{
					return new CourseAnalysisContribution();
				}
			});
			availableContributions.add(new ContributionBuilder("Straight Leg Forecast")
			{
				@Override
				public BaseContribution create()
				{
					return new StraightLegForecastContribution();
				}
			});
			availableContributions.add(new ContributionBuilder("ATB Forecast")
			{
				@Override
				public BaseContribution create()
				{
					return new ATBForecastContribution();
				}
			});
		}
		return new ArrayList<ContributionBuilder>(availableContributions);		
	}	
}
