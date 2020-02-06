
package ASSET.Util.XML.Control.Observers;

import ASSET.Models.Decision.TargetType;
import ASSET.Scenario.Observers.ScenarioObserver;
import ASSET.Scenario.Observers.Recording.CSVExportStatusObserver;
import ASSET.Scenario.Observers.Recording.DebriefReplayObserver;
import ASSET.Util.XML.Decisions.Util.TargetTypeHandler;


/**
 * read in a debrief replay observer from file
 */
abstract class CSVExportStatusObserverHandler extends CoreFileObserverHandler
{

  private final static String type = "CSVStatusObserver";

  TargetType _targetType = null;
  String _subjectName = null;
  private static final String SUBJECT_NAME = "SubjectName";
  private static final String TARGET_TYPE = "SubjectToTrack";


  public CSVExportStatusObserverHandler(String type)
  {
    super(type);

    addHandler(new TargetTypeHandler(TARGET_TYPE)
    {
      public void setTargetType(TargetType type1)
      {
        _targetType = type1;
      }
    });
    addAttributeHandler(new HandleAttribute(SUBJECT_NAME)
    {
      public void setValue(String name, final String val)
      {
        _subjectName = val;
      }
    });
  }

  public CSVExportStatusObserverHandler()
  {
    this(type);
  }

  public void elementClosed()
  {
    // create ourselves
    final ScenarioObserver debriefObserver = getObserver(_name, _isActive, _targetType, _subjectName);

    setObserver(debriefObserver);

    // close the parenet
    super.elementClosed();

    // and clear the data
    _targetType = null;

  }

  protected ScenarioObserver getObserver(String name, boolean isActive, TargetType subject, String subjectName)
  {
    return new CSVExportStatusObserver(_directory, _fileName, subject, name, isActive, subjectName);
  }


  abstract public void setObserver(ScenarioObserver obs);

  static public void exportThis(final Object toExport, final org.w3c.dom.Element parent,
                                final org.w3c.dom.Document doc)
  {
    // create ourselves
    final org.w3c.dom.Element thisPart = doc.createElement(type);

    // get data item
    final DebriefReplayObserver bb = (DebriefReplayObserver) toExport;

    // output the parent ttributes
    CoreFileObserverHandler.exportThis(bb, thisPart);

    // output it's attributes
    if (bb.getSubjectToTrack() != null)
    {
      TargetTypeHandler.exportThis(TARGET_TYPE, bb.getSubjectToTrack(), thisPart, doc);
    }

    // output it's attributes
    parent.appendChild(thisPart);

  }


}