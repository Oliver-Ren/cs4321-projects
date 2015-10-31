# Test Plan for testing BNLJ, SMJ

## Test Plan1: small samples
**Precondition:**  
	1.Only three tables: Sailors, Reserves, and Boats  
	   
**Queries:**

		1. SELECT * FROM Sailors;
		2. SELECT Sailors.A FROM Sailors;
		3. SELECT Boats.F, Boats.D FROM Boats;
		4. SELECT Reserves.G, Reserves.H FROM Reserves;
		5. SELECT * FROM Sailors WHERE Sailors.B >= Sailors.C;
		6. SELECT Sailors.A FROM Sailors WHERE Sailors.B >= Sailors.C
		7. SELECT Sailors.A FROM Sailors WHERE Sailors.B >=  Sailors.C AND Sailors.B < Sailors.C;
		8. SELECT * FROM Sailors, Reserves WHERE Sailors.A = Reserves.G;
		9.SELECT * FROM Sailors, Reserves, Boats WHERE Sailors.A = Reserves.G AND Reserves.H = Boats.D;
		10. SELECT * FROM Sailors, Reserves, Boats WHERE Sailors.A = Reserves.G AND Reserves.H = Boats.D AND Sailors.B < 150;

### Case 1: 
	Boats:		Tuples: 3	Range: 2	
	Sailor: 	Tuples: 1 	Range: 5
	Reserves:   Tuples: 10 	Range: 5
	
**Result:** Pass 
	

### Case 2:
	Boats:		Tuples: 10	Range: 3  
	Sailor: 	Tuples: 10 	Range: 5
	Reserves:   Tuples: 15 	Range: 5
**Result:** Pass

## Test Plan2: large smaples
### Case 1: large range 
	Boats:		Tuples: 5000	Range: 1000	
	Sailor: 	Tuples: 6000 	Range: 2000
	Reserves:   Tuples: 2000 	Range: 500
	
**Result:** Pass 
	

### Case 2: small range
	Boats:		Tuples: 5000	Range: 100  
	Sailor: 	Tuples: 6000 	Range: 200
	Reserves:   Tuples: 2000 	Range: 300  
#### Time Consumption: see excel file

**Result:** Pass