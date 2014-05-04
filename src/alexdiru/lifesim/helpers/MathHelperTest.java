package alexdiru.lifesim.helpers;

import junit.framework.TestCase;
import junitx.framework.Assert;

import org.junit.Test;

public class MathHelperTest extends TestCase{
	@SuppressWarnings("deprecation")
	@Test
	public void testIsSquareNumber() {
		Assert.assertTrue(MathHelper.isSquareNumber(1));
		Assert.assertTrue(!MathHelper.isSquareNumber(2));
		Assert.assertTrue(MathHelper.isSquareNumber(4));
		Assert.assertTrue(MathHelper.isSquareNumber(9));
		Assert.assertTrue(!MathHelper.isSquareNumber(15));
		Assert.assertTrue(MathHelper.isSquareNumber(16));
		Assert.assertTrue(MathHelper.isSquareNumber(4507129));
		Assert.assertTrue(!MathHelper.isSquareNumber(4507130));
	}
}
