import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;

public class Graph extends JFrame {
    private int width;
    private int height;

    public Graph(String title, String numberOfJobs, String fiscalYears) {
        super(title);
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Полученные строки разбиваем в массивы
        String[] yearsArray = fiscalYears.split(" ");
        String[] jobsArray = numberOfJobs.split(" ");

        // Добавления набора данных для графика
        for (int i = 0; i < yearsArray.length; i++) {
            dataset.setValue(Integer.parseInt(jobsArray[i]), "Jobs", yearsArray[i]);
        }

        // Создания столбчатой диаграммы, принимая заголовок графика,
        // заголовки осей и созданный набор данных
        JFreeChart chart = ChartFactory.createBarChart("Задача 1",
                "Fiscal years", "Number of jobs", dataset);

        // Создание панели для графика
        ChartPanel panel = new ChartPanel(chart);
        // Установка панели в качестве содержимого фрейма
        setContentPane(panel);
    }

    public void setGraphSize(int width, int height) {
        this.width = width;
        this.height = height;
        setSize(width, height);
    }
}
