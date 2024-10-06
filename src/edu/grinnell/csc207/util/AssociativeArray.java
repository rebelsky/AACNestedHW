package edu.grinnell.csc207.util;

import static java.lang.reflect.Array.newInstance;

/**
 * A basic implementation of Associative Arrays with keys of type K
 * and values of type V. Associative Arrays store key/value pairs
 * and permit you to look up values by key.
 *
 * @author Your Name Here
 * @author Samuel A. Rebelsky
 */
public class AssociativeArray<K, V> {
  // +-----------+---------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The default capacity of the initial array.
   */
  static final int DEFAULT_CAPACITY = 16;

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The size of the associative array (the number of key/value pairs).
   */
  int size;

  /**
   * The array of key/value pairs.
   */
  KVPair<K, V> pairs[];

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new, empty associative array.
   */
  @SuppressWarnings({ "unchecked" })
  public AssociativeArray() {
    // Creating new arrays is sometimes a PITN.
    this.pairs = (KVPair<K, V>[]) newInstance((new KVPair<K, V>()).getClass(),
        DEFAULT_CAPACITY);
    this.size = 0;
  } // AssociativeArray()

  // +------------------+--------------------------------------------
  // | Standard Methods |
  // +------------------+

  /**
   * Create a copy of this AssociativeArray.
   */
  public AssociativeArray<K, V> clone() {
    AssociativeArray<K,V> result = new AssociativeArray<K,V>();
    for (int i = 0; i < size; i++) {
      try {
        result.set(this.pairs[i].key, this.pairs[i].val);
      } catch (NullKeyException nke) {
        // Do nothing
      }
    } // for
    return result;
  } // clone()

  /**
   * Convert the array to a string.
   */
  public String toString() {
    if (this.size() == 0) {
      return "{}";
    } 
    StringBuilder result = new StringBuilder();
    result.append("{");
    result.append(this.pairs[0].toString());
    for (int i = 1; i < this.size(); i++) {
      result.append(", " + this.pairs[i].toString());
    } // for
    result.append("}");
    return result.toString();
  } // toString()

  // +----------------+----------------------------------------------
  // | Public Methods |
  // +----------------+

  /**
   * Set the value associated with key to value. Future calls to
   * get(key) will return value.
   */
  public void set(K key, V value) throws NullKeyException {
    if (key == null) {
      throw new NullKeyException();
    }
    try {
      int index = this.find(key);
      this.pairs[index] = new KVPair<K,V>(key,value);
    } catch (KeyNotFoundException e) {
      if (this.size >= this.pairs.length) {
        this.expand();
      } // if
      this.pairs[this.size++] = new KVPair<K,V>(key, value);
    } // try/catch
  } // set(K,V)

  /**
   * Get the value associated with key.
   *
   * @throws KeyNotFoundException
   *                              when the key does not appear in the associative
   *                              array.
   */
  public V get(K key) throws KeyNotFoundException {
    if (key == null) {
      throw new KeyNotFoundException("null is an invalid key");
    }
    return this.pairs[this.find(key)].val;
  } // get(K)

  /**
   * Determine if key appears in the associative array.
   */
  public boolean hasKey(K key) {
    try {
      this.find(key);
      return true;
    } catch (Exception e) {
      return false;
    }
  } // hasKey(K)

  /**
   * Remove the key/value pair associated with a key. Future calls
   * to get(key) will throw an exception. If the key does not appear
   * in the associative array, does nothing.
   */
  public void remove(K key) {
    try {
      int index = this.find(key);
      this.pairs[index] = this.pairs[--size];
      this.pairs[size] = null;
    } catch (KeyNotFoundException e) {
      // Do nothing
    }
  } // remove(K)

  /**
   * Determine how many values are in the associative array.
   */
  public int size() {
    return this.size;
  } // size()

  /**
   * Get all the keys in the array.
   */
  @SuppressWarnings({"unchecked"})
  public K[] getKeys() {
    K[] keys = (K[]) new Object[this.size];
    for (int i = 0; i < this.size; i++) {
      keys[i] = this.pairs[i].key;
    } // for i
    return keys;
  } // getKeys()

  /**
   * Get all the keys in the array as strings.
   */
  public String[] getKeyStrings() {
    String[] keys = new String[this.size];
    for (int i = 0; i < this.size; i++) {
      keys[i] = this.pairs[i].key.toString();
    } // for
    return keys;
  } // getKeyStrings()

  // +-----------------+---------------------------------------------
  // | Private Methods |
  // +-----------------+

  /**
   * Expand the underlying array.
   */
  public void expand() {
    this.pairs = java.util.Arrays.copyOf(this.pairs, this.pairs.length * 2);
  } // expand()

  /**
   * Find the index of the first entry in `pairs` that contains key.
   * If no such entry is found, throws an exception.
   */
  public int find(K key) throws KeyNotFoundException {
    for (int i = 0; i < this.size; i++) {
      if ((this.pairs[i].key == key) ||
          (this.pairs[i].key.equals(key))) {
        return i;
      } // if
    } // for
    throw new KeyNotFoundException();
  } // find(K)

} // class AssociativeArray
