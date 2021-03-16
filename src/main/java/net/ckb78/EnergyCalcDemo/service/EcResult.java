package net.ckb78.EnergyCalcDemo.service;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class EcResult {

    private Long id;
    private String producer;
    private Units units;
    private String round;
    private double mass;
    private double velocity;
    private double energy;
    private double altEnergy;

    private LocalDateTime calculatedTimeStamp;
    private String humanReadableTimeStamp;

    @Override
    public String toString() {

        return "* Calculation Id. " + id + ", "
                + "Units: " + units + ", "
                + "Producer: " + producer + ", "
                + "Round: " + round + ", "
                + "Velocity: " + velocity + (units == Units.IMPERIAL ? " feet/sec" : "meters/sec") + ", "
                + "Mass: " + mass + (units == Units.IMPERIAL ? " grains" : "grams") + ", "
                + "Energy: " + energy + (units == Units.IMPERIAL ? " foot/pounds" : " joule") + ", "
                + "Entered: " + humanReadableTimeStamp + ".";
    }
}
