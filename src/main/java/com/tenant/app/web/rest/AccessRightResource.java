package com.tenant.app.web.rest;

import com.tenant.app.repository.AccessRightRepository;
import com.tenant.app.service.AccessRightService;
import com.tenant.app.service.dto.AccessRightDTO;
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
 * REST controller for managing {@link com.tenant.app.domain.AccessRight}.
 */
@RestController
@RequestMapping("/api")
public class AccessRightResource {

    private final Logger log = LoggerFactory.getLogger(AccessRightResource.class);

    private static final String ENTITY_NAME = "accessRight";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccessRightService accessRightService;

    private final AccessRightRepository accessRightRepository;

    public AccessRightResource(AccessRightService accessRightService, AccessRightRepository accessRightRepository) {
        this.accessRightService = accessRightService;
        this.accessRightRepository = accessRightRepository;
    }

    /**
     * {@code POST  /access-rights} : Create a new accessRight.
     *
     * @param accessRightDTO the accessRightDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accessRightDTO, or with status {@code 400 (Bad Request)} if the accessRight has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/access-rights")
    public Mono<ResponseEntity<AccessRightDTO>> createAccessRight(@Valid @RequestBody AccessRightDTO accessRightDTO)
        throws URISyntaxException {
        log.debug("REST request to save AccessRight : {}", accessRightDTO);
        if (accessRightDTO.getId() != null) {
            throw new BadRequestAlertException("A new accessRight cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return accessRightService
            .save(accessRightDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/access-rights/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /access-rights/:id} : Updates an existing accessRight.
     *
     * @param id the id of the accessRightDTO to save.
     * @param accessRightDTO the accessRightDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accessRightDTO,
     * or with status {@code 400 (Bad Request)} if the accessRightDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accessRightDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/access-rights/{id}")
    public Mono<ResponseEntity<AccessRightDTO>> updateAccessRight(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AccessRightDTO accessRightDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AccessRight : {}, {}", id, accessRightDTO);
        if (accessRightDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accessRightDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return accessRightRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return accessRightService
                    .update(accessRightDTO)
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
     * {@code PATCH  /access-rights/:id} : Partial updates given fields of an existing accessRight, field will ignore if it is null
     *
     * @param id the id of the accessRightDTO to save.
     * @param accessRightDTO the accessRightDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accessRightDTO,
     * or with status {@code 400 (Bad Request)} if the accessRightDTO is not valid,
     * or with status {@code 404 (Not Found)} if the accessRightDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the accessRightDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/access-rights/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AccessRightDTO>> partialUpdateAccessRight(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AccessRightDTO accessRightDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccessRight partially : {}, {}", id, accessRightDTO);
        if (accessRightDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accessRightDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return accessRightRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AccessRightDTO> result = accessRightService.partialUpdate(accessRightDTO);

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
     * {@code GET  /access-rights} : get all the accessRights.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accessRights in body.
     */
    @GetMapping("/access-rights")
    public Mono<ResponseEntity<List<AccessRightDTO>>> getAllAccessRights(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of AccessRights");
        return accessRightService
            .countAll()
            .zipWith(accessRightService.findAll(pageable).collectList())
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
     * {@code GET  /access-rights/:id} : get the "id" accessRight.
     *
     * @param id the id of the accessRightDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accessRightDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/access-rights/{id}")
    public Mono<ResponseEntity<AccessRightDTO>> getAccessRight(@PathVariable Long id) {
        log.debug("REST request to get AccessRight : {}", id);
        Mono<AccessRightDTO> accessRightDTO = accessRightService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accessRightDTO);
    }

    /**
     * {@code DELETE  /access-rights/:id} : delete the "id" accessRight.
     *
     * @param id the id of the accessRightDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/access-rights/{id}")
    public Mono<ResponseEntity<Void>> deleteAccessRight(@PathVariable Long id) {
        log.debug("REST request to delete AccessRight : {}", id);
        return accessRightService
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
