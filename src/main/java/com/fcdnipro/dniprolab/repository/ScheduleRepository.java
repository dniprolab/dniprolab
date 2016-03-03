package com.fcdnipro.dniprolab.repository;

import com.fcdnipro.dniprolab.domain.Schedule;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Schedule entity.
 */
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

}
