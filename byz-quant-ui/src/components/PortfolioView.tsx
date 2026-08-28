import React, { useState } from 'react';
import { Box, Typography, ToggleButton, ToggleButtonGroup } from '@mui/material';
import { ResponsiveContainer, PieChart, Pie, Cell, Tooltip, AreaChart, Area, XAxis, YAxis } from 'recharts';
import CoinComparisonCard from './CoinComparisonCard'; // 🚀 Yeni atomik grafiğimizi içeri alıyoruz!

const glassStyle = {
  backgroundColor: 'rgba(30, 35, 41, 0.45)',
  backdropFilter: 'blur(12px)',
  WebkitBackdropFilter: 'blur(12px)',
  border: '1px solid rgba(255, 255, 255, 0.08)',
  boxShadow: '0 8px 32px 0 rgba(0, 0, 0, 0.37)',
  padding: '24px',
  borderRadius: '12px',
  boxSizing: 'border-box' as const,
  width: '100%'
};

const HOLDINGS_DATA = [
  { name: 'PEPE', value: 14815.39, percentage: '35.6%', color: '#e67e22', amount: '14,815.39 $' },
  { name: 'SOL', value: 11394.30, percentage: '27.4%', color: '#f1c40f', amount: '11,394.30 $' },
  { name: 'XRP', value: 8110.15, percentage: '19.5%', color: '#e74c3c', amount: '8,110.15 $' },
  { name: 'RENDER', value: 2496.00, percentage: '6.0%', color: '#9b59b6', amount: '2,496.00 $' },
  { name: 'TRUMP', value: 1789.20, percentage: '4.3%', color: '#34495e', amount: '1,789.20 $' },
  { name: 'S', value: 1581.20, percentage: '3.8%', color: '#16a085', amount: '1,581.20 $' },
  { name: 'FET', value: 1413.40, percentage: '3.4%', color: '#c0392b', amount: '1,413.40 $' }
];

const PERFORMANCE_DATA = [
  { time: '04:00', bakiye: 40800 },
  { time: '08:00', bakiye: 41200 },
  { time: '12:00', bakiye: 40950 },
  { time: '16:00', bakiye: 40500 },
  { time: '20:00', bakiye: 41400 },
  { time: '24h', bakiye: 41651.30 }
];

