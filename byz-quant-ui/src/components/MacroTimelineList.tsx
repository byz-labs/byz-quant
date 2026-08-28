import React from 'react';
import { Box, Typography } from '@mui/material';
import type { ReleaseDateDto } from '../types/economic';

interface MacroTimelineListProps {
  calendarData: ReleaseDateDto[] | null;
  isLoading: boolean;
}

export default function MacroTimelineList({ calendarData, isLoading }: MacroTimelineListProps): React.JSX.Element {
  const upcomingReleases = calendarData ? calendarData.slice(0, 6) : [];

  return (
    <Box sx={{
      backgroundColor: 'rgba(30, 35, 41, 0.45)',
      backdropFilter: 'blur(12px)',
      WebkitBackdropFilter: 'blur(12px)',
      border: '1px solid rgba(255, 255, 255, 0.08)',
      boxShadow: '0 8px 32px 0 rgba(0, 0, 0, 0.37)',
      padding: '24px',
      borderRadius: '12px',
      width: '100%',
      boxSizing: 'border-box',
      display: 'flex',
      flexDirection: 'column'
    }}>
      <Typography variant="subtitle2" sx={{ color: '#f0b90b', fontWeight: 'bold', mb: 3, textTransform: 'uppercase', letterSpacing: '1px' }}>
        🗓️ Küresel Ekonomik Takvim & Yayın Zaman Çizelgesi (Timeline)
      </Typography>

      {isLoading ? (
        <Typography sx={{ color: '#848e9c' }}>Takvim güncelleniyor...</Typography>
      ) : (
        <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1.5 }}>
          {upcomingReleases.map((cal: ReleaseDateDto, idx: number) => (
            <Box 
              key={idx} 
              sx={{ 
                display: 'flex', 
                flexDirection: { xs: 'column', sm: 'row' },
                justifyContent: 'space-between', 
                alignItems: { xs: 'flex-start', sm: 'center' },
                padding: '16px 20px', 
                backgroundColor: 'rgba(0, 0, 0, 0.2)', 
                borderRadius: '8px', 
                borderLeft: '4px solid #f0b90b',
                gap: { xs: 1, sm: 0 },
                '&:hover': { backgroundColor: 'rgba(255,255,255,0.02)' }
              }}
            >
              <Box>
                <Typography variant="body2" sx={{ fontWeight: '600', color: '#fff' }}>
                  {cal.release_name}
                </Typography>
                <Typography variant="caption" sx={{ color: '#848e9c' }}>
                  ID: #{cal.release_id} • United States Macro Statistics
                </Typography>
              </Box>
              <Box sx={{ backgroundColor: 'rgba(14, 203, 129, 0.1)', padding: '6px 12px', borderRadius: '4px' }}>
                <Typography variant="caption" sx={{ color: '#0ecb81', fontWeight: 'bold' }}>
                  Yayın Tarihi: {cal.date}
                </Typography>
              </Box>
            </Box>
          ))}
        </Box>
      )}
    </Box>
  );
}
