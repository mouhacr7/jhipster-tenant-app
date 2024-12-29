package com.tenant.app.service;

import com.tenant.app.domain.License;
import com.tenant.app.repository.LicenseRepository;
import com.tenant.app.service.dto.LicenseDTO;
import com.tenant.app.service.mapper.LicenseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link License}.
 */
@Service
@Transactional
public class LicenseService {

    private final Logger log = LoggerFactory.getLogger(LicenseService.class);

    private final LicenseRepository licenseRepository;

    private final LicenseMapper licenseMapper;

    public LicenseService(LicenseRepository licenseRepository, LicenseMapper licenseMapper) {
        this.licenseRepository = licenseRepository;
        this.licenseMapper = licenseMapper;
    }

    /**
     * Save a license.
     *
     * @param licenseDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<LicenseDTO> save(LicenseDTO licenseDTO) {
        log.debug("Request to save License : {}", licenseDTO);
        return licenseRepository.save(licenseMapper.toEntity(licenseDTO)).map(licenseMapper::toDto);
    }

    /**
     * Update a license.
     *
     * @param licenseDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<LicenseDTO> update(LicenseDTO licenseDTO) {
        log.debug("Request to update License : {}", licenseDTO);
        return licenseRepository.save(licenseMapper.toEntity(licenseDTO)).map(licenseMapper::toDto);
    }

    /**
     * Partially update a license.
     *
     * @param licenseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<LicenseDTO> partialUpdate(LicenseDTO licenseDTO) {
        log.debug("Request to partially update License : {}", licenseDTO);

        return licenseRepository
            .findById(licenseDTO.getId())
            .map(existingLicense -> {
                licenseMapper.partialUpdate(existingLicense, licenseDTO);

                return existingLicense;
            })
            .flatMap(licenseRepository::save)
            .map(licenseMapper::toDto);
    }

    /**
     * Get all the licenses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<LicenseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Licenses");
        return licenseRepository.findAllBy(pageable).map(licenseMapper::toDto);
    }

    /**
     * Returns the number of licenses available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return licenseRepository.count();
    }

    /**
     * Get one license by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<LicenseDTO> findOne(Long id) {
        log.debug("Request to get License : {}", id);
        return licenseRepository.findById(id).map(licenseMapper::toDto);
    }

    /**
     * Delete the license by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete License : {}", id);
        return licenseRepository.deleteById(id);
    }
}
