import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import EconomicDashboard from './components/EconomicDashboard';
import PortfolioView from './components/PortfolioView';
import SuperChartView from './components/SuperChartView';
import DashboardHome from './components/DashboardHome';
import TransactionHistoryView from './components/TransactionHistoryView';
import FedStrategyView from './components/FedStrategyView';

export default function App(): React.JSX.Element {
  return (
    <BrowserRouter>
      <Routes>
        {/* Ana Layout Çatısı */}
        <Route path="/" element={<EconomicDashboard />}>
          {/* İlk açılışta doğrudan /dashboard'a uçur */}
          <Route index element={<Navigate to="/dashboard" replace />} />
          
          {/* 🚀 Başlarına slash koyarak url patikalarını Sidebar ile milimetrik eşitliyoruz */}
          <Route path="/dashboard" element={<DashboardHome />} />
          <Route path="/superchart" element={<SuperChartView />} />
          <Route path="/portfolio" element={<PortfolioView />} />
          <Route path="/history" element={<TransactionHistoryView />} />
          <Route path="/fed" element={<FedStrategyView />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
