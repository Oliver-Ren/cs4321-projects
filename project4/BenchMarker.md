# Benchmarking tests for index scan operator

## Test Plan:
We would use three queries showing below to compare the execution time among full scan, unclustered index scan and clustered index scan. 

**Queries:**  
1. SELECT * FROM Boats WHERE Boats.E < 10;   
2. SELECT  * FROM Boats WHERE Boats.E < 300;  
3. SELECT * FROM Sailors WHERE Sailors.A  > 100 and Sailors.A <150;  
**Note**
Query 1 tests the small range selection performance. Query 2 tests the large range selection performance. Query 3 tests a close range selection. 

**Test Description:**  
For each query, we first use full scan operation to output the result, and record the execution time. Then we test the same query using the index file we build on the unclustered file to output the result, and record the execution time.  Lastly, we test the same query using the index file we build on the clustered file to output the result, and record the execution time.

After we record all the data, we compare the execution times for different scan methods using a bar diagram.  

## Relation Description:
Table Boats contains 5000 tuples ranging from 0 - 500
Table Sailors contains 5000 tuples raning from 0 - 500
 
**Schema:**  
Boats: D E F, index on E  
Sailors: A B C, index on A 

## Results: 

**Observation**

 
