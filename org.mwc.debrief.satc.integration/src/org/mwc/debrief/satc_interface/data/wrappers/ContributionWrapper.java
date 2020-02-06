
package org.mwc.debrief.satc_interface.data.wrappers;

import java.util.Date;

import MWC.GUI.CanvasType;
import MWC.GUI.ExcludeFromRightClickEdit;
import MWC.GUI.Plottable;
import MWC.GUI.Plottables;
import MWC.GenericData.HiResDate;
import MWC.GenericData.WorldArea;
import MWC.GenericData.WorldLocation;

import com.planetmayo.debrief.satc.model.contributions.BaseContribution;

public class ContributionWrapper implements Plottable, ExcludeFromRightClickEdit
{
	final BaseContribution _myCont;
	
	protected EditorType _myEditor;

	public ContributionWrapper(BaseContribution contribution)
	{
		_myCont = contribution;
	}

	public BaseContribution getContribution()
	{
		return _myCont;
	}

	public HiResDate get_Start()
	{
		return new HiResDate(_myCont.getStartDate().getTime());
	}

	public void set_Start(HiResDate start)
	{
		_myCont.setStartDate(start.getDate());
	}

	public HiResDate getEnd()
	{
		return new HiResDate(_myCont.getFinishDate().getTime());
	}

	public void setEnd(HiResDate end)
	{
		_myCont.setFinishDate(end.getDate());
	}

	@Override
	public String toString()
	{
		return getName();
	}

	@Override
	public String getName()
	{
		return _myCont.getName();
	}
	
	public void setName(final String name)
	{
		_myCont.setName(name);
	}

	@Override
	public boolean hasEditor()
	{
		return false;
	}

	@Override
	public EditorType getInfo()
	{
		return null;
	}

	/** we implement our own sorting so that contributions are grouped in chronological order
	 * 
	 */
	@Override
	public int compareTo(Plottable arg0)
	{
		ContributionWrapper him = (ContributionWrapper) arg0;
		
		Date myStart = this.getContribution().getStartDate();
		Date hisStart = him.getContribution().getStartDate();
		
		int res = 0;
		
		if((myStart != null) && (hisStart != null))
		{
			res = myStart.compareTo(hisStart); 
		}
		
		// are they the same time?
		if(res == 0)
		{
			// yes, ok - user their natural ordering instead
			res = this.getContribution().compareTo(him.getContribution());
		}
		
		return res;
	}

	@Override
	public void paint(CanvasType dest)
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
		return _myCont.isActive();
	}

	@Override
	public void setVisible(boolean val)
	{
		_myCont.setActive(val);
	}

	@Override
	public double rangeFrom(WorldLocation other)
	{
		return Plottables.INVALID_RANGE;
	}
}