package net.ckb78.EnergyCalcDemo.service;

import lombok.extern.slf4j.Slf4j;
import net.ckb78.EnergyCalcDemo.controller.FormInput;
import net.ckb78.EnergyCalcDemo.repository.EnergyEntity;
import net.ckb78.EnergyCalcDemo.repository.EnergyRepository;
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

    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss");

    @Autowired
    EnergyRepository energyRepository;

    private final static double IMPERIAL_DIMENSIONAL_CONSTANT = 450240;
    private final static double METRIC_DIMENSIONAL_CONSTANT = 1000;

    private static final List<EnergyCalcResult> results = new ArrayList<>();

    public void calculateResult(FormInput input) {
        double bulletWeight;
        double muzzleVelocity;
        checkAndCorrectDecimalDelimiter(input);
        try {
            bulletWeight = Double.parseDouble(input.getMass());
            muzzleVelocity = Double.parseDouble(input.getVelocity());
        } catch (Exception e) {
            bulletWeight = 0;
            muzzleVelocity = 0;
        }
        EnergyCalcResult result = new EnergyCalcResult()
                .setUnits(input.getUnits())
                .setMass(bulletWeight)
                .setVelocity(muzzleVelocity)
                .setCaliber(!(input.getCaliber().isEmpty()) ? input.getCaliber() : "-")
                .setCalculatedTimeStamp(LocalDateTime.now());

        calculateAndSetEnergies(result);
        result.setHumanReadableTimeStamp(result.getCalculatedTimeStamp().format(timeFormatter));

        log.info("MuzzleEnergyService - Completed muzzle energy calculation:\n {} ", result.toString());
        addResult(result);
        saveResult(result);
    }

    public List<EnergyCalcResult> getLatestFiveResults() {
        List<EnergyCalcResult> clonedResults = new ArrayList<>(results);
        Collections.reverse(clonedResults);
        return (clonedResults.size() < 5 ? clonedResults : clonedResults.subList(0, 5));
    }

    private void addResult(EnergyCalcResult result) {
        results.add(result);
    }

    // @PostConstruct
    private void saveResult(EnergyCalcResult result) {
        if (result.getMass() != 0 && result.getVelocity() != 0){
            energyRepository.save(new EnergyEntity()
                    .setUnits(result.getUnits())
                    .setCaliber(result.getCaliber())
                    .setMass(result.getMass())
                    .setVelocity(result.getVelocity())
                    .setEnergy(result.getEnergy()));
        }
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

    private void calculateAndSetEnergies(EnergyCalcResult m) {
        m.setEnergy((m.getUnits() == Units.IMPERIAL) ? getEnergyInFtLbs(m) : getEnergyInJoule(m));
        m.setAltEnergy((m.getUnits() == Units.IMPERIAL) ? footPoundsToJoule(m.getEnergy()) : jouleToFootPounds(m.getEnergy()));
    }

    private double getEnergyInFtLbs(EnergyCalcResult m) {
        return roundDouble(m.getVelocity() * m.getVelocity() * m.getMass() / IMPERIAL_DIMENSIONAL_CONSTANT);
    }

    private double getEnergyInJoule(EnergyCalcResult m) {
        return roundDouble((0.5 * m.getMass() * (m.getVelocity() * m.getVelocity())) / METRIC_DIMENSIONAL_CONSTANT);
    }

    private double footPoundsToJoule(double footPounds) {
        return roundDouble(footPounds * 1.36);
    }

    private double jouleToFootPounds(double joule) {
        return roundDouble(joule * 0.737562149);
    }

    private double roundDouble(double value) {
        return Math.round(value * 100.00) / 100.00;
    }
}
