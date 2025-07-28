module spring.demo {
    // automatic modules
    requires spring.core;
    requires spring.boot;
    requires spring.context;
    requires spring.beans;
    requires spring.boot.autoconfigure;

    opens com.example.springdemo to
            spring.core, spring.beans, spring.context;
}