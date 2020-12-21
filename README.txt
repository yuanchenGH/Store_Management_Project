README for Team Project Four (CS400 @ UW Madison)
==================================================

Every member of a team must have an individual README.txt file filled in in their folder on
the team's GitHub repo.

Name of submitting team member: Yuan Chen
Wisc email of submitting team member: chen2243@wisc.edu
Team name: DC
Role of submitting team member: Front End Developer 2
TA: Yelun
Lecturer: Gary Dahl

Contributions Week 1:
---------------------
I created the store.html file and created website which can display some basic
interface functions. It has two drop list which the user can select the category
and name of an item, and after click the "search" button, the script will print
some dummy information. I also tried to implement a listenser to update the second
drop list according to the option selected in the first one but not successful yet.
I'm a little late to push the readme file, but the html file is pushed before the deadline.

Contributions Week 2:
---------------------
Due to the change of our GUI design, the front end will use JavaFX form now on. The JavaFX 
program will load the store.html file and communicate between the Javascript and Java. New
files like StoreFrontPage.java, application.css, and store.js are added to the repository.

The user interface remains the same, the user can select category and item name from the drop
list. And click the search button will search the hashtable which contains the item information.
Then JavaFX will push the item information into html and call search() function to print the item
information under the table tag. 

Contributions Week 3:
---------------------
Finished Integration of data, back end, front end and test files.

Files written by me:
--------------------
store.html
StoreFrontPage.java
Makefile
readme.txt

Files submitted with this project that were developed in an earlier project:
----------------------------------------------------------------------------
<NONE>

Web address at which the program is available:
----------------------------------------------
<NA>

Additional notes about the submission:
--------------------------------------
Please make sure the JavaFX path is set properly before running the application.
The default path is for JavaFX library in the same folder as the java files.

To run the application, type command: make run
To run the tests, type command: make test

After the GUI is running, select a category from the first drop list to select a
category of items. Click search button to display all the items in the selected category.

To display only one item, select a item from the second drop list after selected a
category. Then click search button.

To change the quantity of a item, input a positive or negative number between -99999 to 
100000 in the input box. Then click the apply change button at the bottom of the GUI window. 
at the end of the dispayed item info. The table will display the updated item quantitiy and
update the groceries.csv file.

To close the window, click the X at the top right corner.  