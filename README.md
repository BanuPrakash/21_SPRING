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




