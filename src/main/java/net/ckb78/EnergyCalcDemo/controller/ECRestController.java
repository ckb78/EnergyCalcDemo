package net.ckb78.EnergyCalcDemo.controller;

import lombok.extern.slf4j.Slf4j;
import net.ckb78.EnergyCalcDemo.service.ECService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class ECRestController {

    @Autowired
    private ECService calcService;

    @GetMapping("/getdata")
    public List<ECDto> getAllData() {
        checkDataAvailability();
        log.info("* GET /getdata");
        return calcService.getAllEnergyData();
    }

    @GetMapping("/getbyid/{id}")
    public ECDto getSingleEnergyData(@PathVariable Long id) {
        checkDataAvailability();
        log.info("* GET /getbyid/" + id);
        return calcService.getDataById(id);
    }

    @GetMapping("/getbycompany/{company}")
    public List<ECDto> getSingleEnergyData(@PathVariable String company) {
        checkDataAvailability();
        log.info("* GET /getbycompany/" + company);
        return calcService.getEnergyDataByCompany(company.toUpperCase());
    }

    @PostMapping(path = "/inputdata", consumes = "application/json", produces = "application/json")
    public ECDto addData(@RequestBody DataInput input) {
        log.info("* POST /inputdata " + "\n " + input.toString());
        checkDataAvailability();
        return calcService.createAndSaveResult(input);
    }

    @DeleteMapping("/deletebyid/{id}")
    public void deleteDataById(@PathVariable Long id) {
        checkDataAvailability();
        log.info("* DELETE /deletebyid/" + id);
        calcService.deleteDataById(id);
    }

    @GetMapping("/populate")
    public List<ECDto> populateWithTestData() {
        log.info("* GET /populate ");
        return calcService.addTestData();
    }

    private void checkDataAvailability() {
        if (calcService.getAllEnergyData().isEmpty())
            populateWithTestData();
    }
}
