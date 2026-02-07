# PS5 Update Blocker (Proxy Edition)

Android için PS5 güncelleme engelleyici. Yerel bir **HTTP Proxy Sunucusu** çalıştırarak PS5 güncelleme isteklerini engeller.

**Bu sürüm, VPN tabanlı eski sürümlere göre daha kararlı çalışır ve Hotspot bağlantılarını tam destekler.**

## Özellikler

- **Proxy Sunucusu**: Port 8080 üzerinde çalışan hafif bir HTTP Proxy.
- **Hotspot Desteği**: Telefonunuzu modem olarak kullanırken PS5 trafiğini filtreler.
- **Wi-Fi Desteği**: Aynı Wi-Fi ağındaki cihazları filtreler.
- **İnternet Dostu**: Sadece engellenen listesindeki siteleri filtreler, diğer trafiğe dokunmaz (Hız kaybı yok).
- **Rootsuz**: Cihazınızda Root yetkisi gerekmez.

## Engellenen Sunucular
Uygulama, PlayStation 5'in güncelleme indirmek için kullandığı 40+ sunucuyu (dau01.ps5..., fuk01.ps5... vb.) otomatik olarak engeller.

## Kurulum ve Kullanım

### 1. Uygulamayı Başlatın
- Uygulamayı açın ve **"Engellemeyi Başlat"** butonuna basın.
- Ekranda **IP Adresi** ve **Port (8080)** bilgisi görünecektir.
- *Örnek IP: 192.168.1.25 veya 192.168.43.1 (Hotspot durumunda)*

### 2. PS5 Ayarlarını Yapın
PlayStation 5 cihazınızda şu adımları izleyin:

1. **Ayarlar > Ağ > Ayarlar > İnternet Bağlantısını Ayarla**
2. Bağlı olduğunuz Wi-Fi ağına (veya telefonunuzun Hotspot ağına) gelin.
3. Seçenekler (Option) tuşuna basın ve **Gelişmiş Ayarlar**'ı seçin.
4. **Proxy Sunucusu** kısmını **Kullan** olarak seçin.
5. **Adres:** Uygulamada yazan IP Adresi (Örn: 192.168.43.1)
6. **Port:** 8080
7. Ayarları kaydedin.

### 3. Test Edin
- PS5 üzerinden "Ayarlar > Sistem > Sistem Yazılımı > Sistem Yazılımı Güncelleme" kısmına gidin.
- "Güncelleme İndirilemedi" hatası alıyorsanız veya güncelleme bulamıyorsa **sistem çalışıyor** demektir.

## Notlar
- Proxy servisi arka planda çalışmaya devam eder (Bildirim çubuğunda görünür).
- İşiniz bittiğinde PS5'teki Proxy ayarını "Kullanma" olarak değiştirmeyi unutmayın, aksi takdirde telefonunuz kapandığında PS5 internete bağlanamaz.

## Lisans
MIT License
