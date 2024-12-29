package com.tenant.app.service;

import com.tenant.app.domain.Module;
import com.tenant.app.repository.ModuleRepository;
import com.tenant.app.service.dto.ModuleDTO;
import com.tenant.app.service.mapper.ModuleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Module}.
 */
@Service
@Transactional
public class ModuleService {

    private final Logger log = LoggerFactory.getLogger(ModuleService.class);

    private final ModuleRepository moduleRepository;

    private final ModuleMapper moduleMapper;

    public ModuleService(ModuleRepository moduleRepository, ModuleMapper moduleMapper) {
        this.moduleRepository = moduleRepository;
        this.moduleMapper = moduleMapper;
    }

    /**
     * Save a module.
     *
     * @param moduleDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModuleDTO> save(ModuleDTO moduleDTO) {
        log.debug("Request to save Module : {}", moduleDTO);
        return moduleRepository.save(moduleMapper.toEntity(moduleDTO)).map(moduleMapper::toDto);
    }

    /**
     * Update a module.
     *
     * @param moduleDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModuleDTO> update(ModuleDTO moduleDTO) {
        log.debug("Request to update Module : {}", moduleDTO);
        return moduleRepository.save(moduleMapper.toEntity(moduleDTO)).map(moduleMapper::toDto);
    }

    /**
     * Partially update a module.
     *
     * @param moduleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ModuleDTO> partialUpdate(ModuleDTO moduleDTO) {
        log.debug("Request to partially update Module : {}", moduleDTO);

        return moduleRepository
            .findById(moduleDTO.getId())
            .map(existingModule -> {
                moduleMapper.partialUpdate(existingModule, moduleDTO);

                return existingModule;
            })
            .flatMap(moduleRepository::save)
            .map(moduleMapper::toDto);
    }

    /**
     * Get all the modules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ModuleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Modules");
        return moduleRepository.findAllBy(pageable).map(moduleMapper::toDto);
    }

    /**
     * Returns the number of modules available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return moduleRepository.count();
    }

    /**
     * Get one module by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ModuleDTO> findOne(Long id) {
        log.debug("Request to get Module : {}", id);
        return moduleRepository.findById(id).map(moduleMapper::toDto);
    }

    /**
     * Delete the module by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Module : {}", id);
        return moduleRepository.deleteById(id);
    }
}
