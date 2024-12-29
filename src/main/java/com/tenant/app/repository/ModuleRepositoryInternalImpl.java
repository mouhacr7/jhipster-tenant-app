package com.tenant.app.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.tenant.app.domain.Module;
import com.tenant.app.domain.enumeration.ModuleCategory;
import com.tenant.app.domain.enumeration.ModuleType;
import com.tenant.app.repository.rowmapper.ModuleRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
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
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Module entity.
 */
@SuppressWarnings("unused")
class ModuleRepositoryInternalImpl extends SimpleR2dbcRepository<Module, Long> implements ModuleRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ModuleRowMapper moduleMapper;

    private static final Table entityTable = Table.aliased("module", EntityManager.ENTITY_ALIAS);

    public ModuleRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ModuleRowMapper moduleMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Module.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.moduleMapper = moduleMapper;
    }

    @Override
    public Flux<Module> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Module> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ModuleSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Module.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Module> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Module> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Module process(Row row, RowMetadata metadata) {
        Module entity = moduleMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Module> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
