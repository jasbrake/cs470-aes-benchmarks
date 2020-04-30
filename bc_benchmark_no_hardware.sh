#!/bin/bash

mkdir out
javac -d "out" -cp "lib/*" src/BCBenchmark.java
java -cp "lib/*:out" -XX:-UseAES -XX:-UseAESInstrinsics BCBenchmark
