package com.byzquant.fed.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class FedCompositeReport {
    private LocalDate reportDate;
    private Double compositeRiskScore;
    private String yieldCurveStatus;
    private Boolean sahmRuleTriggered;
    private String fedPolicyStance;
    // KESİN ÇÖZÜM: Jackson'ın isimlendirme kör noktasını bu mühürle tamamen
    // kırıyoruz!
    private Double treasuryNetLiquidityFlowBillions; // YENİ: Milyar Dolar cinsinden net nakit akışı (Pozitif = Likidite
                                                     // Enjeksiyonu, Negatif = Nakit Çekimi)
    private String botTradingSignal;
    private String macroDetailedSummary;
}
