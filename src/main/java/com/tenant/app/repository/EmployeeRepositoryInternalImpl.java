package com.tenant.app.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.tenant.app.domain.Employee;
import com.tenant.app.domain.Module;
import com.tenant.app.domain.enumeration.UserRole;
import com.tenant.app.repository.rowmapper.ClientRowMapper;
import com.tenant.app.repository.rowmapper.EmployeeRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Employee entity.
 */
@SuppressWarnings("unused")
class EmployeeRepositoryInternalImpl extends SimpleR2dbcRepository<Employee, Long> implements EmployeeRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ClientRowMapper clientMapper;
    private final EmployeeRowMapper employeeMapper;

    private static final Table entityTable = Table.aliased("employee", EntityManager.ENTITY_ALIAS);
    private static final Table clientTable = Table.aliased("client", "client");

    private static final EntityManager.LinkTable accessRightsLink = new EntityManager.LinkTable(
        "rel_employee__access_rights",
        "employee_id",
        "access_rights_id"
    );

    public EmployeeRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ClientRowMapper clientMapper,
        EmployeeRowMapper employeeMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Employee.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.clientMapper = clientMapper;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public Flux<Employee> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Employee> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = EmployeeSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ClientSqlHelper.getColumns(clientTable, "client"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(clientTable)
            .on(Column.create("client_id", entityTable))
            .equals(Column.create("id", clientTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Employee.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Employee> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Employee> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Employee> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Employee> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Employee> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Employee process(Row row, RowMetadata metadata) {
        Employee entity = employeeMapper.apply(row, "e");
        entity.setClient(clientMapper.apply(row, "client"));
        return entity;
    }

    @Override
    public <S extends Employee> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends Employee> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(accessRightsLink, entity.getId(), entity.getAccessRights().stream().map(Module::getId))
            .then();
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager.deleteFromLinkTable(accessRightsLink, entityId);
    }
}
