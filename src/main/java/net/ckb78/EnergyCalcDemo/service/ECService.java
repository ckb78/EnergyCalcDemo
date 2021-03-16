package net.ckb78.EnergyCalcDemo.service;


import lombok.extern.slf4j.Slf4j;
import net.ckb78.EnergyCalcDemo.controller.DataInput;
import net.ckb78.EnergyCalcDemo.controller.ECDto;
import net.ckb78.EnergyCalcDemo.repository.ECDataEntity;
import net.ckb78.EnergyCalcDemo.repository.ECDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ECService {

    private final static int IMPERIAL_DIMENSIONAL_CONSTANT = 450240;
    private final static int METRIC_DIMENSIONAL_CONSTANT = 1000;
    private final static double JOULES_PR_FOOT_POUND = 1.35581795;
    private final static double FOOT_POUNDS_PR_JOULE = 0.737562149;

    private static Long calcCounter = 0L;
    private static final List<ECResult> results = new ArrayList<>();

    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss");

    @Autowired
    ECDataRepository energyRepository;

    @Autowired
    TestDataProviderService dataProviderService;

    public ECDto createAndSaveResult(DataInput input) {
        double bulletWeight, muzzleVelocity;

        try {
            bulletWeight = Double.parseDouble(input.getMass());
            muzzleVelocity = Double.parseDouble(input.getVelocity());
        } catch (Exception e) {
            bulletWeight = 0;
            muzzleVelocity = 0;
        }

        ECResult result = new ECResult()
                .setProducer(input.getProducer())
                .setUnits(input.getUnits())
                .setMass(bulletWeight)
                .setVelocity(muzzleVelocity)
                .setRound(!(input.getRound().isEmpty()) ? input.getRound() : "-")
                .setCalculatedTimeStamp(LocalDateTime.now());
        calculateAndSetEnergies(result);
        setId(result);
        result.setHumanReadableTimeStamp(result.getCalculatedTimeStamp().format(timeFormatter));

        log.info("MuzzleEnergyService - Completed muzzle energy calculation:\n {} ", result.toString());
        addResult(result);
        saveResult(result);

        return entityToDto(energyRepository.getOne(result.getId()));
    }

    private void setId(ECResult result) {
        calcCounter++;
        result.setId(calcCounter + 100);
    }

    public List<ECResult> getLatestFive() {
        List<ECResult> clonedResults = new ArrayList<>(results);
        Collections.reverse(clonedResults);
        return (clonedResults.size() < 5 ? clonedResults : clonedResults.subList(0, 5));
    }

    private void addResult(ECResult result) {
        results.add(result);
    }

    private void saveResult(ECResult result) {
        if (result.getMass() != 0 && result.getVelocity() != 0) {
            energyRepository.save(new ECDataEntity()
                    .setId(result.getId())
                    .setUnits(result.getUnits())
                    .setProducer(result.getProducer().toUpperCase())
                    .setRound(result.getRound())
                    .setMass(result.getMass())
                    .setVelocity(result.getVelocity())
                    .setEnergy(result.getEnergy()))
                    .setCalculatedTimeStamp(result.getCalculatedTimeStamp());
        }
    }

    private void calculateAndSetEnergies(ECResult m) {
        m.setEnergy((m.getUnits() == Units.IMPERIAL) ? getEnergyInFtLbs(m) : getEnergyInJoule(m));
        m.setAltEnergy(ConvertToAltEnergy(m));
    }

    private double getEnergyInFtLbs(ECResult m) {
        return roundDouble(m.getVelocity() * m.getVelocity() * m.getMass() / IMPERIAL_DIMENSIONAL_CONSTANT);
    }

    private double getEnergyInJoule(ECResult m) {
        return roundDouble((0.5 * m.getMass() * (m.getVelocity() * m.getVelocity())) / METRIC_DIMENSIONAL_CONSTANT);
    }

    private double ConvertToAltEnergy(ECResult m) {
        return (m.getUnits() == Units.IMPERIAL) ? roundDouble(m.getEnergy() * JOULES_PR_FOOT_POUND)
                : roundDouble(m.getEnergy() * FOOT_POUNDS_PR_JOULE);
    }

    public boolean validateInput(DataInput input) {
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

    private void checkAndCorrectDecimalDelimiter(DataInput input) {
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

    public List<ECDto> getAllEnergyData() {
        return entityListToDtoList(energyRepository.findAll());
    }

    public ECDto getDataById(Long id) {
        return entityToDto(energyRepository.getOne(id));
    }

    public List<ECDto> getEnergyDataByCompany(String producer) {
        return entityListToDtoList(energyRepository.findAllByProducer(producer));
    }

    private ECDto entityToDto(ECDataEntity entity) {
        return new ECDto()
                .setCalculationId(entity.getId())
                .setProducer(entity.getProducer())
                .setUnits(entity.getUnits())
                .setRound(entity.getRound())
                .setMass(entity.getMass())
                .setVelocity(entity.getVelocity())
                .setEnergy(entity.getEnergy())
                .setCalculatedTimeStamp(entity.getCalculatedTimeStamp());
    }

    public List<ECDto> entityListToDtoList(List<ECDataEntity> entityList) {
        List<ECDto> energyDtoList = new ArrayList<>();
        for (ECDataEntity entity : entityList) {
            energyDtoList.add(entityToDto(entity));
        }
        return energyDtoList;
    }

    public List<ECDto> addTestData() {
        return dataProviderService.populateWithTestData();
    }

    public void deleteDataById(Long id) {
        energyRepository.deleteById(id);
    }
}
