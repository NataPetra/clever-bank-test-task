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

## To use the application (console application)

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
* run the [Main.java](src%2Fmain%2Fjava%2Fby%2Fnata%2FMain.java) and perform the necessary operations in the terminal
note: you can run the compiled jar file through the Windows console using the command (however, the receipt will not be saved)
```text
java -jar clever-bank-test-task-1.0-SNAPSHOT.jar
```

## To use the application (Tomcat version):

* clone project from repository
* uncomment the plugin "war" in the file [build.gradle](build.gradle)
* change dbUrl in file [config.yml](src%2Fmain%2Fresources%2Fconfig.yml)
* run 
```text 
docker compose build 
```
```text
docker compose up -d db
```
```text
docker compose up -d app-test
```
* to execute refill operation go to

[http://localhost:8080/app/controller?command=Refill&account=Account-6404&sum=15](http://localhost:8080/app/controller?command=Refill&account=Account-6404&sum=15)

where account - account number you want to refill, sum - the amount you want to refill with

* to execute withdrawal operation go to

[http://localhost:8080/app/controller?command=Withdrawal&account=Account-6404&sum=15](http://localhost:8080/app/controller?command=Withdrawal&account=Account-6404&sum=15)

where account - account number you want to withdrawal, sum - the amount you want to withdrawal with

* to execute transfer operation go to

[http://localhost:8080/app/controller?command=Transfer&sourceAccount=Account-2765&destinationAccount=Account-7700&sum=100.0](http://localhost:8080/app/controller?command=Transfer&sourceAccount=Account-2765&destinationAccount=Account-7700&sum=100.0)

where sourceAccount - account from where you want to transfer money, destinationAccount - account where you want to transfer money, sum - the amount you want to transfer with

## To run integration tests:

* comment line 65 in the file [build.gradle](build.gradle)
* run [AccountServiceIntegrationTest.java](src%2Ftest%2Fjava%2Fby%2Fnata%2Fservice%2FAccountServiceIntegrationTest.java)


