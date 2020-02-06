
package ASSET.Scenario.Observers;

import ASSET.Scenario.Genetic.ScenarioRunner;
import MWC.GUI.Plottable;

public interface ScenarioObserver extends Plottable
{
  /**
   * configure observer to listen to this scenario
   *
   * @param scenario the new scenario to listen to
   */
  public void setup(ASSET.ScenarioType scenario);

  /**
   * inform observer that scenario is complete, to remove listeners, etc
   * It's ok to clear the score (if applicable) since it will have been
   * retrieved before this point
   */
  public void tearDown(ASSET.ScenarioType scenario);

  /**
   * get the name of this observer
   */
  public String getName();

  /**
   * set the name of this observer
   */
  public void setName(String val);

  /**
   * get whether this observer is active
   */
  public boolean isActive();

  /**
   * set whether this observer is active
   */
  public void setActive(boolean val);


  /**
   * embedded interface for observers which are capable of returning an
   * opinion on what happened
   */
  static public interface ScenarioReferee extends ScenarioObserver
  {
    /**
     * return how well this scenario performed, according to this referee.
     */
    public ScenarioRunner.ScenarioOutcome getOutcome();

  }
}
