
package com.planetmayo.debrief.satc_rcp.ui.contributions;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.planetmayo.debrief.satc.model.contributions.Range1959ForecastContribution;
import com.planetmayo.debrief.satc.model.generator.IContributions;
import com.planetmayo.debrief.satc_rcp.ui.UIUtils;
import com.planetmayo.debrief.satc_rcp.ui.converters.MinMaxLimitObservable;
import com.planetmayo.debrief.satc_rcp.ui.converters.PrefixSuffixLabelConverter;
import com.planetmayo.debrief.satc_rcp.ui.converters.units.KtsToMSec;
import com.planetmayo.debrief.satc_rcp.ui.converters.units.MSecToKts;
import com.planetmayo.debrief.satc_rcp.ui.converters.units.MeterToYds;
import com.planetmayo.debrief.satc_rcp.ui.converters.units.UnitConverter;
import com.planetmayo.debrief.satc_rcp.ui.converters.units.YdsToMeter;

public class Ranging1959ContributionView extends BaseContributionView<Range1959ForecastContribution>
{

	private static final DecimalFormat rangeFormat = new DecimalFormat("0");
	
	private Text speedSoundText;
	private Text fNoughtText;
	private Text rangeText;
	private Text rangeBoundsText;

	public Ranging1959ContributionView(Composite parent, Range1959ForecastContribution contribution,
			IContributions contributions)
	{
		super(parent, contribution, contributions);
		initUI();
	}

	@Override
	protected void initUI()
	{
		GridLayout layout = UIUtils.createGridLayoutWithoutMargins(1, false);
		layout.verticalSpacing = 0;
		mainGroup = new Group(controlParent, SWT.SHADOW_ETCHED_IN);
		mainGroup.setLayout(layout);
		mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		createHeader(mainGroup);
		createBody(mainGroup);

		titleChangeListener =
				attachTitleChangeListener(contribution, getTitlePrefix());
		initializeWidgets();
		
		// add sound speed
		UIUtils.createLabel(bodyGroup, "Sound Speed (m/s):", new GridData(130, SWT.DEFAULT));
		UIUtils.createSpacer(bodyGroup, new GridData(45, SWT.DEFAULT));
		
		// add the speed
		Composite speed = new Composite(bodyGroup, SWT.NONE);
		speed.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		speed.setLayout(new GridLayout(4, false));
		
		speedSoundText = new Text(speed, SWT.BORDER|SWT.TRAIL);
		GridData gd = new GridData(100, SWT.DEFAULT);
		speedSoundText.setLayoutData(gd);
		speedSoundText.addVerifyListener(new DoubleVerifier());
		
		// add FNought
		gd = new GridData(90, SWT.DEFAULT);
		UIUtils.createLabel(speed, "F0 (Hz):", gd);
		
		fNoughtText = new Text(speed, SWT.BORDER|SWT.TRAIL);
		gd = new GridData(100,SWT.DEFAULT);
		fNoughtText.setLayoutData(gd);
		fNoughtText.addVerifyListener(new DoubleVerifier());
		
		// now add the range
		UIUtils.createLabel(bodyGroup, "1959 Range(Yds):", new GridData(120, SWT.DEFAULT));
		UIUtils.createSpacer(bodyGroup, new GridData(1, SWT.DEFAULT));
		
		Composite range = new Composite(bodyGroup, SWT.NONE);
		range.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		range.setLayout(new GridLayout(3, false));
		
		rangeText = new Text(range, SWT.BORDER|SWT.READ_ONLY|SWT.TRAIL);
		gd = new GridData(60,SWT.DEFAULT);
		rangeText.setLayoutData(gd);
		
		rangeBoundsText = new Text(range, SWT.BORDER|SWT.READ_ONLY|SWT.CENTER);
		gd = new GridData(140,SWT.DEFAULT);
		rangeBoundsText.setLayoutData(gd);

		Label lastLabel = new Label(range, SWT.LEFT);
		lastLabel.setText("Calculated data - read only");
		gd = new GridData(220,SWT.DEFAULT);
		lastLabel.setLayoutData(gd);

		context = new DataBindingContext();
		bindValues(context);
	}

