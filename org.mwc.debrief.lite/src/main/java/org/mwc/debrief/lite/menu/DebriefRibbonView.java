package org.mwc.debrief.lite.menu;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JLabel;

import org.geotools.swing.JMapPane;
import org.geotools.swing.action.PanAction;
import org.geotools.swing.action.ZoomInAction;
import org.mwc.debrief.lite.gui.FitToWindow;
import org.mwc.debrief.lite.gui.ZoomOut;
import org.mwc.debrief.lite.map.GeoToolMapRenderer;
import org.mwc.debrief.lite.map.RangeBearingAction;
import org.pushingpixels.flamingo.api.common.FlamingoCommand.FlamingoCommandToggleGroup;
import org.pushingpixels.flamingo.api.ribbon.JRibbon;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies.IconRibbonBandResizePolicy;
import org.pushingpixels.flamingo.api.ribbon.resize.RibbonBandResizePolicy;

import MWC.GUI.Layers;

public class DebriefRibbonView
{
  protected static void addViewTab(final JRibbon ribbon,
      final GeoToolMapRenderer _geoMapRenderer, final Layers layers, JLabel statusBar)
  {
    final JRibbonBand viewBand = new JRibbonBand("View", null);
    final JMapPane mapPane = (JMapPane) _geoMapRenderer.getMap();
    final AbstractAction doFit = new FitToWindow(layers, mapPane);

    // group for the mosue mode radio buttons
    final FlamingoCommandToggleGroup mouseModeGroup =
        new FlamingoCommandToggleGroup();

    viewBand.startGroup();
    MenuUtils.addCommandToggleButton("Pan", "images/16/hand.png", new PanAction(mapPane),
        viewBand, RibbonElementPriority.TOP, true, mouseModeGroup, false);
    final ZoomInAction zoomInAction = new ZoomInAction(mapPane);
    MenuUtils.addCommandToggleButton("Zoom In", "images/16/zoomin.png", zoomInAction,
        viewBand, RibbonElementPriority.TOP, true, mouseModeGroup, true);
    final RangeBearingAction rangeAction = new RangeBearingAction(mapPane, statusBar);
    MenuUtils.addCommandToggleButton("Rne/Brg", "images/16/rng_brg.png", rangeAction,
        viewBand, RibbonElementPriority.TOP, true, mouseModeGroup, false);
    
    viewBand.startGroup();
    MenuUtils.addCommand("Zoom Out", "images/16/zoomout.png", new ZoomOut(
        mapPane), viewBand, RibbonElementPriority.TOP);
    MenuUtils.addCommand("Fit to Window", "images/16/fit_to_win.png",
        doFit, viewBand, null);
    final List<RibbonBandResizePolicy> policies = new ArrayList<>();
    policies.add(new CoreRibbonResizePolicies.Mirror(viewBand));
    policies.add(new CoreRibbonResizePolicies.Mid2Low(viewBand));
    policies.add(new IconRibbonBandResizePolicy(viewBand));
    viewBand.setResizePolicies(policies);
    final RibbonTask fileTask = new RibbonTask("View", viewBand);
    ribbon.addTask(fileTask);
  }
}
