/**
      *  Program #1
      *  Implementation of circular array using a one dimensional array
      *  CS310-1
      *  02/19/2019
      *  @author  Thong Le cssc1489
      */

package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

public class ArrayLinearList<E extends Comparable<E>> implements LinearListADT<E>, Iterable<E> {
	E[] array;
	int front, rear, size, maxCapacity;
	long modificationCounter;

	/**
	 * Constructor with unspecified (default) capacity
	 */
	@SuppressWarnings("unchecked")
	public ArrayLinearList() {
		array = (E[]) new Comparable[DEFAULT_MAX_CAPACITY];
		this.maxCapacity = DEFAULT_MAX_CAPACITY;
		modificationCounter = 0;
		front = 0;
		rear = 0;
		size = 0;
	}

	/**
	 * Constructor with a user defined capacity
	 * 
	 * @param maxCapacity user defined capacity
	 */
	@SuppressWarnings("unchecked")
	public ArrayLinearList(int maxCapacity) {
		array = (E[]) new Comparable[maxCapacity];
		this.maxCapacity = maxCapacity;
		modificationCounter = 0;
		front = 0;
		rear = 0;
		size = 0;
	}

	/**
	 * Outputs the front and rear index of the circular array
	 */
	public void ends() {
		System.out.println("Front: " + front + " Rear: " + rear);
	}

	
	/**
	 * Adds object to front pointer if array is empty, else shifts front pointer to the left
	 * then adds object
	 * 	@param data any object
	 * 	@return boolean true if array not full, object added; false if array full, no insertion
	 */
	public boolean addFirst(E data) {
		if (isFull())
			return false;
		else {
			if (size == 0) {
				front = rear = 0;
				array[front] = data;
			} else if (front == 0 && size != 0) {
				front = maxCapacity - 1;
				array[front] = data;
			} else {
				front--;
				array[front] = data;
			}
		}
		size++;
		modificationCounter++;
		return true;
	}

	/**
	 * Adds object to rear pointer if array is empty, else shifts rear pointer to the right
	 * then adds object
	 * 	@param data any object
	 * 	@return boolean true if array not full, object added; false if array full, no insertion
	 */
	public boolean addLast(E data) {
		if (isFull())
			return false;
		else {
			if (size == 0) {
				front = rear = 0;
				array[rear] = data;
			} else if (rear == maxCapacity - 1 && size != 0) {
				rear = 0;
				array[rear] = data;
			} else {
				rear++;
				array[rear] = data;
			}
		}
		size++;
		modificationCounter++;
		return true;
	}

	/**
	 * Removes element at front pointer, then shifts front pointer to the right
	 * 	@param data any object
	 * 	@return null if array is empty, else returns object at front pointer
	 */
	public E removeFirst() {
		if (isEmpty() == true) {
			return null;
		}
		if (size == 1) {
			size = 0;
			modificationCounter++;
			return array[front];
		}
		/*special case where front pointer is at end of array
		 * move front pointer to front index (0)
		 */
		if (front == maxCapacity - 1) {	
			front = 0;
			size--;
			modificationCounter++;
			return array[maxCapacity - 1];
		} else { //when front pointer is anywhere else
			front++;
			size--;
			modificationCounter++;
			return array[front - 1];
		}
	}

	/**
	 * Removes element at rear pointer, then shifts rear pointer to the left
	 * 	@param data any object
	 * 	@return null if array is empty, else returns object at rear pointer
	 */
	public E removeLast() {
		if (isEmpty() == true) {
			return null;
		}
		if (size == 1) {
			size = 0;
			modificationCounter++;
			return array[rear];
		}
		/*special case where rear pointer is at front of array
		 * move front pointer to final index (maxCapacity - 1)
		 */
		if (rear == 0) {
			rear = maxCapacity - 1;
			size--;
			modificationCounter++;
			return array[0];
		} else { //when rear pointer is anywhere else
			rear--;
			size--;
			modificationCounter++;
			return array[rear + 1];
		}
	}

