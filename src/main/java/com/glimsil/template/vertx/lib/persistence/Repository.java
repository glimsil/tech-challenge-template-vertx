package com.glimsil.template.vertx.lib.persistence;

import com.glimsil.template.vertx.lib.persistence.annotation.DBField;
import com.glimsil.template.vertx.lib.persistence.annotation.DBTable;
import com.glimsil.template.vertx.lib.persistence.config.DatabaseConfig;
import io.vertx.core.Handler;
import io.vertx.ext.sql.ResultSet;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class Repository<T> {

    public void getAll(Handler<DbResult> done) {
        Class<T> type = getManagedClass();
        System.out.println(writeQuery(type));
        DatabaseConfig.getInstance().getClient().query(writeQuery(type), res2 -> {
            if (res2.succeeded()) {
                ResultSet rs = res2.result();
                List<T> result = new ArrayList<>(rs.getNumRows());
                rs.getRows().forEach(x -> result.add(x.mapTo(type)));
                DbResult dbResult = new DbResult();
                dbResult.setResult(result);
                done.handle(dbResult);

            }
        });
    }

    private String writeQuery(Class<T> clazz) {
        DBTable dbTable = clazz.getAnnotation(DBTable.class);
        if(null == dbTable) {
            throw new MissingDbTableException("This pojo is not a table.");
        }
        StringBuilder query = new StringBuilder();
        query.append("SELECT ");
        boolean first = true;
        DBField dbField;
        for(Field f : clazz.getDeclaredFields()) {
            dbField = f.getAnnotation(DBField.class);
            if(null != dbField) {
                if(first) {
                    first = false;
                } else {
                    query.append(", ");
                }
                query.append(dbField.value().isEmpty() ? f.getName() : dbField.value());
            }
        }
        query.append(" FROM " + (dbTable.value().isEmpty() ? clazz.getName() : dbTable.value()));
        return query.toString();
    }

    private Class<T> getManagedClass() {
        ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] actualTypeArguments = superclass.getActualTypeArguments();
        Type type = actualTypeArguments[0];
        return (Class<T>) type;
    }
}
