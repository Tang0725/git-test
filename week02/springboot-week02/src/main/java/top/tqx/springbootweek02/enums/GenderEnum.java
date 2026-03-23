package top.tqx.springbootweek02.enums;

public enum GenderEnum {
    MALE(1, "男"),
    FEMALE(2, "女"),
    UNKNOW(3,"未知");

    private Integer code;
    private String test;

    GenderEnum(Integer code, String test) {
        this.code = code;
        this.test = test;
    }

    public Integer getCode() {
        return code;
    }

    public String getTest() {
        return test;
    }

}
