# PART 1: Java 9 to 21

```
Banu Prakash C
Full Stack Architect, Corporate Trainer
Co-founder & Ex-CTO: Lucida Technologies Pvt Ltd.,
Email: banuprakashc@yahoo.co.in; banuprakash.cr@gmail.com; banu@lucidatechnologies.com
https://www.linkedin.com/in/banu-prakash-50416019/
https://github.com/BanuPrakash/21_SPRING
```

Softwares Required:

JDK
```
openJDK 21 https://jdk.java.net/java-se-ri/21
Option 1: install and add path vi ~/.zshrc export JAVA_HOME=/Users/banuprakash/Desktop/jdk-21 export PATH="/Users/banuprakash/Desktop/jdk-21/bin:"$PATH
Option 2: [better]
USE SDKMAN to manage java
curl -s "https://get.sdkman.io" | bash
sdk install java 21.0.6-tem
sdk default java 21.0.6-tem

https://mydeveloperplanet.com/2022/04/05/how-to-manage-your-jdks-with-sdkman/#:~:text=Some%20time%20ago%2C%20a%20colleague%20of%20mine,maintain%20different%20versions%20of%20JDKs%2C%20Maven%2C%20etc.
```
IDE
```
IntelliJ Ultimate edition https://www.jetbrains.com/idea/download/?section=mac

```

Docker Desktop

===============================

Part 1:
```
Java 9 (September 2017):
1)	Modules
2)	JShell
3)	Improved try with resource Blocks
4)	Immutable Collection [of]
List<String> strList = List.of("A", "B", "C");
earlier we use Collections.toImmutableCollection(..);

Java 10 (March 2018):
1)	Using var keyword

Java 11 (September 2018):
1)	String API updates
a.	isBlank
String s = " ";
s.isBlank();

b.	lines

String str = """
    Hello World \n
    Good Day \n

""";
c.	stripLeading() and stripTrailing()
d.	repeat()

"*".repeat(10);

2)	isEmpty() on Optional class on top of existing isPresent() 

Java 12 (March 2019):
1)	Defaulting Class Data Sharing [CDS]
2)	Application Data Sharing

    Java 13 (September 2019):
1)	Pattern Matching with instanceof
2)	Better NullPointerException
3)	Text Blocks; multiline text


Java 14 (March 2020):
1)	records
2)	Hidden classes

Java 15: (September 2020): Garbage Collector Updates

Java 16(March 2021) : updates

Java 17 (September 2021):
1)	Sealed classes
2)	Pattern Matching switch statement

Java 18:
1)	Simple Server for the web

Java 19:
1)	Virtual Threads (preview)

Java 21 (September 2023):
1) Improvements for Pattern matching on sealed classes, 
2) virtual threads [stable]

```

=====================

Part 1: Migrating Java from version 8 to 21 [9, 11, ..21]
Part 2: Spring Boot and building RESTful WS with JPA [RDBMS]

Day 1:

JPMS: Modules -- Project JigSaw

Modularity:
java 8 rt.jar -- runtime jar file part of jdk and it's not modularized

we might not need even 20% of apis provided by rt.jar
all public members are accessable  from classes added to classpath;

```
    A single jar [api.jar] contains 2 packages

    package com.adobe.module.repo;
    public class UserRepo {
        ///
    }

    package com.adobe.module.service;
    import com.adobe.module.repo.UserRepo;

    public class AppService {
        UserRepo userRepo;
        ...
    }
```

Client applications developed using android/ desktop / tv /web will add [api.jar]

```
    client code can access service package as well as repo package
    no way we can prevent repo access in client
```

Module System: OSGi https://en.wikipedia.org/wiki/OSGi
MANIFEST.MF
```
Bundle-Name:API
 Bundle-SymbolicName: org.wikipedia.helloworld
 Bundle-Description: A Hello World bundle
 Bundle-ManifestVersion: 2
 Bundle-Version: 1.0.0
 Bundle-Activator: org.wikipedia.Activator
 Export-Package: com.adobe.module.service, com.adobe.module.entity;version="1.0.0"
 Import-Package: org.springframework;version="3.5.0"

```

Java 9 made apis become modular; we can only use required modules rather than loading entire rt.jar

Types of Modules:
1) System modules
java --list-modules

```
java --describe-module java.logging
java.logging@21.0.6
exports java.util.logging
requires java.base mandated
provides jdk.internal.logger.DefaultLoggerFinder with sun.util.logging.internal.LoggingProviderImpl
contains sun.net.www.protocol.http.logging
contains sun.util.logging.internal
contains sun.util.logging.resources

```

