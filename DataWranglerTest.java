// --== CS400 File Header Information ==--
// Name: Justin Qiao
// Email: sqiao6@wisc.edu
// Team: DC
// Role: Test Engineer 1
// TA: Yelun Bao
// Lecturer: Florian Heimerl
// Notes to Grader: groceries_for_testing.csv is a copy of our originally created data file that
// will not be changed during interactions with the front end. Tests will fail if this file is not
// stored in the project folder correctly.

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the implementation of Grocery and DataLoader. Checks whether the data in csv file is
 * correctly transfered to the back-end developer.
 * 
 * @author Justin Qiao
 */
class DataWranglerTest {

  /**
   * Setup test target csv file
   * 
   */
  @BeforeEach
  void setup() {
    // set up the test targeting groceries_for_testing.csv
    DataLoader.CSVPATH = "groceries_for_testing.csv";
  }

  /**
   * Recovers csv path stored in DataLoader.java.
   * 
   */
  @AfterEach
  void recover() {
    // restore DataLoader for application
    DataLoader.CSVPATH = "groceries.csv";
  }
  
  
  /**
   * Checks whether the Grocery class is working as excepted. Test fails if the grocery instance's
   * getters return incorrect data.
   */
  @Test
  void testGrocery() {
    try {
      // try to create a Grocery instance
      Grocery grocery = new Grocery("testProduct", 2, 1.99, Grocery.Category.FOOD);
      // test fails if the information is not stored in the object as provided
      if (!grocery.getProductName().equals("testProduct") || grocery.getQuantity() != 2
          || grocery.getPrice() != 1.99 || grocery.getCategory() != Grocery.Category.FOOD)
        fail("Grocery instance was not created properly.");
    } catch (Exception e) { // or if any exception was thrown
      fail("Un expected exception was thorwn during the usage of class Grocery.");
    }
  }

  /**
   * Checks whether the ArrayList given by DataLoader's getGroceries functions properly. Test fails
   * if the csv file information was not stored in the ArrayList correctly.
   */
  @Test
  void testDataLoader() {
    try {
      // load data in groceries_for_testing.csv to DataLoader
      ArrayList<Grocery> groceries = DataLoader.getGroceries();
      // check the number of items
      if (groceries.size() != 69)
        fail("The number of groceries was not initialized correctly according to the csv file.");
      Grocery test = groceries.get(0);
      // check first element
      if (!test.getProductName().equals("Oakley's Sunglasses")
          || !test.getCategory().equals(Grocery.Category.CLOTHING) || test.getPrice() != 100
          || test.getQuantity() != 10)
        fail("The first grocery item was read incorrectly from the csv file.");
      test = groceries.get(29);
      // check a element in the middle
      if (!test.getProductName().equals("Candy Poppers")
          || !test.getCategory().equals(Grocery.Category.FOOD) || test.getPrice() != 4.99
          || test.getQuantity() != 54)
        fail("The 30th grocery item, Candy Poppers, was read incorrectly from the csv file.");
      test = groceries.get(groceries.size() - 1);
      // check last element
      if (!test.getProductName().equals("Buyer's Remorse - The Game")
          || !test.getCategory().equals(Grocery.Category.GAMES) || test.getPrice() != 999.99
          || test.getQuantity() != 999)
        fail("The last grocery item was read incorrectly from the csv file.");
    } catch (Exception e) {
      fail("Unexpected exception was thorwn during the usage of class Grocery.");
    }
  }

}
