package com.byzquant.fed.strategy;

import com.byzquant.fed.entity.MacroObservation;
import java.util.List;

public interface IndicatorFormatStrategy {
    String getSupportedIndicatorId();
    String formatValue(double rawValue, List<MacroObservation> history);
    double scaleChartValue(double rawValue);
    
    // 🚀 SİKİMTRAK LİMİTLERİ SİKEN YENİ METOT: Her gösterge grafikte kaç mum basacağını kendi söyler!
    int getChartLimit();
}
