# Kafka course project
This project is created in order to learn concepts about kafka event driver service.

## How to execute
Execute following commands

- mvn install
- java --jar /target/kafka-course-0.0.1.SNAPSHOT.jar

# Set up kafka in local linux machine, using docker compose

### 1. Simple cluster 
- docker compose up
### 2. Multi cluster 
- docker compose -f docker-compose-multicluster.yml

# Create topic in kafka cluster
### 1. Go inside docker container
- docker exec -ti kafka1 bash
### Execute command for create topic with replication factor and partition number
- kafka-topics --bootstrap-server localhost:9092 --create --topic topic-course-1 --replication-factor 1 --partitions 1

# Produce messages
### 1. Go inside docker container
- docker exec -ti kafka1 bash
### 2. Execute command for producing messages
- kafka-console-producer --bootstrap-server localhost:9092 --topic topic-course-1
#### With key for partitioning
- --property "key.separator=-" --property "parse.key=true"

# Consume messages
### 1. Go inside docker container
- docker exec -ti kafka1 bash
### 2. Execute command for producing messages
- kafka-console-consumer --bootstrap-server localhost:9092 --topic topic-course-1
#### All messages
- --from-beginning
#### With key for partitioning
- --property "key.separator=-" --property "parse.key=true"
#### With group id
-  --group group-course-1
#### With headers 
--property "print.headers=true" --property "print.timestamp=true" 

# Another commands for Kafka cluster
### List topics
- kafka-topics --bootstrap-server localhost:9092 --list
### Describe topic 
- kafka-topics --bootstrap-server localhost:9092 --describe --topic topic-course-1
### Modify topic number partition
- kafka-topics --bootstrap-server localhost:9092 --alter --topic topic-course-1 --partitions 40
### List consumer groups
- kafka-consumer-groups --bootstrap-server localhost:9092 --list

## Credits
This proyect used code of [kafka-for-developers-using-spring-boot-v2][https://github.com/dilipsundarraj1/kafka-for-developers-using-spring-boot-v2/tree/main] by the author @Dilip Sundarraj