import java.util.NoSuchElementException;

import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KeyNotFoundException;

/**
 * Represents the mappings for a single category of items that should
 * be displayed
 * 
 * @author Catie Baker & Samuel A. Rebelsky
 *
 */
public class AACCategory implements AACPage {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /** The name of the category. */
  String catName;

  /** The Associative Array used to store mappings. */
  AssociativeArray<String, String> items;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Creates a new empty category with the given name
   * @param name the name of the category
   */
  public AACCategory(String name) {
    this.catName = name;
    this.items = new AssociativeArray<String, String>();
  } // AACCategory(String)

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Adds the image location, text pairing to the category
   * @param imageLoc the location of the image
   * @param text the text that image should speak
   */
  public void addItem(String imageLoc, String text) {
    try {
      this.items.set(imageLoc, text);
    } catch (Exception e) {
      // Do nothing. We'll never call set with incorrect params.
    } // try/catch
  } // addItem(String, String)

  /**
   * Returns an array of all the images in the category
   * @return the array of image locations; if there are no images,
   * it should return an empty array
   */
  public String[] getImageLocs() {
    Object[] tmp = this.items.getKeys();
    String[] result = new String[tmp.length];
    for (int i = 0; i < tmp.length; i++) {
      result[i] = tmp[i].toString();
    } // for
    return result;
   } //getImageLocs()

  /**
   * Returns the name of the category
   * @return the name of the category
   */
  public String getCategory() {
    return this.catName;
  } // getCategory()

  /**
   * Returns the text associated with the given image in this category
   * @param imageLoc the location of the image
   * @return the text associated with the image
   * @throws NoSuchElementException if the image provided is not in the current
   *        category
   */
  public String select(String imageLoc) {
    try {
      return items.get(imageLoc);
    } catch (KeyNotFoundException e) {
      throw new NoSuchElementException(e.getMessage());
    } // try/catch
  } // select(String)

  /**
   * Determines if the provided images is stored in the category
   * @param imageLoc the location of the category
   * @return true if it is in the category, false otherwise
   */
  public boolean hasImage(String imageLoc) {
    return items.hasKey(imageLoc);
  } // hasImage(String)
} // class AACCategory
