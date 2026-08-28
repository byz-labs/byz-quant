import React from 'react';
import { useLocation, Outlet } from 'react-router-dom';
import { Box, Typography } from '@mui/material';
import { useEconomicData } from '../hooks/useEconomicData';
import Sidebar from './Sidebar';
import CryptoTickerStream from './CryptoTickerStream';
import Header from './Header';

export default function EconomicDashboard(): React.JSX.Element {
  const location = useLocation();

  const fedFunds = useEconomicData('FEDFUNDS');
  const cpi = useEconomicData('CPIAUCSL');
  const nfp = useEconomicData('PAYEMS');

  const globalError = fedFunds.error || cpi.error || nfp.error;
  if (globalError) {
    return (
      <Box sx={{ p: 4, color: '#f6465d', backgroundColor: '#0b0e11', minHeight: '100vh' }}>
        <Typography variant="h6">⚠️ Sistem Hatası: {globalError}</Typography>
      </Box>
    );
  }

  const activeMenu = (location.pathname.substring(1) || 'dashboard') as any;

  // 🚀 SİHİRLİ GEÇİS: Eğer /fed sayfasındaysak kripto şeridini uçuruyoruz!
  const isFedPage = location.pathname === '/fed';

  return (
    <Box sx={{ 
      display: 'flex', 
      width: '100vw', 
      height: '100vh', 
      backgroundColor: '#0b0e11',
      color: '#eaecef',
      overflow: 'hidden'
    }}>
      
      {/* Sol Sabit Menü */}
      <Sidebar />

      {/* Sağ Ana Gövde */}
      <Box 
        component="main" 
        sx={{ 
          flexGrow: 1, 
          height: '100vh', 
          display: 'flex', 
          flexDirection: 'column', 
          p: 3, 
          gap: 3, 
          boxSizing: 'border-box', 
          width: `calc(100vw - 280px)`, 
          minWidth: 0, 
          overflow: 'hidden' // Dış çerçevenin taşmasını engelle
        }}
      >
        {/* Atomik Dinamik Başlık */}
        <Header activeMenu={activeMenu} />

        {/* 🚀 Kripto fiyat şeridi sadece /fed dışındaki sayfalarda görünecek */}
        {!isFedPage && (
          <Box sx={{ width: '100%', flexShrink: 0 }}>
            <CryptoTickerStream />
          </Box>
        )}

        {/* 🚀 KİLİTLİ SAYFA İÇİ AKICI SCROLL ALANI */}
        {/* Sadece bu iç kutu dikeyde kayacak, sayfa nizamı zerre bozulmayacak şef! */}
        <Box sx={{ 
          flexGrow: 1, 
          width: '100%', 
          height: '100%', 
          overflowY: 'auto', 
          pr: 0.5,
          '&::-webkit-scrollbar': { width: '6px' },
          '&::-webkit-scrollbar-thumb': { backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: '4px' }
        }}>
          <Outlet />
        </Box>

      </Box>
    </Box>
  );
}
