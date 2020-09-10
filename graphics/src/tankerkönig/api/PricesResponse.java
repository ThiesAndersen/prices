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

	private String ok;
	private String status;
	private String license;
	private String data;
	private JsonObject prices;
	
	public PricesResponse(String ok, String status, String license, String data, JsonObject prices) {
		this.ok = ok;
		this.status = status;
		this.license = license;
		this.data = data;
		this.prices = prices;
	}

	public String getok() {
		return ok;
	}
	public String getStatus() {
		return status;
	}

	public String getLicense() {
		return license;
	}

	public String getData() {
		return data;
	}
	
	public JsonObject getprices() {
		return prices;
	}

	@Override
	public String toString() {
		return String.format("pricesResponse [ok=%s, status=%s, license=%s, data=%s]", ok, status, license, data);
	}
}
