#!/bin/bash

javac -d "out" -cp "lib/*" src/BCBenchmark.java
java -cp "lib/*:out" BCBenchmark
