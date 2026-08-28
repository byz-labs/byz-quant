import React, { useEffect, useState } from 'react';
import { Box, Typography, Grid, Snackbar, Alert, Backdrop, CircularProgress } from '@mui/material';
import StrategySectionPanel from './StrategySectionPanel';
import MiniChartCard from './MiniChartCard';

export default function FedStrategyView(): React.JSX.Element {
  const [globalLoading, setGlobalLoading] = useState<boolean>(false);
  const [refreshTrigger, setRefreshTrigger] = useState<number>(0);
  const [toast, setToast] = useState<{ open: boolean; message: string; severity: 'success' | 'error' }>({
    open: false, message: '', severity: 'success'
  });

  const triggerNotification = (message: string, severity: 'success' | 'error') => {
    setToast({ open: true, message, severity });
  };

  useEffect(() => {
    const autoIngestAll = async () => {
      setGlobalLoading(true);
      try {
        const response = await fetch('http://localhost:8080/api/v1/macro/admin/ingest/all', {
          method: 'POST'
        });
        const text = await response.text();
        if (response.ok) {
          triggerNotification("✓ 14 Canavarın tamamı arka planda paralel güncellendi!", "success");
          setRefreshTrigger(prev => prev + 1);
        } else {
          triggerNotification(`🚨 Toplu güncelleme hatası: ${text}`, "error");
        }
      } catch (err) {
        triggerNotification("🚨 Backend sunucusuna bağlanılamadı!", "error");
      } finally {
        setGlobalLoading(false);
      }
    };

    autoIngestAll();
  }, []);

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 4, width: '100%' }}>

      <Backdrop sx={{ color: '#f0b90b', zIndex: (theme) => theme.zIndex.drawer + 1, backgroundColor: 'rgba(0,0,0,0.7)' }} open={globalLoading}>
        <Box sx={{ textAlign: 'center', display: 'flex', flexDirection: 'column', gap: 2, items: 'center' }}>
          <CircularProgress color="inherit" />
          <Typography variant="body2" sx={{ fontWeight: 'bold', letterSpacing: '1px' }}>
            BESSENT vs WARSH LİKİDİTE VERİLERİ PARALEL EMİLİYOR...
          </Typography>
        </Box>
      </Backdrop>

      {/* 💰 SAVAŞ ALANI I: 5 CANAVAR YAN YANA */}
      <StrategySectionPanel
        volumeTitle="VOLUME I // PARA POLİTİKASI & LİKİDİTE ORKESTRASI"
        subtitle="Küresel piyasalara zerk edilen taze akaryakıt ve doların ana damarları"
        tacticText="Scott Bessent kısa vadeli bono ihraçlarıyla Ters Repo'yu (RRP) boşaltıp piyasaya sinsi likidite üflüyor. Kevin Warsh ise bilançoyu (WALCL) daraltarak bu vanayı kısmaya çalışıyor."
        isProfit={true}
      >
        {/* 🚀 size={2.4} ile tam 5 kart yan yana kilitlendi şef! */}
        <Grid container spacing={2}>
          <Grid size={{ xs: 12, sm: 6, md: 4, lg: 2.4 }}><MiniChartCard title="Fed Toplam Bilançosu" code="WALCL" desc="Net parasal sıkılaşma hacmi." color="#0ecb81" onNotify={triggerNotification} refreshTrigger={refreshTrigger} /></Grid>
          <Grid size={{ xs: 12, sm: 6, md: 4, lg: 2.4 }}><MiniChartCard title="M2 Para Arzı" code="M2SL" desc="Piyasalarda dolaşan sinsi dolar." color="#38b6ff" onNotify={triggerNotification} refreshTrigger={refreshTrigger} /></Grid>
          <Grid size={{ xs: 12, sm: 6, md: 4, lg: 2.4 }}><MiniChartCard title="Fed Faiz Oranı" code="FEDFUNDS" desc="Kevin Warsh gecelik faiz maliyeti." color="#f0b90b" onNotify={triggerNotification} refreshTrigger={refreshTrigger} /></Grid>
          <Grid size={{ xs: 12, sm: 6, md: 4, lg: 2.4 }}><MiniChartCard title="Ters Repo Havuzu (RRP)" code="RRPONTSYD" desc="Fed otoparkındaki ölü sıcak para." color="#e67e22" onNotify={triggerNotification} refreshTrigger={refreshTrigger} /></Grid>
          <Grid size={{ xs: 12, sm: 6, md: 4, lg: 2.4 }}><MiniChartCard title="Dolar Endeksi (DXY)" code="DTWEXB" desc="Doların küresel ticaret gücü." color="#9b59b6" onNotify={triggerNotification} refreshTrigger={refreshTrigger} /></Grid>
        </Grid>
      </StrategySectionPanel>

      {/* 📈 SAVAŞ ALANI II: 5 CANAVAR YAN YANA */}
      <StrategySectionPanel
        volumeTitle="VOLUME II // ENFLASYON & İŞGÜCÜ KIRBAÇLARI"
        subtitle="Fed'in faiz silahını elinde tutmak için bahane ettiği sinsi temel makro kemikler"
        tacticText="Çekirdek PCE (PCEPI) %2'ye yanaşmadıkça Warsh faiz indirimlerine bodoslama girmeyecek. Ancak Haftalık İşsizlik Başvuruları (ICSA) tırmanırsa, Scott Bessent faiz indirimi için baskıyı arşa çıkaracak."
        isProfit={false}
      >
        <Grid container spacing={2}>
          <Grid size={{ xs: 12, sm: 6, md: 4, lg: 2.4 }}><MiniChartCard title="Çekirdek TÜFE (Core CPI)" code="CPILFESL" desc="Sektördeki yapışkan enflasyon kemiği." color="#e67e22" onNotify={triggerNotification} refreshTrigger={refreshTrigger} /></Grid>
          <Grid size={{ xs: 12, sm: 6, md: 4, lg: 2.4 }}>
            <MiniChartCard
              title="Çekirdek PCE Enflasyonu"
              code="PCEPILFE" // 🚀 Has kurumsal BEA/Investing takvimi ikizi olan canavar kod!
              desc="Kevin Warsh'un taptığı resmi hedef."
              color="#9b59b6"
              onNotify={triggerNotification}
              refreshTrigger={refreshTrigger}
            />
          </Grid><Grid size={{ xs: 12, sm: 6, md: 4, lg: 2.4 }}><MiniChartCard title="Haftalık İşsizlik Maaşı" code="ICSA" desc="İstihdam piyasasındaki erken uyarı." color="#f6465d" onNotify={triggerNotification} refreshTrigger={refreshTrigger} /></Grid>
          <Grid size={{ xs: 12, sm: 6, md: 4, lg: 2.4 }}><MiniChartCard title="Tarım Dışı İstihdam (NFP)" code="PAYEMS" desc="Her ay üretilen taze istihdam motoru." color="#0ecb81" onNotify={triggerNotification} refreshTrigger={refreshTrigger} /></Grid>
          <Grid size={{ xs: 12, sm: 6, md: 4, lg: 2.4 }}><MiniChartCard title="Ekonomik Büyüme (GDP)" code="A191RL1Q225SBEA" desc="Net çeyreklik yıllıklandırılmış büyüme." color="#38b6ff" onNotify={triggerNotification} refreshTrigger={refreshTrigger} /></Grid>
        </Grid>
      </StrategySectionPanel>

      {/* ⚠️ SAVAŞ ALANI III: 4 CANAVAR (Geniş ekranlarda boşluk kalmasın diye dengeli mizanpaj) */}
      <StrategySectionPanel
        volumeTitle="VOLUME III // SİSTEMİK RİSK & KONUT DEFANSLARI"
        subtitle="Finansal sistemde bir bokluk olup olmadığını ve konut kilitlenmesini izleyen erken uyarı radarları"
        tacticText="Tahvil Makası (T10Y2Y) sıfırın altına indiğinde kriz saatini kurar. Fed Acil Kredilerindeki (WLCFLPCL) ani sıçramalar ise bankaların sinsi bir likidite deliğinde boğulduğunun resmi kanıtıdır."
        isProfit={false}
      >
        <Grid container spacing={2}>
          <Grid size={{ xs: 12, sm: 6, md: 4, lg: 3 }}><MiniChartCard title="Fed Acil Banka Kredileri" code="WLCFLPCL" desc="Sıçrama varsa sistemde bokluk vardır!" color="#f6465d" onNotify={triggerNotification} refreshTrigger={refreshTrigger} /></Grid>
          <Grid size={{ xs: 12, sm: 6, md: 4, lg: 3 }}><MiniChartCard title="Finansal Koşullar" code="NFCI" desc="0'ın altı bol kaldıraçlı boğa demektir." color="#38b6ff" onNotify={triggerNotification} refreshTrigger={refreshTrigger} /></Grid>
          <Grid size={{ xs: 12, sm: 6, md: 4, lg: 3 }}><MiniChartCard title="Tahvil Makası (T10Y2Y)" code="T10Y2Y" desc="Sıfırın altı 6-12 ay içinde kesin kriz." color="#0ecb81" onNotify={triggerNotification} refreshTrigger={refreshTrigger} /></Grid>
          <Grid size={{ xs: 12, sm: 6, md: 4, lg: 3 }}><MiniChartCard title="30 Yıllık Mortgage Faizleri" code="MORTGAGE30US" desc="Konut piyasasını felç eden sinsi faiz." color="#34495e" onNotify={triggerNotification} refreshTrigger={refreshTrigger} /></Grid>
        </Grid>
      </StrategySectionPanel>

      <Snackbar open={toast.open} autoHideDuration={3000} onClose={() => setToast(p => ({ ...p, open: false }))} anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}>
        <Alert severity={toast.severity} variant="filled" sx={{ backgroundColor: toast.severity === 'success' ? '#0ecb81 !important' : '#f6465d !important', color: '#000', fontWeight: 'bold' }}>{toast.message}</Alert>
      </Snackbar>
    </Box>
  );
}
