import React from 'react';
import { Box, Typography } from '@mui/material';

const sectionPanelStyle = {
  backgroundColor: 'rgba(20, 21, 26, 0.65)',
  border: '1px solid rgba(255, 255, 255, 0.05)',
  borderRadius: '16px',
  padding: '24px',
  width: '100%',
  boxSizing: 'border-box' as const,
  display: 'flex',
  flexDirection: 'column' as const,
  gap: 3
};

interface StrategySectionPanelProps {
  volumeTitle: string;
  subtitle: string;
  tacticText: string;
  isProfit: boolean;
  children: React.ReactNode;
}

export default function StrategySectionPanel({ volumeTitle, subtitle, tacticText, isProfit, children }: StrategySectionPanelProps): React.JSX.Element {
  return (
    <Box sx={sectionPanelStyle}>
      <Box>
        <Typography variant="h6" sx={{ color: '#f0b90b', fontWeight: 'bold', letterSpacing: '0.5px' }}>
          {volumeTitle}
        </Typography>
        <Typography variant="caption" sx={{ color: '#848e9c' }}>{subtitle}</Typography>
      </Box>

      <Box sx={{
        backgroundColor: isProfit ? 'rgba(14, 203, 129, 0.06)' : 'rgba(246, 70, 93, 0.06)',
        borderLeft: `4px solid ${isProfit ? '#0ecb81' : '#f6465d'}`,
        padding: '12px 16px',
        borderRadius: '4px',
        mb: 1
      }}>
        <Typography variant="caption" sx={{ color: '#eaecef', fontWeight: 'bold', display: 'block' }}>STRATEJİK TAKTİK MATRİSİ:</Typography>
        <Typography variant="caption" sx={{ color: '#848e9c', fontSize: '11px' }}>{tacticText}</Typography>
      </Box>

      {children}
    </Box>
  );
}
