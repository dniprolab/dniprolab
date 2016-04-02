package com.fcdnipro.dniprolab.repository;

import com.fcdnipro.dniprolab.domain.Advert;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Advert entity.
 */
public interface AdvertRepository extends JpaRepository<Advert,Long> {

}
