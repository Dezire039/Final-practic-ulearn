# Final-practic-ulearn
Java. Основы программирования на РТФ. Итоговая практика, гранты


## Описание класса GrantData
- Содержит конструктор, принимающий типы: String companyName, String streetName, double grantSize, int fiscalYear, String businessType, int numberOfJobs
- Геттеры и сеттеры
- Метод printGrantData(GrantData grantData), который выводит объект GrantData в консоль


## Описание класса Main
### Метод main(String[] args)
Последовательно вызывает методы:
 - Для вытаскивания данных из csv файла
 - Для создания и наполнения базы данных GrantData.db
 - Выполения задания 1:
   Постройте график по среднему количеству рабочих мест для каждого фискального года
 - Выполения задания 2:
   Выведите в консоль средний размер гранта для бизнеса типа "Salon/Barbershop"
 - Выполения задания 3:
   Выведите в консоль тип бизнеса, предоставивший наибольшее количество рабочих мест, где размер гранта не превышает $55,000.00
 
### Метод readCSVFile(String fileName)
- Получает имя csv файла
- С помощью com.opencsv.CSVReader вытаскивает данные из файла и заполняет массив public static List<GrantData> grantDataList объектами класса GrantData

### Метод createSQLite()
- Создает базу данных GrantData.db и заполняет ее данными из массива public static List<GrantData> grantDataList

### Метод connectionBD (String columnName, String strRequest)
- Доя запроса к базе данных GrantData.db и получения данных запроса SELECT
- Принимает строку запроса strRequest и название столбца, данные которого возвращает columnName

### Метод buildAGraph() (ЗАДАЧА 1)
- Передает строки запроса к базе данных и имена столбца, которое необходимо вернуть в метод connectionBD
- Полученные строки передает в конструктор класса Graph
- Выводит график: ![image](https://github.com/Dezire039/Final-practic-ulearn/assets/114075427/f9c1b0fc-de8c-4508-ad6b-887f1226a909)

### Методы printAverageGrantSize() и printBusinessType() (ЗАДАЧА 2 и ЗАДАЧА 3)
- Передают SELECT запрос и имя столбца, значения которого нужно вернуть в метод connectionBD
- Выводят полученные значения в консоль: ![Uploading image.png…]()


## Описание класса Graph
- Наследуюет класс JFrame
- Коструктор получает: String title (название графика), String numberOfJobs и String fiscalYears - значения по горизонтали и вертикали графика. Значения в строке разделены пробелом

