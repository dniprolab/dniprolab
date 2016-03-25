package com.fcdnipro.dniprolab.repository;

import com.fcdnipro.dniprolab.domain.Schedule;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the Schedule entity.
 */
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

    List<Schedule> findByDateBefore(LocalDate localDate);

    @Query("select s from Schedule s where s.date >= current_date order by s.date")
    List<Schedule> findFiveClosestScheduleEntries(Pageable pageable);

}
