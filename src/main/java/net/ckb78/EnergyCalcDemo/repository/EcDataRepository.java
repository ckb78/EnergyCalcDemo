package net.ckb78.EnergyCalcDemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EcDataRepository extends JpaRepository<EcDataEntity, Long> {

    List<EcDataEntity> findAllByProducer(String producer);
}
