package com.fcdnipro.dniprolab.web.rest;

import com.fcdnipro.dniprolab.Application;
import com.fcdnipro.dniprolab.domain.VideoAnalytics;
import com.fcdnipro.dniprolab.repository.VideoAnalyticsRepository;
import com.fcdnipro.dniprolab.service.VideoAnalyticsService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the VideoAnalyticsResource REST controller.
 *
 * @see VideoAnalyticsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class VideoAnalyticsResourceIntTest {

    private static final String DEFAULT_LABEL = "AAAAA";
    private static final String UPDATED_LABEL = "BBBBB";
    private static final String DEFAULT_REFERENCE = "AAAAA";
    private static final String UPDATED_REFERENCE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_AUTHOR = "AAAAA";
    private static final String UPDATED_AUTHOR = "BBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private VideoAnalyticsRepository videoAnalyticsRepository;

    @Inject
    private VideoAnalyticsService videoAnalyticsService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restVideoAnalyticsMockMvc;

    private VideoAnalytics videoAnalytics;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VideoAnalyticsResource videoAnalyticsResource = new VideoAnalyticsResource();
        ReflectionTestUtils.setField(videoAnalyticsResource, "videoAnalyticsService", videoAnalyticsService);
        this.restVideoAnalyticsMockMvc = MockMvcBuilders.standaloneSetup(videoAnalyticsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        videoAnalytics = new VideoAnalytics();
        videoAnalytics.setLabel(DEFAULT_LABEL);
        videoAnalytics.setReference(DEFAULT_REFERENCE);
        videoAnalytics.setDescription(DEFAULT_DESCRIPTION);
        videoAnalytics.setAuthor(DEFAULT_AUTHOR);
        videoAnalytics.setDate(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createVideoAnalytics() throws Exception {
        int databaseSizeBeforeCreate = videoAnalyticsRepository.findAll().size();

        // Create the VideoAnalytics

        restVideoAnalyticsMockMvc.perform(post("/api/videoAnalyticss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(videoAnalytics)))
                .andExpect(status().isCreated());

        // Validate the VideoAnalytics in the database
        List<VideoAnalytics> videoAnalyticss = videoAnalyticsRepository.findAll();
        assertThat(videoAnalyticss).hasSize(databaseSizeBeforeCreate + 1);
        VideoAnalytics testVideoAnalytics = videoAnalyticss.get(videoAnalyticss.size() - 1);
        assertThat(testVideoAnalytics.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testVideoAnalytics.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testVideoAnalytics.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVideoAnalytics.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testVideoAnalytics.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoAnalyticsRepository.findAll().size();
        // set the field null
        videoAnalytics.setLabel(null);

        // Create the VideoAnalytics, which fails.

        restVideoAnalyticsMockMvc.perform(post("/api/videoAnalyticss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(videoAnalytics)))
                .andExpect(status().isBadRequest());

        List<VideoAnalytics> videoAnalyticss = videoAnalyticsRepository.findAll();
        assertThat(videoAnalyticss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoAnalyticsRepository.findAll().size();
        // set the field null
        videoAnalytics.setReference(null);

        // Create the VideoAnalytics, which fails.

        restVideoAnalyticsMockMvc.perform(post("/api/videoAnalyticss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(videoAnalytics)))
                .andExpect(status().isBadRequest());

        List<VideoAnalytics> videoAnalyticss = videoAnalyticsRepository.findAll();
        assertThat(videoAnalyticss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVideoAnalyticss() throws Exception {
        // Initialize the database
        videoAnalyticsRepository.saveAndFlush(videoAnalytics);

        // Get all the videoAnalyticss
        restVideoAnalyticsMockMvc.perform(get("/api/videoAnalyticss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(videoAnalytics.getId().intValue())))
                .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
                .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getVideoAnalytics() throws Exception {
        // Initialize the database
        videoAnalyticsRepository.saveAndFlush(videoAnalytics);

        // Get the videoAnalytics
        restVideoAnalyticsMockMvc.perform(get("/api/videoAnalyticss/{id}", videoAnalytics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(videoAnalytics.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVideoAnalytics() throws Exception {
        // Get the videoAnalytics
        restVideoAnalyticsMockMvc.perform(get("/api/videoAnalyticss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVideoAnalytics() throws Exception {
        // Initialize the database
        videoAnalyticsRepository.saveAndFlush(videoAnalytics);

		int databaseSizeBeforeUpdate = videoAnalyticsRepository.findAll().size();

        // Update the videoAnalytics
        videoAnalytics.setLabel(UPDATED_LABEL);
        videoAnalytics.setReference(UPDATED_REFERENCE);
        videoAnalytics.setDescription(UPDATED_DESCRIPTION);
        videoAnalytics.setAuthor(UPDATED_AUTHOR);
        videoAnalytics.setDate(UPDATED_DATE);

        restVideoAnalyticsMockMvc.perform(put("/api/videoAnalyticss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(videoAnalytics)))
                .andExpect(status().isOk());

        // Validate the VideoAnalytics in the database
        List<VideoAnalytics> videoAnalyticss = videoAnalyticsRepository.findAll();
        assertThat(videoAnalyticss).hasSize(databaseSizeBeforeUpdate);
        VideoAnalytics testVideoAnalytics = videoAnalyticss.get(videoAnalyticss.size() - 1);
        assertThat(testVideoAnalytics.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testVideoAnalytics.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testVideoAnalytics.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVideoAnalytics.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testVideoAnalytics.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void deleteVideoAnalytics() throws Exception {
        // Initialize the database
        videoAnalyticsRepository.saveAndFlush(videoAnalytics);

		int databaseSizeBeforeDelete = videoAnalyticsRepository.findAll().size();

        // Get the videoAnalytics
        restVideoAnalyticsMockMvc.perform(delete("/api/videoAnalyticss/{id}", videoAnalytics.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<VideoAnalytics> videoAnalyticss = videoAnalyticsRepository.findAll();
        assertThat(videoAnalyticss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
