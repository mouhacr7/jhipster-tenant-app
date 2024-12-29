package com.tenant.app.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.tenant.app.domain.AccessRight;
import com.tenant.app.repository.rowmapper.AccessRightRowMapper;
import com.tenant.app.repository.rowmapper.EmployeeRowMapper;
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
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the AccessRight entity.
 */
@SuppressWarnings("unused")
class AccessRightRepositoryInternalImpl extends SimpleR2dbcRepository<AccessRight, Long> implements AccessRightRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ModuleRowMapper moduleMapper;
    private final EmployeeRowMapper employeeMapper;
    private final AccessRightRowMapper accessrightMapper;

    private static final Table entityTable = Table.aliased("access_right", EntityManager.ENTITY_ALIAS);
    private static final Table moduleTable = Table.aliased("module", "module");
    private static final Table employeeTable = Table.aliased("employee", "employee");

    public AccessRightRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ModuleRowMapper moduleMapper,
        EmployeeRowMapper employeeMapper,
        AccessRightRowMapper accessrightMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(AccessRight.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.moduleMapper = moduleMapper;
        this.employeeMapper = employeeMapper;
        this.accessrightMapper = accessrightMapper;
    }

    @Override
    public Flux<AccessRight> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<AccessRight> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = AccessRightSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ModuleSqlHelper.getColumns(moduleTable, "module"));
        columns.addAll(EmployeeSqlHelper.getColumns(employeeTable, "employee"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(moduleTable)
            .on(Column.create("module_id", entityTable))
            .equals(Column.create("id", moduleTable))
            .leftOuterJoin(employeeTable)
            .on(Column.create("employee_id", entityTable))
            .equals(Column.create("id", employeeTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, AccessRight.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<AccessRight> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<AccessRight> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private AccessRight process(Row row, RowMetadata metadata) {
        AccessRight entity = accessrightMapper.apply(row, "e");
        entity.setModule(moduleMapper.apply(row, "module"));
        entity.setEmployee(employeeMapper.apply(row, "employee"));
        return entity;
    }

    @Override
    public <S extends AccessRight> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
