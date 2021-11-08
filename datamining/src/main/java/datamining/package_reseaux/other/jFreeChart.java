package datamining.package_reseaux.other;

//import com.mysql.jdbc.Connection;
import java.awt.GridLayout;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.jdbc.JDBCPieDataset;


public class jFreeChart extends javax.swing.JFrame {
    public jFreeChart() {
        initComponents();
        showPieChart();
        showPieChartJdbc();
    }

    private void initComponents() {

    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new jFreeChart().setVisible(true);
            }
        });
    }

    private void showPieChart() {
        // 1. Définir un dataset qui contient les data
        //DefaultPieDataset
        DefaultPieDataset ds = new DefaultPieDataset();
        ds.setValue("Parti du progrès contrôlé", 22.36);
        // 2. Se fournir un JFreeChart
        JFreeChart jfc = ChartFactory.createPieChart("Résulats des élections en Boursoulavie en 2008", ds, true,  true,  true);
        // 3. Fabriquer le Panel
        ChartPanel cp = new ChartPanel(jfc);
        this.getContentPane().setLayout(new GridLayout(2,1));
        this.getContentPane().add(cp);
    }

    private void showPieChartJdbc() {
        JDBCPieDataset jds = null;
        String url = "jdbc:mysql://localhost:3306/Elections";
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("Driver MySQL (com) chargé");
        Connection con = null;
        try {
            con = (Connection) DriverManager.getConnection( url, "CFBUNAT", "ViveLeParti");
            System.out.println("Connexion à la BDD Elections réalisée");
            jds = new JDBCPieDataset(con);
            String req = "SELECT * FROM Boursoulavie2004;";
            jds.executeQuery(req);
            System.out.println("Dataset chargé");
            con.close();
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        JFreeChart jfcbd = ChartFactory.createPieChart("Résulats des élections en Boursoulavie en 2004", jds, true, // générer des légendes ?
            true, // générer des tooltips ?
            true); // générer des URLs?
        // 3. Fabriquer le Panel
        ChartPanel cpbd = new ChartPanel(jfcbd);
        this.getContentPane().add(cpbd);
    }
}
