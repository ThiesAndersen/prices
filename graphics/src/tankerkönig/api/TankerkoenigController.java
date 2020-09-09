/**
 * 
 */
package tankerkönig.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

/**
 * @author thies
 *
 */
public class TankerkoenigController {

	public static final String apiKey = "84bcc00b-d130-c1f8-e104-3db891573e21";
	GsonBuilder gsonBuilder = new GsonBuilder();
	Gson gson  = gsonBuilder.create();
	
	String urlString = "https://creativecommons.tankerkoenig.de/json/prices.php?ids=edf12159-860f-4340-a5c9-ee0b92542894&apikey=84bcc00b-d130-c1f8-e104-3db891573e21";
	
	String radiusElmshorn = "https://creativecommons.tankerkoenig.de/json/list.php?lat=53.7513549&lng=9.6632521&rad=1.5&sort=dist&type=e5&apikey=00000000-0000-0000-0000-000000000002";
	
	/**
	 * 
	 */
	public void getPriceForID() throws IOException {
		URL url = new URL(urlString);

		String jsonText;
		
		InputStream is = url.openStream();
		try (BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")))) {
			jsonText = readAll(rd);
		}

		pricesResponse obj = gson.fromJson(jsonText, pricesResponse.class);
		JsonElement station = obj.getprices().get("edf12159-860f-4340-a5c9-ee0b92542894");
		double superPrice = station.getAsJsonObject().get("e5").getAsDouble();
		System.out.println();

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
