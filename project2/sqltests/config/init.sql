create user 'tester'@'localhost';
create database cs4321 character set = utf8;
grant all on cs4321.* to 'tester'@localhost;
use cs4321;
# create the tables
CREATE TABLE Sailors
(
A bigint,
B bigint,
C bigint
);

CREATE TABLE Boats
(
D bigint,
E bigint,
F bigint
);

CREATE TABLE Reserves
(
G bigint,
H bigint
);

INSERT INTO Sailors VALUES(1,200,50);
INSERT INTO Sailors VALUES(2,200,200);
INSERT INTO Sailors VALUES(3,100,105);
INSERT INTO Sailors VALUES(4,100,50);
INSERT INTO Sailors VALUES(5,100,500);
INSERT INTO Sailors VALUES(6,300,400);


INSERT INTO Boats VALUES(101,2,3);
INSERT INTO Boats VALUES(102,3,4);
INSERT INTO Boats VALUES(104,104,2);
INSERT INTO Boats VALUES(103,1,1);
INSERT INTO Boats VALUES(107,2,8);

INSERT INTO Reserves VALUES(1,101);
INSERT INTO Reserves VALUES(1,102);
INSERT INTO Reserves VALUES(1,103);
INSERT INTO Reserves VALUES(2,101);
INSERT INTO Reserves VALUES(3,102);
INSERT INTO Reserves VALUES(4,104);

