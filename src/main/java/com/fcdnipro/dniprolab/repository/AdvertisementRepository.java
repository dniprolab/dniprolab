package com.fcdnipro.dniprolab.repository;

import com.fcdnipro.dniprolab.domain.Advertisement;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Advertisement entity.
 */
public interface AdvertisementRepository extends JpaRepository<Advertisement,Long> {

}
