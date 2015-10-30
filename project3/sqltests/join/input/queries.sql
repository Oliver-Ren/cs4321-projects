SELECT * FROM Sailors S1, Reserves R where S1.A = R.G;
SELECT * FROM Boats, Reserves, Sailors where Boats.D < Reserves.G;
SELECT * FROM Boats, Reserves, Sailors where Boats.D > Reserves.G and Sailors.A = Reserves.G;
SELECT * FROM Boats, Reserves, Sailors S2 where Boats.D < Reserves.G and S2.A = Reserves.G;
