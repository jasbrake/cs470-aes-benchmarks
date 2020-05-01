# JMU CS470 AES Benchmarks
This repo contains a research project for the JMU CS course Parallel and Distributed Systems. The purpose of this project was to benchmark and compare various AES implementations under different circumstances primarily focusing on Java implementations.

# Team
* Jason Brake
* Will Dickison
* Michael McGloin
* Steven Taylor

# Project Files
* `benchmarks/` contains scripts to compile and run each benchmark.
* `lib/` contains Java libraries.
* `scripts/` contains misc scripts for data generation, set up of benchmarks, etc.
* `src/` contains java source code of our benchmarks.
* `results/` contains the data our benchmarks generated.
* `benchmark.sh` a script to generate the data files and run all the benchmarks.
* `openssl_benchmark.sh` a script to run the same encryption/decryption with the command line version of OpenSSL, but we never got it working correctly.

# Environment
All of our benchmarks were run on a Google Cloud Compute instance of type n1-standard-2 (2 vCPUs, 7.5 GB memory) with an Intel Skylake CPU.

This code was tested in a Linux environment running OpenJDK 1.8.

# Run
To run all the benchmarks run `benchmark.sh` or run an individual benchmark using the corresponding script in `benchmarks/`.
