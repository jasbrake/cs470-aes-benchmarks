#!/bin/bash

mkdir -p out
javac -d "out" -cp "lib/*" src/RijndaelBenchmark.java src/Rijndael/*.java
java -cp "lib/*:out" -XX:-UseAES -XX:-UseAESIntrinsics RijndaelBenchmark "rijndael_no_hardware_results.csv"
