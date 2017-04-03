package main.rem.otdel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author maximus
 */
public class PhoneNaZamenu extends javax.swing.JFrame implements UpdatesDataInForms {

    /**
     * Creates new form PhoneNaZamenu
     */
    private ListenerCloseForm listenerCloseForm;
    private int PK;
    private Orders orders;

    public PhoneNaZamenu() {
        initComponents();
        addDataInTable();
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.orders = orders;
    }

    public void setListenerCloseForm(ListenerCloseForm listenerCloseForm) {
        this.listenerCloseForm = listenerCloseForm;
    }

    @Override
    public void addDataInTable() {
        this.setEnabled(true);
        ResultSet resSet = null;
        try {
            resSet = RepairMobile.st.executeQuery("select "
                    + "PK_KEYOFCHANGEMOBILE,"
                    + "MODEL,"
                    + "IMEINUMBER from replacemobile");

        } catch (SQLException ex) {
            Logger.getLogger(ChooseExistClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTable1.setModel(DbUtils.resultSetToTableModel(resSet));
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(0);

        jTable1.getColumnModel().getColumn(1).setHeaderValue("Модель");
        jTable1.getColumnModel().getColumn(2).setHeaderValue("Imei");

        //извлекаем телефоны на замену у клиентов
        try {
            resSet = RepairMobile.st.executeQuery("select "
                    + " ClientMobile.PK_ClientMobile,"
                    + " client.FAMOFCLIENT || ' ' || client.NAMEOFCLIENT  || ' ' || client.OTCOFCLIENT,"
                    + " ReplaceMobile.Model,"
                    + " TO_CHAR(ClientMobile.DateDilivery, 'DD.MM.YYYY'),"
                    + " TO_CHAR(ClientMobile.DateReturn, 'DD.MM.YYYY')"
                    + " from ClientMobile"
                    + " inner join Client on Client.PK_Client=ClientMobile.PK_Client"
                    + " inner join ReplaceMobile on ReplaceMobile.PK_KeyOfChangeMobile=ClientMobile.PK_KeyOfChangeMobile"
            );
                    
        } catch (SQLException ex) {
            Logger.getLogger(ChooseExistClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTable2.setModel(DbUtils.resultSetToTableModel(resSet));
        jTable2.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable2.getColumnModel().getColumn(0).setMinWidth(0);
        jTable2.getColumnModel().getColumn(0).setPreferredWidth(0);

        jTable2.getColumnModel().getColumn(1).setHeaderValue("Клиент");
        jTable2.getColumnModel().getColumn(2).setHeaderValue("Телефон");
        jTable2.getColumnModel().getColumn(3).setHeaderValue("Дата отдачи");
        jTable2.getColumnModel().getColumn(4).setHeaderValue("Дата возврата");

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable( )         {             @Override             public boolean isCellEditable(int row, int column)             {                 return false;             }         };
        jButtonCancel = new javax.swing.JButton();
        jButtonAdd = new javax.swing.JButton();
        jButtonUpdateTelefon = new javax.swing.JButton();
        jButtonDeleteTelefon = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable( )         {             @Override             public boolean isCellEditable(int row, int column)             {                 return false;             }         };
        jButtonUpdateClientTelefon = new javax.swing.JButton();
        jButtonDeleteClientTelefon = new javax.swing.JButton();
        jButtonCancel1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Телефны на замену");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButtonCancel.setText("Отмена");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jButtonAdd.setText("Добавить");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jButtonUpdateTelefon.setText("Изменить");
        jButtonUpdateTelefon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpdateTelefonActionPerformed(evt);
            }
        });

        jButtonDeleteTelefon.setText("Удалить");
        jButtonDeleteTelefon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteTelefonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonCancel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonDeleteTelefon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonUpdateTelefon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonAdd, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButtonAdd)
                        .addGap(7, 7, 7)
                        .addComponent(jButtonUpdateTelefon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonDeleteTelefon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 193, Short.MAX_VALUE)
                        .addComponent(jButtonCancel))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Имеющиеся телефоны", jPanel4);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Заголовок 3", "Заголовок 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jButtonUpdateClientTelefon.setText("Изменить");
        jButtonUpdateClientTelefon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpdateClientTelefonActionPerformed(evt);
            }
        });

        jButtonDeleteClientTelefon.setText("Удалить");
        jButtonDeleteClientTelefon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteClientTelefonActionPerformed(evt);
            }
        });

        jButtonCancel1.setText("Отмена");
        jButtonCancel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancel1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonCancel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonDeleteClientTelefon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonUpdateClientTelefon, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonUpdateClientTelefon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonDeleteClientTelefon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonCancel1))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Телефоны у клиентов", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        listenerCloseForm.event();
        updateParent();
    }//GEN-LAST:event_formWindowClosing

    private void jButtonDeleteTelefonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteTelefonActionPerformed
        // TODO add your handling code here:
        if (jTable1.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Выделите строку для удаления");
        } else {
            try {
                Object PK = jTable1.getValueAt(jTable1.getSelectedRow(), 0);
                int primKey = Integer.parseInt(PK.toString());

                int option = JOptionPane.showConfirmDialog(this, "Вы уверены что хотите удалить запись",
                        "Удаление записи", JOptionPane.YES_NO_CANCEL_OPTION);
                if (option == 0) {
                    RepairMobile.st.executeQuery("delete from replacemobile where PK_KEYOFCHANGEMOBILE=" + PK);
                    addDataInTable();
                    updateParent();
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Удаление данной записи невозможно. Возможно телефон уже был выдан клиенту.");
                Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButtonDeleteTelefonActionPerformed

    private void jButtonUpdateTelefonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpdateTelefonActionPerformed
        // TODO add your handling code here:
        if (jTable1.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Выделите запись для изменения");
        } else {
            Object PK = jTable1.getValueAt(jTable1.getSelectedRow(), 0);
            int primKey = Integer.parseInt(PK.toString());
            PhoneNaZamenuAddUpdate phoneNaZamenuAddUpdate = new PhoneNaZamenuAddUpdate(1, primKey);
            phoneNaZamenuAddUpdate.setListenerCloseForm(new ListenerCloseForm(this));
            phoneNaZamenuAddUpdate.setVisible(true);
        }
    }//GEN-LAST:event_jButtonUpdateTelefonActionPerformed

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        // TODO add your handling code here:
        PhoneNaZamenuAddUpdate phoneNaZamenuAddUpdate = new PhoneNaZamenuAddUpdate(0, -1);
        phoneNaZamenuAddUpdate.setListenerCloseForm(new ListenerCloseForm(this));
        phoneNaZamenuAddUpdate.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        listenerCloseForm.event();
        updateParent();
        this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonUpdateClientTelefonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpdateClientTelefonActionPerformed
        if (jTable2.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Выделите запись для изменения");
        } else {
            Object PK = jTable2.getValueAt(jTable2.getSelectedRow(), 0);
            int primKey = Integer.parseInt(PK.toString());
            PhoneNaZamenuDate phoneNaZamenuDate = new PhoneNaZamenuDate(1, primKey);
            phoneNaZamenuDate.setListenerCloseForm(new ListenerCloseForm(this));
            phoneNaZamenuDate.setVisible(true);
        }
    }//GEN-LAST:event_jButtonUpdateClientTelefonActionPerformed

    private void jButtonDeleteClientTelefonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteClientTelefonActionPerformed
        if (jTable2.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Выделите строку для удаления");
        } else {
            try {
                Object PK = jTable2.getValueAt(jTable2.getSelectedRow(), 0);
                int primKey = Integer.parseInt(PK.toString());

                int option = JOptionPane.showConfirmDialog(this, "Вы уверены что хотите удалить запись",
                        "Удаление записи", JOptionPane.YES_NO_CANCEL_OPTION);
                if (option == 0) {
                    RepairMobile.st.executeQuery("delete from ClientMobile where PK_ClientMobile=" + PK);
                    addDataInTable();
                    updateParent();
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Удаление данной записи невозможно.");
                Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButtonDeleteClientTelefonActionPerformed

    private void jButtonCancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancel1ActionPerformed
        listenerCloseForm.event();
        updateParent();
        this.dispose();
    }//GEN-LAST:event_jButtonCancel1ActionPerformed

    public void updateParent() {

        if (listenerCloseForm.updatesDataInForms instanceof Orders) {
            ((Orders) listenerCloseForm.updatesDataInForms).addTopData();
        } else {
            listenerCloseForm.event();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonCancel1;
    private javax.swing.JButton jButtonDeleteClientTelefon;
    private javax.swing.JButton jButtonDeleteTelefon;
    private javax.swing.JButton jButtonUpdateClientTelefon;
    private javax.swing.JButton jButtonUpdateTelefon;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
