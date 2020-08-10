/**
 * 
 */
package graphics;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author thies
 *
 */
public class test {
	
	private static Path path 			= Paths.get("C:\\Users\\thies\\Desktop\\Tankstellenpreiseeee.txt");
	
	public static void main(String[] args) {

		try {
			if (!Files.exists(path)) {
				Files.createFile(path);
			}
			BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.WRITE);
			String test = "TEST";
			writer.write(test);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	

}
