# quarkus-kafka-producer project

### Basic Usage

Run kafka cluster

```bash
podman-compose up -d
```

Create kafka topics

```bash
add_path /opt/kafka_2.13-2.8.0/bin
kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic purchases
```

Check
```bash
kafkacat -b localhost:9092 -L
```

Watch processed topic
```bash
kafkacat -b localhost:9092 -t purchases -o beginning -C -f '\nKey (%K bytes): %k
Value (%S bytes): %s
Timestamp: %T
Partition: %p
Offset: %o
Headers: %h'
```

Run Streaming application
````bash
cd quarkus-kafka-producer
mvn quarkus:dev
````

Make a random purchase
```bash
curl -s -H 'accept: */*' http://localhost:8082/buy | jq .
```
