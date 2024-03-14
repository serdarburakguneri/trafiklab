# trafiklab

This is a simple app that uses the Trafiklab API to display some information about the Stockholm public transport
system.

## Tech/Framework Used

- Java 17
- Spring Boot
- Maven
- Postgres

## Features

- Displaying bus lines and their stops

## Installation

1. Clone the repository

```bash
git@github.com:serdarburakguneri/trafiklab.git
```

2. cd into the directory

```bash
cd trafiklab
```

3. Compile the project

```bash
mvn clean compile
```

4. The app uses a Postgres database. You can use the following command to start a Postgres container if you don't have
   Postgres installed on your machine:

```bash
cd docker

docker-compose up
```

Requirements:

You need to have Docker daemon running on your machine.

Additionally, you need to have Docker Compose installed. You may
install it from [here](https://docs.docker.com/compose/install/)

By modifying the `docker-compose.yml` file, you can change the database port, name, user and password. Be sure to modify
the `application.properties` file accordingly.

Ensure that you are not using a port that is already in use.

5. Modify the `application.properties` file to match your database and trafiklab api configuration.

For postgres, the default configuration is:

```properties

# R2DBC properties
spring.r2dbc.url=r2dbc:postgresql://localhost:5432/trafiklab
spring.r2dbc.username=postgres
spring.r2dbc.password=postgres
# Flyway properties
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
spring.flyway.url=jdbc:postgresql://localhost:5432/trafiklab
spring.flyway.user=postgres
spring.flyway.password=postgres

```

For the Trafiklab API, you need to obtain an API key from [Trafiklab](https://www.trafiklab.se/)

The api url is pointing to the [stops and lines](https://www.trafiklab.se/api/trafiklab-apis/sl/stops-and-lines-2/) API
endpoint.

```properties

# Trafiklab API properties
trafiklab.api.key=<your-api-key>
trafiklab.api.url=https://api.sl.se/api2/LineData.json

```

You may also want to change the port that the app is running on. The default port is 8080.

```properties  
server.port=8080
```  

As a final step, you may disable the CLI runner by setting the following property to false:

```properties
trafiklab.data.sync-on-start=true
```

Otherwise, the app will try to fetch the data from the Trafiklab API and store it in the database on startup.

6. Run the app

```bash
mvn spring-boot:run
```

You should be seeing from the logs that the app is starting and the database migrations are being applied. After that,
the
app will try to fetch the data from the Trafiklab API and store it in the database which may take a while. You may
follow
the progress from the console logs.

7. http://localhost:8080/journey endpoint will display the bus lines and their stops. You may use a REST client like
   Postman to make a GET request to this endpoint.

   Alternatively, you may use the following command to make a GET request to the endpoint:

```bash
curl -i http://localhost:8080/journey
```  

To save the response to a file, you may use the following command:

```bash
curl -i http://localhost:8080/journey -o result.json
```

## Tests

You may run the tests with the following command:

```bash
mvn clean test
```

There are some unit tests and integration tests in the project.

The integration test (JourneyControllerTest) is using the `Testcontainers` library to start a Postgres container and run
the tests against it.

## Challenges & Missing Parts And Possible Improvements

- The data fetched from Trafiklab is large. Therefore, one should increase the buffers and timeouts of web clients or
  read the data as a stream.
  My choice was to fetch the data as a stream, save it in a file to be sure that the data is fetched properly and then
  read the file as chunks and
  save it to the database in a reactive way.

- There are some inconsistencies in the data fetched from Trafiklab. For example, some lines are duplicated.

  For example;

  ```json
  {
  "LineNumber": "282",
  "LineDesignation": "282",
  "DefaultTransportMode": "",
  "DefaultTransportModeCode": "BUS",
  "LastModifiedUtcDateTime": "2010-03-29 00:00:00.000",
  "ExistsFromDate": "2010-03-29 00:00:00.000"
  },
  {
  "LineNumber": "282",
  "LineDesignation": "282",
  "DefaultTransportMode": "",
  "DefaultTransportModeCode": "BUS",
  "LastModifiedUtcDateTime": "2007-08-24 00:00:00.000",
  "ExistsFromDate": "2007-08-24 00:00:00.000"
  }
  ```

There may be a logical explanation for this. But it's blocking me to implement the uniqueness constraint on the line
number in the database.
An alternative solution would be to ignore the old data and only save the latest data.

- The data is relational. There are lines, stops and journey patterns that show the many-to-many relationship of lines
  and stops. Therefore,
  I believe that a relational database is a good choice for this project. Using Redis or a document-based database would
  bring some benefits
  for persisting the data faster, but it would complicate the querying and the data consistency. (From listing lines and
  stops perspective,
  thinking that the data is not changing much, a non-relational database would make sense. We might have line documents
  including stop count and list of stops. This way querying this information would be fast.)


- The app is fetching current data from the Trafiklab API on startup and every night at 03:00 CET thanks to a scheduler.
  It ignores existing data not to duplicate it
  during the fetch. However, it doesn't delete the data that is not present in the Trafiklab API anymore. A cache
  database and a good cache invalidation strategy
  would be beneficial for this case. With a relational database, solving this problem looks like a challenge.

- For a real time application, CQRS may be a good choice. We may have an application that is responsible for fetching
  the data from the Trafiklab API and storing it in the database.
  Then, we may have another application that is responsible for serving the data to the clients. This way, we may scale
  the read and write operations independently. Current implementation is not a good fit for scaling.

  
