#!/bin/bash

head -c 256MB < /dev/urandom > data256.bin
head -c 512MB < /dev/urandom > data512.bin
head -c 1024MB < /dev/urandom > data1024.bin
