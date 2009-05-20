package MWC.GUI.ptplot.jfreeChart.Utils;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.ImageObserver;

import com.jrefinery.legacy.chart.*;
import com.jrefinery.legacy.chart.entity.*;
import com.jrefinery.legacy.chart.tooltips.XYToolTipGenerator;
import com.jrefinery.legacy.chart.urls.XYURLGenerator;
import com.jrefinery.legacy.data.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ian.Mayo
 * Date: Feb 5, 2003
 * Time: 10:59:40 AM
 * To change this template use Options | File Templates.
 */
//////////////////////////////////////////////////
// custom renderer, which uses the specified color for the data series
//////////////////////////////////////////////////
public final class ColourStandardXYItemRenderer extends StandardXYItemRenderer
{

  /** A working line (to save creating thousands of instances). */
  private Line2D workingLine = new Line2D.Double(0.0, 0.0, 0.0, 0.0);

  /**
   * Constructs a new renderer.
   * <p>
   * To specify the type of renderer, use one of the constants: SHAPES, LINES or SHAPES_AND_LINES.
   *
   * @param type  the type of renderer.
   * @param toolTipGenerator  the tooltip generator.
   * @param urlGenerator  the URL generator.
   */
  public ColourStandardXYItemRenderer(final int type, final XYToolTipGenerator toolTipGenerator, final XYURLGenerator urlGenerator) {
    super(type, toolTipGenerator, urlGenerator);
  }

  /**
   * Returns a legend item for a series.
   *
   * @param series  the series (zero-based index).
   *
   * @return a legend item for the series.
   */
  public LegendItem getLegendItem(int series) {

    XYPlot plot = this.getPlot();

    XYDataset dataset = plot.getXYDataset();
    String label = dataset.getSeriesName(series);
    String description = label;
    Shape shape = null;
    Paint paint = getPaint(plot, series, 0,0,0);
    Paint outlinePaint = paint;
    Stroke stroke = plot.getSeriesStroke(series);
    Stroke outlineStroke = plot.getSeriesOutlineStroke(series);

    return new LegendItem(label, description,
                          shape, paint, outlinePaint, stroke, outlineStroke);
  }


  /**
   * Returns the paint used to draw a single data item.
   * <P>
   * If null is returned, the renderer defaults to the paint for the current series.
   *
   * @param plot  the plot (can be used to obtain standard color information etc).
   * @param series  the series index.
   * @param item  the item index.
   * @param x  the x value of the item.
   * @param y  the y value of the item.
   *
   * @return The paint.
   */
  protected final Paint getPaint(final com.jrefinery.legacy.chart.Plot plot,
                                 final int series,
                                 final int item,
                                 final double x,
                                 final double y) {
    Color theColor = null;

    final Dataset data = plot.getDataset();

    Paint res = null;

    if(data instanceof TimeSeriesCollection)
    {
      TimeSeriesCollection tsc = (TimeSeriesCollection)data;
      // get the data series
      BasicTimeSeries bts = tsc.getSeries(series);
      TimeSeriesDataPair tsdp = bts.getDataPair(item);
      if(tsdp instanceof AttractiveDataItem)
      {
        AttractiveDataItem cdi = (AttractiveDataItem)tsdp;
        theColor = cdi.getColor();
      }
    }

    if(theColor != null)
      res = theColor;
    else
      res = super.getPaint(plot, series, item, x, y);

    return res;
  }

  /** accessor method to find out if we should connect this point to the previous one
   *
   * @param plot  the plot (can be used to obtain standard color information etc).
   * @param series  the series index.
   * @param item  the item index.
   * @param x  the x value of the item.
   * @param y  the y value of the item.
   * @return yes/no
   */
  protected boolean connectToPrevious(final com.jrefinery.legacy.chart.Plot plot, final int series, final int item, final double x, final double y)
  {
    final Dataset data = plot.getDataset();

    boolean res = true;

    if(data instanceof TimeSeriesCollection)
    {
      TimeSeriesCollection tsc = (TimeSeriesCollection)data;
      // get the data series
      BasicTimeSeries bts = tsc.getSeries(series);
      TimeSeriesDataPair tsdp = bts.getDataPair(item);
      if(tsdp instanceof AttractiveDataItem)
      {
        AttractiveDataItem cdi = (AttractiveDataItem)tsdp;
        res = cdi.connectToPrevious();
      }
    }

    return res;
  }


