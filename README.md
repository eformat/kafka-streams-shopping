# kafka-streaming-shopping

![images/kafka-shopping.png](images/kafka-shopping.png)

### Basic Usage

Run kafka cluster

```bash
podman-compose up -d
```

Create kafka topics

```bash
add_path /opt/kafka_2.13-2.8.0/bin
for x in purchases masked rewards bigspenders north south storage; do
  kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic $x;
done
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

kafkacat -b localhost:9092 -t masked -o beginning -C -f '\nKey (%K bytes): %k
Value (%S bytes): %s
Timestamp: %T
Partition: %p
Offset: %o
Headers: %h'

kafkacat -b localhost:9092 -t rewards -o beginning -C -f '\nKey (%K bytes): %k
Value (%S bytes): %s
Timestamp: %T
Partition: %p
Offset: %o
Headers: %h'

kafkacat -b localhost:9092 -t bigspenders -o beginning -s key=q -C -f '\nKey (%K bytes): %k
Value (%S bytes): %s
Timestamp: %T
Partition: %p
Offset: %o
Headers: %h'

kafkacat -b localhost:9092 -t north -o beginning -C -f '\nKey (%K bytes): %k
Value (%S bytes): %s
Timestamp: %T
Partition: %p
Offset: %o
Headers: %h'

kafkacat -b localhost:9092 -t south -o beginning -C -f '\nKey (%K bytes): %k
Value (%S bytes): %s
Timestamp: %T
Partition: %p
Offset: %o
Headers: %h'
```

Install data-library
```bash
cd data-library && mvn package install
```

Run quarkus-kafka-streaming
````bash
cd quarkus-kafka-streaming
mvn quarkus:dev
````

Run aurkus-kafka-producer
````bash
cd quarkus-kafka-producer
mvn quarkus:dev
````

Make a random purchase
```bash
curl -s -H 'accept: */*' http://localhost:8082/buy | jq .
```

