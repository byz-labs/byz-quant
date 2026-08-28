import React from 'react';
import { Box, Typography } from '@mui/material';
import type { ObservationDto } from '../types/economic';

interface MacroAnalysisCardProps {
    seriesId: string;
    indicatorData: ObservationDto[] | null;
    isLoading: boolean;
}

export default function MacroAnalysisCard({ seriesId, indicatorData, isLoading }: MacroAnalysisCardProps): React.JSX.Element {
    const latestActual = indicatorData && indicatorData.length > 0 ? parseFloat(indicatorData[0].value).toFixed(2) : '-';
    const latestDate = indicatorData && indicatorData.length > 0 ? indicatorData[0].date : '-';
    const previousValue = indicatorData && indicatorData.length > 1 ? parseFloat(indicatorData[1].value).toFixed(2) : '-';
    return (
        <Box sx={{
            backgroundColor: 'rgba(30, 35, 41, 0.45)',
            backdropFilter: 'blur(12px)',
            WebkitBackdropFilter: 'blur(12px)',
            border: '1px solid rgba(255, 255, 255, 0.08)',
            boxShadow: '0 8px 32px 0 rgba(0, 0, 0, 0.37)',
            padding: '24px',
            borderRadius: '12px',
            width: '100%'
        }}>
            <Typography variant="subtitle2" sx={{ color: '#f0b90b', fontWeight: 'bold', mb: 3, textTransform: 'uppercase' }}>
                📊 Makro Durum Analizi ({seriesId})
            </Typography>

            {isLoading ? (
                <Typography sx={{ color: '#848e9c' }}>Veriler yükleniyor...</Typography>
            ) : (
                <Box sx={{
                    display: 'flex',
                    flexDirection: { xs: 'column', sm: 'row' },
                    gap: 4,
                    alignItems: { xs: 'flex-start', sm: 'center' },
                    padding: '24px',
                    backgroundColor: 'rgba(0,0,0,0.2)',
                    borderRadius: '8px'
                }}>
                    <Box>
                        <Typography variant="caption" sx={{ color: '#848e9c', textTransform: 'uppercase', display: 'block', mb: 1 }}>
                            Son Açıklanan (Actual)
                        </Typography>
                        <Typography variant="h3" sx={{ fontWeight: 'bold', color: '#0ecb81' }}>
                            {latestActual}%
                        </Typography>
                        <Typography variant="caption" sx={{ color: '#848e9c', mt: 0.5, display: 'block' }}>
                            Tarih: {latestDate}
                        </Typography>
                    </Box>
                    <Box sx={{ display: { xs: 'none', sm: 'block' }, borderLeft: '1px solid rgba(255,255,255,0.08)', height: '60px' }} />
                    <Box>
                        <Typography variant="caption" sx={{ color: '#848e9c', textTransform: 'uppercase', display: 'block', mb: 1 }}>
                            Önceki Veri (Previous)
                        </Typography>
                        <Typography variant="h3" sx={{ fontWeight: 'bold', color: '#848e9c' }}>
                            {previousValue}%
                        </Typography>
                    </Box>
                </Box>
            )}
        </Box>
    );
}
