package com.tenant.app.service;

import com.tenant.app.domain.AccessRight;
import com.tenant.app.repository.AccessRightRepository;
import com.tenant.app.service.dto.AccessRightDTO;
import com.tenant.app.service.mapper.AccessRightMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link AccessRight}.
 */
@Service
@Transactional
public class AccessRightService {

    private final Logger log = LoggerFactory.getLogger(AccessRightService.class);

    private final AccessRightRepository accessRightRepository;

    private final AccessRightMapper accessRightMapper;

    public AccessRightService(AccessRightRepository accessRightRepository, AccessRightMapper accessRightMapper) {
        this.accessRightRepository = accessRightRepository;
        this.accessRightMapper = accessRightMapper;
    }

    /**
     * Save a accessRight.
     *
     * @param accessRightDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AccessRightDTO> save(AccessRightDTO accessRightDTO) {
        log.debug("Request to save AccessRight : {}", accessRightDTO);
        return accessRightRepository.save(accessRightMapper.toEntity(accessRightDTO)).map(accessRightMapper::toDto);
    }

    /**
     * Update a accessRight.
     *
     * @param accessRightDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AccessRightDTO> update(AccessRightDTO accessRightDTO) {
        log.debug("Request to update AccessRight : {}", accessRightDTO);
        return accessRightRepository.save(accessRightMapper.toEntity(accessRightDTO)).map(accessRightMapper::toDto);
    }

    /**
     * Partially update a accessRight.
     *
     * @param accessRightDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<AccessRightDTO> partialUpdate(AccessRightDTO accessRightDTO) {
        log.debug("Request to partially update AccessRight : {}", accessRightDTO);

        return accessRightRepository
            .findById(accessRightDTO.getId())
            .map(existingAccessRight -> {
                accessRightMapper.partialUpdate(existingAccessRight, accessRightDTO);

                return existingAccessRight;
            })
            .flatMap(accessRightRepository::save)
            .map(accessRightMapper::toDto);
    }

    /**
     * Get all the accessRights.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<AccessRightDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AccessRights");
        return accessRightRepository.findAllBy(pageable).map(accessRightMapper::toDto);
    }

    /**
     * Returns the number of accessRights available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return accessRightRepository.count();
    }

    /**
     * Get one accessRight by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<AccessRightDTO> findOne(Long id) {
        log.debug("Request to get AccessRight : {}", id);
        return accessRightRepository.findById(id).map(accessRightMapper::toDto);
    }

    /**
     * Delete the accessRight by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete AccessRight : {}", id);
        return accessRightRepository.deleteById(id);
    }
}
