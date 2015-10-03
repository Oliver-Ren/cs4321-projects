select DISTINCT * from Sailors;
select DISTINCT B from Sailors;
select DISTINCT C FROM Sailors order by C;
select DISTINCT * from Sailors S1, Sailors S2, Reserves R;
select distinct * from Sailors S1, Sailors S2, Reserves R order by S2.A;
select distinct S1.A, R.G from Sailors S1, Sailors S2, Reserves R where R.G = S2.A;
