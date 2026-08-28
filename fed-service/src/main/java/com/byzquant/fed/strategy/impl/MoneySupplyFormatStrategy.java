package com.byzquant.fed.strategy.impl;

import com.byzquant.fed.entity.MacroObservation;
import com.byzquant.fed.strategy.IndicatorFormatStrategy;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class MoneySupplyFormatStrategy implements IndicatorFormatStrategy {

    @Override
    public String getSupportedIndicatorId() {
        return "M2SL";
    }

    @Override
    public String formatValue(double rawValue, List<MacroObservation> history) {
        return String.format("%.1f T", rawValue / 1_000.0);
    }

    @Override
    public double scaleChartValue(double rawValue) {
        return rawValue / 1_000.0;
    }

    // DefaultFormatStrategy.java sınıfının en altına bu metodu ekle şef:
    @Override
    public int getChartLimit() {
        return 52; // 🚀 Haftalık ve diğer standart veriler için 52 hafta kilitli!
    }

}
