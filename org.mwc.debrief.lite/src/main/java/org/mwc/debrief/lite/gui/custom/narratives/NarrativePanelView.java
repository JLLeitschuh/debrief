package org.mwc.debrief.lite.gui.custom.narratives;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.BoxLayout;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import org.jdesktop.swingx.JXPanel;

public class NarrativePanelView extends JPanel
{

  /**
   *
   */
  private static final long serialVersionUID = 1218759123615315561L;

  public NarrativePanelView(final NarrativePanelToolbar toolbar,
      final AbstractNarrativeConfiguration model)
  {
    super();

    final NarrativePanelToolbar _toolbar = toolbar;

    setLayout(new BorderLayout());

    add(_toolbar, BorderLayout.NORTH);
    final JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    final JScrollPane scrollPane = new JScrollPane();
    scrollPane.setHorizontalScrollBarPolicy(
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setVerticalScrollBarPolicy(
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    //scrollPane.getLayout().preferredLayoutSize(this);

    //scrollPane.setPreferredSize(new Dimension(200, 100));
    scrollPane.getViewport().add(toolbar.getNarrativeList());
    //final JPanel mainPanel = new JPanel();
    final JTextField filterTextField = new JTextField();
    filterTextField.setPreferredSize(new Dimension(30, 20));
    mainPanel.add(filterTextField);

    /*JScrollPane scroll = new JScrollPane();
    scroll.getViewport().setSize(100, 200);
    mainPanel.add(toolbar.getNarrativeList());
    scroll.setViewportView(mainPanel);
    //mainPanel.setScrollableTracksViewportWidth(false);

    /*final int maxBarSize = 100;
    final int barSize = 60;
    final int deltaBarSize = maxBarSize - barSize;
    final JScrollBar scrollB2 = new JScrollBar(JScrollBar.VERTICAL, 0, barSize, 0, maxBarSize);
    scrollB2.addAdjustmentListener(new AdjustmentListener(
        )
    {
      
      @Override
      public void adjustmentValueChanged(AdjustmentEvent e)
      {
        int visible = scrollB2.getValue() * toolbar.getNarrativeList().getModel().getSize() / deltaBarSize;
        System.out.println(scrollB2.getValue() + " " + visible);
        toolbar.getNarrativeList().ensureIndexIsVisible(visible);
      }
    });*/
    //toolbar.getNarrativeList().set
    //toolbar.getNarrativeList().setDragEnabled(true);
    //add(toolbar.getNarrativeList(), BorderLayout.CENTER);
    //add(scrollPane, BorderLayout.CENTER);
    add(scrollPane, BorderLayout.CENTER);
    
    
    //add(scrollB2, BorderLayout.EAST);
  }
}
