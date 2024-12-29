package com.tenant.app.repository;

import com.tenant.app.domain.AccessRight;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the AccessRight entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccessRightRepository extends ReactiveCrudRepository<AccessRight, Long>, AccessRightRepositoryInternal {
    Flux<AccessRight> findAllBy(Pageable pageable);

    @Query("SELECT * FROM access_right entity WHERE entity.module_id = :id")
    Flux<AccessRight> findByModule(Long id);

    @Query("SELECT * FROM access_right entity WHERE entity.module_id IS NULL")
    Flux<AccessRight> findAllWhereModuleIsNull();

    @Query("SELECT * FROM access_right entity WHERE entity.employee_id = :id")
    Flux<AccessRight> findByEmployee(Long id);

    @Query("SELECT * FROM access_right entity WHERE entity.employee_id IS NULL")
    Flux<AccessRight> findAllWhereEmployeeIsNull();

    @Override
    <S extends AccessRight> Mono<S> save(S entity);

    @Override
    Flux<AccessRight> findAll();

    @Override
    Mono<AccessRight> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AccessRightRepositoryInternal {
    <S extends AccessRight> Mono<S> save(S entity);

    Flux<AccessRight> findAllBy(Pageable pageable);

    Flux<AccessRight> findAll();

    Mono<AccessRight> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<AccessRight> findAllBy(Pageable pageable, Criteria criteria);

}
