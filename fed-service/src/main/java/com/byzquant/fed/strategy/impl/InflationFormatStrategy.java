package com.byzquant.fed.strategy.impl;

import com.byzquant.fed.entity.MacroObservation;
import com.byzquant.fed.strategy.IndicatorFormatStrategy;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class InflationFormatStrategy implements IndicatorFormatStrategy {

    @Override
    public String getSupportedIndicatorId() {
        return "INFLATION_GENERIC";
    }

    @Override
    public String formatValue(double rawValue, List<MacroObservation> history) {
        // 🚀 FRED'den gelen taze yıllık %3.34 veya %3.10 değerini kurumsal yüzde nizamıyla doğrudan üfle şef!
        return String.format("%.2f%%", rawValue);
    }

    @Override
    public double scaleChartValue(double rawValue) {
        return rawValue; // Grafik çizgisi de yüzde oran trendi olarak aksın şef!
    }

    @Override
    public int getChartLimit() {
        return 24;
    }
}
