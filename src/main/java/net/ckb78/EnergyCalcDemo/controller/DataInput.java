package net.ckb78.EnergyCalcDemo.controller;

import lombok.Data;
import lombok.experimental.Accessors;
import net.ckb78.EnergyCalcDemo.service.Units;

@Data
@Accessors(chain = true)
public class DataInput {

    private String producer;
    private String round;
    private Units units;
    private String mass;
    private String velocity;

    @Override
    public String toString() {

        return "Producer: " + producer + ", "
                + "Round: " + round + ", "
                + "Velocity: " + velocity + ", "
                + "mass: " + mass + ", "
                + "units: " + units + ".";
    }
}
