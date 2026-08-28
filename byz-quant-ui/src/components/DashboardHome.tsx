import React from 'react';
import { useEconomicData } from '../hooks/useEconomicData';
import { Box } from '@mui/material';
import MacroTrendCard from './MacroTrendCard';
import MacroTimelineList from './MacroTimelineList';

export default function DashboardHome(): React.JSX.Element {
  // Bağımsız sayfada üç makro veriyi de aslanlar gibi paralel yönetiyoruz
  const fedFunds = useEconomicData('FEDFUNDS');
  const cpi = useEconomicData('CPIAUCSL');
  const nfp = useEconomicData('PAYEMS');

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3, width: '100%', height: '100%', overflowY: 'auto' }}>
      <Box sx={{ display: 'flex', gap: '24px', flexWrap: 'wrap', width: '100%' }}>
        <MacroTrendCard title="🏛️ FED FAİZ ORANI" hookResult={fedFunds} color="#0ecb81" unit="%" />
        <MacroTrendCard title="📈 TÜFE ENFLASYON (CPI)" hookResult={cpi} color="#38b6ff" unit="%" />
        <MacroTrendCard title="💼 TARIM DIŞI İSTİHDAM (NFP)" hookResult={nfp} color="#f0b90b" unit="M" />
      </Box>
      <MacroTimelineList calendarData={fedFunds.calendarData} isLoading={fedFunds.isLoading} />
    </Box>
  );
}
