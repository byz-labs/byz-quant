import React, { useState, useEffect } from 'react';
import { Box, Typography, IconButton, CircularProgress } from '@mui/material';
import { ResponsiveContainer, AreaChart, Area, XAxis, YAxis, Tooltip } from 'recharts';
import RefreshIcon from '@mui/icons-material/Refresh';

const glassCardStyle = {
    backgroundColor: 'rgba(30, 35, 41, 0.45)',
    backdropFilter: 'blur(12px)',
    WebkitBackdropFilter: 'blur(12px)',
    border: '1px solid rgba(255, 255, 255, 0.08)',
    boxShadow: '0 4px 24px 0 rgba(0, 0, 0, 0.30)',
    padding: '14px 18px',
    borderRadius: '10px',
    boxSizing: 'border-box' as const,
    width: '100%',
    display: 'flex',
    flexDirection: 'column' as const,
    gap: 1
};

interface MiniChartCardProps {
    title: string;
    code: string;
    desc: string;
    color: string;
    onNotify: (message: string, severity: 'success' | 'error') => void;
    refreshTrigger: number;
}

interface ChartPoint {
    date: string;
    value: number;
}

export default function MiniChartCard({ title, code, desc, color: defaultColor, onNotify, refreshTrigger }: MiniChartCardProps): React.JSX.Element {
    const [loading, setLoading] = useState<boolean>(false);
    const [liveValue, setLiveValue] = useState<string>('...');
    const [chartData, setChartData] = useState<ChartPoint[]>([]);
    const [dynamicColor, setDynamicColor] = useState<string>(defaultColor);

    const fetchLiveValues = async () => {
        try {
            const resValue = await fetch(`http://localhost:8080/api/v1/macro/indicator/${code}`);
            const resSeries = await fetch(`http://localhost:8080/api/v1/macro/indicator/${code}/series`);

            if (resValue.ok && resSeries.ok) {
                const valueData = await resValue.json();
                const seriesData: ChartPoint[] = await resSeries.json();

                if (valueData.value !== undefined && valueData.value !== null) {

                    // 🚀 POLİMORFİZMİN ZAFERİ: O SİKİMTRAK SWITCH(CODE) BLOĞU TAMAMEN ÇÖPE ATILDI!
                    // Kart artık backend'den gelen hazır formatlı metni ve ölçeklenmiş seriyi düz basar şef!
                    setLiveValue(valueData.value);
                    setChartData(seriesData);

                    // Trend rengini en temiz dizilimle ayarla
                    if (seriesData.length >= 2) {
                        const first = seriesData[0].value;
                        const last = seriesData[seriesData.length - 1].value;
                        if (last < first) setDynamicColor('#f6465d'); // Düşüş varsa kriz kırmızısı
                        else setDynamicColor('#0ecb81'); // Yükseliş varsa neon yeşil
                    }
                }
            }
        } catch (err) {
            console.error("Canlı makro verileri emilemedi:", err);
        }
    };

    useEffect(() => {
        fetchLiveValues();
    }, [code, refreshTrigger]);

    const handleRefresh = async () => {
        setLoading(true);
        try {
            const response = await fetch(`http://localhost:8080/api/v1/macro/admin/ingest/${code}`, {
                method: 'POST'
            });
            const textData = await response.text();
            if (response.ok) {
                onNotify(textData, 'success');
                fetchLiveValues();
            } else {
                onNotify(`🚨 ${code} Emme Başarısız: ${textData}`, 'error');
            }
        } catch (err) {
            onNotify('🚨 Sunucu bağlantı hatası!', 'error');
        } finally {
            setLoading(false);
        }
    };

    const values = chartData.map(d => d.value);
    const minVal = values.length > 0 ? Math.min(...values) * 0.999 : 0;
    const maxVal = values.length > 0 ? Math.max(...values) * 1.001 : 100;

    return (
        <Box sx={glassCardStyle}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <Box>
                    <Typography variant="caption" sx={{ color: '#848e9c', fontWeight: 'bold', fontSize: '10px', textTransform: 'uppercase', letterSpacing: '0.3px' }}>
                        {title}
                    </Typography>
                    <Typography variant="body1" sx={{ fontWeight: 'bold', color: '#fff', mt: 0.2, fontSize: '18px' }}>
                        {liveValue}
                    </Typography>
                </Box>

                <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                    <Typography variant="caption" sx={{ backgroundColor: 'rgba(255,255,255,0.04)', color: '#f0b90b', px: 0.8, py: 0.3, borderRadius: '3px', fontSize: '9px', fontFamily: 'monospace', fontWeight: 'bold' }}>
                        {code}
                    </Typography>
                    <IconButton onClick={handleRefresh} disabled={loading} size="small" sx={{ color: '#848e9c', '&:hover': { color: '#f0b90b' }, p: 0.3 }}>
                        {loading ? <CircularProgress size={12} sx={{ color: '#f0b90b' }} /> : <RefreshIcon sx={{ fontSize: '14px' }} />}
                    </IconButton>
                </Box>
            </Box>

            <Box sx={{ width: '100%', height: 45, mt: 0.2 }}>
                <ResponsiveContainer width="100%" height="100%">
                    <AreaChart data={chartData} margin={{ top: 2, right: 2, left: 2, bottom: 2 }}>
                        <XAxis dataKey="date" hide />
                        <YAxis hide domain={[minVal, maxVal]} />
                        <Tooltip
                            formatter={(val: any) => [parseFloat(val).toFixed(2), 'Değer']}
                            contentStyle={{ backgroundColor: '#1e2329', borderColor: 'rgba(255,255,255,0.1)', color: '#fff', fontSize: '9px', padding: '4px 8px' }}
                        />
                        <defs>
                            <linearGradient id={`grad-${code}`} x1="0" y1="0" x2="0" y2="1">
                                <stop offset="5%" stopColor={dynamicColor} stopOpacity={0.20} />
                                <stop offset="95%" stopColor={dynamicColor} stopOpacity={0.0} />
                            </linearGradient>
                        </defs>
                        <Area type="monotone" dataKey="value" stroke={dynamicColor} strokeWidth={1.2} fill={`url(#grad-${code})`} />
                    </AreaChart>
                </ResponsiveContainer>
            </Box>

            <Typography variant="caption" sx={{ color: '#848e9c', fontSize: '10px', lineHeight: 1.3, mt: 0.2 }}>
                {desc}
            </Typography>
        </Box>
    );
}
