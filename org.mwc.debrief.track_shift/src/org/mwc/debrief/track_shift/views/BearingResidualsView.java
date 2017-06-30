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
package org.mwc.debrief.track_shift.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.mwc.cmap.core.CorePlugin;
import org.mwc.debrief.track_shift.TrackShiftActivator;

import Debrief.Wrappers.Track.ITimeVariableProvider;
import MWC.GenericData.HiResDate;

public class BearingResidualsView extends BaseStackedDotsView implements
    ITimeVariableProvider
{

  public BearingResidualsView()
  {
    super(true, false);
  }

  private static final String SHOW_COURSE = "SHOW_COURSE";
  private static final String SCALE_ERROR = "SCALE_ERROR";
  private Action showCourse;
  private Action relativeAxes;
  private Action scaleError;

  // protected Action _5degResize;

  @Override
  protected void makeActions()
  {
    super.makeActions();

    // now the course action
    scaleError =
        new Action("Show the error when dragging", IAction.AS_CHECK_BOX)
        {
          @Override
          public void run()
          {
            super.run();
          }
        };
    scaleError.setChecked(false);
    scaleError
        .setToolTipText("Show symbol scaled to per-cut error when dragging");
    scaleError.setImageDescriptor(TrackShiftActivator
        .getImageDescriptor("icons/24/scale.png"));

    // see if there's an existing setting for this.

    // now the course action
    relativeAxes =
        new Action("Use +/- 180 scale for absolute data", IAction.AS_CHECK_BOX)
        {
          @Override
          public void run()
          {
            super.run();
            
            final double minVal;
            final double maxVal;
            if(relativeAxes.isChecked())
            {
              minVal = -180d;
              maxVal = 180d;
            }
            else
            {
              minVal = 0d;
              maxVal = 360d;
            }
            
            _overviewCourseRenderer.setRange(minVal, maxVal);

            processShowCourse();
          }
        };
    relativeAxes.setChecked(false);
    relativeAxes.setToolTipText("Use +/- 180 scale for absolute data");
    relativeAxes.setImageDescriptor(TrackShiftActivator
        .getImageDescriptor("icons/24/swap_axis.png"));

    // now the course action
    showCourse = new Action("Show ownship course", IAction.AS_CHECK_BOX)
    {
      @Override
      public void run()
      {
        super.run();

        processShowCourse();
      }
    };
    showCourse.setChecked(true);
    showCourse.setToolTipText("Show ownship course in overview chart");
    showCourse.setImageDescriptor(TrackShiftActivator
        .getImageDescriptor("icons/24/ShowCourse.png"));

    _autoResize = new Action("Auto resize", IAction.AS_CHECK_BOX)
    {
      @Override
      public void run()
      {
        super.run();
        final boolean val = _autoResize.isChecked();
        if (_showDotPlot.isChecked())
        {
          if (val)
          {
            _dotPlot.getRangeAxis().setAutoRange(true);
            _autoResize.setToolTipText("Keep plot sized to show all data");
          }
          else
          {
            _dotPlot.getRangeAxis().setRange(-5, 5);
            _dotPlot.getRangeAxis().setAutoRange(false);
            _autoResize.setToolTipText("Fix bearing range to +/- 5 degs");
          }
        }
      }
    };
    _autoResize.setChecked(true);
    _autoResize.setToolTipText("Keep plot sized to show all data");
    _autoResize.setImageDescriptor(CorePlugin
        .getImageDescriptor("icons/24/fit_to_win.png"));

  }

  @Override
  protected void addExtras(IToolBarManager toolBarManager)
  {
    super.addExtras(toolBarManager);
    toolBarManager.add(showCourse);
  }

  @Override
  protected void fillLocalToolBar(final IToolBarManager manager)
  {
    manager.add(relativeAxes);
    manager.add(scaleError);
    super.fillLocalToolBar(manager);

  }

  protected void fillLocalPullDown(final IMenuManager manager)
  {
    manager.add(relativeAxes);
    manager.add(scaleError);
    super.fillLocalPullDown(manager);
  }

  protected String getUnits()
  {
    return "\u00b0";
  }

  protected String getType()
  {
    return "Bearing";
  }

  protected void updateData(final boolean updateDoublets)
  {
    // update the current datasets
    _myHelper.updateBearingData(_dotPlot, _linePlot, _targetOverviewPlot,
        _myTrackDataProvider, _onlyVisible.isChecked(), showCourse.isChecked(),
        relativeAxes.isChecked(), _holder, this, updateDoublets,
        _targetCourseSeries, _targetSpeedSeries, ownshipCourseSeries,
        targetBearingSeries, targetCalculatedSeries, _overviewSpeedRenderer,
        _overviewCourseRenderer);
  }

  private void processShowCourse()
  {
    // ok - redraw the plot we may have changed the course visibility
    updateStackedDots(false);

    // ok - if we're on auto update, do the update
    updateLinePlotRanges();
  }

  @Override
  public void init(final IViewSite site, final IMemento memento)
      throws PartInitException
  {
    super.init(site, memento);

    if (memento != null)
    {

      final Boolean doCourse = memento.getBoolean(SHOW_COURSE);
      if (doCourse != null)
        showCourse.setChecked(doCourse.booleanValue());

      final Boolean doScaleError = memento.getBoolean(SCALE_ERROR);
      if (doScaleError != null)
        scaleError.setChecked(doScaleError.booleanValue());
    }
  }

  @Override
  public void saveState(final IMemento memento)
  {
    super.saveState(memento);

    memento.putBoolean(SHOW_COURSE, showCourse.isChecked());
    memento.putBoolean(SCALE_ERROR, scaleError.isChecked());
  }

  @Override
  public long getValueAt(HiResDate dtg)
  {
    // get the time
    RegularTimePeriod myTime = new FixedMillisecond(dtg.getMicros() / 1000);

    // get the set of calculated error values that we have stored in the graph
    TimeSeriesCollection dataset = (TimeSeriesCollection) _dotPlot.getDataset();

    if (dataset != null)
    {
      if (dataset.getSeriesCount() > 0)
      {
        TimeSeries series = dataset.getSeries(0);
        int index = series.getIndex(myTime);
        if (index >= 0)
        {
          TimeSeriesDataItem thisV = series.getDataItem(index);
          return thisV.getValue().longValue();
        }
      }
    }
    return 0;
  }

  @Override
  public boolean applyStyling()
  {
    return scaleError.isChecked();
  }
}
