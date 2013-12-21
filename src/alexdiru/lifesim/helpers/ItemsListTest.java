package alexdiru.lifesim.helpers;

import java.awt.Point;

import junit.framework.Assert;

import org.junit.Test;

import alexdiru.lifesim.main.Food;
import alexdiru.lifesim.main.Item;
import alexdiru.lifesim.main.LifeForm;

public class ItemsListTest {

	@Test
	public void testSort() {
		LifeForm lifeForm = new LifeForm(null);
		lifeForm.setPosition(new Point(0,0));
		ItemsList list = new ItemsList(lifeForm);

		Item item1 = new Food(1,1);
		Item item4 = new Food(100,100);
		Item item2 = new Food(1,2);
		Item item3 = new Food(-3,2);
		list.add(item1);
		list.add(item4);
		list.add(item2);
		list.add(item3);
		
		list.sort();

		Assert.assertTrue(list.get(0) == item4);
		Assert.assertTrue(list.get(1) == item3);
		Assert.assertTrue(list.get(2) == item2);
		Assert.assertTrue(list.get(3) == item1);
	}
}
