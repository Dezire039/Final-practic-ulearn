// Гранты
// Задача 1: Постройте график по среднему количеству рабочих мест для каждого фискального года
// Задача 2: Выведите в консоль средний размер гранта для бизнеса типа "Salon/Barbershop"
// Задача 3: Выведите в консоль тип бизнеса, предоставивший наибольшее количество
// рабочих мест, где размер гранта не превышает $55,000.00

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.*;
import java.util.*;

public class Main {
    public static List<GrantData> grantDataList;

    public static void main(String[] args) {
        // Достаем данные из файла
        grantDataList = readCSVFile("Гранты.csv");

        // Создаем и заполняем базу данных
        createSQLite();

        // Задание 1
        // Постройте график по среднему количеству рабочих мест для каждого фискального года
        buildAGraph();

        // Задание 2
        // Выведите в консоль средний размер гранта для бизнеса типа "Salon/Barbershop"
        printAverageGrantSize();

        // Задание 3
        // Выведите в консоль тип бизнеса, предоставивший наибольшее количество
        // рабочих мест, где размер гранта не превышает $55,000.00
        printBusinessType();
    }

    // Достаем данные из csv файла
    public static List<GrantData> readCSVFile(String fileName) {
        List<GrantData> grantDataList = new ArrayList<>();
        try {
            Reader reader = new FileReader(fileName);
            com.opencsv.CSVReader csvReader = new CSVReader(reader);
            List<String[]> records = csvReader.readAll();
            int count = 0;
            for (String[] record : records) {
                if (count == 0) {
                    count ++;
                    continue;
                }
                if (Objects.equals(record[3], "")) {
                    break;
                }
                GrantData grantData = new GrantData(record[0], record[1],
                        Double.parseDouble(record[2].replace("$", "").
                                replace(",", "")),
                        Integer.parseInt(record[3]),
                        record[4], Integer.parseInt(record[5]));
                grantDataList.add(grantData);
            }
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
        return grantDataList;
    }

    // Создаем и заполняем базу данных
    public static void createSQLite() {
        Connection connection = null;
        try {
            // подключение к базе данных
            connection = DriverManager.getConnection("jdbc:sqlite:GrantData.db");
            Statement statement = connection.createStatement();

            // создание таблиц
            statement.execute("CREATE TABLE IF NOT EXISTS MAINCompanyName " +
                    "(id INTEGER PRIMARY KEY, companyName TEXT, " +
                    "numberOfJobs INTEGER, id_business INTEGER)");
            statement.execute("CREATE TABLE IF NOT EXISTS BusinessTypeAndYear " +
                    "(id_business INTEGER, fiscalYear INTEGER, " +
                    "businessType TEXT, id_street INTEGER," +
                    "id_grant_size INTEGER)");
            statement.execute("CREATE TABLE IF NOT EXISTS GrantSize " +
                    "(id_grant_size INTEGER, grantSize DOUBLE)");
            statement.execute("CREATE TABLE IF NOT EXISTS StreetName " +
                    "(id_street INTEGER, streetName DOUBLE)");

            // запросы для наполнения таблиц
            Map<String, String> MapBusinessType = new HashMap<>();
            int counterForBusiness = 0;
            String str1 = "INSERT INTO 'MAINCompanyName'" +
                    "('companyName', 'numberOfJobs', 'id_business') VALUES (?, ?, ?)";
            String str2 = "INSERT INTO 'BusinessTypeAndYear'" +
                    "('id_business', 'fiscalYear', 'businessType', 'id_street', 'id_grant_size') " +
                    "VALUES (?, ?, ?, ?, ?)";
            Map<Integer, Double> MapGrantSize = new HashMap<>();
            int counterForGrant = 1;
            Map<Integer, String> MapStreetName = new HashMap<>();
            int counterForStreet = 1;
            String str3 = "INSERT INTO 'GrantSize'" +
                    "('id_grant_size', 'grantSize') " +
                    "VALUES (?, ?)";
            String str4 = "INSERT INTO 'StreetName'" +
                    "('id_street', 'streetName') " +
                    "VALUES (?, ?)";

            // наполняем таблицy GrantCompanyName
            for (GrantData arg: grantDataList) {
                if (!MapBusinessType.containsKey(arg.getBusinessType() + "; " +
                        arg.getFiscalYear())) {
                    MapBusinessType.put(arg.getBusinessType() + "; " + arg.getFiscalYear(),
                            arg.getGrantSize() + "; " + arg.getStreetName());
                    counterForBusiness++;
                }
                PreparedStatement pstmt1 = connection.prepareStatement(str1);
                pstmt1.setString(1, arg.getCompanyName());
                pstmt1.setInt(2, arg.getNumberOfJobs());
                pstmt1.setInt(3, counterForBusiness);
                pstmt1.executeUpdate();
                if (!MapGrantSize.containsValue(arg.getGrantSize())) {
                    MapGrantSize.put(counterForGrant, arg.getGrantSize());
                    counterForGrant ++;
                }
                if (!MapStreetName.containsValue(arg.getStreetName())) {
                    MapStreetName.put(counterForStreet, arg.getStreetName());
                    counterForStreet ++;
                }
            }

            // наполняем таблицy BusinessTypeAndYear
            counterForBusiness = 1;
            for(Map.Entry<String, String> entry : MapBusinessType.entrySet()) {
                PreparedStatement pstmt2 = connection.prepareStatement(str2);
                pstmt2.setInt(1, counterForBusiness);
                pstmt2.setInt(2, Integer.parseInt(entry.getKey().split(";")[1].strip()));
                pstmt2.setString(3, entry.getKey().split(";")[0].strip());
                for (Map.Entry<Integer, Double> entry2 : MapGrantSize.entrySet()) {
                    if (Objects.equals(entry2.getValue().toString(), entry.getValue().split(";")[0].strip())) {
                        pstmt2.setInt(4, entry2.getKey());
                    }
                }
                for (Map.Entry<Integer, String> entry3 : MapStreetName.entrySet()) {
                    if (Objects.equals(entry3.getValue(), entry.getValue().split(";")[1].strip())) {
                        pstmt2.setInt(5, entry3.getKey());
                    }
                }
                pstmt2.executeUpdate();
                counterForBusiness ++;
            }

            // наполняем таблицy GrantSize
            Map<Integer, Double> sortedMapGrantSize = new TreeMap<>(MapGrantSize);
            for (Map.Entry<Integer, Double> entry : sortedMapGrantSize.entrySet()) {
                PreparedStatement pstmt3 = connection.prepareStatement(str3);
                pstmt3.setInt(1, entry.getKey());
                pstmt3.setDouble(2, entry.getValue());
                pstmt3.executeUpdate();
            }

            // наполняем таблицy StreetName
            Map<Integer, String> sortedMapStreetName = new TreeMap<>(MapStreetName);
            for (Map.Entry<Integer, String> entry : sortedMapStreetName.entrySet()) {
                PreparedStatement pstmt4 = connection.prepareStatement(str4);
                pstmt4.setInt(1, entry.getKey());
                pstmt4.setString(2, entry.getValue());
                pstmt4.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close(); // закрытие соединения
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Задача 1
    public static void buildAGraph() {
        // Строки запросов к базе данных
        String strRequest1 = "SELECT FiscalYear, SUM(numberOfJobs) AS totalJobs " +
                "FROM MAINCompanyName " +
                "INNER JOIN BusinessTypeAndYear " +
                "ON MAINCompanyName.id_business = BusinessTypeAndYear.id_business " +
                "GROUP BY BusinessTypeAndYear.fiscalYear " +
                "ORDER BY BusinessTypeAndYear.fiscalYear;";
        String strRequest2 = "SELECT fiscalYear " +
                "AS fiscalYear FROM BusinessTypeAndYear " +
                "GROUP BY fiscalYear " +
                "ORDER BY fiscalYear;";

        // Делаем запросы и получаем результат
        String numberOfJobs = connectionBD("totalJobs", strRequest1).strip();
        String fiscalYears = connectionBD("fiscalYear", strRequest2).strip();

        // Создаем график по полученным значениям
        Graph graph = new Graph("Number of jobs/fiscal years (Graph)", numberOfJobs, fiscalYears);
        graph.setSize(800, 400);
        graph.setLocationRelativeTo(null);
        graph.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        graph.setVisible(true);
    }

    // Для подключения и работы с базой данных
    public static String connectionBD (String columnName, String strRequest) {
        Connection connection = null;
        StringBuilder result = new StringBuilder();
        try {
            // Подключение к базе данных
            connection = DriverManager.getConnection("jdbc:sqlite:GrantData.db");
            Statement statement = connection.createStatement();

            // Запрос к базе данных
            ResultSet resultSet = statement.executeQuery(strRequest);
            while(resultSet.next()){
                assert false;
                result.append(resultSet.getString(columnName)).append(" ");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close(); // закрытие соединения
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result.toString();
    }

    // Задача 2
    public static void printAverageGrantSize() {
        // Строка запроса к базе данных
        String strRequest =  "SELECT AVG(grantSize) " +
                "AS averageGrantSize FROM GrantSize " +
                "INNER JOIN BusinessTypeAndYear " +
                "ON GrantSize.id_grant_size = BusinessTypeAndYear.id_grant_size " +
                "WHERE BusinessTypeAndYear.businessType = 'Salon/Barbershop';";

        // Делаем запрос и получаем результат
        String result = connectionBD("averageGrantSize", strRequest);

        // Выводим результат в консоль
        System.out.println("Задача 2");
        System.out.println("Средний размер гранта для Salon/Barbershop: " + result);
        System.out.println();
    }

    // Задача 3
    public static void printBusinessType() {
        // Строка запроса к базе данных
        String strRequest = "SELECT businessType " +
                "AS oneBusinessType FROM BusinessTypeAndYear " +
                "INNER JOIN MAINCompanyName " +
                "ON BusinessTypeAndYear.id_business = MAINCompanyName.id_business " +
                "INNER JOIN GrantSize " +
                "ON BusinessTypeAndYear.id_grant_size = GrantSize.id_grant_size " +
                "WHERE GrantSize.grantSize < 55000.00 " +
                "GROUP BY BusinessTypeAndYear.businessType " +
                "ORDER BY MAINCompanyName.numberOfJobs DESC " +
                "LIMIT 1;";

        // Делаем запрос и получаем результат
        String result = connectionBD("oneBusinessType", strRequest);

        // Выводим результат в консоль
        System.out.println("Задача 3");
        System.out.println("Тип бизнеса, предоставивший наибольшее количество " +
                "рабочих мест, где размер гранта не превышает $55,000.00: " + result);
    }
}
