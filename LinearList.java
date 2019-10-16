/**
  *  Program #2
  *  Implementation of doubly linked list to store data
  *  CS310-1
  *  03/13/2019
  *  @author  Thong Le cssc1489
  */

package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

public class LinearList<E extends Comparable<E>> implements LinearListADT<E>, Iterable<E> {

	@SuppressWarnings("hiding")
	private class Node<E> {
		E data;
		Node<E> next;
		Node<E> prev;

		/**
		 * Constructor for Node class
		 * 
		 * @param data holds the input object
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
	 * Adds the Object obj to the beginning of list and returns true if the list is
	 * not full
	 * 
	 * @param obj any object
	 */
	public boolean addFirst(E obj) {
		Node<E> newNode = new Node<E>(obj);
		if (isEmpty())
			head = tail = newNode;
		else {
			newNode.next = head;
			head.prev = newNode;
			head = newNode;
		}
		modificationCounter++;
		size++;
		return true;
	}

	/**
	 * Adds the Object obj to the end of list and returns true if the list is not
	 * full
	 * 
	 * @param obj any object
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
	 * @param obj any object
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
	 * Returns the first element in the list, null if the list is empty. The list is
	 * not modified.
	 * 
	 * @return null if the linked list is empty, else returns object at head
	 */
	public E peekFirst() {
		if (!isEmpty())
			return head.data;
		else
			return null;
	}

	/**
	 * Returns the last element in the list, null if the list is empty. The list is
	 * not modified.
	 * 
	 * @return null if the linked list is empty, else returns object at tail
	 */
	public E peekLast() {
		if (!isEmpty())
			return tail.data;
		else
			return null;
	}

	/**
	 * Returns true if the parameter object obj is in the list, false otherwise. The
	 * list is not modified.
	 * 
	 * @param obj any object
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
	 * @param obj any object
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
