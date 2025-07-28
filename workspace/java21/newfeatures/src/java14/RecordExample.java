package java14;

record Person(String name, int age) {
    // parameter names has to be name and age
//    public Person(String name, int age) {
//        this.name = name;
//        this.age = age;
//    }
//    // override the constructor
    public Person {
        if(age < 0) {
            throw new IllegalArgumentException("Age should be positive!!!");
        }
        System.out.println(age()); // not valid
    }
}


public class RecordExample {
    public static void main(String[] args) {
     Person p = new Person("Roger", 14);
        System.out.println(p.name() + " ," + p.age());
        // pattern matching records
        if(p instanceof Person(String n, int a)) {
            // extract person into local variables n and a
            System.out.println(n  + " , " + a);
        }
    }
}
