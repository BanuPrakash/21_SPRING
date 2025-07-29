package java21;

class MyTemplateProcessor implements StringTemplate.Processor<String, Throwable> {
    @Override
    public String process(StringTemplate stringTemplate) throws Throwable {
        return null;
    }
}

public class TemplateExample {
    public static void main(String[] args) {
        String p1 = "Laptop";
        double price1 = 98000.00;

        String p2 = "Mouse";
        double price2 = 450.00;

        String orderDetails = STR."""
                    Order Details:
                    --------------------
                    Item:\{p1}, Price: Rs\{price1}
                    Item:\{p2}, Price: Rs\{price2}
                    --------------------

                    Total : \{price1 + price2}
                """;
        System.out.println(orderDetails);
    }
}
