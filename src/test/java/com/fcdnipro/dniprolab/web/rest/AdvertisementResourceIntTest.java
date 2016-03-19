package com.fcdnipro.dniprolab.web.rest;

import com.fcdnipro.dniprolab.Application;
import com.fcdnipro.dniprolab.domain.Advertisement;
import com.fcdnipro.dniprolab.repository.AdvertisementRepository;
import com.fcdnipro.dniprolab.service.AdvertisementService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the AdvertisementResource REST controller.
 *
 * @see AdvertisementResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AdvertisementResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.format(DEFAULT_DATE);
    private static final String DEFAULT_TEXT = "AAAAA";
    private static final String UPDATED_TEXT = "BBBBB";
    private static final String DEFAULT_AUTHOR = "AAAAA";
    private static final String UPDATED_AUTHOR = "BBBBB";

    @Inject
    private AdvertisementRepository advertisementRepository;

    @Inject
    private AdvertisementService advertisementService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAdvertisementMockMvc;

    private Advertisement advertisement;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AdvertisementResource advertisementResource = new AdvertisementResource();
        ReflectionTestUtils.setField(advertisementResource, "advertisementService", advertisementService);
        this.restAdvertisementMockMvc = MockMvcBuilders.standaloneSetup(advertisementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        advertisement = new Advertisement();
        advertisement.setDate(DEFAULT_DATE);
        advertisement.setText(DEFAULT_TEXT);
        advertisement.setAuthor(DEFAULT_AUTHOR);
    }

    @Test
    @Transactional
    public void createAdvertisement() throws Exception {
        int databaseSizeBeforeCreate = advertisementRepository.findAll().size();

        // Create the Advertisement

        restAdvertisementMockMvc.perform(post("/api/advertisements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(advertisement)))
                .andExpect(status().isCreated());

        // Validate the Advertisement in the database
        List<Advertisement> advertisements = advertisementRepository.findAll();
        assertThat(advertisements).hasSize(databaseSizeBeforeCreate + 1);
        Advertisement testAdvertisement = advertisements.get(advertisements.size() - 1);
        assertThat(testAdvertisement.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAdvertisement.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testAdvertisement.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
    }

    @Test
    @Transactional
    public void checkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = advertisementRepository.findAll().size();
        // set the field null
        advertisement.setText(null);

        // Create the Advertisement, which fails.

        restAdvertisementMockMvc.perform(post("/api/advertisements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(advertisement)))
                .andExpect(status().isBadRequest());

        List<Advertisement> advertisements = advertisementRepository.findAll();
        assertThat(advertisements).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAdvertisements() throws Exception {
        // Initialize the database
        advertisementRepository.saveAndFlush(advertisement);

        // Get all the advertisements
        restAdvertisementMockMvc.perform(get("/api/advertisements?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(advertisement.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
                .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR.toString())));
    }

    @Test
    @Transactional
    public void getAdvertisement() throws Exception {
        // Initialize the database
        advertisementRepository.saveAndFlush(advertisement);

        // Get the advertisement
        restAdvertisementMockMvc.perform(get("/api/advertisements/{id}", advertisement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(advertisement.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAdvertisement() throws Exception {
        // Get the advertisement
        restAdvertisementMockMvc.perform(get("/api/advertisements/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdvertisement() throws Exception {
        // Initialize the database
        advertisementRepository.saveAndFlush(advertisement);

		int databaseSizeBeforeUpdate = advertisementRepository.findAll().size();

        // Update the advertisement
        advertisement.setDate(UPDATED_DATE);
        advertisement.setText(UPDATED_TEXT);
        advertisement.setAuthor(UPDATED_AUTHOR);

        restAdvertisementMockMvc.perform(put("/api/advertisements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(advertisement)))
                .andExpect(status().isOk());

        // Validate the Advertisement in the database
        List<Advertisement> advertisements = advertisementRepository.findAll();
        assertThat(advertisements).hasSize(databaseSizeBeforeUpdate);
        Advertisement testAdvertisement = advertisements.get(advertisements.size() - 1);
        assertThat(testAdvertisement.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAdvertisement.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testAdvertisement.getAuthor()).isEqualTo(UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    public void deleteAdvertisement() throws Exception {
        // Initialize the database
        advertisementRepository.saveAndFlush(advertisement);

		int databaseSizeBeforeDelete = advertisementRepository.findAll().size();

        // Get the advertisement
        restAdvertisementMockMvc.perform(delete("/api/advertisements/{id}", advertisement.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Advertisement> advertisements = advertisementRepository.findAll();
        assertThat(advertisements).hasSize(databaseSizeBeforeDelete - 1);
    }
}
