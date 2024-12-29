package com.tenant.app.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.tenant.app.domain.ModuleSubscription;
import com.tenant.app.repository.rowmapper.ClientRowMapper;
import com.tenant.app.repository.rowmapper.ModuleRowMapper;
import com.tenant.app.repository.rowmapper.ModuleSubscriptionRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the ModuleSubscription entity.
 */
@SuppressWarnings("unused")
class ModuleSubscriptionRepositoryInternalImpl
    extends SimpleR2dbcRepository<ModuleSubscription, Long>
    implements ModuleSubscriptionRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ModuleRowMapper moduleMapper;
    private final ClientRowMapper clientMapper;
    private final ModuleSubscriptionRowMapper modulesubscriptionMapper;

    private static final Table entityTable = Table.aliased("module_subscription", EntityManager.ENTITY_ALIAS);
    private static final Table moduleTable = Table.aliased("module", "module");
    private static final Table clientTable = Table.aliased("client", "client");

    public ModuleSubscriptionRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ModuleRowMapper moduleMapper,
        ClientRowMapper clientMapper,
        ModuleSubscriptionRowMapper modulesubscriptionMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ModuleSubscription.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.moduleMapper = moduleMapper;
        this.clientMapper = clientMapper;
        this.modulesubscriptionMapper = modulesubscriptionMapper;
    }

    @Override
    public Flux<ModuleSubscription> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ModuleSubscription> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ModuleSubscriptionSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ModuleSqlHelper.getColumns(moduleTable, "module"));
        columns.addAll(ClientSqlHelper.getColumns(clientTable, "client"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(moduleTable)
            .on(Column.create("module_id", entityTable))
            .equals(Column.create("id", moduleTable))
            .leftOuterJoin(clientTable)
            .on(Column.create("client_id", entityTable))
            .equals(Column.create("id", clientTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ModuleSubscription.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ModuleSubscription> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ModuleSubscription> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private ModuleSubscription process(Row row, RowMetadata metadata) {
        ModuleSubscription entity = modulesubscriptionMapper.apply(row, "e");
        entity.setModule(moduleMapper.apply(row, "module"));
        entity.setClient(clientMapper.apply(row, "client"));
        return entity;
    }

    @Override
    public <S extends ModuleSubscription> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
