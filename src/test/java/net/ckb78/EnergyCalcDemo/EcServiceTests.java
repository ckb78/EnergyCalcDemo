package net.ckb78.EnergyCalcDemo;

import net.ckb78.EnergyCalcDemo.controller.DataInput;
import net.ckb78.EnergyCalcDemo.service.Units;
import net.ckb78.EnergyCalcDemo.service.EcService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DataJpaTest
class MuzzleEnergyResultServiceTests {

    @Autowired
    EcService ecService;

    @Test
    void getEnergyInFtLbsTest() {
        DataInput input = new DataInput()
                .setUnits(Units.IMPERIAL)
                .setVelocity("1060.92")
                .setMass("40")
                .setProducer("CCI")
                .setRound(".22 Long Rifle Standard Velocity");

        ecService.newCalculation(input);
        assertEquals(100, ecService.getLatestFive().get(0).getEnergy());
        assertEquals(".22 Long Rifle Standard Velocity", ecService.getLatestFive().get(0).getRound());
    }

    @Test
    void getEnergyInJoule() {
        DataInput input = new DataInput()
                .setUnits(Units.METRIC)
                .setVelocity("300")
                .setMass("2,6")
                .setProducer("CCI")
                .setRound(".22 Long Rifle Subsonic");

        ecService.newCalculation(input);
        assertEquals(117, ecService.getLatestFive().get(0).getEnergy());
        assertEquals(".22 Long Rifle Subsonic", ecService.getLatestFive().get(0).getRound());
    }

    @Test
    void saveResultToInMemoryDatabase() {
        DataInput input = new DataInput()
                .setUnits(Units.METRIC)
                .setVelocity("300")
                .setMass("2,6")
                .setProducer("CCI")
                .setRound(".22 Long Rifle Subsonic");

        ecService.newCalculation(input);
        assertEquals(117, ecService.getLatestFive().get(0).getEnergy());
        assertEquals(".22 Long Rifle Subsonic", ecService.getLatestFive().get(0).getRound());
    }
}

