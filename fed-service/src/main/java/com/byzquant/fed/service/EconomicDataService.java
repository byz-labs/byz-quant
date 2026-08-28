package com.byzquant.fed.service;

import com.byzquant.fed.dto.EconomicDataResponse;
import com.byzquant.fed.dto.ReleaseDatesResponse;

public interface EconomicDataService {
    EconomicDataResponse getIndicatorData(String seriesId);
    ReleaseDatesResponse getReleaseCalendar();
}
