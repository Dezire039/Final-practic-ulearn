# ГРАНТЫ
# Final-practic-ulearn
Java. Основы программирования на РТФ. Итоговая практика


## Описание класса GrantData
- Содержит конструктор, принимающий типы: String companyName, String streetName, double grantSize, int fiscalYear, String businessType, int numberOfJobs
- Геттеры и сеттеры
- Метод printGrantData(GrantData grantData), который выводит объект GrantData в консоль


## Описание класса Main
### Метод main(String[] args)
Последовательно вызывает методы:
 - Для вытаскивания данных из csv файла
 - Для создания и наполнения базы данных GrantData.db
 - Выполение задания 1:
   Постройте график по среднему количеству рабочих мест для каждого фискального года
 - Выполение задания 2:
   Выведите в консоль средний размер гранта для бизнеса типа "Salon/Barbershop"
 - Выполение задания 3:
   Выведите в консоль тип бизнеса, предоставивший наибольшее количество рабочих мест, где размер гранта не превышает $55,000.00
 
### Метод readCSVFile(String fileName)
- Получает имя csv файла
- С помощью com.opencsv.CSVReader вытаскивает данные из файла и заполняет массив public static List<GrantData> grantDataList объектами класса GrantData

## Описание класса SQLite
### Метод createSQLiteDB()
- Создает базу данных GrantData.db и в бд создает набор таблиц (по 3-ей нормальной форме)
- Заполняет ее данными из массива public static List<GrantData> grantDataList

### Метод fillingMainTable()
- Наполняет таблицy MAINCompanyName

### Метод fillingBusinessTypeAndYearTable()
- Наполняет таблицy BusinessTypeAndYear

### Метод fillingGrantSizeTable()
- Наполняет таблицy GrantSize

### Метод fillingStreetNameTable()
- Наполняет таблицy StreetName
  
### Метод connectionBD (String columnName, String strRequest)
- Для запроса к базе данных GrantData.db и получения данных запроса SELECT
- Принимает строку запроса strRequest и название столбца, данные которого возвращает columnName

### Метод buildAGraph() (ЗАДАЧА 1)
- Передает строки запроса к базе данных и имена столбца, которое необходимо вернуть в метод connectionBD
- Полученные строки передает в конструктор класса Graph
- Выводит график: ![image](https://github.com/Dezire039/Final-practic-ulearn/assets/114075427/02373b97-a6c0-426e-b87e-afb7d1d4a719)

### Методы printAverageGrantSize() и printBusinessType() (ЗАДАЧА 2 и ЗАДАЧА 3)
- Передают SELECT запрос и имя столбца, значения которого нужно вернуть в метод connectionBD
- Выводят полученные значения в консоль: ![image](https://github.com/Dezire039/Final-practic-ulearn/assets/114075427/6b34d4d6-c328-417f-8a62-f5613bffdc93)

## Описание класса Graph
- Наследует класс JFrame
- Коструктор получает: String title (название графика), String numberOfJobs и String fiscalYears - значения по горизонтали и вертикали графика. Значения в строке разделены пробелом
