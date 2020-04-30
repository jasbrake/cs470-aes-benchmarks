#!/bin/bash

echo "Encrypting..."
time openssl aes-256-cbc -a -in $1 -out $1.enc -pass pass:"wabbit"
echo "Decrypting..."
time openssl aes-256-cbc -d -a -in $1.enc -out $1.out -pass pass:"wabbit"
if cmp $1 $1.out -s; then
	echo "files different"
fi
#rm $1.enc $1.out
