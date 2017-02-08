/*
 *    Debrief - the Open Source Maritime Analysis Application
 *    http://debrief.info
 *
 *    (C) 2000-20016, PlanetMayo Ltd
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the Eclipse Public License v1.0
 *    (http://www.eclipse.org/legal/epl-v10.html)
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 */
package org.mwc.debrief.dis.editor;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class InputFileRuleScanner extends RuleBasedScanner
{
  private static Color COMMENT_COLOR = new Color(Display.getCurrent(), new RGB(
      63, 127, 95));
  private static Color BLOCK_COLOR = new Color(Display.getCurrent(), new RGB(
      95, 2, 8));
  private static Color BLOCK_BACK_COLOR = new Color(Display.getCurrent(),
      new RGB(195, 192, 228));

  // the color

  public InputFileRuleScanner()
  {
    // get ready for list
    IRule[] rules = new IRule[2];

    // start with comment marker
    IToken commentToken =
        new Token(new TextAttribute(COMMENT_COLOR, null, SWT.ITALIC));
    rules[0] = (new EndOfLineRule("//", commentToken));

    // and a block token
    IToken blockToken =
        new Token(new TextAttribute(BLOCK_COLOR, BLOCK_BACK_COLOR, SWT.NONE));
    rules[1] = (new EndOfLineRule("<<", blockToken));

    setRules(rules);
  }
}
