package com.byzquant.fed.client;

public interface EconomicDataClient {
    /**
     * Verilen endpoint ve parametrelere göre dış servise istek atar ve sonucu hedef tipe dönüştürür.
     * 
     * @param endpoint Çağrılacak alt URL (örn: "/series/observations")
     * @param responseType Sonucun dönüştürüleceği Record/Sınıf tipi (Polimorfik hedef)
     * @param uriVariables URL içindeki dinamik parametreler (series_id vb.)
     * @return Dönüştürülmüş hedef nesne <T>
     */
    <T> T fetch(String endpoint, Class<T> responseType, Object... uriVariables);
}
