# kafka-streaming-shopping

### Basic Usage

Run kafka cluster

```bash
podman-compose up -d
```

Create kafka topics

```bash
add_path /opt/kafka_2.13-2.8.0/bin
kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic purchases
kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic storage
```

Check
```bash
kafkacat -b localhost:9092 -L
```

Watch processed topic
```bash
kafkacat -b localhost:9092 -t storage -o beginning -C -f '\nKey (%K bytes): %k
Value (%S bytes): %s
Timestamp: %T
Partition: %p
Offset: %o
Headers: %h'
```
# kafka-streams-shopping
