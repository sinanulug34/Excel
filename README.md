# Excel

- Fizy için test edilmiş ve onaylanmıştır. Bip raporlaması için test edilmelidir.

# Kullanım

- JenkinsService sınıfında tanımlı fizyPath değişkeninin doğru URL'i gösterdiğinden emin olunmalıdır.

- base.xlsx içindeki verilerin doğruluğundan emin olunmalıdır. Özellikle A Kolonu(Paket Özelliği), B Kolonu(İndirme)
ve D Kolonundan(Senaryo Numarası) herhangi birindeki hata yanlış raporlamaya sebep olacaktır. Testlerin eşleştirilmesi
amacıyla üretilen ID bu 3 kolonu esas almaktadır.

