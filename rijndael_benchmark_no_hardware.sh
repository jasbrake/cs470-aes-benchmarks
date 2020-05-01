#!/bin/bash

mkdir out
javac -d "out" -cp "lib/*" src/RijndaelBenchmark.java
java -cp "lib/*:out" -XX:-UseAES -XX:-UseAESInstrinsics RijndaelBenchmark