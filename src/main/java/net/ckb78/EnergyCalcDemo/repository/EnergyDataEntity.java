package net.ckb78.EnergyCalcDemo.repository;


import lombok.Data;
import lombok.experimental.Accessors;
import net.ckb78.EnergyCalcDemo.service.Units;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Entity
public class EnergyDataEntity {

    @Id
    private Long id;
    private String producer;
    private Units units;
    private String caliber;
    private double mass;
    private double velocity;
    private double energy;
    private LocalDateTime calculatedTimeStamp;
}
