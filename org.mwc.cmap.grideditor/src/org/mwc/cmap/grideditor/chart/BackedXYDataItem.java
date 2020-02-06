
package org.mwc.cmap.grideditor.chart;

import org.jfree.data.xy.XYDataItem;

import MWC.GUI.TimeStampedDataItem;

public class BackedXYDataItem extends XYDataItem implements BackedChartItem {

	private static final long serialVersionUID = 3331357479111378526L;

	private final TimeStampedDataItem myDomainItem;

	public BackedXYDataItem(final double x, final double y, final TimeStampedDataItem domainItem) {
		super(x, y);
		myDomainItem = domainItem;
	}

	public TimeStampedDataItem getDomainItem() {
		return myDomainItem;
	}
	
	@Override
	public Object clone() {
		//sic
		return super.clone();
	}

}
