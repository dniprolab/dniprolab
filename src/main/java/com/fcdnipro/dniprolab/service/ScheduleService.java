package com.fcdnipro.dniprolab.service;

import com.fcdnipro.dniprolab.domain.Schedule;
import com.fcdnipro.dniprolab.repository.ScheduleRepository;
import com.fcdnipro.dniprolab.repository.search.ScheduleSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Schedule.
 */
@Service
@Transactional
public class ScheduleService {

    private final Logger log = LoggerFactory.getLogger(ScheduleService.class);

    @Inject
    private ScheduleRepository scheduleRepository;

    @Inject
    private ScheduleSearchRepository scheduleSearchRepository;

    /**
     * Save a schedule.
     * @return the persisted entity
     */
    public Schedule save(Schedule schedule) {
        log.debug("Request to save Schedule : {}", schedule);
        Schedule result = scheduleRepository.save(schedule);
        scheduleSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the schedules.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Schedule> findAll(Pageable pageable) {
        log.debug("Request to get all Schedules");
        Page<Schedule> result = scheduleRepository.findAll(pageable);
        return result;
    }

    /**
     *  get one schedule by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Schedule findOne(Long id) {
        log.debug("Request to get Schedule : {}", id);
        Schedule schedule = scheduleRepository.findOne(id);
        return schedule;
    }

    /**
     *  delete the  schedule by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Schedule : {}", id);
        scheduleRepository.delete(id);
        scheduleSearchRepository.delete(id);
    }

    /**
     * search for the schedule corresponding
     * to the query.
     */
    @Transactional(readOnly = true)
    public List<Schedule> search(String query) {

        log.debug("REST request to search Schedules for query {}", query);
        return StreamSupport
            .stream(scheduleSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    /*
    * This method everyday removing passed schedule entries
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void removeOldEntries(){
        log.debug("Invoke method of removing passed entries in schedule");
        LocalDate now = LocalDate.now();
        List<Schedule> list = scheduleRepository.findByDateBefore(now);
        for(Schedule schedule : list){
            scheduleRepository.delete(schedule);
            scheduleSearchRepository.delete(schedule);
        }
    }

    public List<Schedule> getFiveClosestEntries(){
        log.debug("Invoke search for closest schedule entries");
        PageRequest pageRequest = new PageRequest(0, 5);
        List<Schedule> schedules = scheduleRepository.findFiveClosestScheduleEntries(pageRequest);
        return schedules;
    }
}
