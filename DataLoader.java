// --== CS400 File Header Information ==--
// Name: Alexander Peseckis
// Email: peseckis@wisc.edu
// Team: DC
// Role: Data Wrangler
// TA: Yulan BAO
// Lecturer: Florian Heimerl
// Notes to Grader: <optional extra notes>

import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;

public class DataLoader {
    public static String CSVPATH = "groceries.csv";

    public static ArrayList<Grocery> getGroceries() {
        ArrayList<Grocery> groceries = new ArrayList<>();

        File file = new File(CSVPATH);
        Scanner reader;

        try {
            reader = new Scanner(file);
        } catch (Exception e) {
            throw new RuntimeException("Could not find the groceries.csv file.");
        }

        while (reader.hasNextLine()) {
            String currLine = reader.nextLine();

            // If the current line is the header row, don't try to parse it
            if (currLine.equals("Product Name, Quantity, Price ($), Category"))
                continue;

            // The components of the Grocery Item
            String[] components = currLine.split(",");

            // Trim the spaces off each component
            for (int i = 0; i < components.length; i++)
                components[i] = components[i].trim();

            if (components.length != 4)
                continue;

            String name = components[0];
            int quantity = Integer.parseInt(components[1]);
            double price = Double.parseDouble(components[2]);

            // The category as a string of the Grocery
            String cat = components[3].toLowerCase();
            Grocery.Category category = null;
            switch (cat) {
                case "clothing":
                    category = Grocery.Category.CLOTHING;
                    break;
                case "furniture":
                    category = Grocery.Category.FURNITURE;
                    break;
                case "schoolsupplies":
                    category = Grocery.Category.SCHOOLSUPPLIES;
                    break;
                case "games":
                    category = Grocery.Category.GAMES;
                    break;
                case "food":
                    category = Grocery.Category.FOOD;
                    break;
                default:
                    throw new RuntimeException("A row in the CSV did not have a valid Category.");
            }

            groceries.add(new Grocery(name, quantity, price, category));
        }

        return groceries;
    }
}





