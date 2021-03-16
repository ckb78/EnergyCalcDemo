package net.ckb78.EnergyCalcDemo.controller;

import lombok.extern.slf4j.Slf4j;
import net.ckb78.EnergyCalcDemo.service.ECService;
import net.ckb78.EnergyCalcDemo.service.TestDataProviderService;
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
        return calcService.getAllEnergyData();
    }

    @GetMapping("/getbyid/{id}")
    public ECDto getSingleEnergyData(@PathVariable Long id) {
        return calcService.getEnergyDataById(id);
    }

    @GetMapping("/getbycompany/{company}")
    public List<ECDto> getSingleEnergyData(@PathVariable String company) {
        return calcService.getEnergyDataByCompany(company.toUpperCase());
    }

    @GetMapping("/populate")
    public List<ECDto> populateWithTestData() {
        return calcService.addTestData();
    }

    @PostMapping(path = "/inputdata", consumes = "application/json", produces = "application/json")
    public ECDto addData(@RequestBody DataInput input) {
        if (calcService.validateInput(input)) {
            return calcService.createAndSaveResult(input);
        }
        return null;
    }
}
