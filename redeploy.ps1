# --- AYARLAR ---
$glassfishBin = "C:\Users\ASUS\OneDrive\Belgeler\glassfish7\glassfish\bin"
$projectRoot = "c:\Users\ASUS\Downloads\OgrenciOtomasyon_Yeni\OgrenciOtomasyon"
$domainName = "domain1"
$warName = "OgrenciOtomasyon-1.0-SNAPSHOT.war"
$url = "http://localhost:8080/OgrenciOtomasyon-1.0-SNAPSHOT/"

Clear-Host
Write-Host "🚀 Antigravity Derin Temizlik ve Dağıtım Sistemi Başlıyor..." -ForegroundColor Cyan
Write-Host "------------------------------------------------------------"

# 1. GlassFish Durdur
Write-Host "🛑 1/5 Sunucu durduruluyor (Varsa kilitler kaldırılıyor)..." -ForegroundColor Yellow
cd $glassfishBin
.\asadmin.bat stop-domain $domainName

# Arka planda kalan java süreçlerini temizlemeye çalış (isteğe bağlı ama etkili)
# Stop-Process -Name "java" -Force -ErrorAction SilentlyContinue

# 2. Derin Temizlik (Kilitli dosya hatalarını önler)
Write-Host "🧹 2/5 Önbellek ve eski uygulama dosyaları temizleniyor..." -ForegroundColor Yellow
$domainPath = "C:\Users\ASUS\OneDrive\Belgeler\glassfish7\glassfish\domains\$domainName"

# Uygulama, generated ve osgi-cache klasörlerini temizle
$foldersToClean = @("$domainPath\applications\*", "$domainPath\generated\*", "$domainPath\osgi-cache\*")
foreach ($folder in $foldersToClean) {
    if (Test-Path $folder) {
        Remove-Item -Recurse -Force $folder -ErrorAction SilentlyContinue
    }
}

# 3. Maven Build
Write-Host "📦 3/5 Proje yeniden derleniyor (Maven)..." -ForegroundColor Yellow
cd $projectRoot
.\mvnw.cmd clean package

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ HATA: Maven derleme başarısız oldu! Lütfen kodunuzdaki hataları kontrol edin." -ForegroundColor Red
    exit
}

# 4. Sunucu Başlat ve Deploy
Write-Host "⚡ 4/5 Sunucu başlatılıyor..." -ForegroundColor Yellow
cd $glassfishBin
.\asadmin.bat start-domain $domainName

Write-Host "🚀 4.5/5 Uygulama yükleniyor (Deploy)..." -ForegroundColor Yellow
.\asadmin.bat deploy --force "$projectRoot\target\$warName"

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ HATA: Deploy başarısız oldu!" -ForegroundColor Red
    exit
}

# 5. Tamamlandı ve Aç
Write-Host "------------------------------------------------------------"
Write-Host "✅ İŞLEM BAŞARIYLA TAMAMLANDI!" -ForegroundColor Green
Write-Host "🌐 Tarayıcı açılıyor: $url" -ForegroundColor Cyan
Start-Process $url
