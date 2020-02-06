
package org.mwc.cmap.core.property_support;

import java.text.ParseException;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.mwc.cmap.core.property_support.lengtheditor.LengthPropertyCellEditor;
import org.mwc.cmap.gridharness.data.WorldDistance;

import MWC.GenericData.WorldDistance.ArrayLength;
import MWC.Utilities.ReaderWriter.XML.MWCXMLReader;


public class ArrayLengthHelper extends EditorHelper
{

	public ArrayLengthHelper()
	{
		super(ArrayLength.class);
	}

	public CellEditor getCellEditorFor(final Composite parent)
	{
		final CellEditor res = new LengthPropertyCellEditor(parent);
		return res; 
	}

	@SuppressWarnings({ "rawtypes" })
	public boolean editsThis(final Class target)
	{
		return (target == ArrayLength.class);
	}

	public Object translateToSWT(final Object value)
	{
		final ArrayLength len = (ArrayLength) value;
		return len.getValueIn(WorldDistance.METRES) + " m";
	}

	public Object translateFromSWT(final Object value)
	{
		String theStr = (String) value;
		if(theStr.contains("m"))
		{
			theStr = theStr.replace('m', ' ');
		}
			
		double len = 0;
		try 
		{
			len = MWCXMLReader.readThisDouble(theStr);
		} 
		catch (final ParseException e) 
		{
			MWC.Utilities.Errors.Trace.trace(e);
		}
		final ArrayLength newVal = new ArrayLength(len); 
		return newVal;
	}

	public ILabelProvider getLabelFor(final Object currentValue)
	{
		final ILabelProvider label1 = new LabelProvider()
		{
			public String getText(final Object element)
			{
				final String res = (String) element;
				return res;
			}

			public Image getImage(final Object element)
			{
				return null;
			}

		};
		return label1;
	}
}