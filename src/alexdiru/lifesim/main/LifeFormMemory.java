package alexdiru.lifesim.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import alexdiru.lifesim.helpers.ItemsList;

public class LifeFormMemory {
	
	private int memorySize;
	private List<Item> storedItems = new CopyOnWriteArrayList<Item>();
	
	public LifeFormMemory(int memorySize) {
		this.memorySize = memorySize;
	}
	
	public void clear() {
		storedItems.clear();
	}
	
	public void store(Item item) { 
		if (memorySize == 0)
			return;
		
		//Duplicate items
		if (storedItems.contains(item))
			return;

		if (storedItems.size() >= memorySize)
			storedItems.remove(0);

		storedItems.add(item);
	}
	
	public void store(ItemsList items) {
		for (Item item : items)
			store(item);
	}
	
	public Item getClosestItem(LifeForm lifeForm) {
		return LifeForm.getClosestItemInList(storedItems, lifeForm.getXPosition(), lifeForm.getYPosition());
	}
	
	public void removeFromMemory(Item item) {
		storedItems.remove(item);
	}

	public List<Item> getItems() {
		return storedItems;
	}
}
