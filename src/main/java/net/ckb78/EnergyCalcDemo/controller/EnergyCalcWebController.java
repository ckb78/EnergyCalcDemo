package net.ckb78.EnergyCalcDemo.controller;

import lombok.extern.slf4j.Slf4j;
import net.ckb78.EnergyCalcDemo.service.EnergyCalcService;
import net.ckb78.EnergyCalcDemo.service.Units;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class EnergyCalcWebController {

    @GetMapping("/")
    public String GetInputNo(Model model, EnergyCalcService calcService, @ModelAttribute("input") FormInput input) {
        log.info("MuzzleEnergyController - GET: \"/\"");
        input.setUnits(Units.METRIC);
        model.addAttribute("results", calcService.getLatestFiveResults());
        return "kalkulator";
    }

    @PostMapping("/")
    public String getResultsNo(Model model, EnergyCalcService calcService, @ModelAttribute("input") FormInput input) {
        log.info("MuzzleEnergyController - POST: \"/\", {}", input.toString());
        calcService.calculateResult(input);
        model.addAttribute("results", calcService.getLatestFiveResults());
        return "kalkulator";
    }

    @GetMapping("/en")
    public String GetInput(Model model, EnergyCalcService calcService, @ModelAttribute("input") FormInput input) {
        log.info("MuzzleEnergyController - GET: \"/en\"");
        input.setUnits(Units.IMPERIAL);
        model.addAttribute("results", calcService.getLatestFiveResults());
        return "calculator";
    }

    @PostMapping("/en")
    public String getResult(Model model, EnergyCalcService calcService, @ModelAttribute("input") FormInput input) {
        log.info("MuzzleEnergyController - POST: \"/en\", {}", input.toString());
        calcService.calculateResult(input);
        model.addAttribute("results", calcService.getLatestFiveResults());
        return "calculator";
    }
}

