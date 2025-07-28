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
4)	Immutable Collection

Java 10 (March 2018):
1)	Using var keyword

Java 11 (September 2018):
1)	String API updates
a.	isBlank
b.	lines
c.	stripLeading() and stripTrailing()
d.	repeat()
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


