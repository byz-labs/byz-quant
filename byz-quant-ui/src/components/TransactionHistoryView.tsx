import React, { useState } from 'react';
import { Box, Typography } from '@mui/material';
import TransactionForm from './TransactionForm';
import AssetAccordionItem from './AssetAccordionItem';

const COIN_LIST = ['BTC', 'ETH', 'SOL', 'PEPE', 'RENDER', 'XRP', 'TRUMP'];
const CURRENT_MARKET_PRICES: Record<string, number> = { BTC: 64250, ETH: 3450, SOL: 145.8, PEPE: 0.0000085, RENDER: 5.2, XRP: 0.58, TRUMP: 4.1 };

interface Transaction {
  id: number;
  date: string;
  coin: string;
  type: 'BUY' | 'SELL';
  amount: number;
  price: number;
  total: number;
}

export default function TransactionHistoryView(): React.JSX.Element {
  const [transactions, setTransactions] = useState<Transaction[]>([
    { id: 1, date: '2026-08-26', coin: 'BTC', type: 'BUY', amount: 0.05, price: 60000, total: 3000 },
    { id: 2, date: '2026-08-25', coin: 'BTC', type: 'BUY', amount: 0.02, price: 62000, total: 1240 },
    { id: 3, date: '2026-08-24', coin: 'BTC', type: 'SELL', amount: 0.01, price: 65000, total: 650 },
    { id: 4, date: '2026-08-25', coin: 'ETH', type: 'BUY', amount: 1.5, price: 3600, total: 5400 },
    { id: 5, date: '2026-08-24', coin: 'ETH', type: 'SELL', amount: 0.3, price: 3300, total: 990 },
    { id: 6, date: '2026-08-23', coin: 'SOL', type: 'BUY', amount: 20, price: 160, total: 3200 }
  ]);

  const handleAddTransaction = (newTxData: { date: string; coin: string; type: 'BUY' | 'SELL'; amount: number; price: number }) => {
    const newTx: Transaction = {
      id: Date.now(),
      ...newTxData,
      total: newTxData.amount * newTxData.price
    };
    setTransactions([newTx, ...transactions]);
  };

  // Matematiksel Hesaplama ve Gruplama Motoru
  const groupedData = COIN_LIST.map((c) => {
    const coinTx = transactions.filter((t) => t.coin === c);
    if (coinTx.length === 0) return null;

    let totalAmount = 0;
    let totalCost = 0;

    [...coinTx].reverse().forEach((tx) => {
      if (tx.type === 'BUY') {
        totalAmount += tx.amount;
        totalCost += tx.total;
      } else if (totalAmount > 0) {
        const avgPriceBeforeSell = totalCost / totalAmount;
        totalAmount -= tx.amount;
        totalCost = totalAmount * avgPriceBeforeSell;
      } else {
        totalAmount -= tx.amount;
      }
    });

    const avgBuyPrice = totalAmount > 0 ? totalCost / totalAmount : 0;
    const currentPrice = CURRENT_MARKET_PRICES[c] || 0;
    const currentTotalValue = totalAmount * currentPrice;
    const pnlProfit = currentTotalValue - totalCost;
    const pnlPercentage = totalCost > 0 ? (pnlProfit / totalCost) * 100 : 0;

    return {
      coin: c,
      totalAmount,
      totalCost,
      avgBuyPrice,
      currentTotalValue,
      pnlProfit,
      pnlPercentage,
      isProfit: pnlProfit >= 0,
      txList: coinTx
    };
  }).filter(Boolean);

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 4, width: '100%' }}>
      {/* İzolasyon Hücresi 1: Form */}
      <TransactionForm onAdd={handleAddTransaction} />

      {/* İzolasyon Hücresi 2: Listeleme Başlığı ve Alt Hücreler */}
      <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, width: '100%' }}>
        <Typography variant="subtitle2" sx={{ color: '#f0b90b', fontWeight: 'bold', mb: 1, textTransform: 'uppercase' }}>
          📋 VARLIK BAZLI PORTFÖY ÖZETİ & DETAYLAR
        </Typography>

        {groupedData.map((group: any) => (
          <AssetAccordionItem key={group.coin} group={group} />
        ))}
      </Box>
    </Box>
  );
}
