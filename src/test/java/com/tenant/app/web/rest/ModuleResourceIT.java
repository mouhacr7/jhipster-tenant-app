package com.tenant.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.tenant.app.IntegrationTest;
import com.tenant.app.domain.Module;
import com.tenant.app.domain.enumeration.ModuleCategory;
import com.tenant.app.domain.enumeration.ModuleType;
import com.tenant.app.repository.EntityManager;
import com.tenant.app.repository.ModuleRepository;
import com.tenant.app.service.dto.ModuleDTO;
import com.tenant.app.service.mapper.ModuleMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link ModuleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ModuleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ModuleType DEFAULT_TYPE = ModuleType.GESTION;
    private static final ModuleType UPDATED_TYPE = ModuleType.CLIENTELE;

    private static final ModuleCategory DEFAULT_CATEGORY = ModuleCategory.GESTION;
    private static final ModuleCategory UPDATED_CATEGORY = ModuleCategory.CLIENTELE;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/modules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Module module;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Module createEntity(EntityManager em) {
        Module module = new Module()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .category(DEFAULT_CATEGORY)
            .active(DEFAULT_ACTIVE);
        return module;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Module createUpdatedEntity(EntityManager em) {
        Module module = new Module()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .category(UPDATED_CATEGORY)
            .active(UPDATED_ACTIVE);
        return module;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Module.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        module = createEntity(em);
    }

    @Test
    void createModule() throws Exception {
        int databaseSizeBeforeCreate = moduleRepository.findAll().collectList().block().size();
        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll().collectList().block();
        assertThat(moduleList).hasSize(databaseSizeBeforeCreate + 1);
        Module testModule = moduleList.get(moduleList.size() - 1);
        assertThat(testModule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testModule.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testModule.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testModule.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testModule.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    void createModuleWithExistingId() throws Exception {
        // Create the Module with an existing ID
        module.setId(1L);
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        int databaseSizeBeforeCreate = moduleRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll().collectList().block();
        assertThat(moduleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = moduleRepository.findAll().collectList().block().size();
        // set the field null
        module.setName(null);

        // Create the Module, which fails.
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Module> moduleList = moduleRepository.findAll().collectList().block();
        assertThat(moduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = moduleRepository.findAll().collectList().block().size();
        // set the field null
        module.setType(null);

        // Create the Module, which fails.
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Module> moduleList = moduleRepository.findAll().collectList().block();
        assertThat(moduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = moduleRepository.findAll().collectList().block().size();
        // set the field null
        module.setCategory(null);

        // Create the Module, which fails.
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Module> moduleList = moduleRepository.findAll().collectList().block();
        assertThat(moduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = moduleRepository.findAll().collectList().block().size();
        // set the field null
        module.setActive(null);

        // Create the Module, which fails.
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Module> moduleList = moduleRepository.findAll().collectList().block();
        assertThat(moduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllModules() {
        // Initialize the database
        moduleRepository.save(module).block();

        // Get all the moduleList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(module.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE.toString()))
            .jsonPath("$.[*].category")
            .value(hasItem(DEFAULT_CATEGORY.toString()))
            .jsonPath("$.[*].active")
            .value(hasItem(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    void getModule() {
        // Initialize the database
        moduleRepository.save(module).block();

        // Get the module
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, module.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(module.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE.toString()))
            .jsonPath("$.category")
            .value(is(DEFAULT_CATEGORY.toString()))
            .jsonPath("$.active")
            .value(is(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    void getNonExistingModule() {
        // Get the module
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingModule() throws Exception {
        // Initialize the database
        moduleRepository.save(module).block();

        int databaseSizeBeforeUpdate = moduleRepository.findAll().collectList().block().size();

        // Update the module
        Module updatedModule = moduleRepository.findById(module.getId()).block();
        updatedModule
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .category(UPDATED_CATEGORY)
            .active(UPDATED_ACTIVE);
        ModuleDTO moduleDTO = moduleMapper.toDto(updatedModule);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, moduleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll().collectList().block();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
        Module testModule = moduleList.get(moduleList.size() - 1);
        assertThat(testModule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testModule.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testModule.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testModule.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testModule.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    void putNonExistingModule() throws Exception {
        int databaseSizeBeforeUpdate = moduleRepository.findAll().collectList().block().size();
        module.setId(count.incrementAndGet());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, moduleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll().collectList().block();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchModule() throws Exception {
        int databaseSizeBeforeUpdate = moduleRepository.findAll().collectList().block().size();
        module.setId(count.incrementAndGet());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll().collectList().block();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamModule() throws Exception {
        int databaseSizeBeforeUpdate = moduleRepository.findAll().collectList().block().size();
        module.setId(count.incrementAndGet());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll().collectList().block();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateModuleWithPatch() throws Exception {
        // Initialize the database
        moduleRepository.save(module).block();

        int databaseSizeBeforeUpdate = moduleRepository.findAll().collectList().block().size();

        // Update the module using partial update
        Module partialUpdatedModule = new Module();
        partialUpdatedModule.setId(module.getId());

        partialUpdatedModule.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).active(UPDATED_ACTIVE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedModule.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedModule))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll().collectList().block();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
        Module testModule = moduleList.get(moduleList.size() - 1);
        assertThat(testModule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testModule.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testModule.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testModule.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testModule.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    void fullUpdateModuleWithPatch() throws Exception {
        // Initialize the database
        moduleRepository.save(module).block();

        int databaseSizeBeforeUpdate = moduleRepository.findAll().collectList().block().size();

        // Update the module using partial update
        Module partialUpdatedModule = new Module();
        partialUpdatedModule.setId(module.getId());

        partialUpdatedModule
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .category(UPDATED_CATEGORY)
            .active(UPDATED_ACTIVE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedModule.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedModule))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll().collectList().block();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
        Module testModule = moduleList.get(moduleList.size() - 1);
        assertThat(testModule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testModule.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testModule.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testModule.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testModule.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    void patchNonExistingModule() throws Exception {
        int databaseSizeBeforeUpdate = moduleRepository.findAll().collectList().block().size();
        module.setId(count.incrementAndGet());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, moduleDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll().collectList().block();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchModule() throws Exception {
        int databaseSizeBeforeUpdate = moduleRepository.findAll().collectList().block().size();
        module.setId(count.incrementAndGet());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll().collectList().block();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamModule() throws Exception {
        int databaseSizeBeforeUpdate = moduleRepository.findAll().collectList().block().size();
        module.setId(count.incrementAndGet());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll().collectList().block();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteModule() {
        // Initialize the database
        moduleRepository.save(module).block();

        int databaseSizeBeforeDelete = moduleRepository.findAll().collectList().block().size();

        // Delete the module
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, module.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Module> moduleList = moduleRepository.findAll().collectList().block();
        assertThat(moduleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
