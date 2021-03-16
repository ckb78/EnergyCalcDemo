package net.ckb78.EnergyCalcDemo.service;


import lombok.extern.slf4j.Slf4j;
import net.ckb78.EnergyCalcDemo.controller.DataInput;
import net.ckb78.EnergyCalcDemo.controller.EcDto;
import net.ckb78.EnergyCalcDemo.repository.EcDataEntity;
import net.ckb78.EnergyCalcDemo.repository.EcDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class EcService {

    private final static int IMPERIAL_DIMENSIONAL_CONSTANT = 450240;
    private final static int METRIC_DIMENSIONAL_CONSTANT = 1000;
    private final static double JOULES_PR_FOOT_POUND = 1.35581795;
    private final static double FOOT_POUNDS_PR_JOULE = 0.737562149;

    private static Long calcCounter = 0L;
    private static final List<EcResult> results = new ArrayList<>();

    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss");

    @Autowired
    EcDataRepository energyRepository;

    @Autowired
    TestDataProviderService dataProviderService;

    public EcDto createAndSaveResult(DataInput input) {
        double bulletWeight, muzzleVelocity;

        try {
            bulletWeight = Double.parseDouble(input.getMass());
            muzzleVelocity = Double.parseDouble(input.getVelocity());
        } catch (Exception e) {
            bulletWeight = 0;
            muzzleVelocity = 0;
        }

        EcResult result = new EcResult()
                .setProducer(input.getProducer())
                .setUnits(input.getUnits())
                .setMass(bulletWeight)
                .setVelocity(muzzleVelocity)
                .setRound(!(input.getRound().isEmpty()) ? input.getRound() : "-")
                .setCalculatedTimeStamp(LocalDateTime.now());
        calculateAndSetEnergies(result);
        setId(result);
        result.setHumanReadableTimeStamp(result.getCalculatedTimeStamp().format(timeFormatter));

        log.info("EcService - Completed muzzle energy calculation:\n {} ", result.toString());
        addResult(result);
        saveResult(result);

        return entityToDto(energyRepository.getOne(result.getId()));
    }

    private void setId(EcResult result) {
        calcCounter++;
        result.setId(calcCounter + 100);
    }

    public List<EcResult> getLatestFive() {
        List<EcResult> clonedResults = new ArrayList<>(results);
        Collections.reverse(clonedResults);
        return (clonedResults.size() < 5 ? clonedResults : clonedResults.subList(0, 5));
    }

    private void addResult(EcResult result) {
        results.add(result);
    }

    private void saveResult(EcResult result) {
        if (result.getMass() != 0 && result.getVelocity() != 0) {
            energyRepository.save(new EcDataEntity()
                    .setId(result.getId())
                    .setUnits(result.getUnits())
                    .setProducer(result.getProducer().toUpperCase())
                    .setRound(result.getRound())
                    .setMass(result.getMass())
                    .setVelocity(result.getVelocity())
                    .setEnergy(result.getEnergy())
                    .setCalculatedTimeStamp(result.getCalculatedTimeStamp()));
        }
    }

    private void calculateAndSetEnergies(EcResult m) {
        m.setEnergy((m.getUnits() == Units.IMPERIAL) ? getEnergyInFtLbs(m) : getEnergyInJoule(m));
        m.setAltEnergy(ConvertToAltEnergy(m));
    }

    private double getEnergyInFtLbs(EcResult m) {
        return roundDouble(m.getVelocity() * m.getVelocity() * m.getMass() / IMPERIAL_DIMENSIONAL_CONSTANT);
    }

    private double getEnergyInJoule(EcResult m) {
        return roundDouble((0.5 * m.getMass() * (m.getVelocity() * m.getVelocity())) / METRIC_DIMENSIONAL_CONSTANT);
    }

    private double ConvertToAltEnergy(EcResult m) {
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
            log.warn("EcService - Invalid Input Data!");
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

    public List<EcDto> getAllEnergyData() {
        return entityListToDtoList(energyRepository.findAll());
    }

    public EcDto getDataById(Long id) {
        return entityToDto(energyRepository.getOne(id));
    }

    public List<EcDto> getEnergyDataByCompany(String producer) {
        return entityListToDtoList(energyRepository.findAllByProducer(producer));
    }

    private EcDto entityToDto(EcDataEntity entity) {
        return new EcDto()
                .setCalculationId(entity.getId())
                .setProducer(entity.getProducer())
                .setUnits(entity.getUnits())
                .setRound(entity.getRound())
                .setMass(entity.getMass())
                .setVelocity(entity.getVelocity())
                .setEnergy(entity.getEnergy())
                .setCalculatedTimeStamp(entity.getCalculatedTimeStamp());
    }

    public List<EcDto> entityListToDtoList(List<EcDataEntity> entityList) {
        List<EcDto> energyDtoList = new ArrayList<>();
        for (EcDataEntity entity : entityList) {
            energyDtoList.add(entityToDto(entity));
        }
        return energyDtoList;
    }

    public List<EcDto> addTestData() {
        return dataProviderService.populateWithTestData();
    }

    public void deleteDataById(Long id) {
        energyRepository.deleteById(id);
    }
}