	@Override
	protected void bindValues(DataBindingContext context)
	{
		PrefixSuffixLabelConverter labelConverter = new PrefixSuffixLabelConverter(Object.class, "", " Yds", rangeFormat);
		labelConverter.setNestedUnitConverter(UnitConverter.RANGE_YDS.getModelToUI());
		
		IObservableValue errorValue = BeansObservables.observeValue(
				contribution, Range1959ForecastContribution.RANGE);
		IObservableValue observationNumberValue = BeansObservables.observeValue(
				contribution, Range1959ForecastContribution.OBSERVATIONS_NUMBER);		
		bindCommonHeaderWidgets(context, errorValue, observationNumberValue, 
				new PrefixSuffixLabelConverter(Object.class, " Measurements"), labelConverter);
		bindCommonDates(context);

		// bind SpeedSound
		bindSpeed(context);
		
		// bind FNought
		bindFNought(context);
		
		// bind range
		bindRange(context);
		
		// bind range bounds
		bindRangeBounds(context);
	}

	private void bindRangeBounds(DataBindingContext context)
	{
		PrefixSuffixLabelConverter minMaxConverter = new PrefixSuffixLabelConverter(Object.class, "", "", rangeFormat);
		minMaxConverter.setNestedUnitConverter(UnitConverter.RANGE_YDS.getModelToUI());
		
		IObservableValue minRangeValue = BeansObservables.observeValue(contribution, Range1959ForecastContribution.MIN_RANGE);
		IObservableValue maxRangeValue = BeansObservables.observeValue(contribution, Range1959ForecastContribution.MAX_RANGE);
		
		IObservableValue rangeBoundsValue = new MinMaxLimitObservable(minRangeValue, maxRangeValue, 
				minMaxConverter);
		ISWTObservableValue rangeBoundsTextValue = WidgetProperties.text(SWT.FocusOut)
				.observe(rangeBoundsText);
		context.bindValue(rangeBoundsTextValue, rangeBoundsValue);
	}

	private void bindFNought(DataBindingContext context)
	{
		IObservableValue fNoughtValue = BeansObservables.observeValue(contribution,
				Range1959ForecastContribution.F_NOUGHT);
		ISWTObservableValue fNoughtTextValue = WidgetProperties.text(SWT.FocusOut)
				.observe(fNoughtText);
		
		IConverter modelToUI = new ModelToUIFNoughtConverter();
		
		IConverter uiToModel = new MsToKtsConverter();
		
		context.bindValue(fNoughtTextValue, fNoughtValue,
				UIUtils.converterStrategy(uiToModel),
				UIUtils.converterStrategy(modelToUI));
	}

	private void bindSpeed(DataBindingContext context)
	{
		IObservableValue soundValue = BeansObservables.observeValue(contribution,
				Range1959ForecastContribution.SOUND_SPEED);
		ISWTObservableValue soundTextValue = WidgetProperties.text(SWT.FocusOut)
				.observe(speedSoundText);
		
		IConverter modelToUI = new KtsToMsConverter();
		
		IConverter uiToModel = new MsToKtsConverter();
		
		context.bindValue(soundTextValue, soundValue,
				UIUtils.converterStrategy(uiToModel),
				UIUtils.converterStrategy(modelToUI));
	}
	
	private void bindRange(DataBindingContext context)
	{
		IObservableValue rangeValue = BeansObservables.observeValue(contribution,
				Range1959ForecastContribution.RANGE);
		ISWTObservableValue rangeTextValue = WidgetProperties.text(SWT.FocusOut)
				.observe(rangeText);
		
		IConverter modelToUI = new ModelToUIRangeConverter();
		
		IConverter uiToModel = new UIToModelRangeConverter();
		
		context.bindValue(rangeTextValue, rangeValue,
				UIUtils.converterStrategy(uiToModel),
				UIUtils.converterStrategy(modelToUI));
	}
	
	
	@Override
	protected void initializeWidgets()
	{
	}

	@Override
	protected String getTitlePrefix()
	{
		return "1959 Ranging Forecast - ";
	}

