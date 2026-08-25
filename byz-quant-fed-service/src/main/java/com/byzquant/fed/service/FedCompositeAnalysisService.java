package com.byzquant.fed.service;

import com.byzquant.fed.entity.FedObservationEntity;
import com.byzquant.fed.repository.FedObservationRepository;
import com.byzquant.fed.dto.FedCompositeReport;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FedCompositeAnalysisService {

    private final FedObservationRepository observationRepository;

    public FedCompositeReport generateCompositeReport() {
        // 1. Gerekli tüm serilerin ham verilerini hafızaya çek (Hazine 'WDTGAL' dahil
        // edildi)
        List<FedObservationEntity> yieldCurveObs = observationRepository.findBySeriesIdOrderByDateAsc("T10Y2Y");
        List<FedObservationEntity> unrateObs = observationRepository.findBySeriesIdOrderByDateAsc("UNRATE");
        List<FedObservationEntity> fedfundsObs = observationRepository.findBySeriesIdOrderByDateAsc("FEDFUNDS");
        List<FedObservationEntity> tgaObs = observationRepository.findBySeriesIdOrderByDateAsc("WDTGAL");

        if (yieldCurveObs.isEmpty() || unrateObs.isEmpty() || fedfundsObs.isEmpty() || tgaObs.isEmpty()) {
            throw new IllegalStateException(
                    "Kompozit analiz için gerekli serilerin (T10Y2Y, UNRATE, FEDFUNDS, WDTGAL) ham verileri DB'de eksik!");
        }

        double riskScore = 0.0;
        StringBuilder statusSummary = new StringBuilder();

        // --- MATEMATİK 1: YIELD CURVE (TERS GETİRİ) ANALİZİ ---
        FedObservationEntity latestYield = yieldCurveObs.get(yieldCurveObs.size() - 1);
        double currentYieldGap = Double.parseDouble(latestYield.getValue());
        String yieldStatus = "NORMAL";

        if (currentYieldGap < 0) {
            yieldStatus = "INVERTED (Ters Getiri - Kriz Kapıda)";
            riskScore += 35.0;
            statusSummary.append("Tahvil eğrisi TERS dönmüş durumda, bankacılık marjları baskı altında. ");
        } else {
            boolean wasInvertedRecently = yieldCurveObs.stream()
                    .skip(Math.max(0, yieldCurveObs.size() - 6))
                    .anyMatch(o -> Double.parseDouble(o.getValue()) < 0);

            if (wasInvertedRecently && currentYieldGap > 0) {
                yieldStatus = "UNINVERTING (Dikleşme - Kriz Başladı!)";
                riskScore += 45.0;
                statusSummary.append(
                        "Tahvil eğrisi terslikten çıkıyor (Uninverting)! Finansal tarih bu evrede ayı piyasasının başladığını tesciller. ");
            } else {
                statusSummary.append("Tahvil eğrisi sağlıklı ve pozitif dikeyde. ");
            }
        }

        // --- MATEMATİK 2: SAHM RULE (İŞSİZLİK MOMENTUMU) ANALİZİ ---
        boolean sahmTriggered = false;
        if (unrateObs.size() >= 12) {
            double current3MonthMA = unrateObs.stream()
                    .skip(unrateObs.size() - 3)
                    .mapToDouble(o -> Double.parseDouble(o.getValue()))
                    .average()
                    .orElse(0.0);

            double min12Month = unrateObs.stream()
                    .skip(unrateObs.size() - 12)
                    .mapToDouble(o -> Double.parseDouble(o.getValue()))
                    .min()
                    .orElse(0.0);

            if ((current3MonthMA - min12Month) >= 0.50) {
                sahmTriggered = true;
                riskScore += 35.0;
                statusSummary
                        .append("SAHM KURALI TETİKLENDİ! İşsizlikteki ivme resesyonun resmen başladığını onaylıyor. ");
            }
        }

        // --- MATEMATİK 3: HAZİNE TGA LİKİDİTE ŞALTERİ (MİKTARSAL ANALİZ) ---
        FedObservationEntity latestTga = tgaObs.get(tgaObs.size() - 1);
        double currentTga = Double.parseDouble(latestTga.getValue());

        int oneMonthAgoTgaIndex = Math.max(0, tgaObs.size() - 4);
        double pastTga = Double.parseDouble(tgaObs.get(oneMonthAgoTgaIndex).getValue());

        // TGA hesabındaki ham değişim (Milyon Dolar)
        double tgaMonthlyChange = currentTga - pastTga;

        // KESİN ÇÖZÜM: Piyasaya akan net sıcak para miktarı (TGA düşerse piyasaya para
        // akar, bu yüzden eksiyle çarpıyoruz)
        // Milyon dolardan Milyar dolara çevirmek için de 1000'e bölüyoruz
        double netLiquidityFlowBillions = (-tgaMonthlyChange) / 1000.0;

        if (tgaMonthlyChange > 20000.0) {
            riskScore += 15.0;
            statusSummary.append(String.format("UYARI: Hazine piyasadan net %.2f Milyar $ nakit çekti! ",
                    Math.abs(netLiquidityFlowBillions)));
        } else if (tgaMonthlyChange < -20000.0) {
            riskScore = Math.max(0.0, riskScore - 10.0);
            statusSummary.append(String.format("MÜJDE: Hazine piyasaya net %.2f Milyar $ sıcak para pompaladı! ",
                    Math.abs(netLiquidityFlowBillions)));
        } else {
            statusSummary.append("Hazine nakit akışı piyasada nötr dengede. ");
        }

        // --- MATEMATİK 4: PARASAL REJİM VE PANİK KESİNTİSİ CHECK'İ ---
        double currentFedFunds = Double.parseDouble(fedfundsObs.get(fedfundsObs.size() - 1).getValue());
        double pastFedFunds = Double.parseDouble(fedfundsObs.get(Math.max(0, fedfundsObs.size() - 3)).getValue());

        String policyStance = "NEUTRAL (Nötr)";
        if (currentFedFunds < pastFedFunds && (yieldStatus.contains("INVERTED") || sahmTriggered)) {
            policyStance = "PANIC CUTS (Panik Faiz İndirimleri)";
            riskScore += 10.0;
            statusSummary.append("FED, makro hasarı yamamak için acil panik faiz indirim döngüsüne girmiş durumda. ");
        } else if (currentFedFunds > pastFedFunds) {
            policyStance = "TIGHTENING (Sıkılaşma)";
        } else if (currentFedFunds < pastFedFunds) {
            policyStance = "EASING (Gevşeme)";
        }

        // --- NİHAİ TRADE SİNYALİ SEÇİM MOTORU ---
        String botSignal = "RISK-ON (Agresif Alım Modu)";
        riskScore = Math.min(100.0, riskScore); // Maksimum %100 sınırla

        if (riskScore >= 50.0) {
            botSignal = "RISK-OFF (Korumacı Mod: Nakit, Altın, Kısa Vadeli Tahvil)";
        }

        // KESİN ÇÖZÜM: Unutulan o kritik satırı nihayet yerine mıhlıyoruz!
        return FedCompositeReport.builder()
                .reportDate(LocalDate.now())
                .compositeRiskScore(riskScore)
                .yieldCurveStatus(yieldStatus)
                .sahmRuleTriggered(sahmTriggered)
                .fedPolicyStance(policyStance)
                .treasuryNetLiquidityFlowBillions(netLiquidityFlowBillions) // İşte saatlerdir eksik olan o hayat
                                                                            // kurtaran satır!
                .botTradingSignal(botSignal)
                .macroDetailedSummary(statusSummary.toString().trim())
                .build();
    }
}
