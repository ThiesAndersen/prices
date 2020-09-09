package graphics;

import static tankerkönig.api.Url.TEXT_FILE;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import enums.DayOfWeek;

public class GraphicsController {
	
	private GraphicsView view;
	double sum;

	public GraphicsController(GraphicsView view) {
		this.view = view;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public HashMap<DayOfWeek, List<String>> parseInput() throws IOException, ParseException {
		
		HashMap<DayOfWeek, List<String>> map = new HashMap<>();
		
		ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(TEXT_FILE, StandardCharsets.UTF_8);
		
		// das da parser -> Tanklager Baack;1,08€;Kiebitzreiher Chaussee ;25358 Horst-Hahnenkamp;Thu Apr 23 14:19:46 CEST 2020
		
		String price = null;
		Date date = null;
		DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.US);
		
		for (String line : lines) {
			price = null;
			date = null;
			int start = 0;
			int end = 0;
			for (int i = 0; i < line.length(); i++) {
				if (line.charAt(i) != (char) 59) {
					continue;
				}
				end = i;
				String subLine = line.substring(start, end);
				start = end+1;
				
				if (subLine.contains("€")) {
					price = subLine;
				}
				if (subLine.contains("CEST 2020")) {
					date = dateFormat.parse(subLine);
				}
				
			}
			
			if (price != null && date != null) {
				Calendar calendar = Calendar.getInstance(); 
				calendar.setTime(date);
				int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
				
				switch (dayOfWeek) {
				case 1: {
					LinkedList<String> priceList = (LinkedList<String>) map.computeIfAbsent(DayOfWeek.SUNDAY, k -> new LinkedList<String>());
					priceList.add(price);
					break;
				}
				case 2: {
					LinkedList<String> priceList = (LinkedList<String>) map.computeIfAbsent(DayOfWeek.MONDAY, k -> new LinkedList<String>());
					priceList.add(price);
					break;
				}
				case 3: {
					LinkedList<String> priceList = (LinkedList<String>) map.computeIfAbsent(DayOfWeek.TUESDAY, k -> new LinkedList<String>());
					priceList.add(price);
					break;
				}
				case 4: {
					LinkedList<String> priceList = (LinkedList<String>) map.computeIfAbsent(DayOfWeek.WEDNESDAY, k -> new LinkedList<String>());
					priceList.add(price);
					break;
				}
				case 5: {
					LinkedList<String> priceList = (LinkedList<String>) map.computeIfAbsent(DayOfWeek.THURSDAY, k -> new LinkedList<String>());
					priceList.add(price);
					break;
				}
				case 6: {
					LinkedList<String> priceList = (LinkedList<String>) map.computeIfAbsent(DayOfWeek.FRIDAY, k -> new LinkedList<String>());
					priceList.add(price);
					break;
				}
				case 7: {
					LinkedList<String> priceList = (LinkedList<String>) map.computeIfAbsent(DayOfWeek.SATURDAY, k -> new LinkedList<String>());
					priceList.add(price);
					break;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + dayOfWeek);
				}
			}
		}
		return map;
	}
	
	/**
	 * 
	 * @param dayToPriceList
	 * @param day
	 * @return the average for the given day
	 */
	public double getAverage(DayOfWeek day) {
		HashMap<DayOfWeek, List<String>> dayToPriceList = view.getDayToPriceList();
		if (!dayToPriceList.containsKey(day)) {
			return 0;
		}
		
		sum = 0;
		
		LinkedList<String> list = (LinkedList<String>) dayToPriceList.get(day);
		
		list.forEach(priceString -> {
		double price = Double.parseDouble(priceString.substring(0, priceString.length()-1).replace(",", "."));
		sum += price;
		});
		
		return sum / list.size();
	}
}
