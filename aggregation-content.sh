echo "Starting AggregationServer..."
java AggregationServer &
java ContentServer localhost:4567 contentserverfeeds_1.txt

