package com.tenant.app.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ModuleSubscriptionSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("subscription_date", table, columnPrefix + "_subscription_date"));
        columns.add(Column.aliased("active", table, columnPrefix + "_active"));

        columns.add(Column.aliased("module_id", table, columnPrefix + "_module_id"));
        columns.add(Column.aliased("client_id", table, columnPrefix + "_client_id"));
        return columns;
    }
}
