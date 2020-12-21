// --== CS400 File Header Information ==--
// Name: Allistair Mascarenhas
// Email: anmascarenha@wisc.edu
// Team: DC
// Role: Back End Developer 2
// TA: Yelun Bao
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import java.util.*;
import java.io.*;

/**
 * This class contains methods which handle the back end of the Grocery Store Inventory Management
 * System.
 * 
 * @author Allistair
 * 
 */
public class StoreBackEnd {

  /*
   * Hash table which stores the data from a csv file by category. The key is the Category
   * enumeration and the value is an ArrayList of Grocery objects which fall in that category
   */
  protected static Hashtable<Grocery.Category, ArrayList<Grocery>> categories;

  /**
   * This method gets the data from the DataWranglers and sorts it by category before adding each of
   * them to the hash table. This method must be called first in order to make use of the other
   * methods.
   */
  protected static void loadGroceries() {
    categories = new Hashtable<>();
    ArrayList<Grocery> inventory = DataLoader.getGroceries();

    ArrayList<Grocery> clothing = new ArrayList<>();
    ArrayList<Grocery> furniture = new ArrayList<>();
    ArrayList<Grocery> schoolSupplies = new ArrayList<>();
    ArrayList<Grocery> games = new ArrayList<>();
    ArrayList<Grocery> food = new ArrayList<>();

    for (Grocery item : inventory) {
      switch (item.getCategory()) {
        case CLOTHING:
          clothing.add(item);
          break;

        case FURNITURE:
          furniture.add(item);
          break;

        case SCHOOLSUPPLIES:
          schoolSupplies.add(item);
          break;

        case GAMES:
          games.add(item);
          break;

        case FOOD:
          food.add(item);
          break;
      }
    }

    categories.put(Grocery.Category.CLOTHING, clothing);
    categories.put(Grocery.Category.FURNITURE, furniture);
    categories.put(Grocery.Category.SCHOOLSUPPLIES, schoolSupplies);
    categories.put(Grocery.Category.GAMES, games);
    categories.put(Grocery.Category.FOOD, food);
  }

  /**
   * A getter method which returns an ArrayList of Grocery object which belong to a certain category
   * from the hash table.
   * 
   * @param category - String later converted to an enumeration and is the key for the hash table
   * @return ArrayList<Grocery> if the key if the parameter category is a valid enumeration else
   *         return null if the key isn't valid or if the value doesn't exist in the hash table
   */
  public static ArrayList<Grocery> getGroceryList(String category) {
    Grocery.Category key = stringToEnum(category);

    // check if key is a valid enumeration
    if (key == null)
      return null;

    return categories.get(key);
  }

  /**
   * Takes in the name of the category and item and the number to change the quantity by. It then
   * updates the csv to contain the new quantity and returns the updated Grocery object.
   * 
   * @param category - String later converted to an enumeration and is the key for the hash table
   * @param itemName - String used to search for item in ArrayList in order to change its quantity
   * @param change   - Increases or decreases the quantity of an item if change is positive or
   *                 negative respectively. If change = 0 then this method acts as a lookup method.
   * @return item with the updated quantity or null if item wasn't found in hash table
   */
  public static Grocery changeQuantity(String category, String itemName, int change) {
    try {
      Grocery.Category key = stringToEnum(category);

      if (key == null) {
        return null;
      }

      ArrayList<Grocery> itemList = categories.get(key);
      for (Grocery item : itemList) {

        // checks if item is found
        if (item.getProductName().equals(itemName)) {
          int newQuantity = item.getQuantity() + change;

          // new quantity can't be negative so there's a lower limit of 0
          if (newQuantity < 0) {
            item.setQuantity(0);
          } else if (newQuantity > 1000000) {
            // new quantity can't be over 1 million (arbitrary large number) so the upper limit is 1
            // million
            item.setQuantity(1000000);
          } else {
            item.setQuantity(newQuantity);
          }

          // only updates csv if there's a change to the quantity
          if (change != 0)
            updateCsv(item);


          return item;
        }
      }
      // return null if item isn't found
      return null;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;

  }

  /**
   * Helper method that updates the csv to include the new quantity of the item. It copies the
   * contents of the oldFile into a new file and only modifying the line with the matching item.
   * Then it deletes the oldFile and renames the newFile to the oldFile's name.
   * 
   * @param item - Grocery object with updated quantity
   */
  protected static void updateCsv(Grocery item) {
    File fileToModify = new File(DataLoader.CSVPATH);
    String fileContents = "";

    try {
      BufferedReader reader = new BufferedReader(new FileReader(fileToModify));
      FileWriter writer = null;

      String line = reader.readLine();
      String[] arr;
      while (line != null) {
        arr = line.split(",");
        if (arr[0].equals(item.getProductName())
            && arr[3].trim().toUpperCase().equals(item.getCategory().toString())) {
          line = item.getProductName() + "," + item.getQuantity() + "," + item.getPrice() + ","
              + enumToString(item.getCategory());
        }

        fileContents += line + "\n";
        line = reader.readLine();
      }

      writer = new FileWriter(fileToModify);
      writer.write(fileContents);

      reader.close();
      writer.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }



  /**
   * Helper method which takes in a string and returns the matching Grocery.Category enumeration.
   * 
   * @param category - String
   * @return Enumeration from Grocery.Category or null if parameter doesn't match any of the
   *         enumerations
   */
  protected static Grocery.Category stringToEnum(String category) {
    if (category == null)
      return null;

    for (Grocery.Category cat : Grocery.Category.values()) {
      // case insensitive check
      if (cat.toString().equalsIgnoreCase(category))
        return cat;
    }

    return null;
  }

  /**
   * Helper method which takes in a Grocery.Category enumeration and returns the matching
   * UpperCamelCase string.
   * 
   * @param cat - Grocery.Category enumeration
   * @return String in UpperCamelCase
   */
  protected static String enumToString(Grocery.Category cat) {
    if (cat == null)
      return null;

    switch (cat) {
      case SCHOOLSUPPLIES:
        return "SchoolSupplies";

      default:
        String category = cat.toString().toLowerCase();
        category = Character.toUpperCase(category.charAt(0)) + category.substring(1);
        return category;
    }
  }
}
