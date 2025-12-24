# MarketPulse

Android-приложение для просмотра финансовых данных:  
котировок акций, криптовалют и курсов фиатных валют.

Проект выполнен в рамках семестровой работы по дисциплине  
«Мобильная разработка».

---

## Стек технологий

- Kotlin  
- Android SDK  
- Jetpack Compose (Material 3)  
- MVVM  
- Navigation Compose  
- Kotlin Coroutines  
- Flow / StateFlow  
- Retrofit  
- OkHttp  
- Room  
- DataStore  

---

## Используемые API

- **Finnhub API** — котировки акций и поиск тикеров  
- **Currency API** — курсы фиатных валют  

### Примечание
Finnhub API может быть недоступен без VPN у некоторых интернет-провайдеров.  
Для корректной работы рекомендуется использовать VPN или провайдера Goodline.

---

## Основной функционал

- Главный экран с избранными активами и курсами валют  
- Поиск акций по тикеру или названию  
- Экран детальной информации об активе  
- Добавление и удаление активов из избранного  
- Экран настроек (базовая валюта, TTL кэша, автообновление)  
- Поддержка оффлайн-режима  

---

## Скриншоты

### Главный экран
<img width="415" height="685" src="https://github.com/user-attachments/assets/d1251173-d47f-4ec1-beb4-7be5981aac43" />

### Поиск
<img width="385" height="688" src="https://github.com/user-attachments/assets/3d4a0d27-dcbf-40fb-8a60-313e577aa250" />

### Детали актива
<img width="387" height="571" src="https://github.com/user-attachments/assets/1073fd2c-e443-4b75-a734-63a23205af9c" />

### Избранное
<img width="380" height="688" src="https://github.com/user-attachments/assets/ed12ba45-0790-4219-8959-b08a516647a6" />

### Оффлайн-режим
<img width="389" height="695" src="https://github.com/user-attachments/assets/da6b5401-2497-4f14-9097-0ef3858f0b3a" />

### Настройки фиата
<img width="380" height="678" alt="image" src="https://github.com/user-attachments/assets/56611160-8609-4642-8c5d-47aca1f50ed7" />


---

## Архитектура

Проект реализован с использованием архитектуры MVVM и разделён на слои:

- presentation — UI и ViewModel  
- domain — доменные модели и интерфейсы  
- data — работа с сетью и локальной базой данных  
- core — инфраструктурные и вспомогательные компоненты  

ViewModel не взаимодействует напрямую с API или базой данных.  
Все данные получаются через репозиторий.