	@Override
	protected void bindMaxMinEstimate(IObservableValue estimate,
			IObservableValue min, IObservableValue max)
	{
		
	}

	@Override
	protected void createLimitAndEstimateSliders()
	{
	}
	
	private class ModelToUIRangeConverter implements IConverter
	{
		@Override
		public Object getToType()
		{
			return String.class;
		}

		@Override
		public Object getFromType()
		{
			return Double.class;
		}

		@Override
		public Object convert(Object fromObject)
		{
			if (fromObject == null)
			{
				return null;
			}
			//int value = ((Double) fromObject).intValue();
			int value = new MeterToYds().safeConvert((Double)fromObject).intValue();
			return new String(new Integer(value).toString());
		}
	}

	private class ModelToUIFNoughtConverter implements IConverter
	{
		@Override
		public Object getToType()
		{
			return String.class;
		}

		@Override
		public Object getFromType()
		{
			return Double.class;
		}

		@Override
		public Object convert(Object fromObject)
		{
			if (fromObject == null)
			{
				return null;
			}
			double value = ((Double) fromObject).doubleValue();
			BigDecimal temp = BigDecimal.valueOf(value).setScale(2, BigDecimal.ROUND_HALF_UP);
			return temp.toString();
		}
	}


	private class KtsToMsConverter implements IConverter
	{
		final DecimalFormat df = new DecimalFormat("0.0");
		final KtsToMSec converter = new KtsToMSec();
	
		@Override
		public Object getToType()
		{
			return String.class;
		}
	
		@Override
		public Object getFromType()
		{
			return Double.class;
		}
	
		@Override
		public Object convert(Object fromObject)
		{
			if (fromObject == null)
			{
				return null;
			}
			Double kts = (Double) fromObject;
			Double ms = (Double) converter.convert(kts);
			String res = df.format(ms);
			return res;
		}
	}

	private class MsToKtsConverter implements IConverter
	{
		final MSecToKts converter = new MSecToKts();
		
		@Override
		public Object getToType()
		{
			return Double.class;
		}
		
		@Override
		public Object getFromType()
		{
			return String.class;
		}
		
		@Override
		public Object convert(Object fromObject)
		{
			String s = (String) fromObject;
			if (fromObject == null || s.isEmpty()) {
				return null;
			}
			
			double ms = new Double((String)fromObject).doubleValue();
			Double kts = (Double) converter.convert(ms);			
			return new Double(kts);
		}

	}

	private class UIToModelRangeConverter implements IConverter
	{
		
		@Override
		public Object getToType()
		{
			return Double.class;
		}
		
		@Override
		public Object getFromType()
		{
			return String.class;
		}
		
		@Override
		public Object convert(Object fromObject)
		{
			String s = (String) fromObject;
			if (fromObject == null || s.isEmpty()) {
				return null;
			}
			//double value = new Double((String)fromObject).doubleValue();
			Double value = new Double((String)fromObject);
			value = new YdsToMeter().safeConvert(value);
			return new Double(value);
		}

	}

	private class DoubleVerifier implements VerifyListener
	{

		@Override
		public void verifyText(VerifyEvent e)
		{
			if (e.text.isEmpty())
			{
				return;
			}
			String s = e.text;
			char[] chars = new char[s.length()];
			s.getChars(0, chars.length, chars, 0);
			for (int i = 0; i < chars.length; i++)
			{
				if (!('0' <= chars[i] && chars[i] <= '9'))
				{
					if (chars[i] != '.')
					{
						Display.getCurrent().beep();
						e.doit = false;
						return;
					}
				}
			}
			if (e.widget instanceof Text)
			{
				Text text = (Text) e.widget;
				final String oldS = text.getText();
				final String newS = oldS.substring(0, e.start) + e.text
						+ oldS.substring(e.end);
				int index = newS.indexOf(".");
				if (index >= 0)
				{
					int i2 = newS.indexOf(".", index + 1);
					if (i2 >= 0)
					{
						Display.getCurrent().beep();
						e.doit = false;
						return;
					}
				}
				
			}
		}

	}
}
