/**
 * 
 */
package tankerkönig.api;

import com.google.gson.JsonArray;

/**
 * @author thies
 *
 */
public class StationsResponse {
	
	private boolean ok;
	private String license;
	private String data;
	private String status;
	private JsonArray stations;
	
	public StationsResponse(boolean ok, String license, String data, String status, JsonArray stations) {
		this.ok = ok;
		this.license = license;
		this.data = data; 
		this.status = status; 
		this.stations = stations;
	}

	/**
	 * @return the ok
	 */
	public boolean isOk() {
		return ok;
	}

	/**
	 * @return the license
	 */
	public String getLicense() {
		return license;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return the stations
	 */
	public JsonArray getStations() {
		return stations;
	}

	@Override
	public String toString() {
		return String.format("StationsResponse [ok=%s, license=%s, data=%s, status=%s, stations=%s]", ok, license, data, status, stations);
	}
}
