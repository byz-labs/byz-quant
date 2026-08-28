import React, { useState } from 'react';
import { Box, Typography, TextField, MenuItem, Button } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';

const COIN_LIST = ['BTC', 'ETH', 'SOL', 'PEPE', 'RENDER', 'XRP', 'TRUMP'];

interface TransactionFormProps {
  onAdd: (tx: { date: string; coin: string; type: 'BUY' | 'SELL'; amount: number; price: number }) => void;
}

export default function TransactionForm({ onAdd }: TransactionFormProps): React.JSX.Element {
  const [coin, setCoin] = useState<string>('BTC');
  const [type, setType] = useState<'BUY' | 'SELL'>('BUY');
  const [amount, setAmount] = useState<string>('');
  const [price, setPrice] = useState<string>('');
  const [date, setDate] = useState<string>('2026-08-27');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!amount || !price || !date) return;

    onAdd({
      date,
      coin,
      type,
      amount: parseFloat(amount),
      price: parseFloat(price)
    });
    
    setAmount('');
    setPrice('');
  };

  return (
    <Box 
      component="form" 
      onSubmit={handleSubmit} 
      sx={{
        backgroundColor: 'rgba(30, 35, 41, 0.45)',
        backdropFilter: 'blur(12px)',
        border: '1px solid rgba(255, 255, 255, 0.08)',
        boxShadow: '0 8px 32px 0 rgba(0, 0, 0, 0.37)',
        padding: '24px',
        borderRadius: '12px',
        width: '100%'
      }}
    >
      <Typography variant="subtitle2" sx={{ color: '#f0b90b', fontWeight: 'bold', mb: 3, textTransform: 'uppercase' }}>
        ➕ YENİ TRADE KAYDI EKLE
      </Typography>
      <Box sx={{ display: 'flex', gap: 2, flexWrap: 'wrap', alignItems: 'center' }}>
        <TextField
          type="date"
          label="İşlem Tarihi"
          value={date}
          onChange={(e) => setDate(e.target.value)}
          slotProps={{ inputLabel: { shrink: true } }}
          sx={{ flex: 1, minWidth: '150px', '& .MuiOutlinedInput-root': { color: '#fff', '& fieldset': { borderColor: 'rgba(255,255,255,0.1)' }, '&:hover fieldset': { borderColor: '#f0b90b' } }, '& .MuiInputLabel-root': { color: '#848e9c' } }}
        />
        <TextField
          select
          label="Varlık (Asset)"
          value={coin}
          onChange={(e) => setCoin(e.target.value)}
          sx={{ flex: 1, minWidth: '120px', '& .MuiOutlinedInput-root': { color: '#fff', '& fieldset': { borderColor: 'rgba(255,255,255,0.1)' }, '&:hover fieldset': { borderColor: '#f0b90b' } }, '& .MuiInputLabel-root': { color: '#848e9c' } }}
        >
          {COIN_LIST.map((c) => <MenuItem key={c} value={c}>{c}</MenuItem>)}
        </TextField>
        <TextField
          select
          label="Tip (Type)"
          value={type}
          onChange={(e) => setType(e.target.value as any)}
          sx={{ flex: 1, minWidth: '120px', '& .MuiOutlinedInput-root': { color: type === 'BUY' ? '#0ecb81' : '#f6465d', '& fieldset': { borderColor: 'rgba(255,255,255,0.1)' }, '&:hover fieldset': { borderColor: '#f0b90b' } }, '& .MuiInputLabel-root': { color: '#848e9c' } }}
        >
          <MenuItem value="BUY" style={{ color: '#0ecb81', fontWeight: 'bold' }}>BUY (Alım)</MenuItem>
          <MenuItem value="SELL" style={{ color: '#f6465d', fontWeight: 'bold' }}>SELL (Satım)</MenuItem>
        </TextField>
        <TextField
          type="number"
          label="Miktar (Amount)"
          placeholder="0.00"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
          required
          slotProps={{ htmlInput: { step: 'any', min: 0 } }}
          sx={{ flex: 1, minWidth: '130px', '& .MuiOutlinedInput-root': { color: '#fff', '& fieldset': { borderColor: 'rgba(255,255,255,0.1)' }, '&:hover fieldset': { borderColor: '#f0b90b' } }, '& .MuiInputLabel-root': { color: '#848e9c' } }}
        />
        <TextField
          type="number"
          label="Birim Fiyat ($)"
          placeholder="0.00"
          value={price}
          onChange={(e) => setPrice(e.target.value)}
          required
          slotProps={{ htmlInput: { step: 'any', min: 0 } }}
          sx={{ flex: 1, minWidth: '130px', '& .MuiOutlinedInput-root': { color: '#fff', '& fieldset': { borderColor: 'rgba(255,255,255,0.1)' }, '&:hover fieldset': { borderColor: '#f0b90b' } }, '& .MuiInputLabel-root': { color: '#848e9c' } }}
        />
        <Button type="submit" variant="contained" startIcon={<AddIcon />} sx={{ height: '56px', px: 4, backgroundColor: '#f0b90b', color: '#000', fontWeight: 'bold', '&:hover': { backgroundColor: '#d4a307' } }}>
          Deftere İşle
        </Button>
      </Box>
    </Box>
  );
}
