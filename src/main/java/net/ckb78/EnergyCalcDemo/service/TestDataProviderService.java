package net.ckb78.EnergyCalcDemo.service;

import net.ckb78.EnergyCalcDemo.controller.DataInput;
import net.ckb78.EnergyCalcDemo.controller.ECDto;
import net.ckb78.EnergyCalcDemo.repository.ECDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestDataProviderService {

    @Autowired
    private ECService calcService;

    @Autowired
    ECDataRepository energyRepository;

    List<ECDto> populateWithTestData() {

        calcService.createAndSaveResult(new DataInput()
                .setProducer("CCI")
                .setUnits(Units.IMPERIAL)
                .setVelocity("1000")
                .setMass("40")
                .setCaliber(".22 LR Standard (DEMO DATA)"));

        calcService.createAndSaveResult(new DataInput()
                .setProducer("CCI")
                .setUnits(Units.IMPERIAL)
                .setVelocity("1250")
                .setMass("40")
                .setCaliber(".22 LR Mini Mag (DEMO DATA)"));

        calcService.createAndSaveResult(new DataInput()
                .setProducer("CCI")
                .setUnits(Units.IMPERIAL)
                .setVelocity("1630")
                .setMass("32")
                .setCaliber(".22 LR Stinger (DEMO DATA)"));

        calcService.createAndSaveResult(new DataInput()
                .setProducer("Hornady")
                .setUnits(Units.IMPERIAL)
                .setVelocity("2400")
                .setMass("160")
                .setCaliber(".30-30 LeverEvolution (DEMO DATA)"));

        calcService.createAndSaveResult(new DataInput()
                .setProducer("Elay")
                .setUnits(Units.IMPERIAL)
                .setVelocity("980")
                .setMass("40")
                .setCaliber(".22 LR Subsonic (DEMO DATA)"));

        calcService.createAndSaveResult(new DataInput()
                .setProducer("SK")
                .setUnits(Units.IMPERIAL)
                .setVelocity("1073")
                .setMass("40")
                .setCaliber(".22 LR Rifle Match (DEMO DATA)"));

        return calcService.entityListToDtoList(energyRepository.findAll());
    }
}
