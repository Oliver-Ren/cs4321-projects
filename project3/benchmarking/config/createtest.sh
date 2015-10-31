#! /bin/bash
testname="$1"
rm -rf "$testname"
mkdir "$testname"
mkdir "$testname"/expected_humanreadable
mkdir "$testname"/output_humanreadable
mkdir "$testname"/out_sorted_human
mkdir "$testname"/exp_sorted_human
mkdir "$testname"/output
mkdir "$testname"/expected
mkdir "$testname"/temp
cp -r samples/input "$testname"
mv "$testname" .. 
