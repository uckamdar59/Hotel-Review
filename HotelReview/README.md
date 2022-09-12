# Hotel Review

This project is created for a hotel review application.

### Prerequisites
- Postgres SQL
- Java 8
- Start postgres server locally
- Update `DB connection properties` to match local postgres credentials in [application.properties](./src/main/resources/application.properties)

The build tool for this project is maven. To run this application, simply run `mvn spring-boot:run`

## Notes

- Firstly, need to create `hotel` database in postgres and then execute the database scripts mentioned in the `database.properties` (commented out)
- The Api Routes are mentioned in the controller package. Base Url is : http://127.0.0.1:8080/
- Have tried to implement basic functionalities

## More Features which can be added

- Integration of flyway can be done.
- Authentication can be added.
- More valid exception can be thrown


