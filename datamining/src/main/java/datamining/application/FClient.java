package datamining.application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultComboBoxModel;

import datamining.package_reseaux.other.MySQL;



public class FClient extends javax.swing.JFrame {
    public FClient() {
        initComponents();
    }

    //@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LMois = new javax.swing.JComboBox<>();
        LCompagnie = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        LRequete = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        BValider = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        LMois.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tous les mois" }));

        LCompagnie.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Toutes les compagnies" }));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Mois & Compagnie");

        LRequete.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "REG_CORR_LUG", "REG_CORR_LUG_PLUS", "ANOVA_1_LUG", "ANOVA_2_LUG_HF" }));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Requête");

        BValider.setText("Valider");
        BValider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BValiderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(LMois, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(LCompagnie, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(144, 144, 144)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(166, 166, 166)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(157, 157, 157)
                        .addComponent(BValider))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addComponent(LRequete, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LMois, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LCompagnie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(LRequete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(BValider)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BValiderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BValiderActionPerformed
        Client.Connection(LMois.getSelectedItem().toString(), LCompagnie.getSelectedItem().toString(), LRequete.getSelectedItem().toString());
    }//GEN-LAST:event_BValiderActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    //Class driver = Class.forName("com.mysql.cj.jdbc.Driver");
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } 
                catch (ClassNotFoundException e) {
                    System.out.println("Driver MySQL non chargé " + e.getMessage());
                }

                try {
                    Connection con = MySQL.MySQL_Connexion("bd_airport", "3306", "localhost", "hugo", "hugo");
                    Statement instruc = con.createStatement();
                    System.out.println("--Après le query--");

                    FClient e = new FClient();

                    e.FillUI(con, instruc);
                }
                catch (SQLException e) {
                    System.out.println("Erreur JDBC-OBCD : " + e.getMessage() + " ** " + e.getSQLState() + "--\n\n");
                }
            }
        });
    }

    private void FillUI(Connection con, Statement instruc) {
        try {
            ResultSet resultat = instruc.executeQuery("SELECT DISTINCT EXTRACT(MONTH FROM dateVol) FROM vols");

            DefaultComboBoxModel<String> dcbm = new DefaultComboBoxModel<String>();
            dcbm.addElement("Tous les mois");
            while(resultat.next()) {
                switch(Integer.parseInt(resultat.getString(1))) {
                    case 1:
                        dcbm.addElement("Janvier");
                    break;
                    case 2:
                        dcbm.addElement("Février");
                    break;
                    case 3:
                        dcbm.addElement("Mars");
                    break;
                    case 4:
                        dcbm.addElement("Avril");
                    break;
                    case 5:
                        dcbm.addElement("Mai");
                    break;
                    case 6:
                        dcbm.addElement("Juin");
                    break;
                    case 7:
                        dcbm.addElement("Juillet");
                    break;
                    case 8:
                        dcbm.addElement("Août");
                    break;
                    case 9:
                        dcbm.addElement("Septembre");
                    break;
                    case 10:
                        dcbm.addElement("Octobre");
                    break;
                    case 11:
                        dcbm.addElement("Novembre");
                    break;
                    case 12:
                        dcbm.addElement("Décembre");
                    break;
                };
            }
            LMois.setModel(dcbm);


            resultat = instruc.executeQuery("SELECT DISTINCT NomCompagnie FROM compagnies");

            dcbm = new DefaultComboBoxModel<String>();
            dcbm.addElement("Toutes les compagnies");
            while(resultat.next())
                dcbm.addElement(resultat.getString(1));
            LCompagnie.setModel(dcbm);

            setVisible(true);
        }
        catch (SQLException e) {
            System.out.println("Erreur JDBC-OBCD : " + e.getMessage() + " ** " + e.getSQLState() + "--\n\n");
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BValider;
    private javax.swing.JComboBox<String> LCompagnie;
    private javax.swing.JComboBox<String> LMois;
    private javax.swing.JComboBox<String> LRequete;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables
}
