#!/bin/bash

mkdir -p out
javac -d "out" -cp "lib/*" src/RijndaelBenchmark.java
java -cp "lib/*:out" RijndaelBenchmark "rijndael_results.csv"