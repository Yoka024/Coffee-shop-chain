# Coffee Shop Chain 

Посилання на рендер:
https://coffee-shop-chain.onrender.com

### 1. Запуск додатку
```bash
# Через IntelliJ IDEA: запустіть CoffeeShopChainApplication.java
```

### 2. браузер
Перейдіть на: `http://localhost:8080`

### 3. Реєстрація та вхід
- **Зареєструйтеся** - створіть новий акаунт
- **Увійдіть** - використайте свої облікові дані
- **Дашборд** - після входу отримаєте доступ до панелі управління

##  Можливості

-  **Автентифікація** - реєстрація, логін, JWT токени
-  **OAuth2** - вхід через Google
-  **REST API** 
-  **База даних** 
-  **Дашборд** - управління кав'ярнями та меню
-  **Spring Security** - захист маршрутів
-  **Логування** - ELK стек 

## 🛠 Технології

- **Backend**: Spring Boot 3.4.4, Spring Security, JWT
- **База даних**:  PostgreSQL 
- **Frontend**: Thymeleaf, HTML/CSS
- **Безпека**: JWT + Session + OAuth2
- **Логування**: Logstash + ELK Stack

## Маршрути

- `/` - Головна сторінка
- `/auth/register` - Реєстрація
- `/auth/login` - Вхід
- `/dashboard` - Панель управління (потребує авторизації)
- `/h2-console` - H2 Database Console

##  Тестування

Додаток автоматично створює тестові дані:
- **Кав'ярня**: "Central Coffee"
- **Меню**: Еспресо, Капучино, Круасан


## 📋 TODO

- [ ] Docker контейнери
- [ ] Деплой на Render
- [ ] GitHub 