2) Named modules: recommended for new java projected development

project contains module-info.java 
Despite numerous benfits, proper moduels are still rare species in real world; even Spring framework is not modularized

jars are added to module path instead of classpath

3) unnamed modules

4) Automatic modules
    Example: spring.core is added to module path and not classpath; spring.core becomes automatic module
    module shopapp {
        requires spring.core;
    }
=====

Java Modules using Maven Modules and building a application image of less code footprint

Maven project with 3 maven modules [each one of these will be JPMS]

jlink --module-path api.jar:impl.jar:client.jar --add-modules client,api,impl --output myimage --launcher MYAPP=client/client.Main

banuprakash@Banuprakashs-MacBook-Pro bin % ./MYAPP
Logging Std Impl: Hello World!!!

```
# Use a base image with OpenJDK 21
FROM openjdk:21-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled JAR file from your build output to the container
# Replace 'your-application.jar' with the actual name of your JAR file
COPY target/your-application.jar app.jar

# Expose the port your application listens on (if applicable, e.g., for a web app)
EXPOSE 8080

# Define the command to run your application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Spring uses ByteBuddy / JavaAssist / CGLib libraries for reflection API and creating proxies
My Module is not exposing our classes to them for reflection.

```
module spring.demo {
    // automatic modules
    requires spring.core;
    requires spring.boot;
    requires spring.context;
    requires spring.beans;
    requires spring.boot.autoconfigure;

    opens com.example.springdemo to
            spring.core, spring.beans, spring.context;

    opens com.example.springdemo.entity to org.hibernate;
}

```

Java 9: Improved try with resource Blocks

```
    class MyThread extends Thread implements AutoClosable {
        ...
        @Override
        public void close() throws Exception {

        }
    }

    Older Java 7:
    try (MyThread th = new MyThread()) {
        ...
        th.doSomething();
    } catch(Exception ex) { 
    }

    With Java 9:
    MyThread th = new MyThread();
    try (th) {
        ...
    } catch(Exception ex) {

    }
    no need for finally block
```

Java 10: var keyword
var is an inference type. Compiler will inter the type; not dynamic typing like JavaScript var

java 13: Pattern Matching
* instanceof operator enables assigning a variable as part of type check, cleaner code
* enhanced switch, switch as functional, arrow vs yield

java 14: record
Special type designed to serve as efficeitn way to declare an immutable DTO;
reduces boilerplate code

```
record Product(String title, double price) {}

Same as creating a Product class with Parameterized constructor, getters, hashCode, equals, toString
No setters

Product p = new Product("iPhone", 89000.00);

p.title(); p.price();

Not understood by frameworks like Hibernate / Spring --> they use getXXX() and setXXX(); --> can use lombok

``

java 14 and 15: Hidden classes
--enable-preview in java 14

* Runtime classes: not present in source or .class file
* not discoverable: not accessible via reflection or classpath
- Can't use Class.forName() of classloader
* Main purpose is meant to be used by frameworks / tool
* Garbage Collector: Automatically cleaned up when no longer referenced

Byte Buddy/ JavaAssit is a code generation and manipulation library for creating and modifying Java classes during the runtime of a Java application

Hidden class:
Runtime
Not visible via reflection
not reusable, one time per lookup
meant for framework sort of code

Anonymous class:
Compile time
fully visible
resuable
enclosing class logic; new Comparator<Product> {
    // int compare(...) {}
}

Real World use cases:
1) Dynamic proxies for lazy loading
2) Telemetry like scenarios like profile, count, gauge
3) bytecode instrumentaion for optimzation

===============

Day 2

Java 17 :Sealed class 
Explicit control over which classes can extend or implement a interface

Why?
* Prevent unwanted subclassing
* Safer Switch pattern matching
* API contracts are cleaner
* Better modeling / tooling

sealed, final, non-sealed

```
    public sealed class JsonValue permits JsonObject, JsonPrimitive, JsonArray{

    }

    public final class JsonObject extends JsonValue {

    }

    public sealed class JsonPrimitive extends JsonValue permits JsonString, JsonNumber, JsonBoolean{
        
    }

    public final class JsonArray extends JsonValue {
        
    }

    // need to make below code invalid
    public class JsonSomeType extends JsonValue {
        
    }

    sealed class Node permits Element, CDATASection, CharacterData, Comment {

    }

    org.w3c.dom.Element --> dozens of HTML element sub classes, we might create our own shadow elements like
    <product></product>

    non-sealed class Element extends Node {

    }

```

