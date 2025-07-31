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

SQL is based on table and column names


JP-QL is based on class and field names

select reg_no, fuel_type from vehicles;



select registrationNumber, fuelType from Vehicle










