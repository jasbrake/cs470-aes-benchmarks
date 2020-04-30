#!/bin/bash

IV="2B4D6251655468576D5A713474367739"
KEY128="48404D635166546A576E5A7234753778"
KEY256="4D6351655468576D5A7134743777217A25432A462D4A614E645267556A586E32"

echo "128 bit"
echo "Encrypting..."
time openssl aes-128-cbc -in "bbb.mp4" -out "bbb_openssl_128.bin" -K $KEY128 -iv $IV
echo "Decrypting..."
time openssl aes-128-cbc -d -a -in "bbb_openssl_128.bin" -out "bbb_openssl_128.mp4" -K $KEY128 -iv $IV

# validate that it was encrypted/decrypted properly
if cmp "bbb.mp4" "bbb_openssl_128.mp4" -s; then
	echo "128 Encryption/Decryption FAILED"
fi
# Cleanup
rm "bbb_openssl_128.bin" "bbb_openssl_128.mp4"

echo "128 bit"
echo "Encrypting..."
time openssl aes-256-cbc -in "bbb.mp4" -out "bbb_openssl_256.bin" -K $KEY256 -iv $IV
echo "Decrypting..."
time openssl aes-256-cbc -d -a -in "bbb_openssl_256.bin" -out "bbb_openssl_256.mp4" -K $KEY256 -iv $IV

# validate that it was encrypted/decrypted properly
if cmp "bbb.mp4" "bbb_openssl_256.mp4" -s; then
	echo "256 Encryption/Decryption FAILED"
fi
# Cleanup
rm "bbb_openssl_256.bin" "bbb_openssl_256.mp4"
