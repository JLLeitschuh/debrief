
package MWC.GUI;

/**
 * logging service that accomodates a surrogate - so the fancy RCP gui can
 * register as a logger with the legacy ASSET code
 * 
 * @author ian
 * 
 */
public class LoggingService implements ErrorLogger
{

	static ErrorLogger _substituteParent;
	static LoggingService _singleton;

	public static void initialise(final ErrorLogger logger)
	{
		_substituteParent = logger;
	}

	@Override
	public void logError(final int status, final String text, final Exception e)
	{
		if (_substituteParent != null)
			_substituteParent.logError(status, text, e);
		else
		{
			System.err.println("Error:" + text);
			if (e != null)
				e.printStackTrace();
		}

	}

  @Override
  public void
      logError(int status, String text, Exception e, boolean revealLog)
  {
    logError(status, text, e);
  }


  @Override
  public void logStack(int status, String text)
  {
    logError(status, "Stack requested:" + text, null);
  }

	public static LoggingService INSTANCE()
	{
		if (_singleton == null)
			_singleton = new LoggingService();

		return _singleton;
	}

}
