/**
 * 
 */
package graphics;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxisSet;
import org.eclipse.swtchart.IAxisTick;
import org.eclipse.swtchart.IBarSeries;
import org.eclipse.swtchart.IGrid;
import org.eclipse.swtchart.ILegend;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.ISeriesLabel;
import org.eclipse.swtchart.ISeriesSet;
import org.eclipse.swtchart.ITitle;
import org.eclipse.swtchart.LineStyle;

import enums.DayOfWeek;
import tankerkönig.api.TankerkoenigController;

/**
 * @author thies
 *
 */
public class GraphicsView {
	
	private GraphicsController controller = new GraphicsController(this);
	private HashMap<DayOfWeek, List<String>> dayToPriceList = new HashMap<>();

	/**
	 * Colors and fonts
	 */
	private Color blue 			= new Color(Display.getDefault(), 0, 0, 255);
	private Color lightBlue 	= new Color(Display.getDefault(), 102, 194, 255);
	private Color red 			= new Color(Display.getDefault(), 255, 0, 0);
	private Color green 		= new Color(Display.getDefault(), 0, 255, 0);
	private Color lightGreen	= new Color(Display.getDefault(), 80, 240, 180);
	private Color white 		= new Color(Display.getDefault(), 255, 255, 255);
	private Color black 		= new Color(Display.getDefault(), 0, 0, 0);
	private Color yellow 		= new Color(Display.getDefault(), 255, 204, 0);
	private Color brown 		= new Color(Display.getDefault(), 102, 51, 0);
	private Color pink 			= new Color(Display.getDefault(), 255, 128, 128);
	private Color grey 			= new Color(Display.getDefault(), 234, 234, 225);
	private Font tahoma_14 		= new Font(Display.getDefault(), "Tahoma", 10, SWT.BOLD);
	
	@PostConstruct
	public void createComposite(Composite parent, ESelectionService selectionService) {
		
		parent.setLayout(GridLayoutFactory.fillDefaults().create());
		Button button = new Button(parent, SWT.NONE);
		button.setText("Daten einlesen");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TankerkoenigController apiController = new TankerkoenigController();
//				apiController.getPriceForID("edf12159-860f-4340-a5c9-ee0b92542894");
				apiController.getStations();
			}
		});
		
		try {
			dayToPriceList = controller.parseInput();
		} catch (IOException | ParseException e1) {
			System.out.println("Parser nicht erfolgreich");
			e1.printStackTrace();
		}
		
		Chart chart = new Chart(parent, SWT.NONE);
		chart.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		ITitle graphTitle = chart.getTitle();
		graphTitle.setText("Benzinpreis (Super)");
		graphTitle.setForeground(black);
		
		double[] ySeries = { 
				controller.getAverage(DayOfWeek.MONDAY),
				controller.getAverage(DayOfWeek.TUESDAY),
				controller.getAverage(DayOfWeek.WEDNESDAY),
				controller.getAverage(DayOfWeek.THURSDAY),
				controller.getAverage(DayOfWeek.FRIDAY),
				controller.getAverage(DayOfWeek.SATURDAY),
				controller.getAverage(DayOfWeek.SUNDAY),
		};
		
//		double[] ySeries = { 
//				1.00, 3.00, 6.00, 9.00, 2.00, 7.00, 6.00
//		};
		
		ISeriesSet seriesSet = chart.getSeriesSet();
//		ISeries series = seriesSet.createSeries(SeriesType.LINE, "01 bis 06 Apr");
//		series.setYSeries(ySeries);
		String seriesId = "line series";
		IBarSeries series = (IBarSeries) seriesSet.createSeries(SeriesType.BAR, "Durchschnittlicher Preis pro Wochentag");
		series.setYSeries(ySeries);
		series.setBarPadding(60);
		series.setBarColor(lightBlue);
		ISeriesLabel seriesLabel = series.getLabel();
		seriesLabel.setFormat("Ø ##.00€");
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
		
		//Chartfarbe innen und außen
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
		yAxisTitle.setText("Preis (in €)");
		yAxisTitle.setForeground(black);
		IAxisTick yTick = yAxis.getTick();
		yTick.setForeground(black);
		yTick.setFont(tahoma_14);
		
		IGrid xGrid = axisSet.getXAxis(0).getGrid();
		xGrid.setForeground(white);
		xGrid.setStyle(LineStyle.SOLID);
	}
	
	/**
	 * @return dayToPriceList
	 */
	public HashMap<DayOfWeek, List<String>> getDayToPriceList() {
		return dayToPriceList;
	}
}
