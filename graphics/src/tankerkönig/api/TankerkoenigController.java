/**
 * 
 */
package tankerkönig.api;

import static tankerkönig.api.Constants.SINGLE_STATION;
import static tankerkönig.api.Constants.STATIONS_RADIUS;
import static tankerkönig.api.Constants.TEXT_FILE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Calendar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author thies
 *
 */
public class TankerkoenigController {
	
	private GsonBuilder gsonBuilder = new GsonBuilder();
	private Gson gson  = gsonBuilder.create();
	
	/**
	 * 
	 */
	public void getStations() {
		String jsonText = getJsonTextFromUrl(STATIONS_RADIUS);
		StationsResponse stationsResponse = gson.fromJson(jsonText, StationsResponse.class);
		String seperator = ";";
		try {
			ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(TEXT_FILE, StandardCharsets.UTF_8);
			for (JsonElement station : stationsResponse.getStations()) {
				JsonObject jsonObject = station.getAsJsonObject();
				String id = jsonObject.get("id").getAsString();
				String name = jsonObject.get("brand").getAsString();
				String street = jsonObject.get("street").getAsString();
				String houseNumber = jsonObject.get("houseNumber").getAsString();
				String postCode = jsonObject.get("postCode").getAsString();
				String place = jsonObject.get("place").getAsString();

				String price = getPriceForID(id);
				if (price == null) {
					//EVTL Listen machen , damit es zusammen ist, für was ausgelesen werden konnte und für was nicht
					System.out.println("Für folgende ID konnten keine Daten ausgelesen werden -> " + id);
					continue;
				}

				lines.add(name + seperator + price + "€" + seperator + street + " " + houseNumber + seperator + postCode
						+ " " + place + seperator + Calendar.getInstance().getTime() + seperator);
				
				System.out.println("Daten erfolgreich ausgelesen für ID -> " + id);
			}
			
			Files.write(TEXT_FILE, lines, StandardCharsets.UTF_8, StandardOpenOption.WRITE);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @param id
	 * @return the price with 2 digits
	 */
	public String getPriceForID(String id) {
		String jsonText = getJsonTextFromUrl(Constants.get(SINGLE_STATION, id));
		if (jsonText == null) {
			return null;
		}
		
		PricesResponse obj = gson.fromJson(jsonText, PricesResponse.class);
		if (obj == null) {
			return null;
		}
		
		JsonElement station = obj.getprices().get(id);
		if (station == null) {
			return null;
		}
		
		String status = station.getAsJsonObject().get("status").getAsString();
		if (status.equals("closed")) {
			return null;
		}
		
		double superPrice = station.getAsJsonObject().get("e5").getAsDouble();
		return String.format("%.2f", superPrice);
	}
	
	private String getJsonTextFromUrl(String urlString) {
		String jsonText = "";
		try {
			URL url = new URL(urlString);
			InputStream is = url.openStream();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")))) {
				jsonText = readAll(reader);
			}
			return jsonText;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String readAll(Reader rd) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		int character;
		while ((character = rd.read()) != -1) {
			stringBuilder.append((char) character);
		}
		return stringBuilder.toString();
	}
}