========


Java 12 (March 2019):
ClassLoader --> loads the class
* findLoadedClass()
* loadClass()
* findSystemClass()
* defineClass()
* verify()

Time spent for loading during startup.

1)	Defaulting Class Data Sharing [CDS]
In Java 6, we had to explictly configure, it became default in java 12

classlist in JDK

2)	Application Data Sharing
for classes other than JDK provided api classes

https://github.com/spring-projects/spring-petclinic

```
./mvnw clean package -Dmaven.test.skip=true

java -jar target/spring-petclinic-3.5.0-SNAPSHOT.jar

Started PetClinicApplication in 5.518 seconds 

Steps for appCDS:

https://docs.spring.io/spring-boot/reference/packaging/efficient.html

 java -Djarmode=tools -jar target/spring-petclinic-3.5.0-SNAPSHOT.jar extract


java -XX:ArchiveClassesAtExit=./application.jsa -Dspring.context.exit=onRefresh -jar \
spring-petclinic-3.5.0-SNAPSHOT/spring-petclinic-3.5.0-SNAPSHOT.jar

Or

java -XX:ArchiveClassesAtExit=./application.jsa -XX:DumpLoadedClassList=files.lst -Dspring.context.exit=onRefresh -jar \
spring-petclinic-3.5.0-SNAPSHOT/spring-petclinic-3.5.0-SNAPSHOT.jar


java -XX:SharedArchiveFile=application.jsa -jar \
spring-petclinic-3.5.0-SNAPSHOT/spring-petclinic-3.5.0-SNAPSHOT.jar

Started PetClinicApplication in 1.495 seconds (process running for 1.814)

AOT, GraalVM
```

Java 21: Virtual Threads

Virtual Threads are lightweight and created by JVM, not managed by the OS
Thousands can be created without worryning about memory.
Platform Thread -- each thread by default needs 1MB

Virtual Thread are perfect for:
1) Web servers
2) APis that call many APIs or database [assume here we have 50 connections]

How do they work?
* start virtual thread to handle a task
* If it needs to wait (DB or API), JVM parks it.
* Real OS thread is freed and can be used for sone other work
* When response comes back, JVM resumes the Virtual thread;
Virtual thread can now pick any of PT --> OS Thread

--> VT --> PT --> OS for task depending of CPU
--> VT --> DB call here release PT --> OS thread
once DB returns a value --> pick any available PT --> OS thread

===============

brew install siege
brew install hey

 siege http://localhost:1234/example -c 200 -r 5
 hey http://localhost:1234/example -c 200 -t 1000

===

Java 21: Preview []
String Template : interpolation to Java

javac --release 21 --enable-preview YourClass.java

JShell for REPL.

===============================

* Modules - JPMS
* record - DTOs, immutable data object
* sealed classes - permits, sealed, final , non-sealed
* Pattern matching - instanceof , switch arrow, yield, functional switch ,switch with sealed
* Virtual Threads
-Xss:64KB 
* CDS and AppCDS

=====================

Web Container for web components like Servlet/ JSP/ Filter/ Listener, 
EJB Container for distributed computing
ServletContext is an interface using which you can access web container

Spring Framework and Spring Boot Framework
ApplicaitonContext/BeanFactory is an interface using which you can access spring container

Spring provides a Container [layer on top of JVM],
minimalistic capability of this container is life cycle management and wiring dependencies.

Any object managed by spring container is termed as a bean.

SOLID Design Principles:
D - Dependency Injection

```

UI -> Service --> Repository --> Database Connection

public class Client {
    Service service = new Service();
}

public class SErvice {
    UserRepo userRepo = new UserRepo();
}


public class UserRepo {
    Connection con = DataSource.getConnection();
}

Dependency Injection : Flow is reveresed, InversionOfControl

Loosely coupled application
UI <-- Service <-- Repository <-- Database Connection

```

Metadata in the form of XML or Annotation should be provided to Spring Framework for managing the beans.

Life Cycle Management:
Spring instantiates objects of classes which has one of these annotations at class level:
1) @Component
2) @Repository
3) @Service
4) @Configuration
5) @Controller
6) @RestController
7) @ControllerAdvice
8) @ShellComponent

Wiring:
1) @Autowired - Setter DI
2) Constructor DI
```
    beans.xml
    <bean id="userRepo" class="pkg.UserRepositoryJdbcImpl" />

```

Example:

