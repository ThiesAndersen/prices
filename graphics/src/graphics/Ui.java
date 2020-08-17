/**
 * 
 */
package graphics;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxisSet;
import org.eclipse.swtchart.IAxisTick;
import org.eclipse.swtchart.IBarSeries;
import org.eclipse.swtchart.IGrid;
import org.eclipse.swtchart.ILegend;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.ISeriesLabel;
import org.eclipse.swtchart.ISeriesSet;
import org.eclipse.swtchart.ITitle;
import org.eclipse.swtchart.LineStyle;

import enums.DayOfWeek;

/**
 * @author thies
 *
 */
public class Ui {
	
	private Color blue = new Color(Display.getDefault(), 0, 0, 255);
	private Color lightBlue = new Color(Display.getDefault(), 102, 194, 255);
	private Color red = new Color(Display.getDefault(), 255, 0, 0);
	private Color green = new Color(Display.getDefault(), 0, 255, 0);
	private Color lightGreen = new Color(Display.getDefault(), 80, 240, 180);
	private Color white = new Color(Display.getDefault(), 255, 255, 255);
	private Color black = new Color(Display.getDefault(), 0, 0, 0);
	private Color yellow = new Color(Display.getDefault(), 255, 204, 0);
	private Color brown = new Color(Display.getDefault(), 102, 51, 0);
	private Color pink = new Color(Display.getDefault(), 255, 128, 128);
	private Color grey = new Color(Display.getDefault(), 234, 234, 225);
	
	Font tahoma_14 = new Font(Display.getDefault(), "Tahoma", 10, SWT.BOLD);
	
	private HashMap<DayOfWeek, List<String>> dayToPriceList = new HashMap<>();
	private double offsetPrice;
	
	@PostConstruct
	public void createComposite(Composite parent, ESelectionService selectionService) {
		
		
		parent.setLayout(GridLayoutFactory.fillDefaults().create());
		Button button = new Button(parent, SWT.NONE);
		button.setText("Daten einlesen");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Reader reader = new Reader();
				try {
					reader.readWebsite();
					System.out.println("Tankstellenpreise erfolgreich eingelesen und gespeichert");
				} catch (IOException | ParseException exception) {
					System.out.println("Webseite konnte nicht erfolgreich gelesen werden");
					exception.printStackTrace();
				}
			}
		});
		
		Parser parser = new Parser();
		try {
			dayToPriceList = parser.parseInput();
		} catch (IOException | ParseException e1) {
			System.out.println("Parser nicht erfolgreich");
			e1.printStackTrace();
		}
		
		Label testLabel = new Label(parent, SWT.NONE);
		testLabel.setText("Hier kommen Filter hin =)");
		
		Chart chart = new Chart(parent, SWT.NONE);
		chart.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		ITitle graphTitle = chart.getTitle();
		graphTitle.setText("Benzinpreise");
		graphTitle.setForeground(black);
		
		
		double[] ySeries = { 
				getAverage(DayOfWeek.MONDAY),
				getAverage(DayOfWeek.TUESDAY),
				getAverage(DayOfWeek.WEDNESDAY),
				getAverage(DayOfWeek.THURSDAY),
				getAverage(DayOfWeek.FRIDAY),
				getAverage(DayOfWeek.SATURDAY),
				getAverage(DayOfWeek.SUNDAY),
		};
		
		ISeriesSet seriesSet = chart.getSeriesSet();
//		ISeries series = seriesSet.createSeries(SeriesType.LINE, "01 bis 06 Apr");
//		series.setYSeries(ySeries);
		String seriesId = "line series";
		IBarSeries series = (IBarSeries) seriesSet.createSeries(SeriesType.BAR, "Durchschnittlicher Preis pro Wochentag");
		series.setYSeries(ySeries);
		series.setBarPadding(60);
		series.setBarColor(lightBlue);
		ISeriesLabel seriesLabel = series.getLabel();
		seriesLabel.setFormat("ÿ ##.00Ä");
		seriesLabel.setForeground(black);
		series.getLabel().setVisible(true);
		
		//Legende erzeugen & anpassen
		ILegend legend = chart.getLegend();
		legend.setPosition(SWT.TOP);
		legend.setBackground(white);
		Font legendFont = new Font(Display.getDefault(), "Tahoma", 12, SWT.BOLD);
		legend.setFont(legendFont);
		
		// X und Y range anpassen
		IAxisSet axisSet = chart.getAxisSet();
		axisSet.adjustRange();
		
		//Chartfarbe innen und auﬂen
		chart.setBackground(grey);
		chart.setBackgroundInPlotArea(white);
		
		//X-Achse anpassen
		IAxis xAxis = axisSet.getXAxis(0);
		xAxis.setCategorySeries(new String[] {"Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"});
		xAxis.enableCategory(true);
		ITitle xAxisTitle = xAxis.getTitle();
		xAxisTitle.setText("Wochentag");
		xAxisTitle.setForeground(black);
		IAxisTick xTick = xAxis.getTick();
		xTick.setForeground(black);
		xTick.setFont(tahoma_14);
		
		//Y-Achse anpassen
		IAxis yAxis = axisSet.getYAxis(0);
		ITitle yAxisTitle = yAxis.getTitle();
		yAxisTitle.setText("Preis (in Ä)");
		yAxisTitle.setForeground(black);
		IAxisTick yTick = yAxis.getTick();
		yTick.setForeground(black);
		yTick.setFont(tahoma_14);
		
		IGrid xGrid = axisSet.getXAxis(0).getGrid();
		xGrid.setForeground(white);
		xGrid.setStyle(LineStyle.SOLID);
	}
	
	private double getAverage(DayOfWeek day) {
		if (!dayToPriceList.containsKey(day)) {
			return 0;
		}
		
		offsetPrice = 0;
		
		LinkedList<String> list = (LinkedList<String>) dayToPriceList.get(day);
		
		list.forEach(priceString -> {
		double price = Double.parseDouble(priceString.substring(0, priceString.length()-1).replace(",", "."));
		offsetPrice += price;
			
		});
		
		return offsetPrice / list.size();
	}
	
}
