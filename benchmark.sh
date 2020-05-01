#!/bin/bash

echo "Generating test data..."
./scripts/generate_data.sh

for f in benchmarks/*.sh; do
	echo "Executing $f..."
	bash "$f" -H
done

echo "Finished."
