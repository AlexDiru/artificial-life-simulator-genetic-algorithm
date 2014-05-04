package alexdiru.lifesim.helpers;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

public class FileHelperTest extends TestCase {
	@SuppressWarnings("deprecation")
	@Test
	public void testFileIO() {
		List<String> lines = Arrays.asList("hello", "world","im","alex");
		FileHelper.writeAllLines("test.txt", lines);
		lines = FileHelper.readAllLines("test.txt");

		Assert.assertTrue(lines.get(0).equals("hello"));
		Assert.assertTrue(lines.get(1).equals("world"));
		Assert.assertTrue(lines.get(2).equals("im"));
		Assert.assertTrue(lines.get(3).equals("alex"));
	}
}
