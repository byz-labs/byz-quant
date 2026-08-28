import React from 'react';
import { Box, Typography } from '@mui/material';
import type { ReleaseDateDto } from '../types/economic';

interface MacroCalendarCardProps {
  calendarData: ReleaseDateDto[] | null;
  isLoading: boolean;
}

export default function MacroCalendarCard({ calendarData, isLoading }: MacroCalendarCardProps): React.JSX.Element {
  const upcomingReleases = calendarData ? calendarData.slice(0, 4) : [];

  return (
    <Box sx={{
      backgroundColor: 'rgba(30, 35, 41, 0.45)',
      backdropFilter: 'blur(12px)',
      WebkitBackdropFilter: 'blur(12px)',
      border: '1px solid rgba(255, 255, 255, 0.08)',
      boxShadow: '0 8px 32px 0 rgba(0, 0, 0, 0.37)',
      padding: '24px',
      borderRadius: '12px',
      width: '100%'
    }}>
      <Typography variant="subtitle2" sx={{ color: '#f0b90b', fontWeight: 'bold', mb: 3, textTransform: 'uppercase' }}>
        🗓️ Gelecek Fed Yayın Planı
      </Typography>
      
      {isLoading ? (
        <Typography sx={{ color: '#848e9c' }}>Takvim güncelleniyor...</Typography>
      ) : (
        <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
          {upcomingReleases.length > 0 ? upcomingReleases.map((cal: ReleaseDateDto, idx: number) => (
            <Box key={idx} sx={{ 
              padding: '14px', 
              backgroundColor: 'rgba(0,0,0,0.15)', 
              borderRadius: '6px', 
              borderLeft: '4px solid #f0b90b' 
            }}>
              <Typography variant="body2" sx={{ fontWeight: '600', color: '#fff' }}>
                {cal.release_name}
              </Typography>
              <Typography variant="caption" sx={{ color: '#0ecb81', mt: 0.5, display: 'block', fontWeight: 'bold' }}>
                Planlanan Tarih: {cal.date}
              </Typography>
            </Box>
          )) : (
            <Typography variant="body2" sx={{ color: '#848e9c' }}>Planlanmış yeni yayın bulunmuyor.</Typography>
          )}
        </Box>
      )}
    </Box>
  );
}
