package com.outforce.desafio.repository;

import com.outforce.desafio.domain.enums.CuponStatus;
import com.outforce.desafio.entity.CuponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuponRepository extends JpaRepository<CuponEntity, String> {
    Optional<CuponEntity> findByIdAndStatus(String id, CuponStatus status);
}
