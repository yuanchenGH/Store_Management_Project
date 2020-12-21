// --== CS400 File Header Information ==--
// Name: Yuan Chen	
// Email: chen2243@wisc.edu
// Team: DC
// Role: Front End Developer 2
// TA: Yelun Bao
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLSelectElement;
import org.w3c.dom.html.HTMLTableElement;
import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class StoreFrontEnd extends Application {	
	private WebView view;
	private WebEngine engine;
	private HTMLSelectElement select;
	private HTMLSelectElement select2;
	private HTMLTableElement itemTable;
	private Document doc;
	private ArrayList<Grocery> items;
    private Alert a1; 
    private Alert a2;
    private int index1;
    private int index2;
    private int valuei;
    private Grocery.Category[] keys;
    private String key;
    private String value;

	@Override
	public void start(Stage stage) {
		StoreBackEnd.loadGroceries();//load groceries.csv file into a hash table
		keys = Grocery.Category.values();//get the list of categories as keys for the hash table		
		view = new WebView();
		stage.setTitle("Store Management System");
		engine = view.getEngine();
		engine.load(getClass().getResource("store.html").toString());//load store.html file
		
		//event listener to monitor the html file
        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                //if new page has loaded, process:
            	//load page content to doc
        		doc = engine.getDocument();
        		//get drop list into select(categories) and select2(items) and set the selected index to 0
        		select = (HTMLSelectElement) doc.getElementById("category");
        		select.setSelectedIndex(0);
        		select2 = (HTMLSelectElement) doc.getElementById("item");
        		select2.setSelectedIndex(0);
        		//add categories into first drop list
        		for (Grocery.Category key: keys) {
        			Element opt = doc.createElement("option");
        			opt.setTextContent(StoreBackEnd.enumToString(key));
        			select.appendChild(opt);
        		}
        		//call java function in javascript
        		JSObject window = (JSObject) engine.executeScript("window");
                window.setMember("app", this);//integration of java and javascript
                //call printItem() java function in javascript if search button is clicked
                engine.executeScript("document.getElementById('search').addEventListener('click', function(event){app.printItem();});");
                //call updateItem() java function in javascript if category is changed
                engine.executeScript("document.getElementById('category').addEventListener('change', function(event){app.updateItem();});");
                engine.executeScript("document.getElementById('changeQuant').addEventListener('click', function(event){app.changeQuant();});");
            }          		  		
        });
		
		GridPane pane = new GridPane();
		pane.add(view, 0, 2);
		Scene scene = new Scene(pane,800,600);
		stage.setScene(scene);		
		stage.show();		
	}
	
	/**
	 * This method update the item drop list according to the selected 
	 * category in the first drop list
	 */
	public void updateItem() {
		index1 = select.getSelectedIndex();
		if(index1 != 0) {
			//if category is selected, clear the previous item drop list
	    	while(select2.getChildNodes().getLength() > 2) {
	    		select2.removeChild(select2.getLastChild());
	    	}
	    	//get item list of the selected category
//	    	key = select.getOptions().item(index1).getTextContent();
	    	key = StoreBackEnd.enumToString(keys[index1 - 1]);
	    	items = StoreBackEnd.getGroceryList(key);
	    	//append the items to the second drop list
	    	for (Grocery item: items) {
		    	Element opt = doc.createElement("option");
		    	opt.setTextContent(item.getProductName());
		    	select2.appendChild(opt);
	    	}
		}
	}
	
	/**
	 * This method add items and their information into the search output
	 */
	public void printItem() {
		//get the index value of both drop list
		index1 = select.getSelectedIndex();
		index2 = select2.getSelectedIndex();
		items = StoreBackEnd.getGroceryList(key);
		
		//if no category is selected, display warning
		if (index1 == 0) {
			a1 = new Alert(AlertType.ERROR);
			a1.setContentText("Please select a category!");
			a1.show();
		}
		//if an item is selected in the second drop list, display that item
		else if(index2 != 0) {
			Grocery item = items.get(index2 - 1);
			engine.executeScript("clearItems()");
			engine.executeScript("itemName.push('"+item.getProductName()+"')");
			engine.executeScript("itemQuant.push("+item.getQuantity()+")");
			engine.executeScript("itemPrice.push("+item.getPrice()+")");
	    	engine.executeScript("search()");
		}
		//if no item is selected in the second drop list 
		//display all items in the category
		else {
			//delete all entries of previous collection of items in 2nd drop list 
			engine.executeScript("clearItems()");
			//push name, quantity and price of an item into javascript
			for (Grocery item: items) {
				engine.executeScript("itemName.push('"+item.getProductName()+"')");
				engine.executeScript("itemQuant.push("+item.getQuantity()+")");
				engine.executeScript("itemPrice.push("+item.getPrice()+")");
			}
			//execute javascript function search()
	    	engine.executeScript("search()");
		}
	}
	
	/**
	 * This method change the quantity of an item
	 */
	public void changeQuant() {
		//get the table field
		itemTable = (HTMLTableElement) doc.getElementById("print");
		//get the number of rows in the table
		int rows = itemTable.getRows().getLength();
		//no item is selected, get the value in the input field of all items
		if (index2 == 0) {
			for (int i = 0; i < rows - 1; i ++ ) {
				HTMLInputElement input = (HTMLInputElement) doc.getElementById("item" + i);
				value = input.getValue();
				//if there is a value in input, call getInput and changeQuantHelpler method
				//these two method validates the input and change the quantity of the item
				//if input is valid
				if (value != null) {
					valuei = getInput(value);
					String cat = StoreBackEnd.enumToString(keys[index1 - 1]);
					String name = items.get(i).getProductName();
					StoreBackEnd.changeQuantity(cat, name, valuei);
				}
			}
		}
		//if an item is selected, get the value in the input field of the selected item
		//if there is a value in input, call getInput and changeQuantHelpler method
		//these two method validates the input and change the quantity of the item
		//if input is valid
		else {
			HTMLInputElement input = (HTMLInputElement) doc.getElementById("item0");
			value = input.getValue();
			valuei = getInput(value);
			String cat = StoreBackEnd.enumToString(keys[index1 - 1]);
			String name = items.get(index2 - 1).getProductName();
			StoreBackEnd.changeQuantity(cat, name, valuei);
		}
		printItem();
	}
	
	/**
	 * This method validates the input, display a warning if the input is invalid
	 * If the input is valid, the method parse the input into a integer and return the value  
	 * @param s String from input
	 * @return integer value of the input
	 */
	public int getInput(String s) {
		int value = 0;
		a2 = new Alert(AlertType.ERROR);
		a2.setContentText("Please enter a integer between -99999 to 100000!");
		//try to parse the input to an integer value
		try {
			if (s.length() > 6) {
				a2.show();
			} else {
				value = Integer.parseInt(s);
			}			
		} catch(NumberFormatException e) {			
			//show alert if input is not valid
			a2.show();
		}
		return value;
	}

	/**
	 * main method
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
