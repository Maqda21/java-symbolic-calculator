# Makefile for the Symbolic Calculator Project

# Directories
SRC_DIR = src
OUT_DIR = classes
LIB_DIR = lib

# Classpath including output directory and libraries
CLASSPATH = $(OUT_DIR);$(LIB_DIR)/junit-jupiter-api-5.10.0.jar;$(LIB_DIR)/junit-jupiter-engine-5.10.0.jar


# JUnit standalone JAR for running tests
JUNIT_JAR = $(LIB_DIR)\junit-platform-console-standalone-1.8.1.jar

# Create the output directory if it doesn't exist
create-dir:
	if not exist $(OUT_DIR) mkdir $(OUT_DIR)

# Compile all Java files with Calculator.java
all: create-dir
	javac -d $(OUT_DIR) -sourcepath $(SRC_DIR) $(SRC_DIR)/org/ioopm/calculator/Calculator.java

# Compile all Java files with TestCalculator.java
calculator: create-dir
	javac -cp "$(LIB_DIR)\junit-jupiter-api-5.10.0.jar;$(LIB_DIR)\junit-jupiter-engine-5.10.0.jar;$(LIB_DIR)\junit-platform-console-standalone-1.8.1.jar" -d $(OUT_DIR) -sourcepath $(SRC_DIR) $(SRC_DIR)/org/ioopm/calculator/TestCalculator.java

# Run the program with Calculator.java
run:
	java -cp "$(CLASSPATH)" org.ioopm.calculator.Calculator

# Run the program with TestCalculator.java
test:
	java -cp "$(CLASSPATH)" -jar $(JUNIT_JAR) --class-path "$(CLASSPATH)" -c org.ioopm.calculator.TestCalculator

#run system level test
#note: if make systemtest does not work, just copy and paste the command into bash terminal in calculator directory
systemtest: 
	bash run_tests.sh
# Clean compiled classes
clean:
	if exist $(OUT_DIR) rmdir /s /q $(OUT_DIR)
