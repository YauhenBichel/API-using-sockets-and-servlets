# Webservers
- A webserver (RawSocketServer) that uses raw sockets to communicate with clients
- A webserver (JettyServer) that uses Jetty/servlets

# API design, implementation and running:
## CLI arguments
    -hotels input/hotels/hotels.json -reviews input/reviews -threads 3

## Raw Socket Server
    port is 8080
    host is localhost

## Jetty Server
    port is 8090
    host is localhost

## Find a word
    >http://localhost:8080/index?word=nice&num=2

    >http://localhost:8090/index?word=nice&num=2

## Find hotel
    >http://localhost:8080/hotelInfo?hotelId=25622

    >http://localhost:8090/hotelInfo?hotelId=25622

## Find reviews
    >http://localhost:8080/reviews?hotelId=10323&num=2

    >http://localhost:8090/reviews?hotelId=10323&num=2

## Find weather by lat and lng
    >http://localhost:8080/weather?hotelId=25622

    >http://localhost:8090/weather?hotelId=25622

### Socket api client uses URL api.open-meteo.com
    >https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&hourly=temperature_2m&daily=wind_speed_10m_max&forecast_days=1

# How to run with JAR files
## Raw Socket Server
```
cd out/artifacts/HttpServerRawSocket_jar
```

 ```
 java -jar project4HttpServer.jar -hotels ./../../../input/hotels/hotels.json -reviews ./../../../input/reviews -threads 3
 ```

## Jetty Server
```
cd out/artifacts/HttpServerJetty_jar
```

 ```
 java -cp project4HttpServer.jar JettyServerDriver -hotels ./../../../input/hotels/hotels.json -reviews ./../../../input/reviews -threads 3

 ```

# Possible issues
## Port is used
    >sudo lsof -t -i:<port>
    >sudo kill -9 <pid>

## py tests for RawSocket server
    >python3 testProject4.py localhost 8080
    >python3 testProject4Weather.py localhost 8080

## py tests for Jetty server
    >python3 testProject4.py localhost 8090
    >python3 testProject4Weather.py localhost 8090
