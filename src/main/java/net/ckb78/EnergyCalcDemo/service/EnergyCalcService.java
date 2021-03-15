package net.ckb78.EnergyCalcDemo.service;


import lombok.extern.slf4j.Slf4j;
import net.ckb78.EnergyCalcDemo.controller.EnergyDto;
import net.ckb78.EnergyCalcDemo.controller.FormInput;
import net.ckb78.EnergyCalcDemo.repository.EnergyDataEntity;
import net.ckb78.EnergyCalcDemo.repository.EnergyDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class EnergyCalcService {

    private final static int IMPERIAL_DIMENSIONAL_CONSTANT = 450240;
    private final static int METRIC_DIMENSIONAL_CONSTANT = 1000;
    private final static double JOULES_PR_FOOT_POUND = 1.35581795;
    private final static double FOOT_POUNDS_PR_JOULE = 0.737562149;

    private static Long calcCounter = 0L;
    private static final List<EnergyCalcResult> results = new ArrayList<>();

    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss");

    @Autowired
    EnergyDataRepository energyRepository;

    public void createAndSaveResult(FormInput input) {
        double bulletWeight, muzzleVelocity;

        try {
            bulletWeight = Double.parseDouble(input.getMass());
            muzzleVelocity = Double.parseDouble(input.getVelocity());
        } catch (Exception e) {
            bulletWeight = 0;
            muzzleVelocity = 0;
        }

        EnergyCalcResult result = new EnergyCalcResult()
                .setProducer(input.getProducer())
                .setUnits(input.getUnits())
                .setMass(bulletWeight)
                .setVelocity(muzzleVelocity)
                .setCaliber(!(input.getCaliber().isEmpty()) ? input.getCaliber() : "-")
                .setCalculatedTimeStamp(LocalDateTime.now());
        calculateAndSetEnergies(result);
        setId(result);
        result.setHumanReadableTimeStamp(result.getCalculatedTimeStamp().format(timeFormatter));

        log.info("MuzzleEnergyService - Completed muzzle energy calculation:\n {} ", result.toString());
        addResult(result);
        saveResult(result);
    }

    private void setId(EnergyCalcResult result) {
        calcCounter++;
        result.setId(calcCounter + 100);
    }

    public List<EnergyCalcResult> getLatestFive() {
        List<EnergyCalcResult> clonedResults = new ArrayList<>(results);
        Collections.reverse(clonedResults);
        return (clonedResults.size() < 5 ? clonedResults : clonedResults.subList(0, 5));
    }

    private void addResult(EnergyCalcResult result) {
        results.add(result);
    }

    private void saveResult(EnergyCalcResult result) {
        if (result.getMass() != 0 && result.getVelocity() != 0){
            energyRepository.save(new EnergyDataEntity()
                    .setId(result.getId())
                    .setUnits(result.getUnits())
                    .setProducer(result.getProducer().toUpperCase())
                    .setCaliber(result.getCaliber())
                    .setMass(result.getMass())
                    .setVelocity(result.getVelocity())
                    .setEnergy(result.getEnergy()))
                    .setCalculatedTimeStamp(result.getCalculatedTimeStamp());
        }
    }

    private void calculateAndSetEnergies(EnergyCalcResult m) {
        m.setEnergy((m.getUnits() == Units.IMPERIAL) ? getEnergyInFtLbs(m) : getEnergyInJoule(m));
        m.setAltEnergy(ConvertToAltEnergy(m));
    }

    private double getEnergyInFtLbs(EnergyCalcResult m) {
        return roundDouble(m.getVelocity() * m.getVelocity() * m.getMass() / IMPERIAL_DIMENSIONAL_CONSTANT);
    }

    private double getEnergyInJoule(EnergyCalcResult m) {
        return roundDouble((0.5 * m.getMass() * (m.getVelocity() * m.getVelocity())) / METRIC_DIMENSIONAL_CONSTANT);
    }

    private double ConvertToAltEnergy(EnergyCalcResult m) {
        return (m.getUnits() == Units.IMPERIAL) ? roundDouble(m.getEnergy() * JOULES_PR_FOOT_POUND)
                : roundDouble(m.getEnergy() * FOOT_POUNDS_PR_JOULE);
    }

    public boolean validateInputData(FormInput input) {
        checkAndCorrectDecimalDelimiter(input);
        try {
            double m = Double.parseDouble(input.getMass());
            double v = Double.parseDouble(input.getVelocity());
            return m > 0.0 && v > 0.0;
        } catch (Exception e) {
            log.warn("* Invalid Input Data!");
        }
        return false;
    }

    private void checkAndCorrectDecimalDelimiter(FormInput input) {
        if (input.getVelocity().contains(",") || input.getMass().contains(",")) {
            input.setVelocity(fixDecimalDelimiter(input.getVelocity()));
            input.setMass(fixDecimalDelimiter(input.getMass()));
        }
    }

    private String fixDecimalDelimiter(String s) {
        if (s.contains(",")) {
            int i = s.indexOf(",");
            StringBuilder c = new StringBuilder(s);
            c.setCharAt(i, '.');
            return c.toString();
        }
        return s;
    }

    private double roundDouble(double value) {
        return Math.round(value * 100.00) / 100.00;
    }

    public List<EnergyDto> getAllEnergyData() {
        return entityListToDtoList(energyRepository.findAll());
    }

    public EnergyDto getEnergyDataById(Long id) {
        return entityToDto(energyRepository.getOne(id));
    }
    public List<EnergyDto> getEnergyDataByCompany(String producer) {
        return entityListToDtoList(energyRepository.findAllByProducer(producer));
    }

    private EnergyDto entityToDto(EnergyDataEntity entity) {
        return new EnergyDto()
                .setCalculationId(entity.getId())
                .setProducer(entity.getProducer())
                .setUnits(entity.getUnits())
                .setCaliber(entity.getCaliber())
                .setMass(entity.getMass())
                .setVelocity(entity.getVelocity())
                .setEnergy(entity.getEnergy())
                .setCalculatedTimeStamp(entity.getCalculatedTimeStamp());
    }

    private List<EnergyDto> entityListToDtoList(List<EnergyDataEntity> entityList) {
        List<EnergyDto> energyDtoList = new ArrayList<>();
        for (EnergyDataEntity entity : entityList) {
            energyDtoList.add(entityToDto(entity));
        }
        return energyDtoList;
    }

    public List<EnergyDto> populateWithTestData() {

        createAndSaveResult(new FormInput()
                .setProducer("CCI")
                .setUnits(Units.IMPERIAL)
                .setVelocity("1000")
                .setMass("40")
                .setCaliber(".22 LR Standard"));

        createAndSaveResult(new FormInput()
                .setProducer("CCI")
                .setUnits(Units.IMPERIAL)
                .setVelocity("1630")
                .setMass("32")
                .setCaliber(".22 LR Stinger"));

        createAndSaveResult(new FormInput()
                .setProducer("Hornady")
                .setUnits(Units.IMPERIAL)
                .setVelocity("2400")
                .setMass("160")
                .setCaliber(".30-30 LeverEvolution"));

        return entityListToDtoList(energyRepository.findAll());
    }
}
