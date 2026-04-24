package top.tqx.week08.entity;

import lombok.Data;

@Data
public class User {
    private String name;
    private String age;
    private String email;
    private Address address;

}
