/**
  *  Program #4
  *  BST implementation of the DictionaryADT
  *  CS310-1
  *  05/09/2019
  *  @author  Thong Le cssc1489
  */

package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

public class BinarySearchTree<K extends Comparable<K>, V> implements DictionaryADT<K, V> {
	private Node<K, V> root;
	private long modCounter;
	private int curSize;

	@SuppressWarnings("hiding")
	private class Node<K, V> {
		private K key;
		private V value;
		private Node<K, V> leftChild;
		private Node<K, V> rightChild;

		public Node(K key, V value) {
			this.key = key;
			this.value = value;
			leftChild = rightChild = null;
		}
	}

	public BinarySearchTree() {
		root = null;
		curSize = 0;
	}

	// Returns true if the dictionary has an object identified by
	// key in it, otherwise false.
	@Override
	public boolean contains(K key) {
		return getValue(key) != null;
	}

	// Adds the given key/value pair to the dictionary. Returns
	// false if the dictionary is full, or if the key is a duplicate.
	// Returns true if addition succeeded.
	@Override
	public boolean add(K key, V value) {
		if (contains(key) == true) return false;	// duplicate, reject
		if (root == null)							// add when empty
			root = new Node<K, V>(key, value);
		else
			insert(key, value, root, null, false);
		curSize++;
		modCounter++;
		return true;
	}

	// Inserts new key/value pair in BST structure ordered by key
	private void insert(K key, V value, Node<K, V> node, Node<K, V> parent, boolean wasLeft) {
		if (node == null) {
			if (wasLeft)
				parent.leftChild = new Node<K, V>(key, value);
			else
				parent.rightChild = new Node<K, V>(key, value);
		}
		else if (((Comparable<K>)key).compareTo((K) node.key) < 0)
			insert(key, value, node.leftChild, node, true);		// go left
		else
			insert(key, value, node.rightChild, node, false);	// go right
	}
	
	// Deletes the key/value pair identified by the key parameter.
	// Returns true if the key/value pair was found and removed,
	// otherwise false.
	@Override
	public boolean delete(K key) {
		if (isEmpty())					// empty tree
			return false;
		
		boolean deleteRoot = false;		// indicates whether deleting root
		if (((Comparable<K>)key).compareTo(root.key) == 0) {
			deleteRoot = true;
		}
		
		if (deleteRoot) {	// case 1: delete at root
			// case 1.1: zero children
			if (root.leftChild == null && root.rightChild == null) {
				root = null;
			}
			// case 1.2: one child (left)
			else if (root.leftChild != null && root.rightChild == null) {
				root = root.leftChild;
			}
			// case 1.3: one child (right)
			else if (root.leftChild == null && root.rightChild != null) {
				root = root.rightChild;
			}
			// case 1.4: two children
			else {
				// find minimum value on right subtree
				Node<K, V> successor = root.rightChild;
				Node<K, V> sucParent = null;
				while (successor.leftChild != null) {
					sucParent = successor;
					successor = successor.leftChild;
				}
				if (sucParent == null) {	// right child doesn't have left children
					successor.leftChild = root.leftChild;
				}
				else {
					sucParent.leftChild = successor.rightChild;
					successor.leftChild = root.leftChild;
					successor.rightChild = root.rightChild;
				}
				root = successor;
			}
		}
		else {				// case 2: delete at !root
			Node<K, V> current = root;	// current starts at root
			Node<K, V> parent = null;
			boolean wasLeft = false;	// tracks relationship between current and parent nodes
			
			// look for node to delete
			while (((Comparable<K>) key).compareTo(current.key) != 0) {
				if (((Comparable<K>) key).compareTo(current.key) < 0) {
					parent = current;
					current = current.leftChild;
					wasLeft = true;
				} else {
					parent = current;
					current = current.rightChild;
					wasLeft = false;
				}
				if (current == null)
					return false; // not found
			}
			// case 2.1: zero children
			if (current.leftChild == null && current.rightChild == null) {
				if (wasLeft) parent.leftChild = null;
				else parent.rightChild = null;
			}
			// case 2.2: one child (left)
			else if (current.leftChild != null && current.rightChild == null) {
				if (wasLeft) parent.leftChild = current.leftChild;
				else parent.rightChild = current.leftChild;
			}
			// case 2.3: one child (right)
			else if (current.leftChild == null && current.rightChild != null) {
				if (wasLeft) parent.leftChild = current.rightChild;
				else parent.rightChild = current.rightChild;
			}
			// case 2.4: two children
			else if (current.leftChild != null && current.rightChild != null) {
				// find minimum value on right subtree
				Node<K, V> successor = current.rightChild;
				Node<K, V> sucParent = null;
				while (successor.leftChild != null) {
					sucParent = successor;
					successor = successor.leftChild;
				}
				if (sucParent == null) {	// right child doesn't have left children
					successor.leftChild = current.leftChild;
				}
				else {
					sucParent.leftChild = successor.rightChild;
					successor.leftChild = current.leftChild;
					successor.rightChild = current.rightChild;
				}
				if (wasLeft) parent.leftChild = successor;
				else parent.rightChild = successor;
			}
		}			
		curSize--;
		modCounter++;
		return true;
	}
	
	// Returns the value associated with the parameter key. Returns
	// null if the key is not found or the dictionary is empty.
	@Override
	public V getValue(K key) {
		return findValue(key, root);
	}

	private V findValue(K key, Node<K, V> node) {
		if (node == null)
			return null;
		if (((Comparable<K>) key).compareTo(node.key) < 0)	// go left
			return findValue(key, node.leftChild);
		if (((Comparable<K>) key).compareTo(node.key) > 0)	// go right
			return findValue(key, node.rightChild);
		return node.value;
	}

	// Returns the key associated with the parameter value. Returns
	// null if the value is not found in the dictionary. If more
	// than one key exists that matches the given value, returns the
	// first one found.
	@Override
	public K getKey(V value) {
		return findKey(value, root);
	}

	// Traverses recursively through tree and compare specified value
	@SuppressWarnings("unchecked")
	private K findKey(V value, Node<K, V> node) {
		if (node == null)
			return null;
		if (((Comparable<V>)value).compareTo(node.value) == 0)
			return node.key;
		K key = findKey(value, node.leftChild);
		if (key != null)
			return key;
		return findKey(value, node.rightChild);
	}

	@Override
	public int size() {
		return curSize;
	}

	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return root == null;
	}

	@Override
	public void clear() {
		root = null;
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
			return (K) iterArray[iterIndex++].key;
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
			return (V) iterArray[iterIndex++].value;
		}
	}

	abstract class IteratorHelper<E> implements Iterator<E> {
		protected Node<K, V>[] iterArray;
		protected int iterIndex;
		protected int fillIndex;
		protected long modCheck;
		
		@SuppressWarnings("unchecked")
		public IteratorHelper() {
			iterArray = new Node[curSize];
			iterIndex = 0;
			fillIndex = 0;
			modCheck = modCounter;
			inorderFillArray(root);
		}

		// Fills iterator array in-order
		private void inorderFillArray(Node<K, V> node) {
			if (node == null) return;
			inorderFillArray(node.leftChild);
			iterArray[fillIndex++] = node;
			inorderFillArray(node.rightChild);
		}
		
		@Override
		public boolean hasNext() {
			if (modCheck != modCounter)
				throw new ConcurrentModificationException();
			return iterIndex < curSize;
		}

		@Override
		public abstract E next();

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
