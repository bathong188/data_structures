/**
  *  Program #4
  *  Hash table implementation of the DictionaryADT interface, using chaining
  *  CS310-1
  *  05/09/2019
  *  @author  Thong Le cssc1489
  */

package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

public class Hashtable<K extends Comparable<K>, V extends Comparable<V>> implements DictionaryADT<K, V> {
	private int curSize;
	private final int maxSize;
	private final int tableSize;
	private long modCounter;
	private final LinearList<DictionaryNode<K, V>>[] list;

	@SuppressWarnings("unchecked")
	public Hashtable(int n) {
		curSize = 0;
		maxSize = n;
		tableSize = (int) (maxSize * 1.3f);
		modCounter = 0;
		list = new LinearList[tableSize];
		for (int i = 0; i < tableSize; i++)
			list[i] = new LinearList<DictionaryNode<K, V>>();
	}

	@SuppressWarnings("hiding")
	private class DictionaryNode<K, V> implements Comparable<DictionaryNode<K, V>> {
		K key;
		V value;

		public DictionaryNode(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@SuppressWarnings("unchecked")
		@Override
		public int compareTo(DictionaryNode<K, V> node) {
			return ((Comparable<K>) key).compareTo((K) node.key);
		}
	}

	private int getHashCode(K key) {
		return ((key.hashCode() & 0x7FFFFFFF) % tableSize);
	}

	@Override
	public boolean contains(K key) {
		return list[getHashCode(key)].contains(new DictionaryNode<K, V>(key, null));
	}

	@Override
	public boolean add(K key, V value) {
		if (isFull())
			return false;
		if (list[getHashCode(key)].contains(new DictionaryNode<K, V>(key, null)))
			return false;
		list[getHashCode(key)].addLast(new DictionaryNode<K, V>(key, value));
		curSize++;
		modCounter++;
		return true;
	}

	@Override
	public boolean delete(K key) {
		if (isEmpty())
			return false;
		if (list[getHashCode(key)].remove(new DictionaryNode<K, V>(key, null)) == null)
			return false;
		curSize--;
		modCounter++;
		return true;
	}

	@Override
	public V getValue(K key) {
		DictionaryNode<K, V> temp = list[getHashCode(key)].find(new DictionaryNode<K, V>(key, null));
		if (temp == null)
			return null;
		return temp.value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public K getKey(V value) {
		for (int i = 0; i < list.length; i++) {
			Iterator<DictionaryNode<K, V>> tempList = list[i].iterator();
			while (tempList.hasNext()) {
				DictionaryNode<K, V> tempNode = tempList.next();
				if (((Comparable<V>) tempNode.value).compareTo(value) == 0)
					return tempNode.key;
			}
		}
		return null;
	}

	@Override
	public int size() {
		return curSize;
	}

	@Override
	public boolean isFull() {
		return curSize == maxSize;
	}

	@Override
	public boolean isEmpty() {
		return curSize == 0;
	}

	@Override
	public void clear() {
		for (int i = 0; i < list.length; i++)
			list[i].clear();
		curSize = 0;
		modCounter++;
	}

	@Override
	public Iterator<K> keys() {
		return new IteratorKeys<K>();
	}

	@Override
	public Iterator<V> values() {
		return new IteratorValues<V>();
	}

	@SuppressWarnings("hiding")
	private class IteratorKeys<K> extends IteratorHelper<K> {
		public IteratorKeys() {
			super();
		}

		@SuppressWarnings("unchecked")
		public K next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return (K) iterArray[index++].key;
		}
	}

	@SuppressWarnings("hiding")
	private class IteratorValues<V> extends IteratorHelper<V> {
		public IteratorValues() {
			super();
		}

		@SuppressWarnings("unchecked")
		public V next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return (V) iterArray[index++].value;
		}
	}

