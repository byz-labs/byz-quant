import React from 'react';
import { Box, Typography } from '@mui/material';

const glassStyle = {
  backgroundColor: 'rgba(30, 35, 41, 0.45)',
  backdropFilter: 'blur(12px)',
  WebkitBackdropFilter: 'blur(12px)',
  border: '1px solid rgba(255, 255, 255, 0.08)',
  boxShadow: '0 8px 32px 0 rgba(0, 0, 0, 0.37)',
  padding: '16px 20px',
  borderRadius: '12px',
  display: 'flex',
  justifyContent: 'space-between',
  alignItems: 'center',
  flex: 1,
  minWidth: { xs: '100%', sm: 'calc(50% - 8px)', md: 'calc(25% - 12px)' }
};

export default function CryptoTickerStream(): React.JSX.Element {
  const coins = [
    { pair: 'BTC/USDT', price: '$64,250.00', change: '+2.41%', up: true },
    { pair: 'ETH/USDT', price: '$3,450.25', change: '-0.85%', up: false },
    { pair: 'BNB/USDT', price: '$580.10', change: '+1.15%', up: true },
    { pair: 'SOL/USDT', price: '$145.80', change: '+5.62%', up: true }
  ];

  return (
    <Box sx={{ display: 'flex', gap: '16px', flexWrap: 'wrap', width: '100%' }}>
      {coins.map((coin, i) => (
        <Box key={i} sx={glassStyle}>
          <Box>
            <Typography variant="caption" sx={{ color: '#848e9c', fontWeight: 'bold', display: 'block' }}>
              {coin.pair}
            </Typography>
            <Typography variant="body1" sx={{ fontWeight: 'bold', mt: 0.5, color: '#fff' }}>
              {coin.price}
            </Typography>
          </Box>
          <Typography variant="body2" sx={{ color: coin.up ? '#0ecb81' : '#f6465d', fontWeight: 'bold' }}>
            {coin.change}
          </Typography>
        </Box>
      ))}
    </Box>
  );
}
