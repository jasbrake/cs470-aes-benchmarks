#!/bin/bash

mkdir -p out
javac -d "out" -cp "lib/*" src/BCBenchmark.java
java -cp "lib/*:out" BCBenchmark "bc_results.csv"
