package com.byzquant.fed.controller;

import com.byzquant.fed.dto.MacroRiskReport;
import com.byzquant.fed.service.MacroRiskEngine;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/macro")
@CrossOrigin(origins = "*") // React aslanlar gibi takılsın
public class MacroRiskController {

    private final MacroRiskEngine riskEngine;

    public MacroRiskController(MacroRiskEngine riskEngine) {
        this.riskEngine = riskEngine;
    }

    @GetMapping("/risk-status")
    public MacroRiskReport getMacroRiskStatus() {
        return riskEngine.calculateSystemicRisk();
    }
}
