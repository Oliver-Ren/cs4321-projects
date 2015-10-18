# CS 4321 Project 2 #

Guantian Zheng (gz94), Chengxiang Ren (cr486), Mingyuan Huang (mh2239)

In this project, we implemented a simple interpreter for SQL statements which takes in a database and a file containing several SQL queries then process and evaluate each SQL query on the database.

## Project Structure 

The project is structured as follows:

  * the _src_ directory for source files.
  * the _sqltests_ directory for the test queries, database file, expected input and output directory.

For packages for the project source file is arranged by the following rational:

  * the _client_ package is for top-level class.
  * the _operators_ package is for various Operator classes.
  * the _test_ package contains unit tests, end-to-end test as well as the utilities and test test harness.
  * the _util_ package contains various classes for dealing with queries, expressions, and trees as well as data-structures for storing query elements.
  * the _visitors_ package contains generic visitor as well as different concrete visitors.

## Interpreter

The _top-level entry_ of the program is at the client.SQLInterpreter.main().

The Interpreter is in charge of read the input file and transfer to the 
query evalutator, and finally output the result to a file.

## Join Logic

We categorize conditions from the WHERE clause into:
	1. Comparison between constants.
	2. In-table select condition.
	3. Cross-table join condition.

At the start of parsing, all conditions are extracted by recursively breaking up the AND expressions. Then for each table we record: 1) its select conditions, and 2) its join condition with all of the __preceding__ tables. This way, we could move along the FROM tables, and focus only on relevant conditions when a new table needs to be joined with the current left sub-tree. And of course, constant conditions are handled before everything.

## ORDER BY & DISTINCT

We implemented the SortOperator as per the instructions. If certain ordered elements are not projected (e.g., SELECT S.A FROM S ORDER BY S.B) and DISTINCT is required, we have another operator using HashSet to maintain the order.

## Testing

We make use of two testing strategies

1. At the very beginning use the Unit Test for the basic function of the operators including _ScanOperator_ and _SelectOperator_.
2. At the later stage, we are highly dependent on the use of end-to-end tests to ensure well functioning of the whole system.

### Unit Tests

For the basic test, we manually construct operators and dump the tuples to prove that the operator could work with regard to the iterator model.

### End-to-End Tests

The end-to-end test is the test which takes the whole interpreter as the unit under test.
The Test case will take the input query file and then execute the the query, finally print the result into the target file.

### Test-Harness

In order to better conduct the testing, we have written a bash script which dumps the result of the MySQL database as the expected output. Therefore, it is very easy for us to build relatively complex test cases.

Also, we have created the util class _Diff_ for comparing two files which will automatically judge the correctness of the output.


