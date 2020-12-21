# --== CS400 File Header Information ==--
# Name: Yuan Chen   
# Email: chen2243@wisc.edu
# Team: DC
# TA: Yelun
# Lecturer: Gary Dahl
# Notes to Grader: <optional extra notes>

run: compile javafx-sdk frontend
	java --module-path javafx-sdk/lib/ --add-modules javafx.web,javafx.graphics StoreFrontEnd

frontend: javafx-sdk StoreFrontEnd.java
	javac --module-path javafx-sdk/lib/ --add-modules javafx.web,javafx.graphics  StoreFrontEnd.java

compile:
	javac Grocery.java DataLoader.java StoreBackEnd.java

compileTest:
	javac -cp .:junit5.jar DataWranglerTest.java
	javac -cp .:junit5.jar BackendTest.java

test: compileTest
	java -jar junit5.jar -cp . --scan-classpath -n BackendTest
	java -jar junit5.jar -cp . --scan-classpath -n DataWranglerTest

clean:
	$(RM) *.class
