package top.jwxiang.boot.config.enums;

public enum DrinkStatus {
    COFFEE("咖啡",35.50), TEA("奶茶",19.90), JUICE("果汁",9.90);

    private final String type;
    private final double price;

    DrinkStatus(String type,double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }
}
