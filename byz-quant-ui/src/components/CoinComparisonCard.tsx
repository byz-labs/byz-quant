import React, { useState } from 'react';
import { Box, Typography, ToggleButton, ToggleButtonGroup, Button } from '@mui/material';
import { ResponsiveContainer, LineChart, Line, Brush, CartesianGrid, XAxis, YAxis, Tooltip, Legend } from 'recharts';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';

const glassStyle = {
  backgroundColor: 'rgba(30, 35, 41, 0.45)',
  backdropFilter: 'blur(12px)',
  WebkitBackdropFilter: 'blur(12px)',
  border: '1px solid rgba(255, 255, 255, 0.08)',
  boxShadow: '0 8px 32px 0 rgba(0, 0, 0, 0.37)',
  padding: '24px',
  borderRadius: '12px',
  width: '100%',
  boxSizing: 'border-box' as const
};

const MULTI_COIN_DATA = [
  { date: '1. Jun', PEPE: 15, XRP: 10, SOL: 5, RENDER: -5, TRUMP: 20 },
  { date: '8. Jun', PEPE: 12, XRP: 5, SOL: -2, RENDER: -15, TRUMP: 10 },
  { date: '15. Jun', PEPE: 25, XRP: 8, SOL: 12, RENDER: -10, TRUMP: 35 },
  { date: '22. Jun', PEPE: 5, XRP: -12, SOL: -5, RENDER: -25, TRUMP: -10 },
  { date: '29. Jun', PEPE: -10, XRP: -15, SOL: -8, RENDER: -30, TRUMP: -15 },
  { date: '6. Jul', PEPE: 2, XRP: -5, SOL: 5, RENDER: -20, TRUMP: 5 },
  { date: '13. Jul', PEPE: -5, XRP: -8, SOL: 0, RENDER: -22, TRUMP: -5 },
  { date: '20. Jul', PEPE: -8, XRP: -10, SOL: -4, RENDER: -28, TRUMP: -12 },
  { date: '27. Jul', PEPE: -15, XRP: -18, SOL: -10, RENDER: -35, TRUMP: -20 },
  { date: '3. Aug', PEPE: -12, XRP: -14, SOL: -6, RENDER: -32, TRUMP: -10 },
  { date: '10. Aug', PEPE: -2, XRP: -2, SOL: 5, RENDER: -22, TRUMP: 5 },
  { date: '17. Aug', PEPE: 10, XRP: 5, SOL: 15, RENDER: -12, TRUMP: 22 },
  { date: '24. Aug', PEPE: 30, XRP: 18, SOL: 28, RENDER: -8, TRUMP: 68 },
  { date: '27. Aug', PEPE: 22, XRP: 12, SOL: 20, RENDER: -14, TRUMP: 45 }
];

export default function CoinComparisonCard(): React.JSX.Element {
  const [coinTimeFrame, setCoinTimeFrame] = useState<string>('3M');

  return (
    <Box sx={glassStyle}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 2, mb: 3 }}>
        <Box>
          <Typography variant="subtitle2" sx={{ color: '#f0b90b', fontWeight: 'bold' }}>COIN PERFORMANCE COMPARISON</Typography>
          <Typography variant="caption" sx={{ color: '#848e9c' }}>Relative Yüzdelik Getiri Analiz Matrisi (% Change)</Typography>
        </Box>

        <Box sx={{ display: 'flex', gap: 1.5, alignItems: 'center' }}>
          <Button variant="contained" endIcon={<KeyboardArrowDownIcon />} sx={{ backgroundColor: 'rgba(255,255,255,0.03)', color: '#fff', border: '1px solid rgba(255,255,255,0.08)', textTransform: 'none', fontSize: '12px', '&:hover': { backgroundColor: 'rgba(255,255,255,0.06)' } }}>
            5 Coins
          </Button>
          <Button variant="contained" sx={{ backgroundColor: 'rgba(255,255,255,0.03)', color: '#fff', border: '1px solid rgba(255,255,255,0.08)', textTransform: 'none', fontSize: '12px', '&:hover': { backgroundColor: 'rgba(255,255,255,0.06)' } }}>
            Price
          </Button>
          
          <ToggleButtonGroup
            value={coinTimeFrame}
            exclusive
            onChange={(_e, v) => v && setCoinTimeFrame(v)}
            size="small"
            sx={{ backgroundColor: 'rgba(0,0,0,0.2)', border: '1px solid rgba(255,255,255,0.05)', '& .MuiToggleButton-root': { color: '#848e9c', border: 'none', px: 1.5, py: 0.5, fontSize: '11px', fontWeight: 'bold', '&.Mui-selected': { color: '#000', backgroundColor: '#f0b90b' } } }}
          >
            {['24H', '7D', '1M', '3M', '1Y'].map((tf) => (
              <ToggleButton key={tf} value={tf}>{tf}</ToggleButton>
            ))}
          </ToggleButtonGroup>
        </Box>
      </Box>

      <Box sx={{ width: '100%', height: 350 }}>
        <ResponsiveContainer width="100%" height="100%">
          <LineChart data={MULTI_COIN_DATA} margin={{ top: 10, right: 10, left: -20, bottom: 0 }}>
            <CartesianGrid stroke="rgba(255,255,255,0.03)" vertical={false} />
            <XAxis dataKey="date" stroke="#5d656e" style={{ fontSize: '10px' }} tickLine={false} />
            <YAxis stroke="#5d656e" style={{ fontSize: '10px' }} tickFormatter={(tick) => `${tick}%`} tickLine={false} />
            <Tooltip 
              formatter={(val: any, name: any) => [`${val}%`, name]}
              contentStyle={{ backgroundColor: '#1e2329', borderColor: 'rgba(255,255,255,0.1)', borderRadius: '6px', color: '#fff', fontSize: '12px' }}
            />
            <Legend formatter={(value) => <span style={{ color: '#848e9c', fontSize: '12px', fontWeight: '600' }}>● {value}</span>} />
            
            <Line type="monotone" dataKey="PEPE" stroke="#e67e22" strokeWidth={2} dot={false} activeDot={{ r: 4 }} />
            <Line type="monotone" dataKey="SOL" stroke="#f1c40f" strokeWidth={2} dot={false} activeDot={{ r: 4 }} />
            <Line type="monotone" dataKey="XRP" stroke="#e74c3c" strokeWidth={2} dot={false} activeDot={{ r: 4 }} />
            <Line type="monotone" dataKey="RENDER" stroke="#9b59b6" strokeWidth={2} dot={false} activeDot={{ r: 4 }} />
            <Line type="monotone" dataKey="TRUMP" stroke="#0ecb81" strokeWidth={2} dot={false} activeDot={{ r: 4 }} />

            <Brush dataKey="date" height={20} stroke="#2f3336" fill="#14151a" travellerWidth={10} style={{ fontSize: '10px', color: '#848e9c' }} />
          </LineChart>
        </ResponsiveContainer>
      </Box>
    </Box>
  );
}
