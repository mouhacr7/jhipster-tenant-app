package com.tenant.app.web.rest;

import com.tenant.app.repository.LicenseRepository;
import com.tenant.app.service.LicenseService;
import com.tenant.app.service.dto.LicenseDTO;
import com.tenant.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.tenant.app.domain.License}.
 */
@RestController
@RequestMapping("/api")
public class LicenseResource {

    private final Logger log = LoggerFactory.getLogger(LicenseResource.class);

    private static final String ENTITY_NAME = "license";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LicenseService licenseService;

    private final LicenseRepository licenseRepository;

    public LicenseResource(LicenseService licenseService, LicenseRepository licenseRepository) {
        this.licenseService = licenseService;
        this.licenseRepository = licenseRepository;
    }

    /**
     * {@code POST  /licenses} : Create a new license.
     *
     * @param licenseDTO the licenseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new licenseDTO, or with status {@code 400 (Bad Request)} if the license has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/licenses")
    public Mono<ResponseEntity<LicenseDTO>> createLicense(@Valid @RequestBody LicenseDTO licenseDTO) throws URISyntaxException {
        log.debug("REST request to save License : {}", licenseDTO);
        if (licenseDTO.getId() != null) {
            throw new BadRequestAlertException("A new license cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return licenseService
            .save(licenseDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/licenses/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /licenses/:id} : Updates an existing license.
     *
     * @param id the id of the licenseDTO to save.
     * @param licenseDTO the licenseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated licenseDTO,
     * or with status {@code 400 (Bad Request)} if the licenseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the licenseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/licenses/{id}")
    public Mono<ResponseEntity<LicenseDTO>> updateLicense(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LicenseDTO licenseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update License : {}, {}", id, licenseDTO);
        if (licenseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, licenseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return licenseRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return licenseService
                    .update(licenseDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /licenses/:id} : Partial updates given fields of an existing license, field will ignore if it is null
     *
     * @param id the id of the licenseDTO to save.
     * @param licenseDTO the licenseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated licenseDTO,
     * or with status {@code 400 (Bad Request)} if the licenseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the licenseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the licenseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/licenses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<LicenseDTO>> partialUpdateLicense(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LicenseDTO licenseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update License partially : {}, {}", id, licenseDTO);
        if (licenseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, licenseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return licenseRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<LicenseDTO> result = licenseService.partialUpdate(licenseDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /licenses} : get all the licenses.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of licenses in body.
     */
    @GetMapping("/licenses")
    public Mono<ResponseEntity<List<LicenseDTO>>> getAllLicenses(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Licenses");
        return licenseService
            .countAll()
            .zipWith(licenseService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /licenses/:id} : get the "id" license.
     *
     * @param id the id of the licenseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the licenseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/licenses/{id}")
    public Mono<ResponseEntity<LicenseDTO>> getLicense(@PathVariable Long id) {
        log.debug("REST request to get License : {}", id);
        Mono<LicenseDTO> licenseDTO = licenseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(licenseDTO);
    }

    /**
     * {@code DELETE  /licenses/:id} : delete the "id" license.
     *
     * @param id the id of the licenseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/licenses/{id}")
    public Mono<ResponseEntity<Void>> deleteLicense(@PathVariable Long id) {
        log.debug("REST request to delete License : {}", id);
        return licenseService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
