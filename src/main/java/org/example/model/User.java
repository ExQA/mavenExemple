package org.example.model;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class User {
    private int id;
    private String name;
    private String nickname;
    private Date birthday;
    private String password;

}
