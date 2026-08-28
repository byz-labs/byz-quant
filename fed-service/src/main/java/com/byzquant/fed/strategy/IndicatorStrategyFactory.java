package com.byzquant.fed.strategy;

import com.byzquant.fed.entity.MacroObservation;
import com.byzquant.fed.strategy.impl.DefaultFormatStrategy;
import com.byzquant.fed.strategy.impl.InflationFormatStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class IndicatorStrategyFactory {

    private final Map<String, IndicatorFormatStrategy> strategies = new HashMap<>();
    private final DefaultFormatStrategy defaultStrategy;
    private final InflationFormatStrategy inflationStrategy;

    public IndicatorStrategyFactory(List<IndicatorFormatStrategy> strategyList, 
                                    DefaultFormatStrategy defaultStrategy,
                                    InflationFormatStrategy inflationStrategy) {
        this.defaultStrategy = defaultStrategy;
        this.inflationStrategy = inflationStrategy;
        
        // Bahar (Spring) bağlamından gelen tüm somut stratejileri haritaya mühürlüyoruz şef
        strategyList.forEach(s -> {
            if (s.getSupportedIndicatorId() != null) {
                strategies.put(s.getSupportedIndicatorId().toUpperCase(), s);
            }
        });
    }

    /**
     * 🚀 POLİMORFİZMİN HAS KRALI: Sıfır switch-case, doğrudan akıllı nesneyi fırlatır!
     */
    public IndicatorFormatStrategy getStrategy(String indicatorId) {
        String cleanId = indicatorId.trim().toUpperCase();
        
        if (strategies.containsKey(cleanId)) {
            return strategies.get(cleanId);
        }
        
        if ("CPILFESL".equals(cleanId) || "PCEPI".equals(cleanId)) {
            return inflationStrategy;
        }
        
        // 🚀 SİHİRLİ DÜZELTME: Anonim sınıf kontratındaki EKSİK GETCHARTLIMIT soyut metodu milimetrik dolduruldu şef!
        return new IndicatorFormatStrategy() {
            @Override
            public String getSupportedIndicatorId() { 
                return cleanId; 
            }
            
            @Override
            public String formatValue(double rawValue, List<MacroObservation> history) {
                return defaultStrategy.formatCustom(cleanId, rawValue);
            }
            
            @Override
            public double scaleChartValue(double rawValue) {
                return defaultStrategy.scaleCustom(cleanId, rawValue);
            }

            @Override
            public int getChartLimit() {
                // Varsayılan olarak haftalıkların o asil 52 haftalık limitini basar şef!
                return defaultStrategy.getChartLimit();
            }
        };
    }
}
