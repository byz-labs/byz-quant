import React from 'react';
import { Box, Typography } from '@mui/material';
import TradingViewChart from './TradingViewChart';

export default function SuperChartView(): React.JSX.Element {
  return (
    <Box sx={{ 
      display: 'flex', 
      flexDirection: 'column', 
      gap: 1.5, 
      width: '100%', 
      height: '100%', // Kapsayıcı kutunun yüksekliğini tamamen esnetiyoruz
      boxSizing: 'border-box'
    }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexShrink: 0 }}>
        <Typography variant="h6" sx={{ color: '#f0b90b', fontWeight: 'bold', textTransform: 'uppercase', fontSize: '14px', letterSpacing: '1px' }}>
          📈 Advanced Superchart Terminal
        </Typography>
      </Box>

      {/* Grafiğin kalan tüm dikey alanı sıfır hata ile kaplaması için flexGrow veriyoruz */}
      <Box sx={{ flexGrow: 1, width: '100%', height: '100%', minHeight: 0 }}>
        <TradingViewChart symbol="BINANCE:BTCUSDT" />
      </Box>
    </Box>
  );
}
