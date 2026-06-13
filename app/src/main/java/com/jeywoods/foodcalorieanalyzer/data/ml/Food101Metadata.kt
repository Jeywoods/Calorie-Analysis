package com.jeywoods.foodcalorieanalyzer.data.ml

import com.jeywoods.foodcalorieanalyzer.domain.model.FoodItem

object Food101Metadata {
    private val foods = listOf(
        FoodItem(0, "apple_pie", "Яблочный пирог", 237f, 2.4f, 11f, 34f),
        FoodItem(1, "baby_back_ribs", "Ребрышки BBQ", 292f, 19f, 23f, 3f),
        FoodItem(2, "baklava", "Пахлава", 412f, 6f, 26f, 38f),
        FoodItem(3, "beef_carpaccio", "Карпаччо из говядины", 170f, 19f, 10f, 1f),
        FoodItem(4, "beef_tartare", "Тартар из говядины", 176f, 20f, 10f, 1f),
        FoodItem(5, "beet_salad", "Свекольный салат", 73f, 2.3f, 4.8f, 7.6f),
        FoodItem(6, "beignets", "Пончики бенье", 390f, 6f, 21f, 44f),
        FoodItem(7, "bibimbap", "Бибимбап", 143f, 7f, 5f, 18f),
        FoodItem(8, "bread_pudding", "Хлебный пудинг", 230f, 6f, 9f, 32f),
        FoodItem(9, "breakfast_burrito", "Буррито на завтрак", 203f, 10f, 9f, 20f),
        FoodItem(10, "bruschetta", "Брускетта", 213f, 5f, 8f, 31f),
        FoodItem(11, "caesar_salad", "Салат Цезарь", 174f, 11f, 12f, 6f),
        FoodItem(12, "cannoli", "Канноли", 340f, 8f, 17f, 39f),
        FoodItem(13, "caprese_salad", "Салат Капрезе", 139f, 8f, 9f, 5f),
        FoodItem(14, "carrot_cake", "Морковный торт", 399f, 5f, 22f, 48f),
        FoodItem(15, "ceviche", "Севиче", 110f, 16f, 3f, 5f),
        FoodItem(16, "cheesecake", "Чизкейк", 321f, 5.5f, 20f, 30f),
        FoodItem(17, "cheese_plate", "Сырная тарелка", 350f, 22f, 28f, 2f),
        FoodItem(18, "chicken_curry", "Куриное карри", 155f, 14f, 8f, 6f),
        FoodItem(19, "chicken_quesadilla", "Кесадилья с курицей", 255f, 15f, 14f, 18f),
        FoodItem(20, "chicken_wings", "Куриные крылышки", 290f, 20f, 22f, 3f),
        FoodItem(21, "chocolate_cake", "Шоколадный торт", 371f, 4.5f, 17f, 53f),
        FoodItem(22, "chocolate_mousse", "Шоколадный мусс", 264f, 4f, 17f, 25f),
        FoodItem(23, "churros", "Чуррос", 415f, 5f, 22f, 50f),
        FoodItem(24, "clam_chowder", "Клэм-чаудер", 123f, 7f, 6f, 11f),
        FoodItem(25, "club_sandwich", "Клаб-сэндвич", 268f, 18f, 14f, 19f),
        FoodItem(26, "crab_cakes", "Крабовые котлеты", 206f, 16f, 12f, 10f),
        FoodItem(27, "creme_brulee", "Крем-брюле", 347f, 5f, 28f, 18f),
        FoodItem(28, "croque_madame", "Крок-мадам", 256f, 16f, 15f, 15f),
        FoodItem(29, "cup_cakes", "Капкейки", 356f, 4f, 16f, 49f),
        FoodItem(30, "deviled_eggs", "Фаршированные яйца", 172f, 11f, 13f, 3f),
        FoodItem(31, "donuts", "Пончики", 452f, 5f, 25f, 51f),
        FoodItem(32, "dumplings", "Пельмени/димсамы", 170f, 7f, 6f, 22f),
        FoodItem(33, "edamame", "Эдамаме", 121f, 12f, 5f, 9f),
        FoodItem(34, "eggs_benedict", "Яйца Бенедикт", 226f, 13f, 16f, 8f),
        FoodItem(35, "escargots", "Эскарго", 135f, 16f, 6f, 4f),
        FoodItem(36, "falafel", "Фалафель", 333f, 13f, 18f, 32f),
        FoodItem(37, "filet_mignon", "Филе-миньон", 218f, 26f, 12f, 1f),
        FoodItem(38, "fish_and_chips", "Рыба с картофелем фри", 280f, 15f, 15f, 22f),
        FoodItem(39, "foie_gras", "Фуа-гра", 462f, 11f, 44f, 4f),
        FoodItem(40, "french_fries", "Картофель фри", 312f, 3.4f, 15f, 41f),
        FoodItem(41, "french_onion_soup", "Французский луковый суп", 89f, 4f, 4f, 9f),
        FoodItem(42, "french_toast", "Французские тосты", 229f, 7f, 11f, 25f),
        FoodItem(43, "fried_calamari", "Жареные кальмары", 175f, 17f, 8f, 9f),
        FoodItem(44, "fried_rice", "Жареный рис", 163f, 4.3f, 4.8f, 26f),
        FoodItem(45, "frozen_yogurt", "Замороженный йогурт", 159f, 5.5f, 5f, 24f),
        FoodItem(46, "garlic_bread", "Чесночный хлеб", 350f, 8f, 16f, 44f),
        FoodItem(47, "gnocchi", "Ньокки", 177f, 4f, 4f, 31f),
        FoodItem(48, "greek_salad", "Греческий салат", 90f, 3f, 6f, 6f),
        FoodItem(49, "grilled_cheese_sandwich", "Сэндвич с сыром", 360f, 15f, 21f, 28f),
        FoodItem(50, "grilled_salmon", "Лосось на гриле", 280f, 25f, 19f, 0f),
        FoodItem(51, "guacamole", "Гуакамоле", 160f, 2f, 15f, 9f),
        FoodItem(52, "gyoza", "Гёдза", 200f, 9f, 8f, 23f),
        FoodItem(53, "hamburger", "Гамбургер", 295f, 17f, 14f, 24f),
        FoodItem(54, "hot_and_sour_soup", "Кисло-острый суп", 58f, 4f, 1.5f, 7f),
        FoodItem(55, "hot_dog", "Хот-дог", 290f, 10f, 17f, 24f),
        FoodItem(56, "huevos_rancheros", "Уэвос ранчерос", 210f, 10f, 13f, 14f),
        FoodItem(57, "hummus", "Хумус", 166f, 8f, 10f, 14f),
        FoodItem(58, "ice_cream", "Мороженое", 207f, 3.5f, 11f, 24f),
        FoodItem(59, "lasagna", "Лазанья", 135f, 7f, 6f, 14f),
        FoodItem(60, "lobster_bisque", "Лобстерный биск", 95f, 7f, 5f, 5f),
        FoodItem(61, "lobster_roll_sandwich", "Ролл с лобстером", 249f, 16f, 11f, 21f),
        FoodItem(62, "macaroni_and_cheese", "Макароны с сыром", 164f, 7f, 7f, 19f),
        FoodItem(63, "macarons", "Макаруны", 410f, 6f, 18f, 56f),
        FoodItem(64, "miso_soup", "Мисо-суп", 35f, 2.5f, 1f, 4f),
        FoodItem(65, "mussels", "Мидии", 86f, 12f, 2.2f, 3.7f),
        FoodItem(66, "nachos", "Начос", 343f, 9f, 19f, 34f),
        FoodItem(67, "omelette", "Омлет", 154f, 11f, 11f, 1.6f),
        FoodItem(68, "onion_rings", "Луковые кольца", 348f, 4.5f, 19f, 40f),
        FoodItem(69, "oysters", "Устрицы", 68f, 7f, 2.5f, 4.8f),
        FoodItem(70, "pad_thai", "Пад-тай", 153f, 6f, 5f, 21f),
        FoodItem(71, "paella", "Паэлья", 151f, 7f, 5f, 20f),
        FoodItem(72, "pancakes", "Блины", 227f, 6.4f, 9.7f, 28f),
        FoodItem(73, "panna_cotta", "Панна-котта", 298f, 3f, 24f, 18f),
        FoodItem(74, "peking_duck", "Утка по-пекински", 337f, 19f, 28f, 2f),
        FoodItem(75, "pho", "Фо", 45f, 3f, 1f, 6f),
        FoodItem(76, "pizza", "Пицца", 266f, 11f, 10f, 33f),
        FoodItem(77, "pork_chop", "Свиная отбивная", 242f, 27f, 14f, 0f),
        FoodItem(78, "poutine", "Путин", 258f, 7f, 13f, 28f),
        FoodItem(79, "prime_rib", "Ростбиф", 360f, 20f, 30f, 0f),
        FoodItem(80, "pulled_pork_sandwich", "Сэндвич со свининой", 270f, 20f, 12f, 20f),
        FoodItem(81, "ramen", "Рамен", 97f, 4f, 2.5f, 15f),
        FoodItem(82, "ravioli", "Равиоли", 191f, 8f, 6f, 26f),
        FoodItem(83, "red_velvet_cake", "Красный бархатный торт", 368f, 4f, 19f, 46f),
        FoodItem(84, "risotto", "Ризотто", 166f, 5f, 5f, 25f),
        FoodItem(85, "samosa", "Самоса", 262f, 5f, 13f, 31f),
        FoodItem(86, "sashimi", "Сашими", 127f, 21f, 4f, 1f),
        FoodItem(87, "scallops", "Морские гребешки", 111f, 20f, 1.4f, 3.4f),
        FoodItem(88, "seaweed_salad", "Салат из водорослей", 45f, 1.7f, 2.5f, 5f),
        FoodItem(89, "shrimp_and_grits", "Креветки с гритс", 150f, 10f, 6f, 14f),
        FoodItem(90, "spaghetti_bolognese", "Спагетти болоньезе", 132f, 8f, 5f, 14f),
        FoodItem(91, "spaghetti_carbonara", "Спагетти карбонара", 255f, 11f, 14f, 21f),
        FoodItem(92, "spring_rolls", "Спринг-роллы", 98f, 3f, 3f, 15f),
        FoodItem(93, "steak", "Стейк", 271f, 25f, 19f, 0f),
        FoodItem(94, "strawberry_shortcake", "Клубничный торт", 280f, 4f, 13f, 37f),
        FoodItem(95, "sushi", "Суши", 145f, 6f, 3f, 24f),
        FoodItem(96, "tacos", "Тако", 226f, 11f, 12f, 19f),
        FoodItem(97, "takoyaki", "Такояки", 155f, 6f, 6f, 19f),
        FoodItem(98, "tiramisu", "Тирамису", 354f, 6f, 21f, 35f),
        FoodItem(99, "tuna_tartare", "Тартар из тунца", 140f, 20f, 5f, 3f),
        FoodItem(100, "waffles", "Вафли", 291f, 8f, 14f, 33f)
    )

    fun getAllFoods(): List<FoodItem> = foods

    fun getFoodById(id: Int): FoodItem? = foods.getOrNull(id)

    fun getFoodByEnglishName(name: String): FoodItem? =
        foods.find { it.englishName.equals(name, ignoreCase = true) }

    fun searchFoods(query: String): List<FoodItem> =
        foods.filter {
            it.russianName.contains(query, ignoreCase = true) ||
                    it.englishName.contains(query, ignoreCase = true)
        }

    fun getFoodCount(): Int = foods.size
}