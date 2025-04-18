package com.syriabazaar.repository;

import com.syriabazaar.domain.CarModel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CarModel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarModelRepository extends JpaRepository<CarModel, Long> {}
