package com.github.martynfunclub.trackingsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.martynfunclub.trackingsystem.models.Production;
import com.github.martynfunclub.trackingsystem.models.WorkersPlace;

public interface ProductionRepository extends JpaRepository<Production, Long> {
    Production getProductionByPlaceAndEndTimeIsNull(WorkersPlace place);
    Production getProductionByPlace(WorkersPlace place);
}