  /**
   * Draws the visual representation of a single data item.
   *
   * @param g2  the graphics device.
   * @param dataArea  the area within which the data is being drawn.
   * @param info  collects information about the drawing.
   * @param plot  the plot (can be used to obtain standard color information etc).
   * @param domainAxis  the domain (horizontal) axis.
   * @param rangeAxis  the range (vertical) axis.
   * @param data  the dataset.
   * @param series  the series index.
   * @param item  the item index.
   * @param crosshairInfo  information about crosshairs on a plot.
   */
  public void drawItem(Graphics2D g2,
                       Rectangle2D dataArea,
                       ChartRenderingInfo info,
                       XYPlot plot,
                       ValueAxis domainAxis,
                       ValueAxis rangeAxis,
                       XYDataset data,
                       int series,
                       int item,
                       CrosshairInfo crosshairInfo) {

    // setup for collecting optional entity info...
    Shape entityArea = null;
    EntityCollection entities = null;
    if (info != null) {
      entities = info.getEntityCollection();
    }

    Paint seriesPaint = plot.getSeriesPaint(series);
    Stroke seriesStroke = plot.getSeriesStroke(series);
    g2.setPaint(seriesPaint);
    g2.setStroke(seriesStroke);

    // get the data point...
    Number x1n = data.getXValue(series, item);
    Number y1n = data.getYValue(series, item);
    if (y1n != null) {
      double x1 = x1n.doubleValue();
      double y1 = y1n.doubleValue();
      double transX1 = domainAxis.translateValueToJava2D(x1, dataArea);
      double transY1 = rangeAxis.translateValueToJava2D(y1, dataArea);

      Paint paint = getPaint(plot, series, item, transX1, transY1);
      if (paint != null) {
        g2.setPaint(paint);
      }

      if (true) {
        if (item > 0) {

          // find out if we're going to connect this line
          boolean connectToPrevious = connectToPrevious(plot, series, item, transX1, transY1);

          if(connectToPrevious)
          {
            // get the previous data point...
            Number x0n = data.getXValue(series, item - 1);
            Number y0n = data.getYValue(series, item - 1);
            if (y0n != null) {
              double x0 = x0n.doubleValue();
              double y0 = y0n.doubleValue();

              double transX0 = domainAxis.translateValueToJava2D(x0, dataArea);
              double transY0 = rangeAxis.translateValueToJava2D(y0, dataArea);

              workingLine.setLine(transX0, transY0, transX1, transY1);
              if (workingLine.intersects(dataArea)) {
                g2.draw(workingLine);
              }
            }
          }
        }
      }

//          if (this.plotShapes) {
      if(this.getPlotShapes())
      {
        double scale = getShapeScale(plot, series, item, transX1, transY1);
        Shape shape = getShape(plot, series, item, transX1, transY1, scale);
        if (shape.intersects(dataArea)) {
          if (isShapeFilled(plot, series, item, transX1, transY1)) {
            g2.fill(shape);
          }
          else {
            g2.draw(shape);
          }
        }
        entityArea = shape;

      }

      if (false) {
//          if (this.plotImages) {
        // use shape scale with transform??
        double scale = getShapeScale(plot, series, item, transX1, transY1);
        Image image = this.getImage(plot, series, item, transX1, transY1);
        if (image != null) {
          Point hotspot = getImageHotspot(plot, series, item, transX1, transY1, image);
          g2.drawImage(image,
                       (int) (transX1 - hotspot.getX()),
                       (int) (transY1 - hotspot.getY()), (ImageObserver) null);
        }
        // tooltipArea = image; not sure how to handle this yet
      }

      // add an entity for the item...
      if (entities != null) {
        if (entityArea == null) {
          entityArea = new Rectangle2D.Double(transX1 - 2, transY1 - 2, 4, 4);
        }
        String tip = "";
        if (getToolTipGenerator() != null) {
          tip = getToolTipGenerator().generateToolTip(data, series, item);
        }
        String url = null;
        if (getURLGenerator() != null) {
          url = getURLGenerator().generateURL(data, series, item);
        }
        XYItemEntity entity = new XYItemEntity(entityArea, tip, url, series, item);
        entities.addEntity(entity);
      }

      // do we need to update the crosshair values?
      if (domainAxis.isCrosshairLockedOnData()) {
        if (rangeAxis.isCrosshairLockedOnData()) {
          // both axes
          crosshairInfo.updateCrosshairPoint(x1, y1);
        }
        else {
          // just the horizontal axis...
          crosshairInfo.updateCrosshairX(x1);
        }
      }
      else {
        if (rangeAxis.isCrosshairLockedOnData()) {
          // just the vertical axis...
          crosshairInfo.updateCrosshairY(y1);
        }
      }
    }
  }


}
