package com.glimsil.template.vertx.lib.persistence.config;

import com.glimsil.template.vertx.service.ProductService;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;

public class DatabaseConfig {

    JsonObject jsonConfig = new JsonObject();
    private SQLClient client = null;
    private static DatabaseConfig instance = null;

    public static synchronized DatabaseConfig getInstance() {
        if(null == instance) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    private DatabaseConfig() {

        jsonConfig.put("url", "jdbc:postgresql://localhost:5432/postgres");
        jsonConfig.put("driver_class", "org.postgresql.Driver");
        jsonConfig.put("user", "postgres");
        jsonConfig.put("password", "postgres");
    }

    public void startClient(Vertx vertx) {
        if(null == client) {
            client = JDBCClient.createShared(vertx, jsonConfig);
        }
    }

    public SQLClient getClient() {
        return client;
    }



}
