package com.tenant.app.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.tenant.app.domain.License;
import com.tenant.app.repository.rowmapper.LicenseRowMapper;
import com.tenant.app.repository.rowmapper.ModuleRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the License entity.
 */
@SuppressWarnings("unused")
class LicenseRepositoryInternalImpl extends SimpleR2dbcRepository<License, Long> implements LicenseRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ModuleRowMapper moduleMapper;
    private final LicenseRowMapper licenseMapper;

    private static final Table entityTable = Table.aliased("license", EntityManager.ENTITY_ALIAS);
    private static final Table moduleTable = Table.aliased("module", "module");

    public LicenseRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ModuleRowMapper moduleMapper,
        LicenseRowMapper licenseMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(License.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.moduleMapper = moduleMapper;
        this.licenseMapper = licenseMapper;
    }

    @Override
    public Flux<License> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<License> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = LicenseSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ModuleSqlHelper.getColumns(moduleTable, "module"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(moduleTable)
            .on(Column.create("module_id", entityTable))
            .equals(Column.create("id", moduleTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, License.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<License> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<License> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private License process(Row row, RowMetadata metadata) {
        License entity = licenseMapper.apply(row, "e");
        entity.setModule(moduleMapper.apply(row, "module"));
        return entity;
    }

    @Override
    public <S extends License> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