	/**
	 * Removes a specified object if exists in circular array
	 * 	@param data any object
	 * 	@return null if object is not in circular array, else returns the removed object
	 */
	public E remove(E data) {
		int index = findIndex(data);	//finds the index of the specified object
		if (index == -1)
			return null;				//returns null if not found (index = -1)
		E temp = array[index];
		/*case where object exists and is the only element in circular array*/
		if (size == 1) {
			size--;
			modificationCounter++;
			return array[index];
		}
		/*case where rear < front, array starts on the right and wraps around to left*/
		if (rear < front) {
			/*subcase where index is in the right portion of circular array
			 * has to shift from index to the end of array (right portion)
			 * and from [0] to rear pointer (left portion)*/
			if (index >= front) {
				for (int i = index; i < maxCapacity - 1; i++) {
					array[i] = array[i + 1];
				}
				array[maxCapacity - 1] = array[0];
				for (int i = 0; i < rear; i++) {
					array[i] = array[i + 1];
				}
			}
			/*subcase where index is in the left portion of circular array
			 * has to shift from index to rear pointer (left portion)*/
			else {
				for (int i = index; i < rear; i++)
				array[i] = array[i + 1];
			}
			/*2 cases to shift the rear*/
			if (rear == 0)
				rear = maxCapacity - 1;
			else rear--;
			size--;
			modificationCounter++;
			return temp;
		}
		/*case where front < rear (contiguous array)
		 * has to shift from index to rear*/
		else { // front < rear
			for (int i = index; i < rear; i++) {
				array[i] = array[i + 1];
			}
			rear--;
			size--;
			modificationCounter++;
			return temp;
		}
	}

	/**
	 * Returns the integer index of the specified object, if not in circular array returns -1
	 * @param data any object
	 * @return index of specified object; -1 if not found
	 */
	private int findIndex(E data) {
		if (front <= rear) {
			for (int i = front; i <= rear; i++) {
				if (array[i].compareTo(data) == 0)
					return i;
			}
		} else { // front > rear
			for (int i = front; i < maxCapacity; i++) {
				if (array[i].compareTo(data) == 0)
					return i;
			}
			for (int i = 0; i <= rear; i++) {
				if (array[i].compareTo(data) == 0)
					return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the object at the front pointer
	 * 	@return object at front pointer; null if array is empty
	 */
	public E peekFirst() {
		if (isEmpty() == true)
			return null;
		else
			return array[front];
	}

	/**
	 * Returns the object at the rear pointer
	 * 	@return object at rear pointer; null if array is empty
	 */
	public E peekLast() {
		if (isEmpty() == true)
			return null;
		else
			return array[rear];
	}

	/**
	 * Returns true if the specified object is found in circular array, false if not found
	 * 	@param data any object
	 * 	@return boolean true if object is found in circular array, false if not
	 */
	public boolean contains(E data) {
		if (isEmpty() == true) {
			return false;
		} else {
			return findIndex(data) != -1;	//findIndex() returns -1 if object is not found
		}
	}

	/**
	 * Returns the object if it is found in circular array, null if not
	 */
	public E find(E data) {
		if (isEmpty() == true)
			return null;
		else {
			if (findIndex(data) == -1)
				return null;
			else
				return array[findIndex(data)];
		}
	}

	/**
	 * Resets front and rear pointers and size to initial value of empty array
	 */
	public void clear() {
		size = front = rear = 0;
	}

	public boolean isEmpty() {
		return (size == 0);
	}

	public boolean isFull() {
		return (size == maxCapacity);
	}

	public int size() {
		return size;
	}

	public Iterator<E> iterator() {
		return new IteratorHelper();
	}

	class IteratorHelper implements Iterator<E> {
		int iterIndex;
		long stateCheck;
		E[] iterArray;

		/**
		 * Constructor of IteratorHelper class, creates a new array for iterator
		 * copies all elements from circular array starting at front to rear
		 * to iterator array starting at index 0
		 */
		@SuppressWarnings("unchecked")
		public IteratorHelper() {
			iterIndex = 0;
			stateCheck = modificationCounter;
			iterArray = (E[]) new Comparable[maxCapacity];
			if (front <= rear) {
				int y = 0;
				for (int i = front; i <= rear; i++) {
					iterArray[y++] = array[i];
				}
			}
			else { // front > rear
				int y = 0;
				for (int i = front; i < maxCapacity; i++) {
					iterArray[y++] = array[i];
				}
				for (int i = 0; i <= rear; i++) {
						iterArray[y++] = array[i];
				}
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
			return iterArray[iterIndex++];
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
