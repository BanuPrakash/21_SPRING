package java13;

class Employee {
    public String name;
    public int age;
    public Department department;

    public Department getDepartment() {
        return null;
    }
}

class Department {
    public String name;
}

public class EnhancedNullPointerException {
    public static void main(String[] args) {
        String name = new Employee().getDepartment().name;
        System.out.println(name);
    }
}
