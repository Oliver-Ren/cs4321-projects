select distinct S1.A, S2.B from Sailors S1, Sailors S2, Reserves R order by S2.B, R.G, S1.A, S1.C, S1.B, S2.A, S2.C, R.H INTO OUTFILE '/tmp/query3' FIELDS     TERMINATED BY ',' LINES TERMINATED BY '\n';
select distinct R.G, R.H from Sailors S1, Sailors S2, Reserves R order by S2.B, R.G, S1.A, S1.C INTO OUTFILE '/tmp/query4' FIELDS     TERMINATED BY ',' LINES TERMINATED BY '\n';
