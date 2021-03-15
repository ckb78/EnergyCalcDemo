package net.ckb78.EnergyCalcDemo.service;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class EnergyCalcResult {

    private Units units;
    private String caliber;
    private double mass;
    private double velocity;
    private double energy;
    private double altEnergy;
    private int calculationNumber;
    private LocalDateTime calculatedTimeStamp;
    private String humanReadableTimeStamp;

    @Override
    public String toString() {

        return "* Calculation nr. " + calculationNumber + ", "
                + "Units: " + units + ", "
                + "Caliber: " + caliber + ", "
                + "Velocity: " + velocity + (units == Units.IMPERIAL ? "feet/sec" : "meters/sec")
                + "Mass: " + mass + (units == Units.IMPERIAL ? "grains" : "grams")
                + "Energy: " + energy + (units == Units.IMPERIAL ? "foot/pounds" : "joule")
                + "Entered: " + humanReadableTimeStamp + ".";
    }
}
