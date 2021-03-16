package net.ckb78.EnergyCalcDemo.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.ckb78.EnergyCalcDemo.service.Units;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ECDto {
    @JsonProperty("id")
    private Long calculationId;
    @JsonProperty("producer")
    private String producer;
    @JsonProperty("units")
    private Units units;
    @JsonProperty("round")
    private String round;
    @JsonProperty("mass")
    private double mass;
    @JsonProperty("velocity")
    private double velocity;
    @JsonProperty("energy")
    private double energy;
    @JsonProperty("calculated")
    private LocalDateTime calculatedTimeStamp;

}