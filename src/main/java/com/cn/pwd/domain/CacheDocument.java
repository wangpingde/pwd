package com.cn.pwd.domain;

import java.time.Instant;

public class CacheDocument {

    private Instant creationDate;

    private String id;

    private String element;

    public CacheDocument(){

    }

    public CacheDocument(String id, String element) {
        this(id,element,Instant.now());
    }

    public CacheDocument(String id, String element, Instant creationDate) {
        this.id = id;
        this.element = element;
        this.creationDate = creationDate;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }
}
