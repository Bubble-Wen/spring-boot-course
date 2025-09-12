package top.jwxiang.boot.config.enums;

//快递状态(枚举)

public enum ExpressStatus {
    CREATED("已揽收"), TRANSIT("在途中"), SUCCESS("签收");

    private final String label;

    ExpressStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
