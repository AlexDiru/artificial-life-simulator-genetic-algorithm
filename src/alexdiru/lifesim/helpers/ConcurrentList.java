package alexdiru.lifesim.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Replaces CopyOnWriteArrayList as the code required to sort them is quite ugly
 * @author Alex
 *
 * @param <T> The type of the objects the list will hold
 */
public abstract class ConcurrentList<T> implements Iterable<T> {

    /**
     * The list to hold the data
     */
	private List<T> list;
	
	public ConcurrentList() {
		list = new CopyOnWriteArrayList<T>();
	}

    /**
     * Returns a comparator to sort the elements in the list by
     * @return The comparator
     */
    protected abstract Comparator<T> getComparator();

    /**
     * Gets an element from the list at the specified index
     * @param index The index of he element to get
     * @return The element
     */
	public T get(int index) {
		return list.get(index);
	}

    /**
     * Adds an element to the list
     * @param element The element to add
     */
	public void add(T element) {
		list.add(element);
	}

    /**
     * Sorts the list using the subclass's comparator
     */
	public void sort() {
        synchronized (this) {
            List<T> itemList = new ArrayList<T>();
            itemList.addAll(list);

            for (int i = 0; i < itemList.size(); i++)
                if (itemList.get(i) == null)
                    itemList.remove(i);

            Collections.sort(itemList, getComparator());
            list.clear();
            list.addAll(itemList);
        }
	}

    /**
     * Removes an element from the list
     * @param element The element to remove
     */
	public void remove(T element) {
		list.remove(element);
	}

    /**
     * Removes all elements from the list
     */
	public void clear() {
		list.clear();
	}

    /**
     * Gets an iterator of the list
     * @return The iterator
     */
	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

    /**
     * Returns the number of elements in the list
     * @return The list's size
     */
	public int size() {
		return list.size();
	}
}