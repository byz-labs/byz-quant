package com.byzquant.fed.service;

import com.byzquant.fed.dto.MacroRiskReport;
import com.byzquant.fed.entity.MacroObservation;
import com.byzquant.fed.repository.MacroObservationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Service
public class MacroRiskEngine {

    private final MacroObservationRepository observationRepository;

    public MacroRiskEngine(MacroObservationRepository observationRepository) {
        this.observationRepository = observationRepository;
    }

    public MacroRiskReport calculateSystemicRisk() {
        int totalScore = 0;
        LocalDate tenYearsAgo = LocalDate.now().minusYears(10);

        // 1. FED BİLANÇOSU KONTROLÜ (WALCL)
        List<MacroObservation> walclData = observationRepository.findTop2ByIndicatorIdOrderByObservationDateDesc("WALCL");
        if (walclData.size() >= 2) {
            BigDecimal current = walclData.get(0).getObservationValue();
            BigDecimal previous = walclData.get(1).getObservationValue();
            if (current.compareTo(previous) < 0) totalScore -= 1; // Kevin Warsh QT yapıyor, cüzdanı sıkıyor!
            else totalScore += 1;
        }

        // 2. TAHVİL MAKASI KONTROLÜ - KIYAMET ALAMETİ (T10Y2Y)
        List<MacroObservation> yieldCurve = observationRepository.findTop2ByIndicatorIdOrderByObservationDateDesc("T10Y2Y");
        if (!yieldCurve.isEmpty()) {
            BigDecimal current = yieldCurve.get(0).getObservationValue();
            if (current.compareTo(BigDecimal.ZERO) < 0) {
                totalScore -= 3; // Sıfırın altındaysa direkt resesyon cezası çak!
            } else if (yieldCurve.size() >= 2) {
                BigDecimal previous = yieldCurve.get(1).getObservationValue();
                // 🚀 DÜN GECE BAHSETTİĞİN "DÜZELİRKEN KAÇ" TUZAĞI (Un-inversion Kontrolü)
                if (previous.compareTo(BigDecimal.ZERO) < 0 && current.compareTo(BigDecimal.ZERO) >= 0) {
                    totalScore -= 5; // En tehlikeli an! Kriz patladı sinyali!
                }
            }
        }

        // 3. TARİHSEL REKOR (SON 10 YIL) KONTROLLERİ - FAİZ ÖRNEĞİ (FEDFUNDS)
        List<MacroObservation> fedFunds = observationRepository.findTop2ByIndicatorIdOrderByObservationDateDesc("FEDFUNDS");
        if (!fedFunds.isEmpty()) {
            BigDecimal currentFaiz = fedFunds.get(0).getObservationValue();
            Object[] extremumsArray = observationRepository.getHistoricalExtremums("FEDFUNDS", tenYearsAgo);
            if (extremumsArray != null && extremumsArray.length > 0 && extremumsArray[0] != null) {
                Object[] extremums = (Object[]) extremumsArray[0];
                BigDecimal max10Years = (BigDecimal) extremums[0];
                if (currentFaiz.compareTo(max10Years) >= 0) {
                    totalScore -= 3; // Faiz son 10 yılın zirvesindeyse piyasayı domaltırlar!
                }
            }
        }

        // 🚀 NİHAİ SKORLAMA VE SENİN ÜSLUBUNLA TAVSİYE MOTORU
        String advice;
        String color;

        if (totalScore >= 5) {
            color = "#0ecb81"; // Parlak Yeşil
            advice = "RİSKE ABAN KOÇUM! Scott Bessent piyasayı paraya boğuyor, Kevin Warsh faizleri istese de artıramıyor. Likidite mermi gibi akıyor, arkana yaslan ralliye eşlik et!";
        } else if (totalScore <= -3) {
            color = "#f6465d"; // Kıpkırmızı
            advice = "RİSKTEN UZAK DUR, GİRMESİN ZIMPARALI DİLDO! Kevin Warsh piyasayı kurutuyor, işsizlik başvuruları tırmanışta, faizler zirvede. Nakite geç, canını kurtar!";
        } else {
            color = "#f0b90b"; // Altın Sarısı
            advice = "SAKİN OL ŞEF! Piyasa testere modunda. Göstergeler bir içeri bir dışarı gidiyor. Scott Bessent bastırıyor ama Kevin Warsh direniyor. Kaldıracı kıs, bodoslama atlama, trendin kırılmasını bekle.";
        }

        return new MacroRiskReport(totalScore, advice, color, LocalDate.now());
    }
}
