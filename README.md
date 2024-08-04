# be-test

#### 1.System Introduce

| ServiceName | Port          |
| ----------- | ------------- |
| Ping        | 8081(default) |
| Pong        | 8080          |

#### 2.System Teconolgy

| Software        | Version           |
| --------------- | ----------------- |
| JDK             | 17.0              |
| Spring boot     | 3.3.2             |
| Spring web flux | 33.2              |
| Spock           | 2.4-M1-groovy-4.0 |
| Jacoco          | 0.8.11            |

#### 3.System Run

##### 3.1 Ping Service

```
#Dynamic exec according to port
java -jar ping.jar --server.port=8081
java -jar ping.jar --server.port=8082
java -jar ping.jar --server.port=8083
```

##### 3.2 Pong Service

```
Java -jar ping.jar
```

