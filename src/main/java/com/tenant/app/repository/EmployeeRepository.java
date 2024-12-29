package com.tenant.app.repository;

import com.tenant.app.domain.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Employee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeRepository extends ReactiveCrudRepository<Employee, Long>, EmployeeRepositoryInternal {
    Flux<Employee> findAllBy(Pageable pageable);

    @Override
    Mono<Employee> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Employee> findAllWithEagerRelationships();

    @Override
    Flux<Employee> findAllWithEagerRelationships(Pageable page);

    @Query(
        "SELECT entity.* FROM employee entity JOIN rel_employee__access_rights joinTable ON entity.id = joinTable.access_rights_id WHERE joinTable.access_rights_id = :id"
    )
    Flux<Employee> findByAccessRights(Long id);

    @Query("SELECT * FROM employee entity WHERE entity.client_id = :id")
    Flux<Employee> findByClient(Long id);

    @Query("SELECT * FROM employee entity WHERE entity.client_id IS NULL")
    Flux<Employee> findAllWhereClientIsNull();

    @Override
    <S extends Employee> Mono<S> save(S entity);

    @Override
    Flux<Employee> findAll();

    @Override
    Mono<Employee> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EmployeeRepositoryInternal {
    <S extends Employee> Mono<S> save(S entity);

    Flux<Employee> findAllBy(Pageable pageable);

    Flux<Employee> findAll();

    Mono<Employee> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Employee> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Employee> findOneWithEagerRelationships(Long id);

    Flux<Employee> findAllWithEagerRelationships();

    Flux<Employee> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