```
    public class User {
        username;
        password;
    }


    interface UserRepo {
        void register(User user);
    }

    // CRUD operations
    @Repository
    class UserRepoDbImpl implements UserRepo {
        public void register(User user) {
            ...
        }
    }

    @Service
    class AppService {
        @Autowired
        UserRepo userRepo;

        public void newUser(User user) {
            userRepo.register(user);
        }
    }

ApplicationContext context =  new ClassPathXmlApplicationContext("beans.xml");


ApplicationContext context = new AnnotationConfigApplicationContext();
context.scan("com.adobe.prj"); 
context.refresh();

in Spring Boot:
ApplicationContext context = SpringApplication.run(SpringDemoApplication.class, args);

userRepoDbImpl

AppService appService = context.getBean("appService", AppService.class);
appService.newUser(new User("roger", "secret123"));

```

Spring Boot framework is a framework on top of Spring Framework.
It's a highly opiniated frameowrk, where lots of configurations comes out of the box.
1) While using database; database connection pool is created using HikariCP library
2) While using JPA/ORM; hibernate is configured out of the box.
3) While using web application; Tomcat web server is configured by default along with Java to JSON and JSON to Java conversion using Jackson libary 


@SpringBootApplication is 3 in one:
1) @Configuration
2) @ComponentScan similar to context.scan("com.adobe.prj"); 
3) @EnableAutoConfiguration --> builtin beans like Tomcat / DatabaseConnection pool


=================


appService

userRepoDatabaseImpl

Field userRepo AppService required a single bean, but 2 were found:
	- userRepoDatabaseImpl:	
    - userRepoMongoImpl: 


Solution 1:
@Primary

Solution 2:
@Qualifier


```
@Service
public class AdminService {
    @Qualifier("userRepoMongoImpl")
    @Autowired
    private UserRepo userRepo; // wiring is done by Spring Container

    public void newUser() {
        userRepo.register();
    }
}

```

Solution 3: @Profile

Solution 4: @ConditionalOnMissingBean

```
@ConditionalOnMissingBean(name="userRepoMongoImpl")
@Repository
public class UserRepoDatabaseImpl implements UserRepo { }

@Repository
public class UserRepoMongoImpl implements UserRepo { }

```

Factory Methods with @Bean
* Need to manage objects of 3rd party classes which can't have any of the above 8 annotations
* Object creation is complex, not just invoking constructor

==========================================

Docker:
Docker is a set of platform as a service (PaaS) products that use OS-level virtualization to deliver software in packages called containers.
* MySQL
* Redis

Softwares are available as images on docker hub
docker run --name local-mysql -p 3306:3306  -e MYSQL_ROOT_PASSWORD=Welcome123 -d mysql

docker exec -it local-mysql bash

mysql -u root -p
Enter Password: Welcome123

==================

Spring ORM Module

https://github.com/spring-projects/spring-framework/blob/main/spring-jdbc/src/main/resources/org/springframework/jdbc/support/sql-error-codes.xml

```

    @Entity
    @Table(name="vehicles")
    public class Vehicle {

        @Column(name="REG_NO")
        String registrationNumber;

        @Column(name="VTYPE", length=50)
        String vehicleType;

        @Column(name="FUEL_TYPE", length=20)
        String fuelType;

        @Column(name="COST_PER_DAY")
        double costPerDay;
    }

```

```
@Configuration
public class AppConfig {

    // factory method
    @Bean
    public DataSource getDataSource() throws Exception{
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass( "org.h2.Driver" ); //loads the jdbc driver
        cpds.setJdbcUrl( "jdbc:h2:mem:testdb" );
        cpds.setUser("sa");
        cpds.setPassword("password");
        cpds.setMinPoolSize(5);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(20);
        return  cpds;
    }

    @Bean
    public EntityManagerFactory emf(DataSource ds) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(ds);
        emf.setJpaVendor(new HibernateJpaVendor());

        return emf;
    }
}
```

What does Spring Boot JPA provide?
1) JPA JavaPersistence API is an abstraction on top of ORM.
JPA --> ORM [Hibernate / toplink / OpenJpa / EclipseLink / KODO / JDO] --> JDBC --> Database

2) Provides DataSource using HikariCP connection pool out of the box based on database properities in application.properties

3) LocalContainerEntityManagerFactoryBean is created with HibernateJPAVendor  out of the box

4) provides JpaRepository interfaces; we just need to create interfaces extending from JpaRepository;
implementation classes [@Repository] are generated by Spring Data Jpa;

