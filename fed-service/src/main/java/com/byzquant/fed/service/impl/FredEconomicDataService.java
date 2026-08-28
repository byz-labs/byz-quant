package com.byzquant.fed.service.impl;

import com.byzquant.fed.client.EconomicDataClient;
import com.byzquant.fed.dto.EconomicDataResponse;
import com.byzquant.fed.dto.ReleaseDatesResponse;
import com.byzquant.fed.service.EconomicDataService;
import org.springframework.stereotype.Service;

@Service
public class FredEconomicDataService implements EconomicDataService {

    private final EconomicDataClient economicDataClient;

    public FredEconomicDataService(EconomicDataClient economicDataClient) {
        this.economicDataClient = economicDataClient;
    }

    @Override
    public EconomicDataResponse getIndicatorData(String seriesId) {
        // Jenerik metodu Series DTO'su ile tetikliyoruz
        return economicDataClient.fetch(
                "/series/observations?series_id={id}&sort_order=desc&limit=10",
                EconomicDataResponse.class,
                seriesId);
    }

    // Gelecekte ekleyeceğimiz takvim endpoint'i için hazır altyapı:
    @Override
    public ReleaseDatesResponse getReleaseCalendar() {
        // realtime_start parametresi ile geçmişi tamamen çöpe atıp bugünden sonrasını
        // istiyoruz
        return economicDataClient.fetch(
                "/releases/dates?include_release_dates_with_no_data=true&sort_order=asc&realtime_start=2026-08-27&limit=10",
                ReleaseDatesResponse.class);
    }

}
