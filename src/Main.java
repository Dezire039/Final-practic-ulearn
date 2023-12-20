// Гранты
// Задача 1: Постройте график по среднему количеству рабочих мест для каждого фискального года
// Задача 2: Выведите в консоль средний размер гранта для бизнеса типа "Salon/Barbershop"
// Задача 3: Выведите в консоль тип бизнеса, предоставивший наибольшее количество
// рабочих мест, где размер гранта не превышает $55,000.00

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        // Достаем данные из файла
        //List<GrantData> grantDataList = readCSVFile("Гранты.csv");

        // Создаем и заполняем базу данных
        //SQLite.createSQLiteDB(grantDataList);

        // Задание 1
        // Постройте график по среднему количеству рабочих мест для каждого фискального года
        SQLite.buildAGraph();

        // Задание 2
        // Выведите в консоль средний размер гранта для бизнеса типа "Salon/Barbershop"
        SQLite.printAverageGrantSize();

        // Задание 3
        // Выведите в консоль тип бизнеса, предоставивший наибольшее количество
        // рабочих мест, где размер гранта не превышает $55,000.00
        SQLite.printBusinessType();
    }

    // Достаем данные из csv файла
    public static List<GrantData> readCSVFile(String fileName) {
        List<GrantData> grantDataListСreate = new ArrayList<>();
        try {
            Reader reader = new FileReader(fileName);
            com.opencsv.CSVReader csvReader = new CSVReader(reader);
            List<String[]> records = csvReader.readAll();
            int count = 0;
            for (String[] record : records) {
                // Пропускаем первую строку csv файла тк она является заголовками
                if (count == 0) {
                    count ++;
                    continue;
                }
                // Пропускаем запись, если требуемое поле пустое
                if (record[0] == null || record[0].isEmpty()) {
                    continue;
                }
                GrantData grantData = new GrantData(record[0], record[1],
                        Double.parseDouble(record[2].replace("$", "").
                                replace(",", "")),
                        Integer.parseInt(record[3]),
                        record[4], Integer.parseInt(record[5]));
                grantDataListСreate.add(grantData);
            }
            reader.close();
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
        return grantDataListСreate;
    }
}
