package com.tenant.app.repository.rowmapper;

import com.tenant.app.domain.AccessRight;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link AccessRight}, with proper type conversions.
 */
@Service
public class AccessRightRowMapper implements BiFunction<Row, String, AccessRight> {

    private final ColumnConverter converter;

    public AccessRightRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link AccessRight} stored in the database.
     */
    @Override
    public AccessRight apply(Row row, String prefix) {
        AccessRight entity = new AccessRight();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCanRead(converter.fromRow(row, prefix + "_can_read", Boolean.class));
        entity.setCanWrite(converter.fromRow(row, prefix + "_can_write", Boolean.class));
        entity.setCanDelete(converter.fromRow(row, prefix + "_can_delete", Boolean.class));
        entity.setModuleId(converter.fromRow(row, prefix + "_module_id", Long.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", Long.class));
        return entity;
    }
}
