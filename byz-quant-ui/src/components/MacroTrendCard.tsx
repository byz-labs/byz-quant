import React from 'react';
import { Box, Typography } from '@mui/material';
import { ResponsiveContainer, AreaChart, Area, XAxis, YAxis, Tooltip } from 'recharts';
import type { ObservationDto } from '../types/economic';

interface MacroTrendCardProps {
  title: string;
  hookResult: { indicatorData: ObservationDto[] | null; isLoading: boolean };
  color: string;
  unit: string;
}

export default function MacroTrendCard({ title, hookResult, color, unit }: MacroTrendCardProps): React.JSX.Element {
  const rawData = hookResult.indicatorData || [];
  
  const actual = rawData.length > 0 ? parseFloat(rawData[0].value).toFixed(2) : '-';
  const previous = rawData.length > 1 ? parseFloat(rawData[1].value).toFixed(2) : '-';
  const date = rawData.length > 0 ? rawData[0].date : '-';
  
  const chartData = [...rawData]
    .reverse()
    .filter((d: ObservationDto) => d.value !== '.')
    .map((d: ObservationDto) => ({
      tarih: d.date.substring(5, 10),
      deger: parseFloat(parseFloat(d.value).toFixed(2))
    }));

  return (
    <Box sx={{
      backgroundColor: 'rgba(30, 35, 41, 0.45)',
      backdropFilter: 'blur(12px)',
      WebkitBackdropFilter: 'blur(12px)',
      border: '1px solid rgba(255, 255, 255, 0.08)',
      boxShadow: '0 8px 32px 0 rgba(0, 0, 0, 0.37)',
      padding: '24px',
      borderRadius: '12px',
      flex: '1 1 calc(33.33% - 16px)',
      minWidth: '300px',
      display: 'flex',
      flexDirection: 'column',
      gap: 2,
      boxSizing: 'border-box'
    }}>
      <Typography variant="subtitle2" sx={{ color: '#f0b90b', fontWeight: 'bold' }}>{title}</Typography>
      
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', backgroundColor: 'rgba(0,0,0,0.15)', p: 2, borderRadius: '8px' }}>
        <Box>
          <Typography variant="caption" sx={{ color: '#848e9c', display: 'block' }}>Son (Actual)</Typography>
          <Typography variant="h4" sx={{ fontWeight: 'bold', color: color }}>{actual}{unit}</Typography>
        </Box>
        <Box sx={{ textAlign: 'right' }}>
          <Typography variant="caption" sx={{ color: '#848e9c', display: 'block' }}>Önceki (Previous)</Typography>
          <Typography variant="h6" sx={{ fontWeight: 'bold', color: '#848e9c' }}>{previous}{unit}</Typography>
        </Box>
      </Box>

      {/* Recharts Alanı */}
      {chartData.length > 0 && (
        <Box sx={{ width: '100%', height: 100, mt: 1 }}>
          <ResponsiveContainer width="100%" height="100%">
            <AreaChart data={chartData} margin={{ top: 5, right: 5, left: 5, bottom: 5 }}>
              <XAxis dataKey="tarih" hide />
              <YAxis hide domain={['dataMin - 0.5', 'dataMax + 0.5']} />
              <Tooltip 
                contentStyle={{ backgroundColor: '#1e2329', borderColor: 'rgba(255,255,255,0.1)', borderRadius: '6px', color: '#fff', fontSize: '12px' }}
                itemStyle={{ color: color }}
                labelStyle={{ color: '#848e9c' }}
              />
              <defs>
                <linearGradient id={`grad-${color}`} x1="0" y1="0" x2="0" y2="1">
                  <stop offset="5%" stopColor={color} stopOpacity={0.4}/>
                  <stop offset="95%" stopColor={color} stopOpacity={0.0}/>
                </linearGradient>
              </defs>
              <Area type="monotone" dataKey="deger" stroke={color} strokeWidth={2} fillOpacity={1} fill={`url(#grad-${color})`} />
            </AreaChart>
          </ResponsiveContainer>
        </Box>
      )}

      <Typography variant="caption" sx={{ color: '#848e9c', display: 'block', mt: 0.5 }}>Son Güncelleme: {date}</Typography>
    </Box>
  );
}
