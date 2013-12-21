package alexdiru.lifesim.helpers;

import java.util.Comparator;

import alexdiru.lifesim.main.Item;
import alexdiru.lifesim.main.LifeForm;

/**
 * A custom class to encapsulate the functionality required to store the data  of the items that a life form can see
 */
public class ItemsList extends ConcurrentList<Item> {

    /**
     * The life form which is seeing the items
     */
	private LifeForm lifeForm;

    /**
     * Creates the list
     * @param lifeForm The life form which is seeing the items
     */
	public ItemsList(LifeForm lifeForm) {
		super();
		this.lifeForm = lifeForm;
	}

    /**
     * Returns a comparator which sorts the items by their distance to the life form in descending order
     * @return The comparator
     */
	@Override
	protected Comparator<Item> getComparator() {
		return new Comparator<Item>() {
			@Override
			public int compare(Item i1, Item i2) {
				return Double.compare(MathHelper.getDistanceSquared(lifeForm, i2), MathHelper.getDistanceSquared(lifeForm, i1));
			}
		};
	}
}
