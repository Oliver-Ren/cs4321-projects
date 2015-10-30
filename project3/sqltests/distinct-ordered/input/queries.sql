select distinct * from Sailors S1, Sailors S2, Reserves R order by S2.A, R.H;
select distinct * from Sailors S1, Sailors S2, Reserves R order by S2.B, R.G, S1.A, S1.C;
select distinct S1.A, S2.B from Sailors S1, Sailors S2, Reserves R order by S2.B, R.G, S1.A, S1.C;
select distinct R.G, R.H from Sailors S1, Sailors S2, Reserves R order by S2.B, R.G, S1.A, S1.C;
