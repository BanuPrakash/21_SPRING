package java13;

public class PatternMatching {
    public static void main(String[] args) {

        // multi line string
        String message = """
                Hello User,
                one two three
                """;
        Object obj = new String("Hello World!!");
        // old way
//        if( obj instanceof String) {
//            String s = (String) obj;
//            System.out.println(s.length());
//        }

        if( obj instanceof String s) {
            System.out.println(s.length());
        }

        printType("Hello");
        printType(12);
        printType(null);
        System.out.println(getInfo("RED"));
    }

    // java 17
    static  void printType(Object obj) {
        switch (obj) {
            case Integer i-> System.out.println("Integer " + i);
            case String str -> System.out.println("String " + str);
            case null -> System.out.println("Null Type");
//            case Product p ->
            default -> System.out.println("Unknown type");
        }
    }

    static String getInfo(String data) {
        // switch as functional
        return  switch (data) {
            case "RED", "YELLOW" -> "Stop";
            case "GREEN" -> "Go";
            default -> "Invalid Signal";
        };
    }

    // if multiple statements needs to be executed use yield and not arrow
    static String getInfoMore(String data) {
        // switch as functional
        return  switch (data) {
            case "RED", "YELLOW" : {
                System.out.println("Switch off engine");
                yield "Stop";
            }
            case "GREEN": yield "Go";
            default: yield  "Invalid Signal";
        };
    }
}
