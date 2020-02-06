
package com.planetmayo.debrief.satc.model.contributions;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.planetmayo.debrief.satc.model.ModelObject;
import com.planetmayo.debrief.satc.model.legs.CoreRoute;
import com.planetmayo.debrief.satc.model.legs.LegType;
import com.planetmayo.debrief.satc.model.states.BaseRange.IncompatibleStateException;
import com.planetmayo.debrief.satc.model.states.ProblemSpace;
import com.planetmayo.debrief.satc.model.states.State;

public abstract class BaseContribution extends ModelObject implements
		Comparable<BaseContribution>
{
	protected static final int MEASUREMENT_DEFAULT_SCORE = 100;
	protected static final int FORECAST_DEFAULT_SCORE = 200;
	protected static final int ANALYST_DEFAULT_SCORE = 300;
		
	private static final long serialVersionUID = 1L;

	public static final String WEIGHT = "weight";
	public static final String START_DATE = "startDate";
	public static final String NAME = "name";
	public static final String FINISH_DATE = "finishDate";
	public static final String ACTIVE = "active";
	public static final String ESTIMATE = "estimate";

	/* note: most contributions don't provide a color property
	 * it's just straight legs, really
	 */
	public static final String COLOR = "COLOR";


	public static final String HARD_CONSTRAINTS = "hardConstraint";

	protected String name;
	protected boolean active = true;
	private int weight = 5;
	protected Date startDate;
	protected Date finishDate;

	// custom interface for contributions that have their own color
	public static interface HasColor
	{
		public Color getColor();
	}
	
	protected BaseContribution()
	{
		// give a default name - we can't just rely on the DTG,
		// because the composite straight leg editor generates
		// several at once
		setName("auto_id:" + new Date().getTime() + "+_" + (int)(Math.random() * 1000d));
	}

	/**
	 * apply this contribution to the supplied Problem Space
	 * 
	 * @param space
	 *          the object that we're going to bound
	 */
	public abstract void actUpon(ProblemSpace space)
			throws IncompatibleStateException;

	/**
	 * some external factor has changed, that has affected the hard constraints on
	 * this contribution
	 */
	public void fireHardConstraintsChange()
	{
		firePropertyChange(HARD_CONSTRAINTS, null, this);
	}

	/**
	 * generate the error for this route
	 * 
	 */
	final public double calculateErrorScoreFor(CoreRoute route)
	{
		double res = 0;

		// make sure we're allowed to calc an error score
		if (active)
			if (weight > 0)
			{
				// make sure there's something to decide the score on
				if (route != null)
					if (route.getStates() != null)
						if (route.getStates().size() > 0)
						{
							res = cumulativeScoreFor(route) / (10 - Math.min(weight, 9));
						}
			}
		// ok, done.
		return res;
	}

	/**
	 * calculate the cumulative error score for this route
	 * 
	 * @param route
	 * @return
	 */
	protected double cumulativeScoreFor(CoreRoute route)
	{
		double res = 0;

		// ok, go for it
		ArrayList<State> states = route.getStates();
		Iterator<State> sIter = states.iterator();

		// keep track of how many errors we generate
		int _errCtr = 0;
		
		// for forecasts on straight, we re-use the error score,
		// since it's the same for every state
		Double wholeLegScore = null;

		// ok. work through the states that comprise this leg
		while (sIter.hasNext())
		{
			State thisState = sIter.next();
			double delta = 0;

			Date time = thisState.getTime();

			// check if our time period relates to this time
			boolean isValid = true;

			// check the time values
			if (this.getStartDate() != null)
				if (this.getStartDate().after(time))
					isValid = false;
			if (this.getFinishDate() != null)
				if (this.getFinishDate().before(time))
					isValid = false;

			if (isValid)
			{
				// ok, everything matches up = calculate this error
				
				// do we have a whole-leg-score?
				if(wholeLegScore == null)
				{
					// nope, better calculate it.
					delta = calcError(thisState);
				}
				else
				{
					delta = wholeLegScore;
				}
				
				// store the error against the state
				thisState.setScore(this, delta);

				// and accumulate it
				res += delta;

				// remember that we've increased the num of error values
				_errCtr++;

				// hey, SPECIAL CASE! If we're on a straight leg, and this is a
				// forecast, then we
				// only use the one calculation
				if (route.getType().equals(LegType.STRAIGHT)
						&& this.getDataType().equals(ContributionDataType.FORECAST))
				{
					wholeLegScore = delta;
				}
			}

		}

		// return 0 if no states were processed or mean error otherwise
		return _errCtr == 0 ? 0 : res / _errCtr;
	}

	/**
	 * calculate the error value for this particular state
	 * 
	 * @param thisState
	 * @return
	 */
	protected double calcError(State thisState)
	{
		return 0;
	}

	/**
	 * are my constraints valid for the supplied period?
	 * 
	 * @param route
	 * @return
	 */
	protected boolean validFor(CoreRoute route)
	{
		return true;
	}

	/**
	 * check if this specified time is between our start/finish times, if we have
	 * them
	 * 
	 * @param thisDate
	 *          the date we're checking
	 * @return
	 */
	protected boolean checkInDatePeriod(final Date thisDate)
	{
		long millis = thisDate.getTime();
		if (getStartDate() != null && millis < getStartDate().getTime())
		{
			return false;
		}
		if (getFinishDate() != null && millis > getFinishDate().getTime())
		{
			return false;
		}
		return true;
	}
	
	protected int compareEqualClass(BaseContribution o) 
	{
		return this.getName().compareTo(o.getName());
	}

	@Override
	public int compareTo(BaseContribution o)
	{
		// ok, what type am I?
		int myScore = getSortOrder();
		int hisScore = o.getSortOrder();
		if (myScore == hisScore)
		{
			// try the class names first, to group them
			String myClass = this.getClass().getName();
			String hisClass = o.getClass().getName();
			if (myClass.equals(hisClass))
			{
				// ha-they must be equal, compare the names
				return compareEqualClass(o);
			}
			else
			{
				return myClass.compareTo(hisClass);
			}
		}
		return myScore - hisScore;
	}

	public abstract ContributionDataType getDataType();

	public Date getFinishDate()
	{
		return finishDate;
	}

	public String getName()
	{
		return name;
	}

	/** determine the order in which the contributions are displayed in the View listing
	 * 
	 * @return
	 */
	protected int getSortOrder()
	{
		switch (getDataType())
		{
		case MEASUREMENT:
			return MEASUREMENT_DEFAULT_SCORE;
		case FORECAST:
			return FORECAST_DEFAULT_SCORE;
		default:
			return ANALYST_DEFAULT_SCORE;
		}
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public int getWeight()
	{
		return weight;
	}

	public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean isActive)
	{
		boolean oldActive = active;
		this.active = isActive;
		firePropertyChange(ACTIVE, oldActive, isActive);
	}

	public void setFinishDate(Date newFinishDate)
	{
		Date oldFinishDate = finishDate;
		this.finishDate = newFinishDate;
		firePropertyChange(FINISH_DATE, oldFinishDate, newFinishDate);
	}

	public void setName(String newName)
	{
		String oldName = this.name;
		this.name = newName;
		firePropertyChange(NAME, oldName, newName);
	}

	public void setStartDate(Date newStartDate)
	{
		Date oldStartDate = startDate;
		this.startDate = newStartDate;
		firePropertyChange(START_DATE, oldStartDate, newStartDate);
	}

	public void setWeight(int newWeight)
	{
		int oldWeight = weight;
		this.weight = newWeight;
		firePropertyChange(WEIGHT, oldWeight, newWeight);
	}
}