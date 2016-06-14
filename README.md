# isolation-levels-demo
Demo of various interprocess transaction isolation scenarios using Spring Boot, JPA, Hibernate, Flyway, PostgreSQL


How to use it:


* first of all read the code
* then have java8 installed
* open two consoles:
  * in the first console type: `./mvnw clean spring-boot:run -Pfirst`
  * wait to see a log line like: `" [           main] c.example.jpa.locks.InterProcessTryLock  : Waiting for the other process...
"`
  * in the second console type: `./mvnw clean spring-boot:run -Psecond`
* watch  the logs, the demo will execute JPA transaction isolation scenarios simulating two concurrent transactions on these separate java processes

Enjoy and contribute with new scenarios to demonstrate other isolation types ( e.g. READ UNCOMMITTED - I have issues creating this one )