export default function PortfolioView(): React.JSX.Element {
  const [timeFrame, setTimeFrame] = useState<string>('24H');
  const totalBalance = 41651.30;

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3, width: '100%' }}>
      {/* 4'lü Metrik Şeridi */}
      <Box sx={{ display: 'flex', gap: '16px', flexWrap: 'wrap', width: '100%' }}>
        <Box sx={{ ...glassStyle, flex: '1 1 calc(25% - 12px)', minWidth: '220px', p: 2 }}>
          <Typography variant="caption" sx={{ color: '#848e9c', textTransform: 'uppercase', fontWeight: 'bold' }}>Current Balance</Typography>
          <Typography variant="h5" sx={{ fontWeight: 'bold', color: '#fff', mt: 1 }}>${totalBalance.toLocaleString()}</Typography>
        </Box>
        <Box sx={{ ...glassStyle, flex: '1 1 calc(25% - 12px)', minWidth: '220px', p: 2 }}>
          <Typography variant="caption" sx={{ color: '#848e9c', textTransform: 'uppercase', fontWeight: 'bold' }}>24h Portfolio Change</Typography>
          <Typography variant="h5" sx={{ fontWeight: 'bold', color: '#0ecb81', mt: 1 }}>+$958.15 <span style={{ fontSize: '14px', fontWeight: 'normal' }}>▲ 2.4%</span></Typography>
        </Box>
        <Box sx={{ ...glassStyle, flex: '1 1 calc(25% - 12px)', minWidth: '220px', p: 2 }}>
          <Typography variant="caption" sx={{ color: '#848e9c', textTransform: 'uppercase', fontWeight: 'bold' }}>Total Profit / Loss</Typography>
          <Typography variant="h5" sx={{ fontWeight: 'bold', color: '#f6465d', mt: 1 }}>-$28,607.50 <span style={{ fontSize: '14px', fontWeight: 'normal' }}>▼ 40.7%</span></Typography>
        </Box>
        <Box sx={{ ...glassStyle, flex: '1 1 calc(25% - 12px)', minWidth: '220px', p: 2 }}>
          <Typography variant="caption" sx={{ color: '#848e9c', textTransform: 'uppercase', fontWeight: 'bold' }}>Top Performer 24h</Typography>
          <Typography variant="h5" sx={{ fontWeight: 'bold', color: '#0ecb81', mt: 1, fontSize: '18px' }}>Solana SOL (+$528.68)</Typography>
        </Box>
      </Box>

      {/* Orta İkili Panel Gövdesi */}
      <Box sx={{ display: 'flex', flexDirection: { xs: 'column', lg: 'row' }, gap: '24px', width: '100%', alignItems: 'stretch' }}>
        
        {/* Sol Cam Kart: Holdings */}
        <Box sx={{ ...glassStyle, flex: 1, minWidth: '350px' }}>
          <Typography variant="subtitle2" sx={{ color: '#f0b90b', fontWeight: 'bold', mb: 3 }}>TOTAL HOLDINGS</Typography>
          <Box sx={{ display: 'flex', flexDirection: { xs: 'column', sm: 'row' }, alignItems: 'center', gap: 2 }}>
            <Box sx={{ width: 180, height: 180, position: 'relative', flexShrink: 0 }}>
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie data={HOLDINGS_DATA} cx="50%" cy="50%" innerRadius={55} outerRadius={80} paddingAngle={3} dataKey="value">
                    {HOLDINGS_DATA.map((entry, index) => <Cell key={`cell-${index}`} fill={entry.color} />)}
                  </Pie>
                  <Tooltip formatter={(val: any) => [`$${parseFloat(val).toLocaleString()}`, 'Değer']} />
                </PieChart>
              </ResponsiveContainer>
              <Box sx={{ position: 'absolute', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', textAlign: 'center' }}>
                <Typography variant="caption" sx={{ color: '#848e9c', display: 'block', fontSize: '10px' }}>PEPE</Typography>
                <Typography variant="body2" sx={{ fontWeight: 'bold', color: '#fff', fontSize: '12px' }}>$14,815.39</Typography>
                <Typography variant="caption" sx={{ color: '#e67e22', display: 'block', fontSize: '11px', fontWeight: 'bold' }}>35.6%</Typography>
              </Box>
            </Box>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1, width: '100%', maxHeight: '200px', overflowY: 'auto', pr: 1 }}>
              {HOLDINGS_DATA.map((coin, idx) => (
                <Box key={idx} sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', width: '100%' }}>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                    <Box sx={{ width: 8, height: 8, borderRadius: '50%', backgroundColor: coin.color }} />
                    <Typography variant="body2" sx={{ fontSize: '12px', fontWeight: '500', color: '#eaecef' }}>{coin.name}</Typography>
                  </Box>
                  <Box sx={{ display: 'flex', gap: 2, alignItems: 'center' }}>
                    <Typography variant="caption" sx={{ fontSize: '11px', color: '#848e9c' }}>{coin.amount}</Typography>
                    <Typography variant="caption" sx={{ fontSize: '11px', fontWeight: 'bold', color: '#fff', width: '40px', textAlign: 'right' }}>{coin.percentage}</Typography>
                  </Box>
                </Box>
              ))}
            </Box>
          </Box>
        </Box>

        {/* Sağ Cam Kart: Performance */}
        <Box sx={{ ...glassStyle, flex: 1.5, minWidth: '350px', display: 'flex', flexDirection: 'column' }}>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2, flexShrink: 0 }}>
            <Typography variant="subtitle2" sx={{ color: '#f0b90b', fontWeight: 'bold' }}>TOTAL PERFORMANCE</Typography>
            <ToggleButtonGroup
              value={timeFrame}
              exclusive
              onChange={(_e, v) => v && setTimeFrame(v)}
              size="small"
              sx={{ backgroundColor: 'rgba(0,0,0,0.2)', border: '1px solid rgba(255,255,255,0.05)', '& .MuiToggleButton-root': { color: '#848e9c', border: 'none', px: 1.5, py: 0.5, fontSize: '11px', fontWeight: 'bold', '&.Mui-selected': { color: '#000', backgroundColor: '#f0b90b' } } }}
            >
              {['24H', '7D', '1M', '3M', '1Y'].map((tf) => <ToggleButton key={tf} value={tf}>{tf}</ToggleButton>)}
            </ToggleButtonGroup>
          </Box>
          <Box sx={{ flexGrow: 1, width: '100%', height: 180, minHeight: 0 }}>
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={PERFORMANCE_DATA} margin={{ top: 10, right: 5, left: -25, bottom: 0 }}>
                <XAxis dataKey="time" stroke="#5d656e" style={{ fontSize: '10px' }} tickLine={false} />
                <YAxis stroke="#5d656e" style={{ fontSize: '10px' }} domain={['dataMin - 100', 'dataMax + 100']} tickLine={false} />
                <Tooltip formatter={(val: any) => [`$${val.toLocaleString()}`, 'Bakiye']} contentStyle={{ backgroundColor: '#1e2329', borderColor: 'rgba(255,255,255,0.1)', borderRadius: '6px', color: '#fff', fontSize: '12px' }} />
                <defs>
                  <linearGradient id="colorBakiye" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#0ecb81" stopOpacity={0.3}/>
                    <stop offset="95%" stopColor="#0ecb81" stopOpacity={0.0}/>
                  </linearGradient>
                </defs>
                <Area type="monotone" dataKey="bakiye" stroke="#0ecb81" strokeWidth={2} fillOpacity={1} fill="url(#colorBakiye)" />
              </AreaChart>
            </ResponsiveContainer>
          </Box>
        </Box>
      </Box>

      {/* 🚀 O CANAVAR SİKİĞİ BURADA TEK SATIRDA ÇAĞIRIP İŞİ BİTİRİYORUZ ŞEF! */}
      <CoinComparisonCard />

    </Box>
  );
}
