package com.tenant.app.repository;

import com.tenant.app.domain.ModuleSubscription;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ModuleSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModuleSubscriptionRepository
    extends ReactiveCrudRepository<ModuleSubscription, Long>, ModuleSubscriptionRepositoryInternal {
    Flux<ModuleSubscription> findAllBy(Pageable pageable);

    @Query("SELECT * FROM module_subscription entity WHERE entity.module_id = :id")
    Flux<ModuleSubscription> findByModule(Long id);

    @Query("SELECT * FROM module_subscription entity WHERE entity.module_id IS NULL")
    Flux<ModuleSubscription> findAllWhereModuleIsNull();

    @Query("SELECT * FROM module_subscription entity WHERE entity.client_id = :id")
    Flux<ModuleSubscription> findByClient(Long id);

    @Query("SELECT * FROM module_subscription entity WHERE entity.client_id IS NULL")
    Flux<ModuleSubscription> findAllWhereClientIsNull();

    @Override
    <S extends ModuleSubscription> Mono<S> save(S entity);

    @Override
    Flux<ModuleSubscription> findAll();

    @Override
    Mono<ModuleSubscription> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ModuleSubscriptionRepositoryInternal {
    <S extends ModuleSubscription> Mono<S> save(S entity);

    Flux<ModuleSubscription> findAllBy(Pageable pageable);

    Flux<ModuleSubscription> findAll();

    Mono<ModuleSubscription> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ModuleSubscription> findAllBy(Pageable pageable, Criteria criteria);

}
