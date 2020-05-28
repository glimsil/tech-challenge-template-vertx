package com.glimsil.template.vertx.domain;

import com.glimsil.template.vertx.lib.persistence.annotation.DBField;
import com.glimsil.template.vertx.lib.persistence.annotation.DBTable;

@DBTable("product")
public class Product {
    @DBField("id")
    private String id;
    @DBField
    private String name;
    @DBField
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
