import javax.swing.*;
import java.sql.*;
import java.util.*;

public class SQLite {
    static Map<String, String> MapBusinessType = new HashMap<>();
    static Map<Integer, Double> MapGrantSize = new HashMap<>();
    static Map<Integer, String> MapStreetName = new HashMap<>();

    // Создаем и заполняем базу данных
    public static void createSQLiteDB(List<GrantData> grantDataList) {
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
                    "(id_street INTEGER, streetName TEXT)");

            // Наполнение таблиц
            fillingMainTable(connection, grantDataList);

            fillingBusinessTypeAndYearTable(connection);

            fillingGrantSizeTable(connection);

            fillingStreetNameTable(connection);

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

    // наполняем таблицy MAINCompanyName
    private static void fillingMainTable(Connection connection, List<GrantData> grantDataList) {
        String strRequestForMain = "INSERT INTO 'MAINCompanyName'" +
                "('companyName', 'numberOfJobs', 'id_business') VALUES (?, ?, ?)";
        int counterForBusiness = 0;
        int counterForGrant = 1;
        int counterForStreet = 1;
        try {
            for (GrantData arg : grantDataList) {
                if (!MapBusinessType.containsKey(arg.getBusinessType() + "; " +
                        arg.getFiscalYear())) {
                    MapBusinessType.put(arg.getBusinessType() + "; " + arg.getFiscalYear(),
                            arg.getGrantSize() + "; " + arg.getStreetName());
                    counterForBusiness++;
                }
                PreparedStatement pstmt = connection.prepareStatement(strRequestForMain);
                pstmt.setString(1, arg.getCompanyName());
                pstmt.setInt(2, arg.getNumberOfJobs());
                pstmt.setInt(3, counterForBusiness);
                pstmt.executeUpdate();
                if (!MapGrantSize.containsValue(arg.getGrantSize())) {
                    MapGrantSize.put(counterForGrant, arg.getGrantSize());
                    counterForGrant++;
                }
                if (!MapStreetName.containsValue(arg.getStreetName())) {
                    MapStreetName.put(counterForStreet, arg.getStreetName());
                    counterForStreet++;
                }
                pstmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // наполняем таблицy BusinessTypeAndYear
    private static void fillingBusinessTypeAndYearTable (Connection connection) {
        String strRequestForBusinessType = "INSERT INTO 'BusinessTypeAndYear'" +
                "('id_business', 'fiscalYear', 'businessType', 'id_street', 'id_grant_size') " +
                "VALUES (?, ?, ?, ?, ?)";
        int counterForBusiness = 1;
        try {
            for (Map.Entry<String, String> entry : MapBusinessType.entrySet()) {
                PreparedStatement pstmt = connection.prepareStatement(strRequestForBusinessType);
                pstmt.setInt(1, counterForBusiness);
                pstmt.setInt(2, Integer.parseInt(entry.getKey().split(";")[1].strip()));
                pstmt.setString(3, entry.getKey().split(";")[0].strip());
                for (Map.Entry<Integer, Double> entry2 : MapGrantSize.entrySet()) {
                    if (Objects.equals(entry2.getValue().toString(), entry.getValue().split(";")[0].strip())) {
                        pstmt.setInt(4, entry2.getKey());
                    }
                }
                for (Map.Entry<Integer, String> entry3 : MapStreetName.entrySet()) {
                    if (Objects.equals(entry3.getValue(), entry.getValue().split(";")[1].strip())) {
                        pstmt.setInt(5, entry3.getKey());
                    }
                }
                pstmt.executeUpdate();
                pstmt.close();
                counterForBusiness++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // наполняем таблицy GrantSize
    private static void fillingGrantSizeTable (Connection connection) {
        String strRequestForGrantSize = "INSERT INTO 'GrantSize'" +
                "('id_grant_size', 'grantSize') " +
                "VALUES (?, ?)";
        try {
            Map<Integer, Double> sortedMapGrantSize = new TreeMap<>(MapGrantSize);
            for (Map.Entry<Integer, Double> entry : sortedMapGrantSize.entrySet()) {
                PreparedStatement pstmt = connection.prepareStatement(strRequestForGrantSize);
                pstmt.setInt(1, entry.getKey());
                pstmt.setDouble(2, entry.getValue());
                pstmt.executeUpdate();
                pstmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // наполняем таблицy StreetName
    private static void fillingStreetNameTable (Connection connection) {
        String strRequestForStreetName = "INSERT INTO 'StreetName'" +
                "('id_street', 'streetName') " +
                "VALUES (?, ?)";
        try {
            Map<Integer, String> sortedMapStreetName = new TreeMap<>(MapStreetName);
            for (Map.Entry<Integer, String> entry : sortedMapStreetName.entrySet()) {
                PreparedStatement pstmt = connection.prepareStatement(strRequestForStreetName);
                pstmt.setInt(1, entry.getKey());
                pstmt.setString(2, entry.getValue());
                pstmt.executeUpdate();
                pstmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Метод для подключения и работы с базой данных
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
        graph.setGraphSize(800, 400);
        graph.setLocationRelativeTo(null);
        graph.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        graph.setVisible(true);
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
