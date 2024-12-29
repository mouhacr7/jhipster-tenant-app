package com.tenant.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.tenant.app.IntegrationTest;
import com.tenant.app.domain.License;
import com.tenant.app.repository.EntityManager;
import com.tenant.app.repository.LicenseRepository;
import com.tenant.app.service.dto.LicenseDTO;
import com.tenant.app.service.mapper.LicenseMapper;
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
 * Integration tests for the {@link LicenseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class LicenseResourceIT {

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/licenses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private LicenseMapper licenseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private License license;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static License createEntity(EntityManager em) {
        License license = new License().startDate(DEFAULT_START_DATE).endDate(DEFAULT_END_DATE).active(DEFAULT_ACTIVE);
        return license;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static License createUpdatedEntity(EntityManager em) {
        License license = new License().startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).active(UPDATED_ACTIVE);
        return license;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(License.class).block();
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
        license = createEntity(em);
    }

    @Test
    void createLicense() throws Exception {
        int databaseSizeBeforeCreate = licenseRepository.findAll().collectList().block().size();
        // Create the License
        LicenseDTO licenseDTO = licenseMapper.toDto(license);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(licenseDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll().collectList().block();
        assertThat(licenseList).hasSize(databaseSizeBeforeCreate + 1);
        License testLicense = licenseList.get(licenseList.size() - 1);
        assertThat(testLicense.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testLicense.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testLicense.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    void createLicenseWithExistingId() throws Exception {
        // Create the License with an existing ID
        license.setId(1L);
        LicenseDTO licenseDTO = licenseMapper.toDto(license);

        int databaseSizeBeforeCreate = licenseRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(licenseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll().collectList().block();
        assertThat(licenseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = licenseRepository.findAll().collectList().block().size();
        // set the field null
        license.setStartDate(null);

        // Create the License, which fails.
        LicenseDTO licenseDTO = licenseMapper.toDto(license);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(licenseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<License> licenseList = licenseRepository.findAll().collectList().block();
        assertThat(licenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = licenseRepository.findAll().collectList().block().size();
        // set the field null
        license.setEndDate(null);

        // Create the License, which fails.
        LicenseDTO licenseDTO = licenseMapper.toDto(license);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(licenseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<License> licenseList = licenseRepository.findAll().collectList().block();
        assertThat(licenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = licenseRepository.findAll().collectList().block().size();
        // set the field null
        license.setActive(null);

        // Create the License, which fails.
        LicenseDTO licenseDTO = licenseMapper.toDto(license);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(licenseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<License> licenseList = licenseRepository.findAll().collectList().block();
        assertThat(licenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllLicenses() {
        // Initialize the database
        licenseRepository.save(license).block();

        // Get all the licenseList
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
            .value(hasItem(license.getId().intValue()))
            .jsonPath("$.[*].startDate")
            .value(hasItem(DEFAULT_START_DATE.toString()))
            .jsonPath("$.[*].endDate")
            .value(hasItem(DEFAULT_END_DATE.toString()))
            .jsonPath("$.[*].active")
            .value(hasItem(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    void getLicense() {
        // Initialize the database
        licenseRepository.save(license).block();

        // Get the license
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, license.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(license.getId().intValue()))
            .jsonPath("$.startDate")
            .value(is(DEFAULT_START_DATE.toString()))
            .jsonPath("$.endDate")
            .value(is(DEFAULT_END_DATE.toString()))
            .jsonPath("$.active")
            .value(is(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    void getNonExistingLicense() {
        // Get the license
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingLicense() throws Exception {
        // Initialize the database
        licenseRepository.save(license).block();

        int databaseSizeBeforeUpdate = licenseRepository.findAll().collectList().block().size();

        // Update the license
        License updatedLicense = licenseRepository.findById(license.getId()).block();
        updatedLicense.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).active(UPDATED_ACTIVE);
        LicenseDTO licenseDTO = licenseMapper.toDto(updatedLicense);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, licenseDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(licenseDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll().collectList().block();
        assertThat(licenseList).hasSize(databaseSizeBeforeUpdate);
        License testLicense = licenseList.get(licenseList.size() - 1);
        assertThat(testLicense.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testLicense.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testLicense.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    void putNonExistingLicense() throws Exception {
        int databaseSizeBeforeUpdate = licenseRepository.findAll().collectList().block().size();
        license.setId(count.incrementAndGet());

        // Create the License
        LicenseDTO licenseDTO = licenseMapper.toDto(license);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, licenseDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(licenseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll().collectList().block();
        assertThat(licenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchLicense() throws Exception {
        int databaseSizeBeforeUpdate = licenseRepository.findAll().collectList().block().size();
        license.setId(count.incrementAndGet());

        // Create the License
        LicenseDTO licenseDTO = licenseMapper.toDto(license);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(licenseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll().collectList().block();
        assertThat(licenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamLicense() throws Exception {
        int databaseSizeBeforeUpdate = licenseRepository.findAll().collectList().block().size();
        license.setId(count.incrementAndGet());

        // Create the License
        LicenseDTO licenseDTO = licenseMapper.toDto(license);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(licenseDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll().collectList().block();
        assertThat(licenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateLicenseWithPatch() throws Exception {
        // Initialize the database
        licenseRepository.save(license).block();

        int databaseSizeBeforeUpdate = licenseRepository.findAll().collectList().block().size();

        // Update the license using partial update
        License partialUpdatedLicense = new License();
        partialUpdatedLicense.setId(license.getId());

        partialUpdatedLicense.startDate(UPDATED_START_DATE).active(UPDATED_ACTIVE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLicense.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLicense))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll().collectList().block();
        assertThat(licenseList).hasSize(databaseSizeBeforeUpdate);
        License testLicense = licenseList.get(licenseList.size() - 1);
        assertThat(testLicense.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testLicense.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testLicense.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    void fullUpdateLicenseWithPatch() throws Exception {
        // Initialize the database
        licenseRepository.save(license).block();

        int databaseSizeBeforeUpdate = licenseRepository.findAll().collectList().block().size();

        // Update the license using partial update
        License partialUpdatedLicense = new License();
        partialUpdatedLicense.setId(license.getId());

        partialUpdatedLicense.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).active(UPDATED_ACTIVE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLicense.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLicense))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll().collectList().block();
        assertThat(licenseList).hasSize(databaseSizeBeforeUpdate);
        License testLicense = licenseList.get(licenseList.size() - 1);
        assertThat(testLicense.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testLicense.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testLicense.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    void patchNonExistingLicense() throws Exception {
        int databaseSizeBeforeUpdate = licenseRepository.findAll().collectList().block().size();
        license.setId(count.incrementAndGet());

        // Create the License
        LicenseDTO licenseDTO = licenseMapper.toDto(license);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, licenseDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(licenseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll().collectList().block();
        assertThat(licenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchLicense() throws Exception {
        int databaseSizeBeforeUpdate = licenseRepository.findAll().collectList().block().size();
        license.setId(count.incrementAndGet());

        // Create the License
        LicenseDTO licenseDTO = licenseMapper.toDto(license);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(licenseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll().collectList().block();
        assertThat(licenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamLicense() throws Exception {
        int databaseSizeBeforeUpdate = licenseRepository.findAll().collectList().block().size();
        license.setId(count.incrementAndGet());

        // Create the License
        LicenseDTO licenseDTO = licenseMapper.toDto(license);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(licenseDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll().collectList().block();
        assertThat(licenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteLicense() {
        // Initialize the database
        licenseRepository.save(license).block();

        int databaseSizeBeforeDelete = licenseRepository.findAll().collectList().block().size();

        // Delete the license
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, license.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<License> licenseList = licenseRepository.findAll().collectList().block();
        assertThat(licenseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
