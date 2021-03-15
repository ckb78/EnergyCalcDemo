package net.ckb78.EnergyCalcDemo.controller;

import lombok.Data;
import lombok.experimental.Accessors;
import net.ckb78.EnergyCalcDemo.service.Units;

@Data
@Accessors(chain = true)
public class FormInput {

    private String caliber;
    private Units units;
    private String mass;
    private String velocity;

    @Override
    public String toString() {

        return "Caliber: " + caliber + ", "
                + "Velocity: " + velocity + ", "
                + "mass: " + mass + ", "
                + "units: " + units + ".";
    }
}
