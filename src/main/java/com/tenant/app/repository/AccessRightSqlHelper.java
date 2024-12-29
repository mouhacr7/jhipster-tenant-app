package com.tenant.app.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class AccessRightSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("can_read", table, columnPrefix + "_can_read"));
        columns.add(Column.aliased("can_write", table, columnPrefix + "_can_write"));
        columns.add(Column.aliased("can_delete", table, columnPrefix + "_can_delete"));

        columns.add(Column.aliased("module_id", table, columnPrefix + "_module_id"));
        columns.add(Column.aliased("employee_id", table, columnPrefix + "_employee_id"));
        return columns;
    }
}
