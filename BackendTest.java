// --== CS400 File Header Information ==--
// Name: Justin Qiao
// Email: sqiao6@wisc.edu
// Team: DC
// Role: Test Engineer 1
// TA: Yelun Bao
// Lecturer: Florian Heimerl
// Notes to Grader: groceries_for_testing.csv is a copy of our originally created data file that
// will not be changed during interactions with the front end. Tests will fail if this file is not
// stored in the project folder correctly. A file called groceries_for_testing_copy.csv will be
// created after running this test just for backup and restore purposes.

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

/**
 * Tests the implementation of StoreBackEnd. Checks whether loadGroceries() transfer the data from
 * data wrangler correctly to the HashTable, whether getGroceryList(String category) returns correct
 * List, whether changeQuantity(String category, String itemName,int change) changes Grocery
 * instances correctly, and whether updateCsv(Grocery item) works as expected.
 * 
 * Important Note: Please make sure that the groceries_for_testing.csv file is in the same folder as
 * groceries.csv. groceries_for_testing.csv is a copy of our originally created datafile that will
 * not be changed during interactions with the front end.
 * 
 * @author Justin Qiao
 */
class BackendTest {

  /**
   * Makes copy of groceries_for_testing.csv as groceries_for_testing_copy.csv for recovery after
   * each test.
   * 
   */
  @BeforeEach
  void makeCopy() {
    // set up the test targeting groceries_for_testing.csv
    if (StoreBackEnd.categories != null)
      StoreBackEnd.categories.clear();
    DataLoader.CSVPATH = "groceries_for_testing.csv";
    StoreBackEnd.loadGroceries();
    // make copy of groceries_for_testing.csv
    Path copied = Paths.get("groceries_for_testing_copy.csv");
    Path originalPath = Paths.get("groceries_for_testing.csv");
    try {
      Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Recovers groceries_for_testing.csv from groceries_for_testing_copy.csv after each test.
   * 
   */
  @AfterEach
  void recover() {
    // restore DataLoader for application
    DataLoader.CSVPATH = "groceries.csv";
    // restore groceries_for_testing.csv
    Path copied = Paths.get("groceries_for_testing.csv");
    Path originalPath = Paths.get("groceries_for_testing_copy.csv");
    try {
      Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Checks whether loadGroceries() transfers the data from data wrangler correctly to the
   * HashTable. Test fails if the data in HashTable does not match with the data in the ArrayList
   * from data wrangler. Also checks whether getGroceryList(String category) returns the correct
   * list contains the grocery items under the provided category. Test fails if any matched item was
   * not in the list, or any matched item was added to the returned list.
   * 
   */
  @Test
  void testDataTransfer() {
    try {
      // get lists of all 5 categories
      ArrayList<Grocery> clothing = StoreBackEnd.getGroceryList("clothing");
      ArrayList<Grocery> furniture = StoreBackEnd.getGroceryList("furniture");
      ArrayList<Grocery> schoolSupplies = StoreBackEnd.getGroceryList("schoolsupplies");
      ArrayList<Grocery> games = StoreBackEnd.getGroceryList("games");
      ArrayList<Grocery> food = StoreBackEnd.getGroceryList("food");
      // check the number of different grocery items under each category
      if (clothing.size() != 16 || furniture.size() != 11 || schoolSupplies.size() != 11
          || games.size() != 10 || food.size() != 21)
        fail("The number of items under one or more categories are incorrect.");
      // check whether any grocery item was mismatched with its category
      for (Grocery test : clothing)
        if (!test.getCategory().equals(Grocery.Category.CLOTHING))
          fail("The CLOTHINGs are not stored correctly");
      for (Grocery test : furniture)
        if (!test.getCategory().equals(Grocery.Category.FURNITURE))
          fail("The FURNITUREs are not stored correctly");
      for (Grocery test : schoolSupplies)
        if (!test.getCategory().equals(Grocery.Category.SCHOOLSUPPLIES))
          fail("The SCHOOLSUPPLIES are not stored correctly");
      for (Grocery test : games)
        if (!test.getCategory().equals(Grocery.Category.GAMES))
          fail("The GAMES are not stored correctly");
      for (Grocery test : food)
        if (!test.getCategory().equals(Grocery.Category.FOOD))
          fail("The FOOD are not stored correctly");
    } catch (Exception e) {
      fail("Unexpected exception was thorwn during the data transfers within backend.");
    }
  }

  /**
   * Checks whether changeQuantity(String category, String itemName, int change) changes the
   * quantity of a grocery instance correctly. Also checks whether updateCsv(Grocery item) updates
   * the given item in the csv correctly. Test fails if the expected change did not occur, or ant
   * unexpected changes occurs.
   * 
   */
  @Test
  void testDataModification() {
    try {
      // case 1: increasing the quantity of a grocery item
      StoreBackEnd.changeQuantity("food", "Gone-For-Nows", 10);
      ArrayList<Grocery> testList = StoreBackEnd.getGroceryList("FOOD");
      for (Grocery test : testList)
        if (test.getProductName().equals("Gone-For-Nows"))
          if (test.getQuantity() != 10)
            fail("Failed to add 10 \"Gone-For-Nows\"");
      // case 2: decreasing the quantity of a grocery item, when the resulting value is still
      // positive
      StoreBackEnd.changeQuantity("schoolsupplies", "Computer", -15);
      testList = StoreBackEnd.getGroceryList("SCHOOLSUPPLIES");
      for (Grocery test : testList)
        if (test.getProductName().equals("Computer"))
          if (test.getQuantity() != 44)
            fail("Failed to remove 15 \"Computer\"");
      // case 3: decreasing the quantity of a grocery item, when the resulting value negative. The
      // backend should set the quantity to 0 in this case, preventing any negative quantity
      StoreBackEnd.changeQuantity("furniture", "The Kitchen Sink (Yeah; we took everything)", -5);
      testList = StoreBackEnd.getGroceryList("FURNITURE");
      for (Grocery test : testList)
        if (test.getProductName().equals("The Kitchen Sink (Yeah; we took everything)"))
          if (test.getQuantity() != 0)
            fail("Failed to keep the quantites positive when removing items");
      // case 4: increasing the quantity of a grocery item by 1000001. The backend should set the
      // quantity to 1000000 in this case, matching to the max quantity we set
      StoreBackEnd.changeQuantity("furniture", "The Kitchen Sink (Yeah; we took everything)",
          1000001);
      testList = StoreBackEnd.getGroceryList("FURNITURE");
      for (Grocery test : testList)
        if (test.getProductName().equals("The Kitchen Sink (Yeah; we took everything)"))
          if (test.getQuantity() != 1000000)
            fail("Failed to keep the quantites below capacity, 1000000");
      // case 5: try to change the quantity of something does not exist
      String before = StoreBackEnd.categories.toString();
      StoreBackEnd.changeQuantity("CLOTHING", "test", 999);
      if (!StoreBackEnd.categories.toString().equals(before))
        fail(
            "Failed to maintain data when try to change the quantity of something does not exist.");
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected exception was thorwn during the data modifications within backend.");
    }
  }

}
