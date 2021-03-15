package net.ckb78.EnergyCalcDemo.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.ckb78.EnergyCalcDemo.service.Units;

import java.time.LocalDateTime;

public class EnergyDto {
    @JsonProperty("id")
    private int calculationId;
    @JsonProperty("units")
    private Units units;
    @JsonProperty("caliber")
    private String caliber;
    @JsonProperty("mass")
    private double mass;
    @JsonProperty("velocity")
    private double velocity;
    @JsonProperty("energy")
    private double energy;
    private LocalDateTime calculatedTimeStamp;
}