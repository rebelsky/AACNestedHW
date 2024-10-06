import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KeyNotFoundException;
import edu.grinnell.csc207.util.NullKeyException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.NoSuchElementException;

/**
 * Creates a set of mappings of an AAC that has two levels,
 * one for categories and then within each category, it has
 * images that have associated text to be spoken. This class
 * provides the methods for interacting with the categories
 * and updating the set of images that would be shown and handling
 * an interactions.
 * 
 * @author Catie Baker & YOUR NAME HERE
 *
 */
public class AACMappings implements AACPage {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /** 
   * The current category. Set to null when we are at the top-level
   * category.
   */
  AACCategory currentCategory = null;

  /**
   * Mapping images to categories.
   */
  AssociativeArray<String, AACCategory> categories;

  /**
   * Creates a set of mappings for the AAC based on the provided
   * file. The file is read in to create categories and fill each
   * of the categories with initial items. The file is formatted as
   * the text location of the category followed by the text name of the
   * category and then one line per item in the category that starts with
   * > and then has the file name and text of that image
   * 
   * for instance:
   * img/food/plate.png food
   * >img/food/icons8-french-fries-96.png french fries
   * >img/food/icons8-watermelon-96.png watermelon
   * img/clothing/hanger.png clothing
   * >img/clothing/collaredshirt.png collared shirt
   * 
   * represents the file with two categories, food and clothing
   * and food has french fries and watermelon and clothing has a 
   * collared shirt
   * @param filename the name of the file that stores the mapping information
   */
  public AACMappings(String filename) {
    currentCategory = null;
    categories = new AssociativeArray<String, AACCategory>();

    BufferedReader eyes = null;
    try {
      eyes = new BufferedReader(new FileReader(filename));
    } catch (IOException e) {
      throw new RuntimeException("Could not open file " + filename);
    } // try/catch
    
    String line;
    do {
      try {
        line = eyes.readLine();
      } catch (IOException e) {
        line = null;
      } // try/catch
      System.err.printf("Processing '%s'\n", line);
      if ((null == line) || (line.equals(""))) {
        // Skip it
      } else if (line.charAt(0) == '>') {
        String[] info = line.substring(1).split(" ", 2);
        if (null == currentCategory) {
          throw new RuntimeException("Broken config file");
        } // if
        currentCategory.addItem(info[0], info[1]);
      } else {
        String[] info = line.split(" ", 2);
        AACCategory newCat = new AACCategory(info[1]);
        try {
          this.categories.set(info[0], newCat);
        } catch (Exception e) {
          // We are unlikely to get an exception
        } // try/catch
        this.currentCategory = newCat;
      } // if/else
    } while (null != line);

    this.currentCategory = null;
    try {
      eyes.close();
    } catch (IOException e) {
      // Do nothing
    } // try/catch
  } // AACMappings(String)
 
  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Given the image location selected, it determines the action to be
   * taken. This can be updating the information that should be displayed
   * or returning text to be spoken. If the image provided is a category, 
   * it updates the AAC's current category to be the category associated 
   * with that image and returns the empty string. If the AAC is currently
   * in a category and the image provided is in that category, it returns
   * the text to be spoken.
   * @param imageLoc the location where the image is stored
   * @return if there is text to be spoken, it returns that information, otherwise
   * it returns the empty string
   * @throws NoSuchElementException if the image provided is not in the current 
   * category
   */
  public String select(String imageLoc) {
    if (null == this.currentCategory) {
      try {
        this.currentCategory = this.categories.get(imageLoc);
        return "";
      } catch (KeyNotFoundException e) {
        throw new NoSuchElementException(e.getMessage());
      } // try/catch
    } else {
      return this.currentCategory.select(imageLoc);
    } // if/else
  } // select(String)
  
  /**
   * Provides an array of all the images in the current category
   * @return the array of images in the current category; if there are no images,
   * it should return an empty array
   */
  public String[] getImageLocs() {
    if (null == this.currentCategory) {
      return this.categories.getKeyStrings();
    } else {
      return this.currentCategory.getImageLocs();
    } // if/else
  } // getImageLocs()
  
  /**
   * Resets the current category of the AAC back to the default
   * category
   */
  public void reset() {
    this.currentCategory = null;
  } // reset()
  
  /**
   * Writes the ACC mappings stored to a file. The file is formatted as
   * the text location of the category followed by the text name of the
   * category and then one line per item in the category that starts with
   * > and then has the file name and text of that image
   * 
   * for instance:
   * img/food/plate.png food
   * >img/food/icons8-french-fries-96.png french fries
   * >img/food/icons8-watermelon-96.png watermelon
   * img/clothing/hanger.png clothing
   * >img/clothing/collaredshirt.png collared shirt
   * 
   * represents the file with two categories, food and clothing
   * and food has french fries and watermelon and clothing has a 
   * collared shirt
   * 
   * @param filename the name of the file to write the
   * AAC mapping to
   */
  public void writeToFile(String filename) {
    PrintWriter pen = null;
    try {
      pen = new PrintWriter(new FileWriter(filename));
    } catch (IOException e) {
      return;
    } // try/catch
    String[] catKeys = categories.getKeyStrings();
    for (String key : catKeys) {
      try {
        AACCategory category = categories.get(key);
        pen.printf("%s %s\n", key, category.getCategory());
        String[] subKeys = category.getImageLocs();
        for (String subkey : subKeys) {
          pen.printf(">%s %s\n", subkey, category.select(subkey));
       } // for subkey
      } catch (Exception e) {
        // Skip it
      } // try/catch
    } // for key
    pen.close();
  } // writeToFile(String)
  
  /**
   * Adds the mapping to the current category (or the default category if
   * that is the current category)
   * @param imageLoc the location of the image
   * @param text the text associated with the image
   */
  public void addItem(String imageLoc, String text) {
    if (null == currentCategory) {
      try {
        this.categories.set(imageLoc, new AACCategory(text));
      } catch (NullKeyException e) {
        // Do nothing.
      } // try/catch
    } else {
      this.currentCategory.addItem(imageLoc, text);
    } // if/else
  } // addItem


  /**
   * Gets the name of the current category
   * @return returns the current category or the empty string if 
   * on the default category
   */
  public String getCategory() {
    if (null == currentCategory) {
      return "";
    } else {
      return this.currentCategory.getCategory();
    } // if/else
  } // getCategory()


  /**
   * Determines if the provided image is in the set of images that
   * can be displayed and false otherwise
   * @param imageLoc the location of the category
   * @return true if it is in the set of images that
   * can be displayed, false otherwise
   */
  public boolean hasImage(String imageLoc) {
    if (null == currentCategory) {
      return this.categories.hasKey(imageLoc);
    } else {
      return this.currentCategory.hasImage(imageLoc);
    } // if/else
  } // hasImage(String)

} // class AACMappings
