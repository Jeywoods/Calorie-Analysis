<div align="center">

<img src="https://img.shields.io/badge/Android-7.0%2B-3DDC84?style=for-the-badge&logo=android&logoColor=white"/>
<img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white"/>
<img src="https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white"/>
<img src="https://img.shields.io/badge/TensorFlow_Lite-FF6F00?style=for-the-badge&logo=tensorflow&logoColor=white"/>

<br/><br/>

# 🥗 Анализатор калорий

### Сфотографируй блюдо — узнай всё о его составе

Нейросеть распознаёт еду по фото, считает КБЖУ и ведёт дневник питания с визуальными отчётами.

<br/>

[📲 Скачать APK](#-запуск) · [📖 Документация](#-архитектура) · [🐛 Сообщить о баге](https://github.com/your-username/food-calorie-analyzer/issues)

</div>

---

## ✨ Возможности

<table>
<tr>
<td width="50%">

**📸 Распознавание блюд**
Сфотографируйте еду или выберите из галереи — нейросеть Food101 определит блюдо за секунды.

**🔍 База данных**
Более 100 блюд с поиском по названию и мгновенной фильтрацией.

**⚖️ Полный нутриентный профиль**
Белки, жиры, углеводы, клетчатка, насыщенные жиры, сахар, натрий, калий, холестерин.

**📊 Визуализация**
Круговая диаграмма баланса макронутриентов для каждого приёма пищи.

</td>
<td width="50%">

**📅 Дневник питания**
Запись приёмов пищи по дням с возможностью редактировать вес порций.

**📈 Календарь питания**
Просмотр статистики за любой день через встроенный календарь.

**💡 Советы по питанию**
Персонализированные рекомендации на главном экране.


</td>
</tr>
</table>

---

## 🛠 Технологии

| Категория | Стек |
|:----------|:-----|
| **Язык** | Kotlin |
| **UI** | Jetpack Compose + Material 3 |
| **DI** | Hilt |
| **База данных** | Room |
| **Нейросеть** | TensorFlow Lite (Food101) |
| **API** | Retrofit + OkHttp (CalorieNinjas) |
| **Изображения** | Coil |
| **Навигация** | Navigation Compose |
| **Асинхронность** | Kotlin Coroutines + Flow |

---

## 🏛 Архитектура

Проект построен на **Clean Architecture** с чётким разделением ответственности между слоями.

```
📦 foodcalorieanalyzer
├── 📂 data
│   ├── 📂 local        # Room, DAO, Entity
│   ├── 📂 remote       # API, DTO
│   ├── 📂 mapper       # Маппинг данных
│   └── 📂 ml           # TensorFlow модели
├── 📂 domain
│   ├── 📂 model        # Бизнес-модели
│   ├── 📂 repository   # Интерфейсы репозиториев
│   └── 📂 usecase      # Use cases
├── 📂 presentation
│   ├── 📂 analyzer     # Экран распознавания
│   ├── 📂 diary        # Дневник питания
│   ├── 📂 history      # История и статистика
│   └── 📂 onboarding   # Онбординг
└── 📂 di               # Hilt модули
```

### Поток данных

```
Фото → TFLite (Food101)
           ↓
     Название блюда → CalorieNinjas API
                              ↓
                      Нутриенты → Room DB
                                      ↓
                               Compose UI
```

---

## 🚀 Запуск

### Требования

- Android Studio Hedgehog или новее
- JDK 17+
- Android 7.0+ (API 24)

### Установка

**1. Клонируйте репозиторий**
```bash
git clone https://github.com/your-username/food-calorie-analyzer.git
cd food-calorie-analyzer
```

**2. Добавьте API-ключ**

Создайте файл `gradle.properties` в корне проекта:
```properties
CALORIE_NINJAS_API_KEY=your_api_key_here
```

> Получить ключ можно бесплатно на [calorieninjas.com](https://calorieninjas.com)

**3. Запустите приложение**
```bash
./gradlew installDebug
```

Или откройте проект в Android Studio и нажмите **Run ▶️**

---

## 📱 Скриншоты

> _Добавьте скриншоты в папку `/screenshots` и раскомментируйте блок ниже_

<!--
<div align="center">
<img src="screenshots/home.png" width="200"/>
<img src="screenshots/analyzer.png" width="200"/>
<img src="screenshots/diary.png" width="200"/>
<img src="screenshots/history.png" width="200"/>
</div>
-->

---

## 📄 Лицензия

Распространяется под лицензией MIT. Подробнее — в файле [LICENSE](LICENSE).

---

<div align="center">

</div>
