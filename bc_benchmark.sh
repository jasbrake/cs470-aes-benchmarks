#!/bin/bash

javac -d "out" src/BCBenchmark.java
java -cp "lib/*:out"  BCBenchmark
