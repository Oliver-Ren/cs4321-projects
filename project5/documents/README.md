# CS 4321 Project 5 #

Guantian Zheng (gz94), Chengxiang Ren (cr486), Mingyuan Huang (mh2239)

In this project, we first created a Stat.java class for gathering input statistics. After first executing our interpreter, stat.txt would be generated in the ./db directory. We also refactored the our code using a hashmap<String, List<IndexInfo>> so that it supported multi index per relation. The key of the hashmap is the table name, and the corresponding value is a list of indexInfo objects which provide detailed index information.

We also implemented the union find for optimizing the selections in the logical plan builder. After we built our optimized logical plan builder, we optimized the physical plan builder by optimizing the slection and join/sort oprator. We followed the logic that in most case we would use SMJ first, then choose BNLJ if SMJ is not applicable. For the selection optimization, out first priority is to use indexing, then scan if indexing is not applicable.

After that we implemnted the dynamic programming to choose optimized join order based on its total IO cost. We created a class called VValue to calculate the v value for each relation to help determine the cost of each join subset. 

  

## Project Structure 

The project is structured as follows:

  * the _src_ directory for source files.
  * the _sqltests_ directory for the test queries, database file, expected input and output directory.

For packages for the project source file is arranged by the following rational:

  * the _client_ package is for top-level class.
  * the _btree_ directory is a building B+ tree index file. It also contains serializer, deserializer classes for index searching purpose. 
  * the _operators_ package is for various Operator classes.
  * the _optimizer_ package contains three major classes, JoinOptimizer, SelectionOptimizer, and VValue. These three classese are created for determining the best join order. 
  * the _test_ package contains unit tests, end-to-end test as well as the utilities and test test harness.
  * the _util_ package contains various classes for dealing with queries, expressions, and trees as well as data-structures for storing query elements.
  * the _visitors_ package contains generic visitor as well as different concrete visitors.  

## New Functionalities Added in Ptoject 5

### The Selection Pushing

In order to push selection to proper position in our logical plan, We implemented the union find data structure to group attributes with same range condition. The selection condition, which contains equalsTo, greaterThan, MinorThan expression between one relation and a number or between two attribute of the same relation would be pushed down after the join condition. Uneqality and other usable selection condtion would be pushed up to the join operator. 

### Selection Implementation

We created a class called selectionOptimizer to handle which selection method to use. We first check indexing availability for all the attributes of one relation involved in the selection. If there are no proper indexing for these attributes, we would choose the plain scan method. If there are indexing options on these attributes, we would compare the cost their costs, and choose the one with the lowest cost.

### Join Order choice
We had some problems implenting the dynamic programing method to choose the best join order. The compromised solution we curretnly implemented is to choose join order based on each table size. 

### Join Implementation
Our logic is to use Sort Merg Join if it is applicable. If SMJ is not applicable, we choose to use Block Nested Loop Join. 
  
## Interpreter

The _top-level entry_ of the program is at the client.SQLInterpreter.main().

The Interpreter is in charge of read the input file and transfer to the query evalutator, and finally output the result to a file.

## Join Logic

We categorize conditions from the WHERE clause into:
	1. Comparison between constants.
	2. In-table select condition.
	3. Cross-table join condition.

At the start of parsing, all conditions are extracted by recursively breaking up the AND expressions. Then for each table we record: 1) its select conditions, and 2) its join condition with all of the __preceding__ tables. This way, we could move along the FROM tables, and focus only on relevant conditions when a new table needs to be joined with the current left sub-tree. And of course, constant conditions are handled before everything.

## ORDER BY & DISTINCT

We implemented the SortOperator as per the instructions. If certain ordered elements are not projected (e.g., SELECT S.A FROM S ORDER BY S.B) and DISTINCT is required, we have another operator using HashSet to maintain the order.

## B+ Tree Index File Builder
We provide a BPlusTree class, and its constructor is able to build a serialized index file on one perticular attribute from the given relation data. 
It also provides tree index deserialization functionality when user need to use index scan operator during the execution. This would shorten the tuple searching time.  

## Index Scan Operator
We implemented an index scan operator for index selection using B+ tree index. It evaluates expressions contains the index attribute, and would call deserilizer to fetch the first satified data entry from the leaf node of the B+ tree. After that, it would fetch tuples linearly if the file is clustered, other wise, it will continously fetch next satisfied data entry from the deserializer. 

## Selection Logic 

In our previous design, we gave our logical selection operator a important constrain that the child of a logical selection operator must be a scan operator. Therefore, when we use the visitor pattern to build our physical plan, we only need to traverse down to the selection operator at most. And when the visitor traverse to the selection operator, the logic could decide whther the child of the selection operator should be full scan operator or index scan operator.

As we have stored the information from configuration file into the catalog. Firstly, if the database catalog shows that index query is on, we should check for availability of the index on this query.

We have a method called _hasIdxAttr_ which checks if the index applies to the selection condition. That is, if the selection condition for the this table contians _greater than_, _greater than equal to_, _equals_, _less than_, _less than equal to_ on the indexed attraibute, then we are good to build a _index scan operator_ as the child. Then we calculate the lowKey and highKey depend on all the selection conditiions and then build the operator.

If the _hasIdxAttr_ returns false, we should build the normal full scan operator. 

Finally we return to the uppper level.


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


