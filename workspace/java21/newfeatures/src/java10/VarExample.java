package java10;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;


@interface NotNull {
}
public class VarExample {
    public static void main(String[] args) {
//        Map<String, List<String>> data = getData(); // old way
        var data = getData();
        // Map<String,Map<String, List<Employee>> data;
        // data.forEach( (k,v) -> {})
        // someOtherFn(data);
//        var name = null;  //not valid
        int var = 10; // works file

        var str = "Hello World";
        doTask(s -> s.length() > 5);

        doTask((@NotNull String s) -> s.length() > 5);

        // can use in lambda along with annotations to infer to type
        doTask((@NotNull var s) -> s.length() > 5);
    }

    private static void doTask(Predicate<String> str) {

    }
    private static Map<String, List<String>> getData() {
        return  null;
    }
}
