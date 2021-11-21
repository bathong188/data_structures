/**
  *  Program #3
  *  Implementation of a Priority Queue using a binary heap
  *  CS310-1
  *  04/10/2019
  *  @author  Thong Le cssc1489
  */

package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinaryHeapPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E>, Iterable<E> {
	private final Wrapper<E>[] storage;
	private int size;
	private int entryNumber;
	private final int maxCapacity;
	private long modificationCounter;

	/**
	 * Wrapper class holds the input object and its entry number to determine its
	 * priority
	 */
	@SuppressWarnings("hiding")
	protected class Wrapper<E> implements Comparable<Wrapper<E>> {
		private final long number;
		private final E data;

		/**
		 * Constructor for Wrapper class
		 * 
		 * @param d
		 *            holds the input object
		 */
		public Wrapper(E d) {
			number = entryNumber++;
			data = d;
		}

		@SuppressWarnings("unchecked")
		public int compareTo(Wrapper<E> o) {

			// if two objects are equal, compare their entry number
			if (((Comparable<E>) data).compareTo(o.data) == 0)
				return (int) (number - o.number);
			return ((Comparable<E>) data).compareTo(o.data);
		}
	}

	/**
	 * Constructor with unspecified (default) capacity
	 */
	@SuppressWarnings("unchecked")
	public BinaryHeapPriorityQueue() {
		storage = new Wrapper[DEFAULT_MAX_CAPACITY];
		this.maxCapacity = DEFAULT_MAX_CAPACITY;
		modificationCounter = size = entryNumber = 0;
	}

	/**
	 * Constructor with a user defined capacity
	 * 
	 * @param maxCapacity
	 *            user defined capacity
	 */
	@SuppressWarnings("unchecked")
	public BinaryHeapPriorityQueue(int maxCapacity) {
		storage = new Wrapper[maxCapacity];
		this.maxCapacity = maxCapacity;
		modificationCounter = size = entryNumber = 0;
	}

	/**
	 * Inserts a new object into the priority queue. Returns true if the insertion
	 * is successful. If the PQ is full, the insertion is aborted, and the method
	 * returns false.
	 */
	@Override
	public boolean insert(E object) {
		if (isFull())
			return false;
		Wrapper<E> wrappedObj = new Wrapper<E>(object);
		storage[size++] = wrappedObj;
		modificationCounter++;
		trickleUp(size - 1);
		return true;
	}

	/**
	 * Removes the smallest object of highest priority that has been in the PQ the
	 * longest, and returns it. Returns null if the PQ is empty.
	 */
	@Override
	public E remove() {
		if (isEmpty())
			return null;
		E objToReturn = storage[0].data;
		trickleDown(0);
		size--;
		modificationCounter++;
		return objToReturn;
	}

	/**
	 * 
	 */
	@Override
	public boolean delete(E obj) {
		if (isEmpty())
			return false;
		boolean isFound = false;
		for (int i = 0; i < size; i++) {
			if (storage[i].data.compareTo(obj) == 0) {
				isFound = true;

				// instance of obj at index 0, behaves like remove()
				if (i == 0)
					remove();
				else {
					int parent = (i - 1) >> 1;

					// last element is smaller than parent node of current node (at index i)
					if (storage[size - 1].data.compareTo(storage[parent].data) < 0) {
						storage[i] = storage[size - 1];
						trickleUp(i);
					}

					// last element is greater than parent node of current node (at index i)
					else {
						trickleDown(i);
					}
					size--;
					modificationCounter++;
				}
				i--;
			}
		}
		return isFound;
	}

	/**
	 * Trickles up the object in current index to follow min heap order. Assumes
	 * that user has placed object to trickle up in index.
	 * 
	 * @param index
	 *            the starting index to trickle up in heap
	 */
	private void trickleUp(int index) {
		int newIndex = index;
		int parentIndex = (newIndex - 1) >> 1;
		Wrapper<E> newValue = storage[newIndex];
		while (parentIndex >= 0 && newValue.compareTo(storage[parentIndex]) < 0) {
			storage[newIndex] = storage[parentIndex];
			newIndex = parentIndex;
			parentIndex = (parentIndex - 1) >> 1;
		}
		storage[newIndex] = newValue;
	}

	/**
	 * Uses the last element (at index size - 1) to trickles down from index to a
	 * location that follows min heap order. Then overwrites that location with the
	 * last element (it still remains at index size - 1). Assumes user has not moved
	 * the last element to index.
	 * 
	 * @param index
	 *            the starting index to trickle down in heap
	 */
	private void trickleDown(int index) {
		int current = index;
		int child = getNextChild(current);
		while (child != -1 && storage[current].compareTo(storage[child]) < 0
				&& storage[child].compareTo(storage[size - 1]) < 0) {
			storage[current] = storage[child];
			current = child;
			child = getNextChild(current);
		}
		storage[current] = storage[size - 1];
	}

	/**
	 * Finds and returns the index of the smaller child. Returns index of left child
	 * if there is only one child. Returns -1 if there are no children.
	 * 
	 * @param current
	 *            the parent index to find its children
	 * @return index of the smaller child if exists, else -1
	 */
	private int getNextChild(int current) {
		int left = (current << 1) + 1;
		int right = left + 1;
		if (right < size) { // there are two children
			if (storage[left].compareTo(storage[right]) < 0)
				return left; // the left child is smaller
			return right; // the right child is smaller
		}
		if (left < size) // there is only one child
			return left;
		return -1; // no children
	}

	/**
	 * Returns the smallest object of highest priority that has been in the PQ the
	 * longest. Returns null if the PQ is empty.
	 */
	@Override
	public E peek() {
		if (isEmpty())
			return null;
		return storage[0].data;
	}

	/**
	 * Returns true if the priority queue contains the specified element false
	 * otherwise.
	 */
	@Override
	public boolean contains(E obj) {
		if (isEmpty())
			return false;

		// linear search of index
		for (int i = 0; i < size; i++) {
			if ((storage[i].data).compareTo(obj) == 0)
				return true;
		}
		return false;
	}

	/**
	 * Returns the number of objects currently in the PQ.
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Returns the PQ to an empty state.
	 */
	@Override
	public void clear() {
		size = 0;
		modificationCounter++;
	}

	/**
	 * Returns true if the PQ is empty, otherwise false.
	 */
	@Override
	public boolean isEmpty() {
		return (size == 0);
	}

	/**
	 * Returns true if the PQ is full, otherwise false.
	 */
	@Override
	public boolean isFull() {
		return (size == maxCapacity);
	}

	@Override
	public Iterator<E> iterator() {
		return new IteratorHelper();
	}

	class IteratorHelper implements Iterator<E> {
		private int iterIndex;
		private final long stateCheck;
		private final Wrapper<E>[] iterArray;
		private Wrapper<E> temp;

		/**
		 * Constructor of IteratorHelper class, creates a new array for iterator copies
		 * all elements and shell sorts them (follows PQ order).
		 */
		public IteratorHelper() {
			iterIndex = 0;
			int h = 1;
			stateCheck = modificationCounter;
			iterArray = storage.clone();

			// shell sort
			while (h <= size / 3)
				h = h * 3 + 1;
			while (h > 0) {
				for (int out = h; out < size; out++) {
					temp = iterArray[out];
					int in = out;
					while (in > h - 1 && iterArray[in - h].compareTo(temp) >= 0) {
						iterArray[in] = iterArray[in - h];
						in -= h;
					}
					iterArray[in] = temp;
				}
				h = (h - 1) / 3;
			}
		}

		@Override
		public boolean hasNext() {
			if (stateCheck != modificationCounter)
				throw new ConcurrentModificationException();
			return iterIndex < size;
		}

		@Override
		public E next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return iterArray[iterIndex++].data;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
