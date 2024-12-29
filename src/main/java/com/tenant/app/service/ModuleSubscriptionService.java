package com.tenant.app.service;

import com.tenant.app.domain.ModuleSubscription;
import com.tenant.app.repository.ModuleSubscriptionRepository;
import com.tenant.app.service.dto.ModuleSubscriptionDTO;
import com.tenant.app.service.mapper.ModuleSubscriptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ModuleSubscription}.
 */
@Service
@Transactional
public class ModuleSubscriptionService {

    private final Logger log = LoggerFactory.getLogger(ModuleSubscriptionService.class);

    private final ModuleSubscriptionRepository moduleSubscriptionRepository;

    private final ModuleSubscriptionMapper moduleSubscriptionMapper;

    public ModuleSubscriptionService(
        ModuleSubscriptionRepository moduleSubscriptionRepository,
        ModuleSubscriptionMapper moduleSubscriptionMapper
    ) {
        this.moduleSubscriptionRepository = moduleSubscriptionRepository;
        this.moduleSubscriptionMapper = moduleSubscriptionMapper;
    }

    /**
     * Save a moduleSubscription.
     *
     * @param moduleSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModuleSubscriptionDTO> save(ModuleSubscriptionDTO moduleSubscriptionDTO) {
        log.debug("Request to save ModuleSubscription : {}", moduleSubscriptionDTO);
        return moduleSubscriptionRepository
            .save(moduleSubscriptionMapper.toEntity(moduleSubscriptionDTO))
            .map(moduleSubscriptionMapper::toDto);
    }

    /**
     * Update a moduleSubscription.
     *
     * @param moduleSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModuleSubscriptionDTO> update(ModuleSubscriptionDTO moduleSubscriptionDTO) {
        log.debug("Request to update ModuleSubscription : {}", moduleSubscriptionDTO);
        return moduleSubscriptionRepository
            .save(moduleSubscriptionMapper.toEntity(moduleSubscriptionDTO))
            .map(moduleSubscriptionMapper::toDto);
    }

    /**
     * Partially update a moduleSubscription.
     *
     * @param moduleSubscriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ModuleSubscriptionDTO> partialUpdate(ModuleSubscriptionDTO moduleSubscriptionDTO) {
        log.debug("Request to partially update ModuleSubscription : {}", moduleSubscriptionDTO);

        return moduleSubscriptionRepository
            .findById(moduleSubscriptionDTO.getId())
            .map(existingModuleSubscription -> {
                moduleSubscriptionMapper.partialUpdate(existingModuleSubscription, moduleSubscriptionDTO);

                return existingModuleSubscription;
            })
            .flatMap(moduleSubscriptionRepository::save)
            .map(moduleSubscriptionMapper::toDto);
    }

    /**
     * Get all the moduleSubscriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ModuleSubscriptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ModuleSubscriptions");
        return moduleSubscriptionRepository.findAllBy(pageable).map(moduleSubscriptionMapper::toDto);
    }

    /**
     * Returns the number of moduleSubscriptions available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return moduleSubscriptionRepository.count();
    }

    /**
     * Get one moduleSubscription by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ModuleSubscriptionDTO> findOne(Long id) {
        log.debug("Request to get ModuleSubscription : {}", id);
        return moduleSubscriptionRepository.findById(id).map(moduleSubscriptionMapper::toDto);
    }

    /**
     * Delete the moduleSubscription by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete ModuleSubscription : {}", id);
        return moduleSubscriptionRepository.deleteById(id);
    }
}
