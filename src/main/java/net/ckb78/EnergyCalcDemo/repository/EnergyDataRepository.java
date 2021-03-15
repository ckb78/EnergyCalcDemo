package net.ckb78.EnergyCalcDemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnergyDataRepository extends JpaRepository<EnergyDataEntity, Long> {

    List<EnergyDataEntity> findAllByProducer(String producer);
}
