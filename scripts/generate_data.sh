#!/bin/bash

if [ ! -f data256.bin ]; then
	head -c 256MB < /dev/urandom > data256.bin
fi
if [ ! -f data512.bin ]; then
	head -c 512MB < /dev/urandom > data512.bin
fi
if [ ! -f data1024.bin ]; then
	head -c 1024MB < /dev/urandom > data1024.bin
fi
