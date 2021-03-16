package net.ckb78.EnergyCalcDemo.controller;


import lombok.extern.slf4j.Slf4j;
import net.ckb78.EnergyCalcDemo.service.EcService;
import net.ckb78.EnergyCalcDemo.service.Units;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class EcWebController {

    @Autowired
    private EcService calcService;

    private static Boolean validInput = true;

    @GetMapping("/")
    public String GetInput(Model model, @ModelAttribute("input") DataInput input) {
        log.info("EcWebController - GET: \"/\"");
        input.setUnits(Units.IMPERIAL);
        checkAndPopulate();
        model.addAttribute("validInput", validInput);
        model.addAttribute("results", calcService.getLatestFive());
        return "calculator";
    }

    @PostMapping("/")
    public String getResult(Model model, @ModelAttribute("input") DataInput input) {
        log.info("EcWebController - POST: \"/\", {}", input.toString());
        validInput = calcService.validateInput(input);
        if (validInput) {
            calcService.newCalculation(input);
        }
        model.addAttribute("validInput", validInput);
        model.addAttribute("results", calcService.getLatestFive());
        return "calculator";
    }

    @GetMapping("/no")
    public String GetInputNo(Model model, @ModelAttribute("input") DataInput input) {
        log.info("EcWebController - GET: \"/no\"");
        input.setUnits(Units.METRIC);
        checkAndPopulate();
        model.addAttribute("validInput", validInput);
        model.addAttribute("results", calcService.getLatestFive());
        return "kalkulator";
    }

    @PostMapping("/no")
    public String getResultsNo(Model model, @ModelAttribute("input") DataInput input) {
        log.info("EcWebController - POST: \"/no\", {}", input.toString());
        validInput = calcService.validateInput(input);
        if (validInput) {
            calcService.newCalculation(input);
        }
        model.addAttribute("validInput", validInput);
        model.addAttribute("results", calcService.getLatestFive());
        return "kalkulator";
    }

    private void checkAndPopulate() {
        if (calcService.getLatestFive().isEmpty()) {
            calcService.addTestData();
        }
    }

}

