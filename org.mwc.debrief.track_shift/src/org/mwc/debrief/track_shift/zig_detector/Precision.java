
package org.mwc.debrief.track_shift.zig_detector;

public enum Precision
{
	LOW("Low", 100), MEDIUM("Medium", 200), HIGH("High", 300);

	private final String label;
	private final int numPoints;

	private Precision(String label, int numPoints)
	{
		this.label = label;
		this.numPoints = numPoints;
	}

	public String getLabel()
	{
		return label;
	}
	
	/** find out how many points should be generated when gridding 
	 * a location bounds
	 * 
	 * @return
	 */
	public int getNumPoints() 
	{
		return numPoints;
	}
}
