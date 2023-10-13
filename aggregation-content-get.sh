#!/bin/bash

echo "Starting AggregationServer..."
java AggregationServer &
java ContentServer localhost:4567 contentserverfeeds_1.txt &
java GETClient localhost:4567 > output.txt