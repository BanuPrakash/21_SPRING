package java17;

public class TrafficLightExample {
    public static void main(String[] args) {
        TrafficLight light = new Red();
        String result = switch (light) {
            case Red r -> r.getData();
            case Green g -> g.getData();
            case Yellow y -> y.getData();
        };

        System.out.println(result);
    }
}