```
    public interface VehicleRepo extends JpaRepository<Vehicle, String> {

    }

```
    default methods for CRUD operations are genereted in a class implementing VehicleRepo interface.

    @VehicleDao vehicleDao; // here generated class is wired

    Spring Boot uses ByteBuddy / JavaAssist and CGLib for byte code instrumentaiton
    Byte Buddy is a code generation and manipulation library for creating and modifying Java classes during the runtime of a Java application and without the help of a compiler.

    Javassist is a Java library that simplifies the process of modifying bytecode in Java programs. It allows developers to define new classes at runtime and modify existing class files as the Java Virtual Machine (JVM) loads them.

    Similarly we have MongoRepository for mongodb   


=====================================

Day 4:

Spring boot application with following dependencies:
1) lombok [ code generation library]
2) mysql driver [ JDBC drivers]
3) Spring Data JPA for using JPA on top of ORM
4) later we will add web dependencies

VehicleRentalApplication

Note: @Entity and @Id are compulsory; @Column and @Table are optional
https://docs.spring.io/spring-boot/appendix/application-properties/index.html

1) spring.jpa.hibernate.ddl-auto=create

create tables when application starts; drop tables when application terminates
Good for testing purpose only

2) spring.jpa.hibernate.ddl-auto=update
if table exists map class to existing table
if table is not present, create tables based on mapping
if required alter table [change column length, add new column]
Good for Top to bottom approach

3) spring.jpa.hibernate.ddl-auto=verify
Map classes to existing tables; Alter and creating new tables are not suported;
Good for Bottom to Top approach


CommandLineRunner is an interface provided by Spring Boot that allows for the execution of specific code [run()] immediately after the Spring Boot application context has been loaded and initialized, but before the application starts processing requests. 


JPA and ORM can be used withount Spring Framework.

```

```
Try creating CustomerRepo and link it in RentalService
Write CustomerClient

insert into customers values('roger@adobe.com','Roger','Smith');
insert into customers values('anne@adobe.com','Anne','Hathaway');

```

Mapping Associations:
1) One To Many
2) Many To One
3) One To one
4) Many To Many

https://martinfowler.com/bliki/BoundedContext.html

https://www.database-answers.com/data_models/


Within a Transactional boundary if an entity becomes dirty --> ORMs will flush the state to database by issuing UPDATE SQL.

SELECT * FROM vehicles v WHERE v.reg_no NOT IN (     SELECT r.vehicle_fk     FROM rentals r     WHERE '2025-07-30' BETWEEN r.rent_from AND r.rent_until );

===========

Task : Ticket Tracking application

```
    employees
    email | first_name

    ticket
    ticket_id | raised_date | problem | raised_by | resolved_by | resolved_date | solution

    Use case 1: Raise a ticket

    User case 2: 
    get open tickets [ list ]
    resolve a ticket

```

====================================

Spring Data JPA module: hibernate as ORM Provider and hikariCP for database connection pool
application.properties 
JpaRepository interface : findAll, findById, save, delete
Custom Projections findByXXXX(); And Or
We can also use SQL or JP-QL to write custom queries; can be used for scalar values, joins
@Modifying for custom UPDATE / INSERT or DELETE query
@Transactional --> Atomic operation; if no exception occurs within the method marked with @Transactional it commits else it rollsback. @Transcational for DIRTY checking

====================================

Building RESTful Web Services

REST REpresentational State Transfer is architectural style for distributed systems,
Roy Fielding 2000

Server Side Rendering
Cons
* Heavy payload [html / pdf]
* Client needs to know how to handle HTML / PDF [ Browser]
* Can't have heterogenous clients like Mobile / Tv / Desktop

Pros
* Thin client
* SEO

Client Side Rendering
Pros:
* Can have heterogenous clients like Mobile / Tv / Desktop / Web
* Light payload like JSON / XML
* Clients can consume and create UI as per requirement

Cons:
* Not SEO
* Heavy client 

==============================

Resource --> information on server that we can name like database / file / printer /image

Representation --> state of the resource at a given point of time

Content Negotiation:
Asking for a suitable presentation by a client for the representation
```
JSON
{
    "printer": "HP Printer",
    "status" : "on
}

XML
<printer>
    <name>Hp Printer</name>
    <status> on </status>
