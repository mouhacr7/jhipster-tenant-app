package com.tenant.app.repository.rowmapper;

import com.tenant.app.domain.License;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link License}, with proper type conversions.
 */
@Service
public class LicenseRowMapper implements BiFunction<Row, String, License> {

    private final ColumnConverter converter;

    public LicenseRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link License} stored in the database.
     */
    @Override
    public License apply(Row row, String prefix) {
        License entity = new License();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setStartDate(converter.fromRow(row, prefix + "_start_date", Instant.class));
        entity.setEndDate(converter.fromRow(row, prefix + "_end_date", Instant.class));
        entity.setActive(converter.fromRow(row, prefix + "_active", Boolean.class));
        entity.setModuleId(converter.fromRow(row, prefix + "_module_id", Long.class));
        return entity;
    }
}
