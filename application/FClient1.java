package application;

import java.io.*;
import java.util.Properties;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultComboBoxModel;

import package_reseaux.other.GetDirectory;
import package_reseaux.other.MySQL;

public class FClient1 extends javax.swing.JFrame {

    /**
     * Creates new form FClient1
     */
    public FClient1() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        BValider = new javax.swing.JButton();
        BConnection = new javax.swing.JButton();
        LMois = new javax.swing.JComboBox<>();
        LCompagnie = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        LRequete = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        CheckBoxNbAccompagnants = new javax.swing.JCheckBox();
        CheckBoxAges = new javax.swing.JCheckBox();
        CheckBoxSexe = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        TConclusion = new javax.swing.JTextField();

        jLabel2.setText("jLabel2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        BValider.setText("Valider");
        BValider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BValiderActionPerformed(evt);
            }
        });

        BConnection.setLabel("Connection base de données et RServe");
        BConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BConnectionActionPerformed(evt);
            }
        });

        LMois.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tous les mois" }));

        LCompagnie.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Toutes les compagnies" }));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Mois & Compagnie");

        LRequete.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "REG_CORR_LUG", "REG_CORR_LUG_PLUS", "ANOVA_1_LUG", "ANOVA_2_LUG_HF" }));
        LRequete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LRequeteActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Requête");

        CheckBoxNbAccompagnants.setText("Nbr Accompagnants");
        CheckBoxNbAccompagnants.setEnabled(false);

        CheckBoxAges.setText("Ages voyageurs");
        CheckBoxAges.setEnabled(false);

        CheckBoxSexe.setText("Sexe");
        CheckBoxSexe.setEnabled(false);

        TConclusion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(132, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(BConnection)
                        .addGap(114, 114, 114))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(LRequete, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(139, 139, 139))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(189, 189, 189)
                                        .addComponent(jLabel3))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(CheckBoxNbAccompagnants)
                                        .addGap(53, 53, 53)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(CheckBoxAges)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(CheckBoxSexe))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(BValider)
                                                .addGap(0, 0, Short.MAX_VALUE)))))
                                .addGap(26, 26, 26))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(LMois, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 121, Short.MAX_VALUE)
                                .addComponent(LCompagnie, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(173, 173, 173)
                            .addComponent(jLabel1))
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(TConclusion)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel4))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(BConnection)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LCompagnie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LMois, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(LRequete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CheckBoxAges)
                    .addComponent(CheckBoxNbAccompagnants)
                    .addComponent(CheckBoxSexe))
                .addGap(18, 18, 18)
                .addComponent(BValider)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(26, 26, 26))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(TConclusion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BValiderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BValiderActionPerformed
        int tmp = LMois.getSelectedItem().toString().indexOf(":");
        Client.Statistics(Integer.parseInt(LMois.getSelectedItem().toString().substring(0,tmp)), LCompagnie.getSelectedItem().toString(), LRequete.getSelectedItem().toString(), CheckBoxAges.isSelected(), CheckBoxNbAccompagnants.isSelected());
    }//GEN-LAST:event_BValiderActionPerformed

    private void BConnectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BConnectionActionPerformed
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(GetDirectory.FileDir("properties.txt")));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Connection con = MySQL.MySQL_Connexion("bd_airport", (String)prop.get("DB_port"), "localhost", (String)prop.get("DB"), (String)prop.get("DB_pwd"));
            Statement instruc = con.createStatement();

            FillUI(con, instruc);
        }
        catch (SQLException e) {
            System.out.println("Erreur JDBC-OBCD : " + e.getMessage() + " ** " + e.getSQLState() + "--\n\n");
        }
        
        Client.Connection();
    }//GEN-LAST:event_BConnectionActionPerformed

    private void LRequeteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LRequeteActionPerformed
        CheckBoxNbAccompagnants.setSelected(false);
        CheckBoxAges.setSelected(false);
        CheckBoxSexe.setSelected(false);
        
        CheckBoxNbAccompagnants.setEnabled(false);
        CheckBoxAges.setEnabled(false);
        CheckBoxSexe.setEnabled(false);
        
        System.out.println(LRequete.getSelectedItem());
        if(LRequete.getSelectedIndex()==1)
        {
            CheckBoxNbAccompagnants.setEnabled(true);
            CheckBoxAges.setEnabled(true);
        }
        if(LRequete.getSelectedIndex()==3)
        {
            CheckBoxSexe.setSelected(true);
        }
    }//GEN-LAST:event_LRequeteActionPerformed

    private void ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ActionPerformed

    /**
     * @param args the command line arguments
     */
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FClient1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FClient1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FClient1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FClient1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Client.Proper();
                
                new FClient1().setVisible(true);
            }
        });
    }
    
    private void FillUI(Connection con, Statement instruc) {
        try {
            ResultSet resultat = instruc.executeQuery("SELECT DISTINCT EXTRACT(MONTH FROM dateVol) FROM vols");

            DefaultComboBoxModel<String> dcbm = new DefaultComboBoxModel<String>();
            dcbm.addElement("0: Tous les mois");
            while(resultat.next()) {
                switch(Integer.parseInt(resultat.getString(1))) {
                    case 1:
                        dcbm.addElement("1: Janvier");
                    break;
                    case 2:
                        dcbm.addElement("2: Février");
                    break;
                    case 3:
                        dcbm.addElement("3: Mars");
                    break;
                    case 4:
                        dcbm.addElement("4: Avril");
                    break;
                    case 5:
                        dcbm.addElement("5: Mai");
                    break;
                    case 6:
                        dcbm.addElement("6: Juin");
                    break;
                    case 7:
                        dcbm.addElement("7: Juillet");
                    break;
                    case 8:
                        dcbm.addElement("8: Août");
                    break;
                    case 9:
                        dcbm.addElement("9: Septembre");
                    break;
                    case 10:
                        dcbm.addElement("10: Octobre");
                    break;
                    case 11:
                        dcbm.addElement("11: Novembre");
                    break;
                    case 12:
                        dcbm.addElement("12: Décembre");
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
        }
        catch (SQLException e) {
            System.out.println("Erreur JDBC-OBCD : " + e.getMessage() + " ** " + e.getSQLState() + "--\n\n");
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BConnection;
    private javax.swing.JButton BValider;
    private javax.swing.JCheckBox CheckBoxAges;
    private javax.swing.JCheckBox CheckBoxNbAccompagnants;
    private javax.swing.JCheckBox CheckBoxSexe;
    private javax.swing.JComboBox<String> LCompagnie;
    private javax.swing.JComboBox<String> LMois;
    private javax.swing.JComboBox<String> LRequete;
    private javax.swing.JTextField TConclusion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
