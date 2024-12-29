package com.tenant.app.repository;

import com.tenant.app.domain.License;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the License entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LicenseRepository extends ReactiveCrudRepository<License, Long>, LicenseRepositoryInternal {
    Flux<License> findAllBy(Pageable pageable);

    @Query("SELECT * FROM license entity WHERE entity.module_id = :id")
    Flux<License> findByModule(Long id);

    @Query("SELECT * FROM license entity WHERE entity.module_id IS NULL")
    Flux<License> findAllWhereModuleIsNull();

    @Override
    <S extends License> Mono<S> save(S entity);

    @Override
    Flux<License> findAll();

    @Override
    Mono<License> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface LicenseRepositoryInternal {
    <S extends License> Mono<S> save(S entity);

    Flux<License> findAllBy(Pageable pageable);

    Flux<License> findAll();

    Mono<License> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<License> findAllBy(Pageable pageable, Criteria criteria);

}
