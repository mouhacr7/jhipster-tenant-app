package com.tenant.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.tenant.app.IntegrationTest;
import com.tenant.app.domain.AccessRight;
import com.tenant.app.repository.AccessRightRepository;
import com.tenant.app.repository.EntityManager;
import com.tenant.app.service.dto.AccessRightDTO;
import com.tenant.app.service.mapper.AccessRightMapper;
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
 * Integration tests for the {@link AccessRightResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AccessRightResourceIT {

    private static final Boolean DEFAULT_CAN_READ = false;
    private static final Boolean UPDATED_CAN_READ = true;

    private static final Boolean DEFAULT_CAN_WRITE = false;
    private static final Boolean UPDATED_CAN_WRITE = true;

    private static final Boolean DEFAULT_CAN_DELETE = false;
    private static final Boolean UPDATED_CAN_DELETE = true;

    private static final String ENTITY_API_URL = "/api/access-rights";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccessRightRepository accessRightRepository;

    @Autowired
    private AccessRightMapper accessRightMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private AccessRight accessRight;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccessRight createEntity(EntityManager em) {
        AccessRight accessRight = new AccessRight().canRead(DEFAULT_CAN_READ).canWrite(DEFAULT_CAN_WRITE).canDelete(DEFAULT_CAN_DELETE);
        return accessRight;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccessRight createUpdatedEntity(EntityManager em) {
        AccessRight accessRight = new AccessRight().canRead(UPDATED_CAN_READ).canWrite(UPDATED_CAN_WRITE).canDelete(UPDATED_CAN_DELETE);
        return accessRight;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(AccessRight.class).block();
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
        accessRight = createEntity(em);
    }

    @Test
    void createAccessRight() throws Exception {
        int databaseSizeBeforeCreate = accessRightRepository.findAll().collectList().block().size();
        // Create the AccessRight
        AccessRightDTO accessRightDTO = accessRightMapper.toDto(accessRight);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(accessRightDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the AccessRight in the database
        List<AccessRight> accessRightList = accessRightRepository.findAll().collectList().block();
        assertThat(accessRightList).hasSize(databaseSizeBeforeCreate + 1);
        AccessRight testAccessRight = accessRightList.get(accessRightList.size() - 1);
        assertThat(testAccessRight.getCanRead()).isEqualTo(DEFAULT_CAN_READ);
        assertThat(testAccessRight.getCanWrite()).isEqualTo(DEFAULT_CAN_WRITE);
        assertThat(testAccessRight.getCanDelete()).isEqualTo(DEFAULT_CAN_DELETE);
    }

    @Test
    void createAccessRightWithExistingId() throws Exception {
        // Create the AccessRight with an existing ID
        accessRight.setId(1L);
        AccessRightDTO accessRightDTO = accessRightMapper.toDto(accessRight);

        int databaseSizeBeforeCreate = accessRightRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(accessRightDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AccessRight in the database
        List<AccessRight> accessRightList = accessRightRepository.findAll().collectList().block();
        assertThat(accessRightList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkCanReadIsRequired() throws Exception {
        int databaseSizeBeforeTest = accessRightRepository.findAll().collectList().block().size();
        // set the field null
        accessRight.setCanRead(null);

        // Create the AccessRight, which fails.
        AccessRightDTO accessRightDTO = accessRightMapper.toDto(accessRight);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(accessRightDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AccessRight> accessRightList = accessRightRepository.findAll().collectList().block();
        assertThat(accessRightList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCanWriteIsRequired() throws Exception {
        int databaseSizeBeforeTest = accessRightRepository.findAll().collectList().block().size();
        // set the field null
        accessRight.setCanWrite(null);

        // Create the AccessRight, which fails.
        AccessRightDTO accessRightDTO = accessRightMapper.toDto(accessRight);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(accessRightDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AccessRight> accessRightList = accessRightRepository.findAll().collectList().block();
        assertThat(accessRightList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCanDeleteIsRequired() throws Exception {
        int databaseSizeBeforeTest = accessRightRepository.findAll().collectList().block().size();
        // set the field null
        accessRight.setCanDelete(null);

        // Create the AccessRight, which fails.
        AccessRightDTO accessRightDTO = accessRightMapper.toDto(accessRight);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(accessRightDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AccessRight> accessRightList = accessRightRepository.findAll().collectList().block();
        assertThat(accessRightList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAccessRights() {
        // Initialize the database
        accessRightRepository.save(accessRight).block();

        // Get all the accessRightList
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
            .value(hasItem(accessRight.getId().intValue()))
            .jsonPath("$.[*].canRead")
            .value(hasItem(DEFAULT_CAN_READ.booleanValue()))
            .jsonPath("$.[*].canWrite")
            .value(hasItem(DEFAULT_CAN_WRITE.booleanValue()))
            .jsonPath("$.[*].canDelete")
            .value(hasItem(DEFAULT_CAN_DELETE.booleanValue()));
    }

    @Test
    void getAccessRight() {
        // Initialize the database
        accessRightRepository.save(accessRight).block();

        // Get the accessRight
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, accessRight.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(accessRight.getId().intValue()))
            .jsonPath("$.canRead")
            .value(is(DEFAULT_CAN_READ.booleanValue()))
            .jsonPath("$.canWrite")
            .value(is(DEFAULT_CAN_WRITE.booleanValue()))
            .jsonPath("$.canDelete")
            .value(is(DEFAULT_CAN_DELETE.booleanValue()));
    }

    @Test
    void getNonExistingAccessRight() {
        // Get the accessRight
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAccessRight() throws Exception {
        // Initialize the database
        accessRightRepository.save(accessRight).block();

        int databaseSizeBeforeUpdate = accessRightRepository.findAll().collectList().block().size();

        // Update the accessRight
        AccessRight updatedAccessRight = accessRightRepository.findById(accessRight.getId()).block();
        updatedAccessRight.canRead(UPDATED_CAN_READ).canWrite(UPDATED_CAN_WRITE).canDelete(UPDATED_CAN_DELETE);
        AccessRightDTO accessRightDTO = accessRightMapper.toDto(updatedAccessRight);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, accessRightDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(accessRightDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AccessRight in the database
        List<AccessRight> accessRightList = accessRightRepository.findAll().collectList().block();
        assertThat(accessRightList).hasSize(databaseSizeBeforeUpdate);
        AccessRight testAccessRight = accessRightList.get(accessRightList.size() - 1);
        assertThat(testAccessRight.getCanRead()).isEqualTo(UPDATED_CAN_READ);
        assertThat(testAccessRight.getCanWrite()).isEqualTo(UPDATED_CAN_WRITE);
        assertThat(testAccessRight.getCanDelete()).isEqualTo(UPDATED_CAN_DELETE);
    }

    @Test
    void putNonExistingAccessRight() throws Exception {
        int databaseSizeBeforeUpdate = accessRightRepository.findAll().collectList().block().size();
        accessRight.setId(count.incrementAndGet());

        // Create the AccessRight
        AccessRightDTO accessRightDTO = accessRightMapper.toDto(accessRight);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, accessRightDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(accessRightDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AccessRight in the database
        List<AccessRight> accessRightList = accessRightRepository.findAll().collectList().block();
        assertThat(accessRightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAccessRight() throws Exception {
        int databaseSizeBeforeUpdate = accessRightRepository.findAll().collectList().block().size();
        accessRight.setId(count.incrementAndGet());

        // Create the AccessRight
        AccessRightDTO accessRightDTO = accessRightMapper.toDto(accessRight);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(accessRightDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AccessRight in the database
        List<AccessRight> accessRightList = accessRightRepository.findAll().collectList().block();
        assertThat(accessRightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAccessRight() throws Exception {
        int databaseSizeBeforeUpdate = accessRightRepository.findAll().collectList().block().size();
        accessRight.setId(count.incrementAndGet());

        // Create the AccessRight
        AccessRightDTO accessRightDTO = accessRightMapper.toDto(accessRight);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(accessRightDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AccessRight in the database
        List<AccessRight> accessRightList = accessRightRepository.findAll().collectList().block();
        assertThat(accessRightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAccessRightWithPatch() throws Exception {
        // Initialize the database
        accessRightRepository.save(accessRight).block();

        int databaseSizeBeforeUpdate = accessRightRepository.findAll().collectList().block().size();

        // Update the accessRight using partial update
        AccessRight partialUpdatedAccessRight = new AccessRight();
        partialUpdatedAccessRight.setId(accessRight.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAccessRight.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAccessRight))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AccessRight in the database
        List<AccessRight> accessRightList = accessRightRepository.findAll().collectList().block();
        assertThat(accessRightList).hasSize(databaseSizeBeforeUpdate);
        AccessRight testAccessRight = accessRightList.get(accessRightList.size() - 1);
        assertThat(testAccessRight.getCanRead()).isEqualTo(DEFAULT_CAN_READ);
        assertThat(testAccessRight.getCanWrite()).isEqualTo(DEFAULT_CAN_WRITE);
        assertThat(testAccessRight.getCanDelete()).isEqualTo(DEFAULT_CAN_DELETE);
    }

    @Test
    void fullUpdateAccessRightWithPatch() throws Exception {
        // Initialize the database
        accessRightRepository.save(accessRight).block();

        int databaseSizeBeforeUpdate = accessRightRepository.findAll().collectList().block().size();

        // Update the accessRight using partial update
        AccessRight partialUpdatedAccessRight = new AccessRight();
        partialUpdatedAccessRight.setId(accessRight.getId());

        partialUpdatedAccessRight.canRead(UPDATED_CAN_READ).canWrite(UPDATED_CAN_WRITE).canDelete(UPDATED_CAN_DELETE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAccessRight.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAccessRight))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AccessRight in the database
        List<AccessRight> accessRightList = accessRightRepository.findAll().collectList().block();
        assertThat(accessRightList).hasSize(databaseSizeBeforeUpdate);
        AccessRight testAccessRight = accessRightList.get(accessRightList.size() - 1);
        assertThat(testAccessRight.getCanRead()).isEqualTo(UPDATED_CAN_READ);
        assertThat(testAccessRight.getCanWrite()).isEqualTo(UPDATED_CAN_WRITE);
        assertThat(testAccessRight.getCanDelete()).isEqualTo(UPDATED_CAN_DELETE);
    }

    @Test
    void patchNonExistingAccessRight() throws Exception {
        int databaseSizeBeforeUpdate = accessRightRepository.findAll().collectList().block().size();
        accessRight.setId(count.incrementAndGet());

        // Create the AccessRight
        AccessRightDTO accessRightDTO = accessRightMapper.toDto(accessRight);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, accessRightDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(accessRightDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AccessRight in the database
        List<AccessRight> accessRightList = accessRightRepository.findAll().collectList().block();
        assertThat(accessRightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAccessRight() throws Exception {
        int databaseSizeBeforeUpdate = accessRightRepository.findAll().collectList().block().size();
        accessRight.setId(count.incrementAndGet());

        // Create the AccessRight
        AccessRightDTO accessRightDTO = accessRightMapper.toDto(accessRight);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(accessRightDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AccessRight in the database
        List<AccessRight> accessRightList = accessRightRepository.findAll().collectList().block();
        assertThat(accessRightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAccessRight() throws Exception {
        int databaseSizeBeforeUpdate = accessRightRepository.findAll().collectList().block().size();
        accessRight.setId(count.incrementAndGet());

        // Create the AccessRight
        AccessRightDTO accessRightDTO = accessRightMapper.toDto(accessRight);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(accessRightDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AccessRight in the database
        List<AccessRight> accessRightList = accessRightRepository.findAll().collectList().block();
        assertThat(accessRightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAccessRight() {
        // Initialize the database
        accessRightRepository.save(accessRight).block();

        int databaseSizeBeforeDelete = accessRightRepository.findAll().collectList().block().size();

        // Delete the accessRight
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, accessRight.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<AccessRight> accessRightList = accessRightRepository.findAll().collectList().block();
        assertThat(accessRightList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
