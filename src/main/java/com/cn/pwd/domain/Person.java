package com.cn.pwd.domain;

import java.io.Serializable;

/**
 * @author Jonsy
 *
 */
public class Person implements Serializable {

    private static final long serialVersionUID = -533471021920552399L;

    private String name;
    private String email;

    public Person() {
    }

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
