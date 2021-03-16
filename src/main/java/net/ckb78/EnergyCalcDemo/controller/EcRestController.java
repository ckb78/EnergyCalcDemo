package net.ckb78.EnergyCalcDemo.controller;

import lombok.extern.slf4j.Slf4j;
import net.ckb78.EnergyCalcDemo.service.EcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class EcRestController {

    @Autowired
    private EcService calcService;

    @GetMapping("/getdata")
    public List<EcDto> getAllData() {
        checkDataAvailability();
        log.info("EcRestController - GET /getdata");
        return calcService.getAllEnergyData();
    }

    @GetMapping("/getbyid/{id}")
    public EcDto getSingleEnergyData(@PathVariable Long id) {
        checkDataAvailability();
        log.info("EcRestController - GET /getbyid/" + id);
        return calcService.getDataById(id);
    }

    @GetMapping("/getbycompany/{company}")
    public List<EcDto> getSingleEnergyData(@PathVariable String company) {
        checkDataAvailability();
        log.info("EcRestController - GET /getbycompany/" + company);
        return calcService.getEnergyDataByCompany(company.toUpperCase());
    }

    @PostMapping(path = "/inputdata", consumes = "application/json", produces = "application/json")
    public EcDto addData(@RequestBody DataInput input) {
        log.info("EcRestController - POST /inputdata " + "\n " + input.toString());
        return calcService.newCalculation(input);
    }

    @DeleteMapping("/deletebyid/{id}")
    public void deleteDataById(@PathVariable Long id) {
        checkDataAvailability();
        log.info("EcRestController - DELETE /deletebyid/" + id);
        calcService.deleteDataById(id);
    }

    @GetMapping("/populate")
    public List<EcDto> populateWithTestData() {
        log.info("EcRestController - GET /populate ");
        return calcService.addTestData();
    }

    private void checkDataAvailability() {
        if (calcService.getAllEnergyData().isEmpty())
            calcService.addTestData();
    }
}
