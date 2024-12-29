package com.tenant.app.web.rest;

import com.tenant.app.repository.ModuleSubscriptionRepository;
import com.tenant.app.service.ModuleSubscriptionService;
import com.tenant.app.service.dto.ModuleSubscriptionDTO;
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
 * REST controller for managing {@link com.tenant.app.domain.ModuleSubscription}.
 */
@RestController
@RequestMapping("/api")
public class ModuleSubscriptionResource {

    private final Logger log = LoggerFactory.getLogger(ModuleSubscriptionResource.class);

    private static final String ENTITY_NAME = "moduleSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ModuleSubscriptionService moduleSubscriptionService;

    private final ModuleSubscriptionRepository moduleSubscriptionRepository;

    public ModuleSubscriptionResource(
        ModuleSubscriptionService moduleSubscriptionService,
        ModuleSubscriptionRepository moduleSubscriptionRepository
    ) {
        this.moduleSubscriptionService = moduleSubscriptionService;
        this.moduleSubscriptionRepository = moduleSubscriptionRepository;
    }

    /**
     * {@code POST  /module-subscriptions} : Create a new moduleSubscription.
     *
     * @param moduleSubscriptionDTO the moduleSubscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moduleSubscriptionDTO, or with status {@code 400 (Bad Request)} if the moduleSubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/module-subscriptions")
    public Mono<ResponseEntity<ModuleSubscriptionDTO>> createModuleSubscription(
        @Valid @RequestBody ModuleSubscriptionDTO moduleSubscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ModuleSubscription : {}", moduleSubscriptionDTO);
        if (moduleSubscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new moduleSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return moduleSubscriptionService
            .save(moduleSubscriptionDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/module-subscriptions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /module-subscriptions/:id} : Updates an existing moduleSubscription.
     *
     * @param id the id of the moduleSubscriptionDTO to save.
     * @param moduleSubscriptionDTO the moduleSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moduleSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the moduleSubscriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the moduleSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/module-subscriptions/{id}")
    public Mono<ResponseEntity<ModuleSubscriptionDTO>> updateModuleSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ModuleSubscriptionDTO moduleSubscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ModuleSubscription : {}, {}", id, moduleSubscriptionDTO);
        if (moduleSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moduleSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return moduleSubscriptionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return moduleSubscriptionService
                    .update(moduleSubscriptionDTO)
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
     * {@code PATCH  /module-subscriptions/:id} : Partial updates given fields of an existing moduleSubscription, field will ignore if it is null
     *
     * @param id the id of the moduleSubscriptionDTO to save.
     * @param moduleSubscriptionDTO the moduleSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moduleSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the moduleSubscriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the moduleSubscriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the moduleSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/module-subscriptions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ModuleSubscriptionDTO>> partialUpdateModuleSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ModuleSubscriptionDTO moduleSubscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ModuleSubscription partially : {}, {}", id, moduleSubscriptionDTO);
        if (moduleSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moduleSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return moduleSubscriptionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ModuleSubscriptionDTO> result = moduleSubscriptionService.partialUpdate(moduleSubscriptionDTO);

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
     * {@code GET  /module-subscriptions} : get all the moduleSubscriptions.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of moduleSubscriptions in body.
     */
    @GetMapping("/module-subscriptions")
    public Mono<ResponseEntity<List<ModuleSubscriptionDTO>>> getAllModuleSubscriptions(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of ModuleSubscriptions");
        return moduleSubscriptionService
            .countAll()
            .zipWith(moduleSubscriptionService.findAll(pageable).collectList())
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
     * {@code GET  /module-subscriptions/:id} : get the "id" moduleSubscription.
     *
     * @param id the id of the moduleSubscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moduleSubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/module-subscriptions/{id}")
    public Mono<ResponseEntity<ModuleSubscriptionDTO>> getModuleSubscription(@PathVariable Long id) {
        log.debug("REST request to get ModuleSubscription : {}", id);
        Mono<ModuleSubscriptionDTO> moduleSubscriptionDTO = moduleSubscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moduleSubscriptionDTO);
    }

    /**
     * {@code DELETE  /module-subscriptions/:id} : delete the "id" moduleSubscription.
     *
     * @param id the id of the moduleSubscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/module-subscriptions/{id}")
    public Mono<ResponseEntity<Void>> deleteModuleSubscription(@PathVariable Long id) {
        log.debug("REST request to delete ModuleSubscription : {}", id);
        return moduleSubscriptionService
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