	abstract class IteratorHelper<E> implements Iterator<E> {
		protected DictionaryNode<K, V>[] iterArray;
		protected DictionaryNode<K, V> temp;
		protected int index;
		protected long modCheck;

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public IteratorHelper() {
			iterArray = new DictionaryNode[curSize];
			index = 0;
			int j = 0;
			int h = 1;
			modCheck = modCounter;
			
			// Fills iterator array with all elements in hash table
			for (int i = 0; i < tableSize; i++)
				for (DictionaryNode n : list[i])
					iterArray[j++] = n;

			// Shell sort iterator array
			while (h <= curSize / 3)
				h = h * 3 + 1;
			while (h > 0) {
				for (int out = h; out < curSize; out++) {
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
			if (modCheck != modCounter)
				throw new ConcurrentModificationException();
			return index < curSize;
		}

		@Override
		public abstract E next();

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	//-----------------------------------------------------------------------------------------------
	
	private class LinearList<E extends Comparable<E>> implements Iterable<E> {

		@SuppressWarnings("hiding")
		private class Node<E> {
			E data;
			Node<E> next;
			Node<E> prev;

			/**
			 * Constructor for Node class
			 * 
			 * @param data
			 *            holds the input object
			 */
			public Node(E data) {
				this.data = data;
				next = null;
				prev = null;
			}
		}

		private Node<E> head, tail;
		private int size, modificationCounter;

		/**
		 * Constructor for LinearList
		 */
		public LinearList() {
			head = tail = null;
			size = modificationCounter = 0;
		}

		/**
		 * Adds the Object obj to the end of list and returns true if the list is not
		 * full
		 * 
		 * @param obj
		 *            any object
		 */
		public boolean addLast(E obj) {
			Node<E> newNode = new Node<E>(obj);
			if (isEmpty()) // 1 element in linked list
				head = tail = newNode;
			else { // 2+ elements in linked list
				tail.next = newNode;
				newNode.prev = tail;
				tail = newNode;
			}
			modificationCounter++;
			size++;
			return true;
		}

		/**
		 * Removes and returns the parameter object obj in first position in list if the
		 * list is not empty, null if the list is empty.
		 * 
		 * @return null if object is not in linked list, else returns the removed object
		 */
		public E removeFirst() {
			if (!isEmpty()) {
				E objToReturn = head.data;
				if (head == tail) { // 1 element in linked list
					head = tail = null;
				} else { // 2+ elements in linked list
					head = head.next;
					head.prev = null;
				}
				modificationCounter++;
				size--;
				return objToReturn;
			} else
				return null;
		}

		/**
		 * Removes and returns the parameter object obj in last position in list if the
		 * list is not empty, null if the list is empty.
		 * 
		 * @return null if object is not in linked list, else returns the removed object
		 */
		public E removeLast() {
			if (!isEmpty()) {
				E objToReturn = tail.data;
				if (head == tail) { // 1 element in linked list
					head = tail = null;
				} else { // 2+ elements case
					tail = tail.prev;
					tail.next = null;
				}
				modificationCounter++;
				size--;
				return objToReturn;
			} else
				return null;
		}

		/**
		 * Removes and returns the parameter object obj from the list if the list
		 * contains it, null otherwise. The ordering of the list is preserved. The list
		 * may contain duplicate elements. This method removes and returns the first
		 * matching element found when traversing the list from first position. Note
		 * that you may have to shift elements to fill in the slot where the deleted
		 * element was located.
		 * 
		 * @param obj
		 *            any object
		 * @return null if object is not in linked list, else returns the removed object
		 */
		public E remove(E obj) {
			Node<E> current = head;
			E objToReturn = null;
			if (isEmpty())
				return null;
			if (head == tail) { // case where there is 1 element
				objToReturn = current.data;
				head = tail = null;
			} else { // case where there are 2+ elements
				/* if remove at first */
				if (((Comparable<E>) head.data).compareTo(obj) == 0)
					return removeFirst();
				/* if remove at last */
				else if (((Comparable<E>) tail.data).compareTo(obj) == 0)
					return removeLast();
				/* if remove at middle */
				else {
					while (current != null) {
						if (((Comparable<E>) current.data).compareTo(obj) == 0) {
							objToReturn = current.data;
							current.next.prev = current.prev;
							current.prev.next = current.next;
							break;
						}
						current = current.next; // increment current pointer
					}
				}
			}
			modificationCounter++;
			size--;
			return objToReturn;
		}

		/**
		 * Returns true if the parameter object obj is in the list, false otherwise. The
		 * list is not modified.
		 * 
		 * @param obj
		 *            any object
		 * @return null if the specified object is not found, else returns true
		 */
		public boolean contains(E obj) {
			Node<E> current = head;
			while (current != null) {
				if (((Comparable<E>) current.data).compareTo(obj) == 0)
					return true;
				current = current.next; // increment current pointer
			}
			return false;
		}

		/**
		 * Returns the element matching obj if it is in the list, null otherwise. In the
		 * case of duplicates, this method returns the element closest to front. The
		 * list is not modified.
		 * 
		 * @param obj
		 *            any object
		 * @return null if the specified object is not found, else returns the object
		 */
		public E find(E obj) {
			Node<E> current = head;
			while (current != null) {
				if (((Comparable<E>) current.data).compareTo(obj) == 0)
					return current.data;
				current = current.next; // increment current pointer
			}
			return null;
		}

		/**
		 * The list is returned to an empty state
		 */
		public void clear() {
			head = tail = null;
			size = 0;
			modificationCounter++;
		}

		/**
		 * Returns true if the list is empty (size = 0), otherwise false
		 * 
		 * @return true is list is empty, else false
		 */
		public boolean isEmpty() {
			return (size == 0);
		}

		/**
		 * Returns true if the list is full, otherwise false
		 * 
		 * @return false because linked list is never full
		 */
		public boolean isFull() {
			return false;
		}

		/**
		 * Returns the number of Objects currently in the list
		 * 
		 * @return size
		 */
		public int size() {
			return size;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Iterator iterator() {
			return new IteratorHelper();
		}

		class IteratorHelper implements Iterator<E> {
			Node<E> iterPtr;
			long stateCheck;

			/**
			 * Constructor for iterator class
			 */
			public IteratorHelper() {
				iterPtr = head;
				stateCheck = modificationCounter;
			}

			@Override
			public boolean hasNext() {
				if (stateCheck != modificationCounter)
					throw new ConcurrentModificationException();
				return (iterPtr != null);
			}

			@Override
			public E next() {
				if (!hasNext())
					throw new NoSuchElementException();
				E nodeToReturn = iterPtr.data;
				iterPtr = iterPtr.next;
				return nodeToReturn;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		}
	}
}