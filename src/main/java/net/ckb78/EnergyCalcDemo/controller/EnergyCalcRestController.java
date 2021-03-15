package net.ckb78.EnergyCalcDemo.controller;

import lombok.extern.slf4j.Slf4j;
import net.ckb78.EnergyCalcDemo.service.EnergyCalcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class EnergyCalcRestController {

    @Autowired
    private EnergyCalcService calcService;

    @GetMapping("/getdata")
    public List<EnergyDto> getAllData() {
        return calcService.getAllEnergyData();
    }

    @GetMapping("/getbyid/{id}")
    public EnergyDto getSingleEnergyData(@PathVariable Long id) {
        return calcService.getEnergyDataById(id);
    }

    @GetMapping("/getbycompany/{company}")
    public List<EnergyDto> getSingleEnergyData(@PathVariable String company) {
        return calcService.getEnergyDataByCompany(company.toUpperCase());
    }

    @GetMapping("/populate")
    public List<EnergyDto> populateWithTestData() {
        return calcService.populateWithTestData();
    }

    @PostMapping(path = "/inputdata", consumes = "application/json", produces = "application/json")
    public EnergyDto addData(@RequestBody DataInput input) {
        if (calcService.validateInput(input)) {
            return calcService.createAndSaveResult(input);
        }
        return null;
    }
}
