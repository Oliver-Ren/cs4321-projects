#! /bin/bash
testname="$1"
rm -rf "$testname"
cp -r samples/ "../$testname"
