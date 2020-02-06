
package com.planetmayo.debrief.satc_rcp.ui.converters.units;

import org.eclipse.core.databinding.conversion.IConverter;

public abstract class AbstractUnitConverter implements IConverter
{	
	protected boolean isModelToUI;
	
	public AbstractUnitConverter(boolean isModelToUI) 
	{
		this.isModelToUI = isModelToUI;
	}
	
	protected abstract Double safeConvert(Number obj);

	@Override
	public Object convert(Object obj)
	{
		if (obj == null) {
			return null;
		}		
		Double result = safeConvert((Number) obj);
		return result;
	}

	@Override
	public Object getFromType()
	{
		return Number.class;
	}

	@Override
	public Object getToType()
	{
		return Double.class;
	}	
}
