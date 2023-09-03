# Test project - clever-bank-test-task

## Technical stack

- Java 17
- Gradle
- PostgreSQL
- JDBC
- Lombok
- Servlets
- Docker

## Technical task

Main entities:
- Bank
- Check
- Client
- Transaction

1. Implement operations of replenishment and withdrawal of funds from the account
2. Implement the possibility of transferring funds to another client of Clever-Bank and
   client of another bank. When transferring funds to another bank, use one
   transaction and ensure security.
3. Regularly, according to a schedule (once every half a minute), check whether it is necessary to charge
   percentage (1% - the value is substituted from the configuration file) on the remainder
   bills at the end of the month.
4. Store values in the configuration file - .yml
5. After each operation, it is necessary to generate a check. Save checks to the check folder, in the root of the project
6. Ensure more than 70% unit test coverage (service layers - 100%
   coating).
7. etc.

## To use the application

The console part of the application can be used as follows:
* clone project from repository
* run 
```text 
gradle build 
```
* raise the database in the docker container and push the data there: 
```text 
docker compose build 
```
and then 
```text
docker compose up -d db
```
* run the Main.class and perform the necessary operations in the terminal.
