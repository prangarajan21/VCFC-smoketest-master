#!/bin/sh
for browser in "safari"
do
mvn clean test -DvcfIp=10.9.10.110 -Dbrowser=$browser
done
