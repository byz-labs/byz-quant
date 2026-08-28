import React from 'react';
import { Box } from '@mui/material';
import { AdvancedRealTimeChart } from 'react-ts-tradingview-widgets';

interface TradingViewChartProps {
  symbol: string;
}

export default function TradingViewChart({ symbol }: TradingViewChartProps): React.JSX.Element {
  return (
    <Box 
      sx={{ 
        backgroundColor: 'rgba(30, 35, 41, 0.45)',
        backdropFilter: 'blur(12px)',
        WebkitBackdropFilter: 'blur(12px)',
        border: '1px solid rgba(255, 255, 255, 0.08)',
        boxShadow: '0 8px 32px 0 rgba(0, 0, 0, 0.37)',
        borderRadius: '12px',
        padding: '16px',
        width: '100%',
        height: '100%', 
        boxSizing: 'border-box',
        overflow: 'hidden'
      }}
    >
      <AdvancedRealTimeChart
        symbol={symbol}
        theme="dark"
        autosize
        interval="D"
        timezone="Etc/UTC"
        style="1"
        locale="tr"
        hide_side_toolbar={false}
        allow_symbol_change={true}
        studies={[
          'RSI@tv-basicstudies',
          'MASimple@tv-basicstudies'
        ]}
      />
    </Box>
  );
}
