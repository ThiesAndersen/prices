/**
 * 
 */
package tankerkönig.api;

import com.google.gson.JsonObject;

/**
 * @author thies
 *
 */
public class PricesResponse {

	private boolean ok;
	private String status;
	private String license;
	private String data;
	private JsonObject prices;
	
	public PricesResponse(boolean ok, String status, String license, String data, JsonObject prices) {
		this.ok = ok;
		this.status = status;
		this.license = license;
		this.data = data;
		this.prices = prices;
	}

	/**
	 * @return ok
	 */
	public boolean isOk() {
		return ok;
	}
	
	/**
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return license
	 */
	public String getLicense() {
		return license;
	}

	/**
	 * @return data
	 */
	public String getData() {
		return data;
	}
	
	/**
	 * @return prices
	 */
	public JsonObject getprices() {
		return prices;
	}

	@Override
	public String toString() {
		return String.format("pricesResponse [ok=%s, status=%s, license=%s, data=%s]", ok, status, license, data);
	}
}
