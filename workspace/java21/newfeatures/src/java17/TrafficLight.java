package java17;

// within same file permits is optional
//public sealed interface TrafficLight permits Red, Yellow, Green
public sealed interface TrafficLight {
    String getData();
}

final class Red implements TrafficLight {
    @Override
    public String getData() {
        return "Stop";
    }
}

final class Yellow implements TrafficLight {
    @Override
    public String getData() {
        return "Ready";
    }
}

final class Green implements TrafficLight {
    @Override
    public String getData() {
        return "Go";
    }
}