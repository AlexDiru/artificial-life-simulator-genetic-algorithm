package alexdiru.lifesim.helpers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.NotImplementedException;

public class FileHelper {
	public static List<String> readAllLines(String filePath) {
		try {
			return (List<String>)FileUtils.readLines(new File(filePath));
		} catch (IOException e) {
			throw new FileDoesntExistException(filePath);
		}
	}


	public static void writeAllLines(String filePath, List<String> lines) {
		try {
			FileUtils.writeLines(new File(filePath), lines);
		} catch (IOException e) {
			throw new FileDoesntExistException(filePath);
		}
	}
	
	private static class FileDoesntExistException extends NotImplementedException {
		public FileDoesntExistException(String message) {
			super(message);
		}
	}

	
	
}
