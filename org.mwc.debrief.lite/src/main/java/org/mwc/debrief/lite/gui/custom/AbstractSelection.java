/*
 *    Debrief - the Open Source Maritime Analysis Application
 *    http://debrief.info
 *
 *    (C) 2000-2018, Deep Blue C Technology Ltd
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the Eclipse Public License v1.0
 *    (http://www.eclipse.org/legal/epl-v10.html)
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 */
package org.mwc.debrief.lite.gui.custom;

public class AbstractSelection<T>
{
  private Boolean _selected;
  private T _item;
  
  public AbstractSelection(final T item, final Boolean selected)
  {
    this._selected = selected;
    this._item = item;
  }

  public Boolean isSelected()
  {
    return _selected;
  }

  public void setSelected(final Boolean _selected)
  {
    this._selected = _selected;
  }

  public T getItem()
  {
    return _item;
  }

  public void setItem(final T _item)
  {
    this._item = _item;
  }
}
