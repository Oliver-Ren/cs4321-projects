# CS 4321 Project 2 #

In this project, we implemented a simple interpreter for SQL statements which takes in a database and a file containing several SQL queries then process and evaluate each SQL query on the database.

## Project Setup

_How do I, as a developer, start working on the project?_ 

1. _What dependencies does it have (where are they expressed) and how do I install them?_
2. _How can I see the project working before I change anything?_

## Interpreter

We top-level entry of the program is at the client.SQLInterpreter.main().

The Interpreter is in charge of read the input file and transfer to the 
query evalutator, and finally output the result to a file.

## Join Logic

We categorize conditions from the WHERE clause into:
	1. Comparison between constants.
	2. In-table select condition.
	3. Cross-table join condition.

At the start of parsing, all conditions are extracted by recursively breaking up the AND expressions. Then for each table we record: 1) its select conditions, and 2) its join condition with all of the __preceding__ tables. This way, we could move along the FROM tables, and focus only on relevant conditions when a new table needs to be joined with the current left sub-tree. And of course, constant conditions are handled before everything.

## Testing

We make use of two testing strategies

1. At the very begining use the Unit Test for the basic funciton of the operators.
2. At the later stage, use end-to-end tests to ensure well functioning of the whole system.

### Unit Tests

For the basic test, use manually construct opeartors and dump the tuples to prove that the operator could work with regard to the iterator model.

### End-to-End Tests

The end-to-end test is the test which takes the whole interpreter as the unit under test.
The Test case will take the input query file and then execute the the query, finally print the result into the target file.

### Test-Harness

Inorder to better conduct the testing, we have written a bash script which dumps the resultof the MySQL database as the expected output. Therefore, it is very easy for us to build reletively complex test cases.

Also, we have created the util class for comparing two files which will automatically judgethe correctness of the output.


