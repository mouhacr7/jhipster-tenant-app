package com.tenant.app.repository;

import com.tenant.app.domain.Module;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Module entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModuleRepository extends ReactiveCrudRepository<Module, Long>, ModuleRepositoryInternal {
    Flux<Module> findAllBy(Pageable pageable);

    @Override
    <S extends Module> Mono<S> save(S entity);

    @Override
    Flux<Module> findAll();

    @Override
    Mono<Module> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ModuleRepositoryInternal {
    <S extends Module> Mono<S> save(S entity);

    Flux<Module> findAllBy(Pageable pageable);

    Flux<Module> findAll();

    Mono<Module> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Module> findAllBy(Pageable pageable, Criteria criteria);

}