</printer>
```

Uniform Interface: Uniform Identifier
http://amazon.com/mobiles/iPhone16

Client - Server: decoupling
Stateless: No conversational state of client ; every request from client should be treated as a new one
Cacheable: Response should be cacheable

HTTP Methods and URI
POST --> CREATE
GET --> READ
PUT / PATCH --> UPDATE
DELETE --> DELETE


=============

Spring MVC Web module
* Tomcat Embedded Web Server / Container
    --> Alternates are Jetty / Netty ...
* DispatcherServlet
* HandlerMapping
* Jackson library for Java <--> JSON conversion
 --> alternates are GSON / Jettison / Moxy

```
    POST api/vehicles
    Accept: application/json
    Content-type: application/json

    {
        "registrationNumber": "DH01ED4111",
        "fuelType" : "ELECTRIC",
        "costPerDay" : 8000
    }



    @RestController
    @RequestMapping("api/vehicles")
    public class VehicleController {
        RentalService service;
        @GetMapping()
        public List<Vehicle> getVehicles() {
            return service.getVehicles();
        }

        @PostMapping
        public Vehicle addVehicle(Vehicle v) {
            return service.addVehicle(v);
        }
    }

    @RestController
    @RequestMapping("api/rentals")
    public class RentalController {
        ..
    }
```
@Controller is for Server Side Rendering to return pages
@RestController is for Client side rendering to send different formats of representation

Accept --> what server should send to client
Content-type -> what type of payload is sent by client


@ControllerAdvice is meant for a Global Exception handling;
Gets called when an exception is thrown from @Controller or @RestController

=========================

```
    Code Tangling:
    public void transferFunds(Account fromAcc, Account toAcc, double amt) {
        logger.info("..."); // logger
        Transaction tx = em.getSession().beginTransaction(); // tx
        if(securityContext.getPermission() ...) // security
        try {
            logger.info(..);
            // business logic
            fromAcc.debit(amt);
            logger.info(...)
            // business logic
            toAcc.credit(amt);
            logger.info(..);
        
            tx.commit();
        } catch(DataAccessException ex) {
            tx.rollback();
        }



    }

```
AOP: Aspect Oriented Programming to eliminate Code tangling and code scattering

Aspect:
A modular unit encapsulating a cross-cutting concern, such as logging, security, or transaction management, that applies across multiple parts of an application.

Join Point:
A specific point in the execution of a program where an aspect can be applied. 
Examples include method execution, exception handling. 

Pointcut:
selected specific join points where advice should be applied.

Advice:
The action taken by an aspect at a particular join point. Different types of advice exist: 
Before Advice: Executes before the join point.
After Returning Advice: Executes after the join point completes normally (returns without an exception). 
After Throwing Advice: Executes if the join point throws an exception.
After (Finally) Advice: Executes after the join point, regardless of its outcome (normal return or exception). 
Around Advice: Surrounds the join point, allowing custom behavior before and after its execution, and potentially controlling whether the join point proceeds.


try {
    actualMethodCall()
} catch(Exception ex) {
    aspect related
    throw ex;
}


Actual Code after proxy:
 
 ```
  Object[] args = jp.getArgs();
  log.info("Called :" + jp.getSignature());
  for(Object arg: args) {
            log.info("Argument : " + arg);
  }
 rentalRepo.save(rental);
 return "Vehicle rented!!!";

```

=========================

@RestController, @RequestMapping, @GetMapping, @PostMapping, @PutMapping, @PatchMapping, @DeleteMapping, @PathVariable, @RequestBody, @RequestParam

AOP: Aspect, JoinPoint ,PointCut, Advice [Before, After, Around, AfterThrowing]
@ControllerAdvice [ built-in advice which can be used to handle exceptions thrown from Controller or RestController -- AfterThrowing]

=========

Day 6:

Validation:

```
Java Beans validation
https://jakarta.ee/specifications/bean-validation/3.0/apidocs/jakarta/validation/constraints/package-summary

  <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
  </dependency>

public String rentVehicle(@RequestBody @Valid RentalDTO rentalDTO) {

```

MethodArgumentNotValidException:

```
[Field error in object 'rentalDTO' on field 'email':  default message [Email is required]] 

[Field error in object 'rentalDTO' on field 'rentedDate':  default message [Rented Date should be present or future date!!!]] 

[Field error in object 'rentalDTO' on field 'registrationNumber': default message [Registration Number D2AE1241 is not valid!!!]] ]

```

Caching:
1) client level
Cache-Control: max-age=60*60*24
ETag (entity tag)

2) Web Server Spring Boot

```
     <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-cache</artifactId>
      </dependency>

      We get ConcurrentMapCache as the default cache
      Caffiene Cache: Caffeine is a high-performance, in-memory Java caching library known for its near-optimal hit rates and efficient implementation.

      Redis Cache: External

