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

package com.borlander.rac525791.dashboard.data;

public interface DashboardListener {

	void statusChanged();

	void nameChanged();

	void speedUnitsChanged();

	void depthUnitsChanged();

	void demandedSpeedChanged();

	void demandedDirectionChanged();

	void demandedDepthChanged();

	void actualDepthChanged();

	void actualDirectionChanged();

	void actualSpeedChanged();
	
	public static final DashboardListener NULL = new DashboardListener(){
	
		public void statusChanged() {
			// 
		}
	
		public void speedUnitsChanged() {
			// 
		}
	
		public void nameChanged() {
			// 
		}
	
		public void depthUnitsChanged() {
			// 
		}
	
		public void demandedSpeedChanged() {
			// 
		}
	
		public void demandedDirectionChanged() {
			// 
		}
	
		public void demandedDepthChanged() {
			// 
		}
	
		public void actualSpeedChanged() {
			// 
		}
	
		public void actualDirectionChanged() {
			// 
		}
	
		public void actualDepthChanged() {
			// 
		}
		
	};

}
