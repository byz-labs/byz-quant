import React from 'react';
import { Box, Typography, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Chip, Accordion, AccordionSummary, AccordionDetails } from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';

interface AssetAccordionItemProps {
  group: {
    coin: string;
    totalAmount: number;
    totalCost: number;
    avgBuyPrice: number;
    currentTotalValue: number;
    pnlProfit: number;
    pnlPercentage: number;
    isProfit: boolean;
    txList: any[];
  };
}

export default function AssetAccordionItem({ group }: AssetAccordionItemProps): React.JSX.Element {
  return (
    <Accordion 
      sx={{
        backgroundColor: 'rgba(30, 35, 41, 0.25)',
        backdropFilter: 'blur(12px)',
        border: '1px solid rgba(255, 255, 255, 0.06)',
        borderRadius: '8px !important',
        color: '#fff',
        overflow: 'hidden',
        boxShadow: 'none',
        '&::before': { display: 'none' }
      }}
    >
      <AccordionSummary
        expandIcon={<ExpandMoreIcon sx={{ color: '#848e9c' }} />}
        sx={{
          backgroundColor: group.isProfit ? 'rgba(14, 203, 129, 0.08)' : 'rgba(246, 70, 93, 0.08)',
          borderBottom: '1px solid rgba(255,255,255,0.03)',
          px: 3,
          py: 1
        }}
      >
        <Box sx={{ display: 'flex', width: '100%', flexWrap: 'wrap', gap: 3, alignItems: 'center', justifyContent: 'space-between', pr: 2 }}>
          <Box>
            <Typography variant="h6" sx={{ fontWeight: 'bold', color: '#fff', display: 'flex', alignItems: 'center', gap: 1 }}>
              🪙 {group.coin}
              <Typography variant="body2" sx={{ color: '#848e9c', fontWeight: 'normal' }}>
                ({group.totalAmount.toLocaleString(undefined, { maximumFractionDigits: 4 })} Adet)
              </Typography>
            </Typography>
          </Box>
          <Box sx={{ display: 'flex', gap: 4 }}>
            <Box>
              <Typography variant="caption" sx={{ color: '#848e9c', display: 'block' }}>Toplam Maliyet</Typography>
              <Typography variant="body2" sx={{ fontWeight: '600' }}>${group.totalCost.toLocaleString(undefined, { maximumFractionDigits: 2 })}</Typography>
            </Box>
            <Box>
              <Typography variant="caption" sx={{ color: '#848e9c', display: 'block' }}>Alış Ortalaması</Typography>
              <Typography variant="body2" sx={{ fontWeight: '600', color: '#f0b90b' }}>${group.avgBuyPrice.toLocaleString(undefined, { maximumFractionDigits: 2 })}</Typography>
            </Box>
            <Box sx={{ display: { xs: 'none', sm: 'block' } }}>
              <Typography variant="caption" sx={{ color: '#848e9c', display: 'block' }}>Güncel Varlık Değeri</Typography>
              <Typography variant="body2" sx={{ fontWeight: '600' }}>${group.currentTotalValue.toLocaleString(undefined, { maximumFractionDigits: 2 })}</Typography>
            </Box>
          </Box>
          <Box sx={{ textAlign: 'right' }}>
            <Typography variant="caption" sx={{ color: '#848e9c', display: 'block' }}>Net Kâr / Zarar</Typography>
            <Typography variant="body1" sx={{ fontWeight: 'bold', color: group.isProfit ? '#0ecb81' : '#f6465d' }}>
              {group.isProfit ? '+' : ''}${group.pnlProfit.toLocaleString(undefined, { maximumFractionDigits: 2 })}
              <span style={{ fontSize: '12px', marginLeft: '6px', fontWeight: 'normal' }}>
                ({group.isProfit ? '▲' : '▼'} {group.pnlPercentage.toFixed(2)}%)
              </span>
            </Typography>
          </Box>
        </Box>
      </AccordionSummary>

      <AccordionDetails sx={{ padding: 0, backgroundColor: 'rgba(0,0,0,0.15)' }}>
        <TableContainer sx={{ backgroundColor: 'transparent' }}>
          <Table size="small">
            <TableHead>
              <TableRow sx={{ '& th': { color: '#848e9c', borderBottom: '1px solid #2f3336', fontWeight: 'bold', py: 1.5 } }}>
                <TableCell sx={{ pl: 4 }}>İşlem Tarihi</TableCell>
                <TableCell align="center">Tip</TableCell>
                <TableCell align="right">Miktar</TableCell>
                <TableCell align="right">Birim Fiyat</TableCell>
                <TableCell align="right" sx={{ pr: 4 }}>Toplam Tutar</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {group.txList.map((tx: any) => (
                <TableRow key={tx.id} sx={{ '& td': { color: '#eaecef', borderBottom: '1px solid rgba(255,255,255,0.02)', py: 1.5 } }}>
                  <TableCell sx={{ pl: 4, color: '#848e9c' }}>{tx.date}</TableCell>
                  <TableCell align="center">
                    <Chip 
                      label={tx.type} 
                      size="small"
                      sx={{ 
                        backgroundColor: tx.type === 'BUY' ? 'rgba(14, 203, 129, 0.12)' : 'rgba(246, 70, 93, 0.12)', 
                        color: tx.type === 'BUY' ? '#0ecb81' : '#f6465d', 
                        fontWeight: 'bold',
                        borderRadius: '4px', height: '20px', fontSize: '10px'
                      }} 
                    />
                  </TableCell>
                  <TableCell align="right">{tx.amount}</TableCell>
                  <TableCell align="right">${tx.price.toLocaleString()}</TableCell>
                  <TableCell align="right" sx={{ pr: 4, fontWeight: 'bold', color: tx.type === 'BUY' ? '#fff' : '#848e9c' }}>
                    ${tx.total.toLocaleString(undefined, { minimumFractionDigits: 2 })}
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </AccordionDetails>
    </Accordion>
  );
}
