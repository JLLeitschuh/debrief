/*******************************************************************************
 * Debrief - the Open Source Maritime Analysis Application
 * http://debrief.info
 *  
 * (C) 2000-2020, Deep Blue C Technology Ltd
 *  
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html)
 *  
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *******************************************************************************/

package com.borlander.rac525791.dashboard.layout;

import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

public abstract class SelectableImageFigure extends ImageFigure {
	private final DashboardUIModel mySelector;

	public SelectableImageFigure(DashboardUIModel selector){
		super();
		mySelector = selector;
	}
	
	@Override
	public void setBounds(Rectangle rect) {
		ControlUISuite suite = mySelector.getUISuite(rect.width, rect.height);
		Image image = selectImage(suite.getImages());
		setImage(image);
		super.setBounds(rect);
	}
	
	protected abstract Image selectImage(DashboardImages images);

}
