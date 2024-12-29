package com.tenant.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.tenant.app.IntegrationTest;
import com.tenant.app.domain.ModuleSubscription;
import com.tenant.app.repository.EntityManager;
import com.tenant.app.repository.ModuleSubscriptionRepository;
import com.tenant.app.service.dto.ModuleSubscriptionDTO;
import com.tenant.app.service.mapper.ModuleSubscriptionMapper;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ModuleSubscriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ModuleSubscriptionResourceIT {

    private static final Instant DEFAULT_SUBSCRIPTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUBSCRIPTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/module-subscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ModuleSubscriptionRepository moduleSubscriptionRepository;

    @Autowired
    private ModuleSubscriptionMapper moduleSubscriptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ModuleSubscription moduleSubscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModuleSubscription createEntity(EntityManager em) {
        ModuleSubscription moduleSubscription = new ModuleSubscription().subscriptionDate(DEFAULT_SUBSCRIPTION_DATE).active(DEFAULT_ACTIVE);
        return moduleSubscription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModuleSubscription createUpdatedEntity(EntityManager em) {
        ModuleSubscription moduleSubscription = new ModuleSubscription().subscriptionDate(UPDATED_SUBSCRIPTION_DATE).active(UPDATED_ACTIVE);
        return moduleSubscription;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ModuleSubscription.class).block();
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
        moduleSubscription = createEntity(em);
    }

    @Test
    void createModuleSubscription() throws Exception {
        int databaseSizeBeforeCreate = moduleSubscriptionRepository.findAll().collectList().block().size();
        // Create the ModuleSubscription
        ModuleSubscriptionDTO moduleSubscriptionDTO = moduleSubscriptionMapper.toDto(moduleSubscription);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ModuleSubscription in the database
        List<ModuleSubscription> moduleSubscriptionList = moduleSubscriptionRepository.findAll().collectList().block();
        assertThat(moduleSubscriptionList).hasSize(databaseSizeBeforeCreate + 1);
        ModuleSubscription testModuleSubscription = moduleSubscriptionList.get(moduleSubscriptionList.size() - 1);
        assertThat(testModuleSubscription.getSubscriptionDate()).isEqualTo(DEFAULT_SUBSCRIPTION_DATE);
        assertThat(testModuleSubscription.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    void createModuleSubscriptionWithExistingId() throws Exception {
        // Create the ModuleSubscription with an existing ID
        moduleSubscription.setId(1L);
        ModuleSubscriptionDTO moduleSubscriptionDTO = moduleSubscriptionMapper.toDto(moduleSubscription);

        int databaseSizeBeforeCreate = moduleSubscriptionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModuleSubscription in the database
        List<ModuleSubscription> moduleSubscriptionList = moduleSubscriptionRepository.findAll().collectList().block();
        assertThat(moduleSubscriptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkSubscriptionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = moduleSubscriptionRepository.findAll().collectList().block().size();
        // set the field null
        moduleSubscription.setSubscriptionDate(null);

        // Create the ModuleSubscription, which fails.
        ModuleSubscriptionDTO moduleSubscriptionDTO = moduleSubscriptionMapper.toDto(moduleSubscription);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ModuleSubscription> moduleSubscriptionList = moduleSubscriptionRepository.findAll().collectList().block();
        assertThat(moduleSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = moduleSubscriptionRepository.findAll().collectList().block().size();
        // set the field null
        moduleSubscription.setActive(null);

        // Create the ModuleSubscription, which fails.
        ModuleSubscriptionDTO moduleSubscriptionDTO = moduleSubscriptionMapper.toDto(moduleSubscription);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ModuleSubscription> moduleSubscriptionList = moduleSubscriptionRepository.findAll().collectList().block();
        assertThat(moduleSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllModuleSubscriptions() {
        // Initialize the database
        moduleSubscriptionRepository.save(moduleSubscription).block();

        // Get all the moduleSubscriptionList
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
            .value(hasItem(moduleSubscription.getId().intValue()))
            .jsonPath("$.[*].subscriptionDate")
            .value(hasItem(DEFAULT_SUBSCRIPTION_DATE.toString()))
            .jsonPath("$.[*].active")
            .value(hasItem(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    void getModuleSubscription() {
        // Initialize the database
        moduleSubscriptionRepository.save(moduleSubscription).block();

        // Get the moduleSubscription
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, moduleSubscription.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(moduleSubscription.getId().intValue()))
            .jsonPath("$.subscriptionDate")
            .value(is(DEFAULT_SUBSCRIPTION_DATE.toString()))
            .jsonPath("$.active")
            .value(is(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    void getNonExistingModuleSubscription() {
        // Get the moduleSubscription
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingModuleSubscription() throws Exception {
        // Initialize the database
        moduleSubscriptionRepository.save(moduleSubscription).block();

        int databaseSizeBeforeUpdate = moduleSubscriptionRepository.findAll().collectList().block().size();

        // Update the moduleSubscription
        ModuleSubscription updatedModuleSubscription = moduleSubscriptionRepository.findById(moduleSubscription.getId()).block();
        updatedModuleSubscription.subscriptionDate(UPDATED_SUBSCRIPTION_DATE).active(UPDATED_ACTIVE);
        ModuleSubscriptionDTO moduleSubscriptionDTO = moduleSubscriptionMapper.toDto(updatedModuleSubscription);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, moduleSubscriptionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ModuleSubscription in the database
        List<ModuleSubscription> moduleSubscriptionList = moduleSubscriptionRepository.findAll().collectList().block();
        assertThat(moduleSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        ModuleSubscription testModuleSubscription = moduleSubscriptionList.get(moduleSubscriptionList.size() - 1);
        assertThat(testModuleSubscription.getSubscriptionDate()).isEqualTo(UPDATED_SUBSCRIPTION_DATE);
        assertThat(testModuleSubscription.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    void putNonExistingModuleSubscription() throws Exception {
        int databaseSizeBeforeUpdate = moduleSubscriptionRepository.findAll().collectList().block().size();
        moduleSubscription.setId(count.incrementAndGet());

        // Create the ModuleSubscription
        ModuleSubscriptionDTO moduleSubscriptionDTO = moduleSubscriptionMapper.toDto(moduleSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, moduleSubscriptionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModuleSubscription in the database
        List<ModuleSubscription> moduleSubscriptionList = moduleSubscriptionRepository.findAll().collectList().block();
        assertThat(moduleSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchModuleSubscription() throws Exception {
        int databaseSizeBeforeUpdate = moduleSubscriptionRepository.findAll().collectList().block().size();
        moduleSubscription.setId(count.incrementAndGet());

        // Create the ModuleSubscription
        ModuleSubscriptionDTO moduleSubscriptionDTO = moduleSubscriptionMapper.toDto(moduleSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModuleSubscription in the database
        List<ModuleSubscription> moduleSubscriptionList = moduleSubscriptionRepository.findAll().collectList().block();
        assertThat(moduleSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamModuleSubscription() throws Exception {
        int databaseSizeBeforeUpdate = moduleSubscriptionRepository.findAll().collectList().block().size();
        moduleSubscription.setId(count.incrementAndGet());

        // Create the ModuleSubscription
        ModuleSubscriptionDTO moduleSubscriptionDTO = moduleSubscriptionMapper.toDto(moduleSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ModuleSubscription in the database
        List<ModuleSubscription> moduleSubscriptionList = moduleSubscriptionRepository.findAll().collectList().block();
        assertThat(moduleSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateModuleSubscriptionWithPatch() throws Exception {
        // Initialize the database
        moduleSubscriptionRepository.save(moduleSubscription).block();

        int databaseSizeBeforeUpdate = moduleSubscriptionRepository.findAll().collectList().block().size();

        // Update the moduleSubscription using partial update
        ModuleSubscription partialUpdatedModuleSubscription = new ModuleSubscription();
        partialUpdatedModuleSubscription.setId(moduleSubscription.getId());

        partialUpdatedModuleSubscription.subscriptionDate(UPDATED_SUBSCRIPTION_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedModuleSubscription.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedModuleSubscription))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ModuleSubscription in the database
        List<ModuleSubscription> moduleSubscriptionList = moduleSubscriptionRepository.findAll().collectList().block();
        assertThat(moduleSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        ModuleSubscription testModuleSubscription = moduleSubscriptionList.get(moduleSubscriptionList.size() - 1);
        assertThat(testModuleSubscription.getSubscriptionDate()).isEqualTo(UPDATED_SUBSCRIPTION_DATE);
        assertThat(testModuleSubscription.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    void fullUpdateModuleSubscriptionWithPatch() throws Exception {
        // Initialize the database
        moduleSubscriptionRepository.save(moduleSubscription).block();

        int databaseSizeBeforeUpdate = moduleSubscriptionRepository.findAll().collectList().block().size();

        // Update the moduleSubscription using partial update
        ModuleSubscription partialUpdatedModuleSubscription = new ModuleSubscription();
        partialUpdatedModuleSubscription.setId(moduleSubscription.getId());

        partialUpdatedModuleSubscription.subscriptionDate(UPDATED_SUBSCRIPTION_DATE).active(UPDATED_ACTIVE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedModuleSubscription.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedModuleSubscription))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ModuleSubscription in the database
        List<ModuleSubscription> moduleSubscriptionList = moduleSubscriptionRepository.findAll().collectList().block();
        assertThat(moduleSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        ModuleSubscription testModuleSubscription = moduleSubscriptionList.get(moduleSubscriptionList.size() - 1);
        assertThat(testModuleSubscription.getSubscriptionDate()).isEqualTo(UPDATED_SUBSCRIPTION_DATE);
        assertThat(testModuleSubscription.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    void patchNonExistingModuleSubscription() throws Exception {
        int databaseSizeBeforeUpdate = moduleSubscriptionRepository.findAll().collectList().block().size();
        moduleSubscription.setId(count.incrementAndGet());

        // Create the ModuleSubscription
        ModuleSubscriptionDTO moduleSubscriptionDTO = moduleSubscriptionMapper.toDto(moduleSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, moduleSubscriptionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModuleSubscription in the database
        List<ModuleSubscription> moduleSubscriptionList = moduleSubscriptionRepository.findAll().collectList().block();
        assertThat(moduleSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchModuleSubscription() throws Exception {
        int databaseSizeBeforeUpdate = moduleSubscriptionRepository.findAll().collectList().block().size();
        moduleSubscription.setId(count.incrementAndGet());

        // Create the ModuleSubscription
        ModuleSubscriptionDTO moduleSubscriptionDTO = moduleSubscriptionMapper.toDto(moduleSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModuleSubscription in the database
        List<ModuleSubscription> moduleSubscriptionList = moduleSubscriptionRepository.findAll().collectList().block();
        assertThat(moduleSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamModuleSubscription() throws Exception {
        int databaseSizeBeforeUpdate = moduleSubscriptionRepository.findAll().collectList().block().size();
        moduleSubscription.setId(count.incrementAndGet());

        // Create the ModuleSubscription
        ModuleSubscriptionDTO moduleSubscriptionDTO = moduleSubscriptionMapper.toDto(moduleSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(moduleSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ModuleSubscription in the database
        List<ModuleSubscription> moduleSubscriptionList = moduleSubscriptionRepository.findAll().collectList().block();
        assertThat(moduleSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteModuleSubscription() {
        // Initialize the database
        moduleSubscriptionRepository.save(moduleSubscription).block();

        int databaseSizeBeforeDelete = moduleSubscriptionRepository.findAll().collectList().block().size();

        // Delete the moduleSubscription
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, moduleSubscription.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ModuleSubscription> moduleSubscriptionList = moduleSubscriptionRepository.findAll().collectList().block();
        assertThat(moduleSubscriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
