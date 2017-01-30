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
package org.mwc.cmap.core.property_support;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;

public class TagListHelper extends EditorHelper
{
	String[] _theTags;

	final java.beans.PropertyEditor _propEditor;

	public TagListHelper(final String[] theTags,
			final java.beans.PropertyEditor propEditor)
	{
		super(null);
		_theTags = theTags;
		_propEditor = propEditor;
	}

	public CellEditor getCellEditorFor(final Composite parent)
	{
		return new ComboBoxCellEditor(parent, _theTags, SWT.READ_ONLY){
			public void activate()
		      {
		        
		        super.activate();
		        getControl().getDisplay().asyncExec(new Runnable() {

		          public void run() {
		            if(isActivated())
		              ((CCombo) getControl()).setListVisible(true);
		          }

		        });
		      }
		};
	}

	public Object translateFromSWT(final Object value)
	{
		Object res = value;
		if (value instanceof String)
		{
			_propEditor.setAsText((String) value);
			res = _propEditor.getValue();
		}
		else
		{
			final Integer index = (Integer) value;
			// ok, set the index of the text field first, then get the
			// object vlaue
			final String selectedItem = _theTags[index.intValue()];
			res = translateFromSWT(selectedItem);
		}
		return res;
	}

	public Object translateToSWT(final Object value)
	{
		Object res = value;
		if (value instanceof String)
		{
			// we have to translate the string to the string index
			for (int i = 0; i < _theTags.length; i++)
			{
				final String thisItem = _theTags[i];
				if (thisItem.equals(value))
				{
					res = new Integer(i);
					break;
				}
			}
		}
		else
		{
			// get the string representation of the object, then get the
			// index of
			// that string
			_propEditor.setValue(value);
			final String txtVersion = _propEditor.getAsText();
			if(txtVersion == null)
			{
			  // ok, just use the first one
        res = translateToSWT(_theTags[0]);
			}
			else
			{
			  res = translateToSWT(txtVersion);
			}
		}
		return res;
	}

	public ILabelProvider getLabelFor(final Object value)
	{
		final LabelProvider theProvider = new LabelProvider()
		{
			public String getText(final Object element)
			{
				String res = null;
				_propEditor.setValue(element);
				res = _propEditor.getAsText();
				return res;
			}
		};
		return theProvider;
	}

	@Override
	public Control getEditorControlFor(final Composite parent,
			final IDebriefProperty property)
	{
		final Combo myCombo = new Combo(parent, SWT.NONE);
		
		myCombo.setItems(_theTags);

		// also insert a listener
		myCombo.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(final SelectionEvent e)
			{
				// process the new selection
				final int index = myCombo.getSelectionIndex();
				if (index != -1)
				{
					final String res = _theTags[index];
					property.setValue(res);
				}
			}
		});

		return myCombo;
	}
}