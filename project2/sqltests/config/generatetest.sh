#!/bin/bash
testname="$1"
input="$2"
password="$3"
curr=$(pwd)
testdir="$curr/$testname"
user="root"
database="cs4321"
inputdir="$testdir/input"
expected="$testdir/expected_output"
outputdir="$testdir/output"

rm -rf "$testname"
mkdir "$testname"
mkdir "$expected"
mkdir "$inputdir"
mkdir "$outputdir"
cp -r db "$inputdir"
cp "$input" "$inputdir"

COUNTER=1
while IFS='' read -r line || [[ -n "$line" ]]; do
         mysql --user="$user" --password="$password" --database="$database" \
             --execute="${line//;} INTO OUTFILE '/tmp/query$COUNTER' FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n'; " 
         #| tr '\t' ',' > "./$expected/query$COUNTER" 
        mv /tmp/query$COUNTER "$expected" 
        echo "Text read from file: $expected"
        COUNTER=$((COUNTER+1))
    done < "$input"
rm -rf "../$testname"
mv "$testname" ../
#mysql --user="$user" --password="$password" --database="$database" --execute="DROP DATABASE $user; CREATE DATABASE $database;"
