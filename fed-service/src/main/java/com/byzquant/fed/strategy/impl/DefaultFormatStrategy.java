package com.byzquant.fed.strategy.impl;

import com.byzquant.fed.entity.MacroObservation;
import com.byzquant.fed.strategy.IndicatorFormatStrategy;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DefaultFormatStrategy implements IndicatorFormatStrategy {

    @Override
    public String getSupportedIndicatorId() {
        return "DEFAULT";
    }

    @Override
    public String formatValue(double rawValue, List<MacroObservation> history) {
        return rawValue + ""; // Factory katmanı tarafından ezilecek güvenliFallback
    }

    public String formatCustom(String indicatorId, double rawValue) {
        // 🚀 HARF HATASI BURADA KURUŞU KURUŞUNA DÜZELTİLDİ ŞEF! (Hepsi rawValue oldu)
        return switch (indicatorId) {
            case "RRPONTSYD" -> String.format("%.0f B", rawValue < 10 ? rawValue * 1000 : rawValue);
            case "FEDFUNDS", "MORTGAGE30US", "T10Y2Y", "NFCI", "A191RL1Q225SBEA" -> String.format("%.2f%%", rawValue);
            case "PAYEMS" -> String.format("%.1f M", rawValue / 100.0);
            case "ICSA" -> String.format("%.0f K", rawValue / 1000.0);
            case "WLCFLPCL" -> String.format("$%.2f B", rawValue / 1000.0);
            default -> String.format("%.2f", rawValue);
        };
    }

    public double scaleCustom(String indicatorId, double rawValue) {
        return switch (indicatorId) {
            case "RRPONTSYD" -> rawValue < 10 ? rawValue * 1000 : rawValue;
            case "PAYEMS" -> rawValue / 100.0;
            case "ICSA", "WLCFLPCL" -> rawValue / 1000.0;
            default -> rawValue;
        };
    }

    @Override
    public double scaleChartValue(double rawValue) {
        return rawValue;
    }

    // DefaultFormatStrategy.java sınıfının en altına bu metodu ekle şef:
    @Override
    public int getChartLimit() {
        return 52; // 🚀 Haftalık ve diğer standart veriler için 52 hafta kilitli!
    }

}
