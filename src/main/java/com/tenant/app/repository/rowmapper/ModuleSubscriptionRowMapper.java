package com.tenant.app.repository.rowmapper;

import com.tenant.app.domain.ModuleSubscription;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ModuleSubscription}, with proper type conversions.
 */
@Service
public class ModuleSubscriptionRowMapper implements BiFunction<Row, String, ModuleSubscription> {

    private final ColumnConverter converter;

    public ModuleSubscriptionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ModuleSubscription} stored in the database.
     */
    @Override
    public ModuleSubscription apply(Row row, String prefix) {
        ModuleSubscription entity = new ModuleSubscription();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setSubscriptionDate(converter.fromRow(row, prefix + "_subscription_date", Instant.class));
        entity.setActive(converter.fromRow(row, prefix + "_active", Boolean.class));
        entity.setModuleId(converter.fromRow(row, prefix + "_module_id", Long.class));
        entity.setClientId(converter.fromRow(row, prefix + "_client_id", Long.class));
        return entity;
    }
}
