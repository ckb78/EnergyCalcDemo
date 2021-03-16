package net.ckb78.EnergyCalcDemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ECDataRepository extends JpaRepository<ECDataEntity, Long> {

    List<ECDataEntity> findAllByProducer(String producer);
}
