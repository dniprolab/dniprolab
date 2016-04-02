package com.fcdnipro.dniprolab.web.rest;

import com.fcdnipro.dniprolab.Application;
import com.fcdnipro.dniprolab.domain.Advert;
import com.fcdnipro.dniprolab.repository.AdvertRepository;
import com.fcdnipro.dniprolab.service.AdvertService;

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
 * Test class for the AdvertResource REST controller.
 *
 * @see AdvertResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AdvertResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.format(DEFAULT_DATE);
    private static final String DEFAULT_TEXT = "AAAAA";
    private static final String UPDATED_TEXT = "BBBBB";
    private static final String DEFAULT_AUTHOR = "AAAAA";
    private static final String UPDATED_AUTHOR = "BBBBB";

    @Inject
    private AdvertRepository advertRepository;

    @Inject
    private AdvertService advertService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAdvertMockMvc;

    private Advert advert;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AdvertResource advertResource = new AdvertResource();
        ReflectionTestUtils.setField(advertResource, "advertService", advertService);
        this.restAdvertMockMvc = MockMvcBuilders.standaloneSetup(advertResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        advert = new Advert();
        advert.setDate(DEFAULT_DATE);
        advert.setText(DEFAULT_TEXT);
        advert.setAuthor(DEFAULT_AUTHOR);
    }

    @Test
    @Transactional
    public void createAdvert() throws Exception {
        int databaseSizeBeforeCreate = advertRepository.findAll().size();

        // Create the Advert

        restAdvertMockMvc.perform(post("/api/adverts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(advert)))
                .andExpect(status().isCreated());

        // Validate the Advert in the database
        List<Advert> adverts = advertRepository.findAll();
        assertThat(adverts).hasSize(databaseSizeBeforeCreate + 1);
        Advert testAdvert = adverts.get(adverts.size() - 1);
        assertThat(testAdvert.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAdvert.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testAdvert.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
    }

    @Test
    @Transactional
    public void checkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = advertRepository.findAll().size();
        // set the field null
        advert.setText(null);

        // Create the Advert, which fails.

        restAdvertMockMvc.perform(post("/api/adverts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(advert)))
                .andExpect(status().isBadRequest());

        List<Advert> adverts = advertRepository.findAll();
        assertThat(adverts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAdverts() throws Exception {
        // Initialize the database
        advertRepository.saveAndFlush(advert);

        // Get all the adverts
        restAdvertMockMvc.perform(get("/api/adverts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(advert.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
                .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR.toString())));
    }

    @Test
    @Transactional
    public void getAdvert() throws Exception {
        // Initialize the database
        advertRepository.saveAndFlush(advert);

        // Get the advert
        restAdvertMockMvc.perform(get("/api/adverts/{id}", advert.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(advert.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAdvert() throws Exception {
        // Get the advert
        restAdvertMockMvc.perform(get("/api/adverts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdvert() throws Exception {
        // Initialize the database
        advertRepository.saveAndFlush(advert);

		int databaseSizeBeforeUpdate = advertRepository.findAll().size();

        // Update the advert
        advert.setDate(UPDATED_DATE);
        advert.setText(UPDATED_TEXT);
        advert.setAuthor(UPDATED_AUTHOR);

        restAdvertMockMvc.perform(put("/api/adverts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(advert)))
                .andExpect(status().isOk());

        // Validate the Advert in the database
        List<Advert> adverts = advertRepository.findAll();
        assertThat(adverts).hasSize(databaseSizeBeforeUpdate);
        Advert testAdvert = adverts.get(adverts.size() - 1);
        assertThat(testAdvert.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAdvert.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testAdvert.getAuthor()).isEqualTo(UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    public void deleteAdvert() throws Exception {
        // Initialize the database
        advertRepository.saveAndFlush(advert);

		int databaseSizeBeforeDelete = advertRepository.findAll().size();

        // Get the advert
        restAdvertMockMvc.perform(delete("/api/adverts/{id}", advert.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Advert> adverts = advertRepository.findAll();
        assertThat(adverts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
