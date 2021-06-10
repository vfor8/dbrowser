# DBrowser

A REST service for browsing databases/schemas/tables etc.

## Build
`mvn clean install`

## Run
```shell
java -Dspring.datasource.url=jdbc:mysql://{DB_HOSTNAME}:{DB_PORT}/{DB_NAME} -Dspring.datasource.username={DB_USERNAME} -Dspring.datasource.password={DB_PASSWORD} -jar target/dbrowser-server-0.0.1.jar
```

Replace the placeholders with the correct values:

{DB_HOSTNAME}: hostname where the MySQL database is running
{DB_PORT}: MySQL port
{DB_NAME}: database containing the `connection_details` table (see bellow)
{DB_USERNAME}: username to use to connect to the database
{DB_PASSWORD}: user's password


### Prepare a database
DBrowser stores connection details to a table in MySQL database.

In a MySQL database (corresponding to the connection string provided above) create table `connection_details`, e. g.
```sql
CREATE TABLE connection_details
(
    id            INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(150),
    hostname      VARCHAR(300),
    port          INT,
    database_name VARCHAR(250),
    username      VARCHAR(30),
    password      VARCHAR(40)
);
```

The application cannot run without it.

## API
see [API documentation](doc/api-docs.yaml)

