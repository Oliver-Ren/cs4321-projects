SELECT S1.A FROM Sailors S1, Reserves R where S1.A = R.G;
SELECT Boats.E, Boats.F FROM Boats, Reserves, Sailors where Boats.D < Reserves.G;
SELECT Sailors.C, Sailors.A, Reserves.G FROM Boats, Reserves, Sailors where Boats.D > Reserves.G and Sailors.A = Reserves.G;
SELECT S2.A, S2.B, S2.C, Reserves.H FROM Boats, Reserves, Sailors S2 where Boats.D > Reserves.G and S2.A = Reserves.G;
