package com.byzquant.fed.scheduler;

import com.byzquant.fed.service.FedCategoryService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FedDataWatchdogScheduler {

    private final FedCategoryService fedCategoryService;

    // Hafta içi her gün, FED verilerinin açıklanma saat diliminde her 5 dakikada
    // bir uyanır
    @Scheduled(fixedRate = 300000)
    public void executeWatchdogRetryPipeline() {
        System.out.println("🚀 BYZ QUANT WATCHDOG ACTIVE: FED yayın takvimi tırmalanıyor...");
        fedCategoryService.checkAndFetchScheduledData();
    }

    // KESİN ÇÖZÜM: Her gece 00:00'da 7 altın serinin tamamının takvim fihristini
    // eksiksiz günceller
    @Scheduled(cron = "0 0 0 * * *")
    public void dailyCalendarSyncTask() {
        System.out.println("📅 BYZ QUANT CALENDAR SYNC: 7 ana makro göstergenin yayın takvimleri güncelleniyor...");

        // 1. Faiz Göstergeleri (Release ID: 18)
        fedCategoryService.syncReleaseCalendar("18", "FEDFUNDS");
        fedCategoryService.syncReleaseCalendar("18", "DGS10");
        fedCategoryService.syncReleaseCalendar("18", "T10Y2Y");

        // 2. İstihdam Raporu (Release ID: 50)
        fedCategoryService.syncReleaseCalendar("50", "UNRATE");

        // 3. Enflasyon Verisi (Release ID: 10)
        fedCategoryService.syncReleaseCalendar("10", "CPIAUCSL");

        // 4. Ekonomik Büyüme / GSYH (Release ID: 53)
        fedCategoryService.syncReleaseCalendar("53", "GDPC1");

        // 5. FED Likidite Bilançosu (Release ID: 22)
        fedCategoryService.syncReleaseCalendar("22", "WALCL");

        // 6. ABD Hazine Bakanlığı Genel Hesap Bakkesi (Release ID: 22 - H.4.1 Raporu)
        fedCategoryService.syncReleaseCalendar("22", "WDTGAL");

        // 7. ABD Toplam Borç / GSYH Oranı (Release ID: 52 - Ulusal Hesaplar Kamu Borç Raporu)
        fedCategoryService.syncReleaseCalendar("52", "GFDEGDQ188S");

        System.out.println("✅ BYZ QUANT CALENDAR SYNC COMPLETED: Tüm takvim veritabanı jilet gibi eşitlendi!");
    }
}
