export interface ObservationDto {
  realtime_start: string;
  realtime_end: string;
  date: string;
  value: string;
}

export interface EconomicDataResponse {
  realtime_start: string;
  realtime_end: string;
  order_by: string;
  sort_order: string;
  count: number;
  observations: ObservationDto[];
}

export interface ReleaseDateDto {
  release_id: number;
  release_name: string;
  date: string;
}

export interface ReleaseDatesResponse {
  realtime_start: string;
  realtime_end: string;
  order_by: string;
  sort_order: string;
  count: number;
  release_dates: ReleaseDateDto[];
}

export interface Indicator {
  id: string;
  name: string;
  icon: string;
}