@Configuration
@EnableCaching
public class AppConfig {

@Cacheable(value = "vehiclesCache", key = "#regNo")
@CachePut(value = "vehiclesCache", key = "#regNo")
@CacheEvict(value = "vehiclesCache", key = "#regNo")

```

3) Database
* Swarm Cache
* EHCahe

===================================

Spring Application Event and Async operations

Existing blocking Synchronous Code:

Discharge Patient
```
    // 7 seconds to complete the method
    // tightly coupled
    public String dischargePatient(String id) {
        // sequential execution of task by task
        billingService.processBill(id); //blocking 2 sec
        medicalService.updatePatientHistory(id);//blocking 3sec
        houseKeepingService.cleanAndAssign();//blocking 1 sec
        notificationService.notifyPatient(id);//blocking 1 sec
    }

```

Blocking Tomcat Thread

Thread[#48,http-nio-8080-exec-1,5,main] :  bill process P521A
Thread[#48,http-nio-8080-exec-1,5,main] :  notify patient P521A


Using default Thread pool provided by Spring Framework
@EnableAsync on any config file
@Async

Thread[#65,task-1,5,main] :  bill process P521A
Thread[#66,task-2,5,main] :  notify patient P521A

==================================

https://jsonplaceholder.typicode.com/users

https://jsonplaceholder.typicode.com/posts

===========

```
RestTemplate
    restTemplate.getForObject(...)
    restTemplate.getForEntity(..)
    restTemplate.postForObject("url", String.class, Product.class)
    public List<Product> getListOfMyObjects() {
        String url = "http://localhost:8080/api/products"; // Replace with your actual API endpoint

        ResponseEntity<List<Product>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null, // HttpEntity (can be null for GET requests without a body)
            new ParameterizedTypeReference<List<Product>>() {} // Crucial for correct type inference
        );

        return response.getBody();
    }


   public class MyApiInterceptor implements HandlerInterceptor {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            // Logic to execute BEFORE the controller method is called
            // e.g., logging, authentication, request validation
            System.out.println("Pre-handle logic for: " + request.getRequestURI());
            return true; // Return true to proceed with the request, false to stop
        }

        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
            // Logic to execute AFTER the controller method has executed, but BEFORE the response is sent
            // e.g., modifying model data, adding headers
            System.out.println("Post-handle logic for: " + request.getRequestURI());
        }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            // Logic to execute AFTER the complete request is finished (after view rendering)
            // e.g., cleanup, logging exceptions
            System.out.println("After completion logic for: " + request.getRequestURI());
        }
    }

```



============

Day 7:

Redis:
Caching - caching dependency --> ConcurrentMapCache, Caffiene --> in memory cache
@Cacheable, @CachePut, @CacheEvict, @EnableCaching, @EnableScheduling, @Scheduled

Async operations instead of executing tasks using WebServer Threads [Tomcat / Jetty / Netty]
@EnableAsync, @Async, Executors, ExecutorService, ..., ApplicationEvent and @EventListener
Future and CompletableFuture.

@HttpExchange, @GetExchange, @PostExchange --> RestTemplate / RestClient / WebClient

===================

Redis (Remote Dictionary Server)is an in-memory key–value database, used as a distributed cache.

Redis Cache:
docker run --name some-redis -p 6379:6379 -d redis

By including the below dependency, Spring boot enables redis as CAche Manager instead of default ConcurrentMapCache [@ConditionalOnMissingBean]

```
  <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>

    application.properties
    spring.data.redis.port=6379
    spring.data.redis.host=127.0.0.1

    public class Vehicle implements Serializable {

    }
```

Redis Client to run on NodeJS --> redis commander
npx redis-commander

==========

HATEOAS: HATEOAS, which stands for Hypermedia as the Engine of Application State, is a constraint of the REST architectural style
https://martinfowler.com/articles/richardsonMaturityModel.html

WebMvcLinkBuilder: Builder to ease building Link instances

 ```
  <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-hateoas</artifactId>
        </dependency>

  @RequestMapping("/customers")
 class CustomerController {

   @RequestMapping("/{id}/addresses")
   HttpEntity<Addresses> showAddresses(@PathVariable Long id) { … }
 }

 http://server.com/customers
 customers payload

 Each customer create a link to get addresses

                        /customers                       /2/addresses
 Link link = linkTo(methodOn(CustomerController.class).showAddresses(2L)).withRel("addresses");
    "addresses"
    Link -> /customers/2/addresses
 ```

RepresentationModel --> EntityModel [ entity + links]
RepresentationModel --> CollectionModel [ collection of entities + links]

Affordance, in the context of design and psychology, refers to the perceived and actual properties of an object that suggest how it should be used.

@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL_FORMS)

====

Spring Data Rest
Spring Data REST is to simplify the creation of hypermedia-driven RESTful web services. 
It achieves this by automatically exposing Spring Data repositories as REST endpoints.
Spring Data JPA + HATEOAS = Spring Data REST
* My endpoints doesnt need any business logic
* performing simple CRUD operations
* No need to write RestController

* mysql
* jpa
* Rest Repositories
* lombok

http://localhost:8080/vehicles/search
http://localhost:8080/vehicles/search/findByFuelType?fuelType=PETROL
http://localhost:8080/vehicles/search/findByFuelTypeOrCostPerDayLessThan?fuelType=PETROL&cost=5000

Note: We can't write RestController in this project.

spring.data.rest.base-path=/api
http://localhost:8080/api/vehicles


instead you can use BasePathAwareController to customize

```

