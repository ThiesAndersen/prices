/**
 * 
 */
package tankerkönig.api;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author thies
 *
 */
public class Constants {

	public static final Path TEXT_FILE = Paths.get("C:\\Users\\thies\\Desktop\\Tankstellenpreise.txt");
	
	public static final String API_KEY = "84bcc00b-d130-c1f8-e104-3db891573e21";
	
	public static final String SINGLE_STATION = "https://creativecommons.tankerkoenig.de/json/prices.php?ids=%s&apikey=" + API_KEY;
	public static final String STATIONS_RADIUS = "https://creativecommons.tankerkoenig.de/json/list.php?lat=53.7513549&lng=9.6632521&rad=1.5&sort=dist&type=e5&apikey=" + API_KEY;
	
	public static String get(String url, String id) {
		return String.format(url, id);
	}
}
