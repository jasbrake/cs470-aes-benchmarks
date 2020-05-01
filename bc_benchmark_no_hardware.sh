#!/bin/bash

mkdir -p out
javac -d "out" -cp "lib/*" src/BCBenchmark.java
java -cp "lib/*:out" -XX:-UseAES -XX:-UseAESInstrinsics BCBenchmark "bc_no_hardware_results.csv"