@Configuration
public class RestConfiguration implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        config.disableDefaultExposure();

    }
}

@RepositoryRestResource(collectionResourceRel = "people", path = "people")
public interface PersonRepository extends JpaRepository<Person, Long> {
    @Override
    @RestResource(exported = false)
    void deleteById(Long aLong);
}

```


RESTapi Documentation:
1) RAML
```
/books:
  /{bookTitle}
    get:
      queryParameters:
        author:
          displayName: Author
          type: string
          description: An author's full name
          example: Mary Roach
          required: false
        publicationYear:
          displayName: Pub Year
          type: number
          description: The year released for the first time in the US
          example: 1984
          required: false
        rating:
          displayName: Rating
          type: number
          description: Average rating (1-5) submitted by users
          example: 3.14
          required: false
        isbn:
          displayName: ISBN
          type: string
          minLength: 10
          example: 0321736079
    put:
      queryParameters:
        access_token:
          displayName: Access Token
          type: string
          description: Token giving you permission to make call
          required: true
```
2) OpenAPI - Swagger

```
         <!-- OpenAPI -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.8.9</version>
        </dependency>

        http://localhost:8080/v3/api-docs
        http://localhost:8080/swagger-ui/index.html
```

============

Observability - ability to measure the internal state of the system like (logs, metrics and traces)

actutator dependency

```
     <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

http://localhost:8080/actuator

management.endpoints.web.exposure.include=*

ab -c 50 -n 100 http://localhost:8080/api/vehicles

http://localhost:8080/actuator/metrics
http://localhost:8080/actuator/metrics/http.server.requests

http://localhost:8080/actuator/metrics/jvm.threads.live


Time Series database
Prometheus collects and stores its metrics as time series data to scrape data from actuator.

```

Spring Boot 3.x includes micrometer project defines concepts like meters, counters, timers, gauge...

ab -c 5 -n 30 http://localhost:8080/hello     

http://localhost:8080/actuator/metrics/sample.counter

http://localhost:8080/actuator/health/Database


==================================

Reactive Programming: Declarative programing paradigm concerned with data streams and propagation of change.

User clicks on button, run this function

Existing Solutions: @Async Future and CompletableFuture
It's still synchrounous when response is sent to client [join()]

Subscriber and Publisher, Subscription  model

Spring Webflux module --> for reactive programming --> comes with Netty Web server instead of Tomcat Web Server

Netty is a event driven, non-blocking I/O client-server framework for the development of Java network applications such as protocol servers and clients.

Tomcat and Jetty are Thread based.

Outbound Channel Handler

=================================
docker run -d -p 27017:27017 --name some-mongo mongo

docker exec -it some-mongo bash

mongosh

db.createCollection("movies", { capped: true, size: 100000, max: 1000 });

Prometheus
Flux code
Security



Day 8:

Cache - RedisCacheManager [Serializable , What type of Serialization]
HATEOAS - WebMvcLinkBuilder [linkTo, methodOn, affordance [HAL_FORMS]] ,EntityModel or CollectionModel
Spring Data Rest - project on top of Data Repositories like Spring Data Jpa / Spring Data Mongo
RestConfiguration to customize endpoints, BasePathAwareController

Actuator - Observability [health, info, beans, cache, metrics] io.micrometer [ Counter, Timer , Guages]

Prometheus fundamentally stores all data as time series

```
 <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-registry-prometheus</artifactId>
 </dependency>

 docker compose up
 http://localhost:8080/actuator/prometheus

http://localhost:9090/

http_server_requests_seconds_count

 docker compose down


```










