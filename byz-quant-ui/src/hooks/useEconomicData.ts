import { useState, useEffect, useCallback } from 'react';
import type { ObservationDto, ReleaseDateDto, EconomicDataResponse, ReleaseDatesResponse } from '../types/economic';

const BASE_URL = '/api/v1/economic';

interface LoadingState {
  indicator: boolean;
  calendar: boolean;
}

export function useEconomicData(defaultSeriesId: string = 'FEDFUNDS') {
  const [seriesId, setSeriesId] = useState<string>(defaultSeriesId);
  const [indicatorData, setIndicatorData] = useState<ObservationDto[] | null>(null);
  const [calendarData, setCalendarData] = useState<ReleaseDateDto[] | null>(null);
  const [loading, setLoading] = useState<LoadingState>({ indicator: false, calendar: false });
  const [error, setError] = useState<string | null>(null);

  const fetchCalendar = useCallback(async (): Promise<void> => {
    setLoading(prev => ({ ...prev, calendar: true }));
    try {
      const response = await fetch(`${BASE_URL}/calendar`);
      if (!response.ok) throw new Error('Takvim verisi alınamadı');
      const data: ReleaseDatesResponse = await response.json();
      setCalendarData(data.release_dates || []);
    } catch (err) {
      if (err instanceof Error) setError(err.message);
    } finally {
      setLoading(prev => ({ ...prev, calendar: false }));
    }
  }, []);

  const fetchIndicator = useCallback(async (id: string): Promise<void> => {
    setLoading(prev => ({ ...prev, indicator: true }));
    try {
      const response = await fetch(`${BASE_URL}/series/${id}`);
      if (!response.ok) throw new Error('Gösterge verisi alınamadı');
      const data: EconomicDataResponse = await response.json();
      setIndicatorData(data.observations || []);
    } catch (err) {
      if (err instanceof Error) setError(err.message);
    } finally {
      setLoading(prev => ({ ...prev, indicator: false }));
    }
  }, []);

  useEffect(() => {
    fetchCalendar();
  }, [fetchCalendar]);

  useEffect(() => {
    fetchIndicator(seriesId);
  }, [seriesId, fetchIndicator]);

  return {
    seriesId,
    setSeriesId,
    indicatorData,
    calendarData,
    isLoading: loading.indicator || loading.calendar,
    error
  };
}
