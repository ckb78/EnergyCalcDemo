package net.ckb78.EnergyCalcDemo.service;

import net.ckb78.EnergyCalcDemo.controller.DataInput;
import net.ckb78.EnergyCalcDemo.controller.EcDto;
import net.ckb78.EnergyCalcDemo.repository.EcDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestDataProviderService {

    @Autowired
    private EcService calcService;

    @Autowired
    EcDataRepository energyRepository;

    List<EcDto> populateWithTestData() {

        calcService.createAndSaveResult(new DataInput()
                .setProducer("CCI")
                .setUnits(Units.IMPERIAL)
                .setVelocity("1000")
                .setMass("40")
                .setRound(".22 LR Standard (DEMO DATA)"));

        calcService.createAndSaveResult(new DataInput()
                .setProducer("CCI")
                .setUnits(Units.IMPERIAL)
                .setVelocity("1250")
                .setMass("40")
                .setRound(".22 LR Mini Mag (DEMO DATA)"));

        calcService.createAndSaveResult(new DataInput()
                .setProducer("CCI")
                .setUnits(Units.IMPERIAL)
                .setVelocity("1630")
                .setMass("32")
                .setRound(".22 LR Stinger (DEMO DATA)"));

        calcService.createAndSaveResult(new DataInput()
                .setProducer("Hornady")
                .setUnits(Units.IMPERIAL)
                .setVelocity("2400")
                .setMass("160")
                .setRound(".30-30 LeverEvolution (DEMO DATA)"));

        calcService.createAndSaveResult(new DataInput()
                .setProducer("Elay")
                .setUnits(Units.IMPERIAL)
                .setVelocity("980")
                .setMass("40")
                .setRound(".22 LR Subsonic (DEMO DATA)"));

        calcService.createAndSaveResult(new DataInput()
                .setProducer("SK")
                .setUnits(Units.IMPERIAL)
                .setVelocity("1073")
                .setMass("40")
                .setRound(".22 LR Rifle Match (DEMO DATA)"));

        return calcService.entityListToDtoList(energyRepository.findAll());
    }
}
