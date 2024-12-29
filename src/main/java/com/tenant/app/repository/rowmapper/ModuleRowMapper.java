package com.tenant.app.repository.rowmapper;

import com.tenant.app.domain.Module;
import com.tenant.app.domain.enumeration.ModuleCategory;
import com.tenant.app.domain.enumeration.ModuleType;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Module}, with proper type conversions.
 */
@Service
public class ModuleRowMapper implements BiFunction<Row, String, Module> {

    private final ColumnConverter converter;

    public ModuleRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Module} stored in the database.
     */
    @Override
    public Module apply(Row row, String prefix) {
        Module entity = new Module();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setType(converter.fromRow(row, prefix + "_type", ModuleType.class));
        entity.setCategory(converter.fromRow(row, prefix + "_category", ModuleCategory.class));
        entity.setActive(converter.fromRow(row, prefix + "_active", Boolean.class));
        return entity;
    }
}
