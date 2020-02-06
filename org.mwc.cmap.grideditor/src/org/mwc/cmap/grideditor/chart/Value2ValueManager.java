
package org.mwc.cmap.grideditor.chart;

import java.util.List;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;
import org.mwc.cmap.gridharness.data.GriddableItemDescriptor;
import org.mwc.cmap.gridharness.data.GriddableSeries;

import MWC.GUI.TimeStampedDataItem;

public class Value2ValueManager implements ChartDataManager {

	private final GriddableItemDescriptor myDescriptor;

	private final String myTitle;

	private final GriddableItemChartComponent myXComponent;

	private final GriddableItemChartComponent myYComponent;

	private final ScatteredXYSeries mySeries;

	private final XYSeriesCollection myDataSet;

	private GriddableSeries myInput;

	public Value2ValueManager(final GriddableItemDescriptor descriptor, final GriddableItemChartComponent xComponent, final GriddableItemChartComponent yComponent) {
		myDescriptor = descriptor;
		myXComponent = xComponent;
		myYComponent = yComponent;
		myTitle = descriptor.getTitle();

		mySeries = new ScatteredXYSeries("the-only-series");
		myDataSet = new XYSeriesCollection(mySeries);
	}
	
	protected GriddableSeries getInput() {
		return myInput;
	}

	public ValueAxis createXAxis() {
		return createNumberAxis();
	}

	public ValueAxis createYAxis() {
		return createNumberAxis();
	}

	private NumberAxis createNumberAxis() {
		final NumberAxis result = new NumberAxis();
		result.setAutoRangeIncludesZero(false);
		return result;
	}

	public String getChartTitle() {
		return myTitle;
	}

	public GriddableItemDescriptor getDescriptor() {
		return myDescriptor;
	}

	public XYDataset getXYDataSet() {
		return myDataSet;
	}

	public void handleItemAdded(final int index, final TimeStampedDataItem addedItem) {
		mySeries.insertAt(index, createChartItem(addedItem));
	}

	public void handleItemChanged(final TimeStampedDataItem changedItem) {
		final int index = myInput.getItems().indexOf(changedItem);
		if (index < 0) {
			return;
		}

		final double currentXValue = myXComponent.getDoubleValue(changedItem);
		final BackedXYDataItem chartItem = (BackedXYDataItem) mySeries.getDataItem(index);
		if (chartItem.getDomainItem() != changedItem) {
			final int shouldBeAt = myInput.getItems().indexOf(chartItem.getDomainItem());
			throw new IllegalStateException("domain position for element: " + changedItem + //
					" is " + index + //
					", but chart series contains " + chartItem.getDomainItem() + //
					" which should be at position: " + shouldBeAt);
		}
		if (currentXValue == chartItem.getXValue()) {
			mySeries.updateByIndex(index, myYComponent.getDoubleValue(changedItem));
		} else {
			handleItemDeleted(changedItem);
			handleItemAdded(index, changedItem);
		}
	}

	public void handleItemDeleted(final TimeStampedDataItem deletedItem) {
	  // Note: we used to get the index of the deleted item. We don't any more.  So,
	  // we've got to find the item ourselves
	  
	  final List<?> items = mySeries.getItems();
	  int ctr = 0;
	  for(final Object item: items)
	  {
	    final BackedXYDataItem data = (BackedXYDataItem) item;
	    // does the stored item match ours?
	    if(data.getDomainItem().equals(deletedItem))
	    {
	      mySeries.remove(ctr);
	      break;
	    }
	    ctr++;
	  }
	  
	}

	public void setInput(final GriddableSeries input) {
		myInput = input;
		int index = 0;
		for (final TimeStampedDataItem nextItem : input.getItems()) {
			handleItemAdded(index, nextItem);
			index++;
		}
	}

	public void attach(final JFreeChartComposite chartPanel) {
		//
	}

	public void detach(final JFreeChartComposite chartPanel) {
		//
	}

	private BackedXYDataItem createChartItem(final TimeStampedDataItem domainItem) {
		final double xValue = myXComponent.getDoubleValue(domainItem);
		final double yValue = myYComponent.getDoubleValue(domainItem);
		return new BackedXYDataItem(xValue, yValue, domainItem);
	}

}
