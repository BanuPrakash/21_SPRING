package java17;

public class TrafficLightExample {
    public static void main(String[] args) {
        TrafficLight light = new Red();
        // switch as functional
        String result = switch (light) {
            // if light instanceof Red --> cast it to Red and assign to r
            // r is a Red type-casted one
            case Red r -> r.getData();
            case Green g -> g.getData();
            case Yellow y -> y.getData();
        };

        System.out.println(result);
    }
}
