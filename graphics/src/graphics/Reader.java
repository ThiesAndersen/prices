package graphics;

import static tankerkönig.api.Url.TEXT_FILE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This Reader is deprecated, do NOT use
 * @author thies
 * @deprecated
 *
 */
public class Reader {

	private String urlString = "https://ich-tanke.de/tankstellen/super-e5/umkreis/25337-elmshorn/";

	private String price;
	private String name;
	private String street;
	private String plz;

	public void readWebsite() throws IOException, ParseException {
		if (!Files.exists(TEXT_FILE)) {
			Files.createFile(TEXT_FILE);
		}
		Pattern patternPrice = Pattern.compile("Preisangabe in Euro\">(\\d+,\\d+)");
		Pattern patternName = Pattern.compile("-Tankstelle anzeigen\">([a-zA-Z1-9][^#&<>\"~;$^%{}?]{1,100})");
		Pattern patternStreet = Pattern.compile("</a></h4><p>([a-zA-Z1-9][^#&<>\"~;$^%{}?]{1,100})");
		Pattern patternPLZ = Pattern.compile("([a-zA-Z1-9][^#&<>\"~;$^%{}?]{1,100})</a></p><p><span");
		
		ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(TEXT_FILE, StandardCharsets.UTF_8);
		
		String line = null;
		String seperator = ";";
		URL url = null;	
		
		InputStreamReader inputStreamReader = null;
		url = new URL(urlString);
		if (url != null) {
			inputStreamReader = new InputStreamReader(url.openStream(), "UTF8");
		}
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		System.out.println("Write in file -> " + TEXT_FILE);
		
		while ((line = bufferedReader.readLine()) != null) {
			Matcher matcherPrice = patternPrice.matcher(line);
			Matcher matcherName = patternName.matcher(line);
			Matcher matcherStreet = patternStreet.matcher(line);
			Matcher matcherPLZ = patternPLZ.matcher(line);
			while (matcherPrice.find()) {
				reset();
				price = matcherPrice.group(1) + "€";
				if (matcherName.find()) {
					name = matcherName.group(1);
				}
				if (matcherStreet.find()) {
					street = matcherStreet.group(1);
				}
				if (matcherPLZ.find()) {
					plz = matcherPLZ.group(1);
				}

				lines.add(name + seperator + price + seperator + street.substring(0, street.length() - 4)
						+ seperator + plz + seperator + Calendar.getInstance().getTime() + seperator + System.lineSeparator());
				
			}
		}
		Files.write(TEXT_FILE, lines, StandardCharsets.UTF_8, StandardOpenOption.WRITE);
	}
	
	private void reset() {
		price = "";
		name = "";
		street = "test ";
		plz = "";
	}
}
