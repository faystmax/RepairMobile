/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otchet.timezakaz;

import com.toedter.calendar.JTextFieldDateEditor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import main.rem.otdel.ListenerCloseForm;
import main.rem.otdel.RepairMobile;
import main.rem.otdel.UpdatesDataInForms;
import net.proteanit.sql.DbUtils;
import otchet.managerorder.ManagerOrder;

/**
 *
 * @author Максим
 */
public class TimeZakaz extends javax.swing.JFrame implements UpdatesDataInForms {

    private ListenerCloseForm listenerCloseForm;
    private Date dateStart;
    private Date dateEnd;

    /**
     * Creates new form TimeZakaz
     */
    public TimeZakaz() {
        initComponents();
        jDateChooserStart.setDateFormatString("dd.MM.yyyy");
        jDateChooserEnd.setDateFormatString("dd.MM.yyyy");
        ((JTextFieldDateEditor) jDateChooserStart.getDateEditor()).setEditable(false);
        ((JTextFieldDateEditor) jDateChooserEnd.getDateEditor()).setEditable(false);
    }

    @Override
    public void addDataInTable() {
        this.setEnabled(true);
        Date datenow = new Date();
        java.sql.Date date = new java.sql.Date(datenow.getTime());
        //MainRemOtdel.st.executeQuery("UPDATE SERVERADM.myorder SET  myorder.TIMETODELIVERY= TO_DATE('" + date + "', 'YYYY-MM-DD') WHERE PK_ORDER=" + PK);
        java.sql.Date date2 = null;
        if (jDateChooserEnd.getDate() == null || jDateChooserStart.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Сначала выберите промужуток времени");
            return;
        }
        dateStart = jDateChooserStart.getDate();
        dateEnd = jDateChooserEnd.getDate();
        Date dateS = new java.sql.Date(dateStart.getTime());
        Date dateE = new java.sql.Date(dateEnd.getTime());
        ResultSet resSet = null;

        try {
             resSet = RepairMobile.st.executeQuery("select myorder.PK_ORDER,myorder.NUMOFORDER,"
                    + "TO_CHAR(myorder.TIMETOACCEPT, 'DD.MM.YYYY'),"
                    + "TO_CHAR(myorder.TIMETODELIVERY, 'DD.MM.YYYY'),"
                    + "myorder.COSTOFORDER,myorder.TYPEOFORDER,myorder.PK_MANAGER"
                    + ",myorder.PK_STATUS,"
                    + " status.NAMEOFSTATUS,"
                    + " myorder.PK_CLIENT,"
                    + " client.FAMOFCLIENT || ' ' || client.NAMEOFCLIENT  || ' ' || client.OTCOFCLIENT,"
                    + " modeldevice.nameofmodel "
                    //+ " device.pk_device "
                    // + " manager.FAMOFMANAGER || ' ' || manager.NAMEOFMANAGER  || ' ' || manager.OTCOFMANAGER"
                    + " from myorder "
                    + " inner join status on status.PK_status=myorder.PK_status"
                    + " inner join manager on manager.PK_manager=myorder.PK_manager"
                    + " inner join client on client.PK_client=myorder.PK_client"
                    + " inner join device on device.PK_device=myorder.PK_device"
                    + " inner join modeldevice on modeldevice.PK_modeldevice=device.PK_modeldevice"
                    + " where myorder.TIMETOACCEPT > TO_DATE('" + dateS + "', 'YYYY-MM-DD') and"
                    + "       myorder.TIMETOACCEPT < TO_DATE('" + dateE + "', 'YYYY-MM-DD')  ");
            jTable1.setModel(DbUtils.resultSetToTableModel(resSet));
        } catch (SQLException ex) {
            Logger.getLogger(ManagerOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
        zakazTables(jTable1);

    }
    public void zakazTables(JTable jTable) {
        //
        jTable.getColumnModel().getColumn(1).setHeaderValue("Номер");
        jTable.getColumnModel().getColumn(2).setHeaderValue("Дата создания");
        jTable.getColumnModel().getColumn(3).setHeaderValue("Дата завершения");
        jTable.getColumnModel().getColumn(4).setHeaderValue("Стоимость");
        jTable.getColumnModel().getColumn(5).setHeaderValue("Тип");
        jTable.getColumnModel().getColumn(8).setHeaderValue("Статус");
        jTable.getColumnModel().getColumn(10).setHeaderValue("Клиент");
        jTable.getColumnModel().getColumn(11).setHeaderValue("Устройство");

        //пк заказа
        jTable.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable.getColumnModel().getColumn(0).setMinWidth(0);
        jTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        //пк менеджера
        jTable.getColumnModel().getColumn(6).setMaxWidth(0);
        jTable.getColumnModel().getColumn(6).setMinWidth(0);
        jTable.getColumnModel().getColumn(6).setPreferredWidth(0);
        //пк статуса
        jTable.getColumnModel().getColumn(7).setMaxWidth(0);
        jTable.getColumnModel().getColumn(7).setMinWidth(0);
        jTable.getColumnModel().getColumn(7).setPreferredWidth(0);
        //пк клиета
        jTable.getColumnModel().getColumn(9).setMaxWidth(0);
        jTable.getColumnModel().getColumn(9).setMinWidth(0);
        jTable.getColumnModel().getColumn(9).setPreferredWidth(0);

        for (int i = 0; i < jTable.getRowCount(); i++) {
            if (jTable.getValueAt(i, 5).toString().equals("0")) {
                jTable.setValueAt("Гарантийный", i, 5);
            } else {
                if (jTable.getValueAt(i, 5).toString().equals("1")) {
                    jTable.setValueAt("Не гарантийный", i, 5);
                } else {
                    jTable.setValueAt("Неизвестно", i, 5);
                }
            }
        }
    }
    public void correctSizeTable(javax.swing.JTable jTable) {
        jTable.getColumnModel().getColumn(1).setMaxWidth(60);
        jTable.getColumnModel().getColumn(1).setMinWidth(60);
        jTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        //
        jTable.getColumnModel().getColumn(2).setMaxWidth(100);
        jTable.getColumnModel().getColumn(2).setMinWidth(100);
        jTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        //
        jTable.getColumnModel().getColumn(3).setMaxWidth(100);
        jTable.getColumnModel().getColumn(3).setMinWidth(100);
        jTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        //
        jTable.getColumnModel().getColumn(4).setMaxWidth(90);
        jTable.getColumnModel().getColumn(4).setMinWidth(90);
        jTable.getColumnModel().getColumn(4).setPreferredWidth(90);
        //
        jTable.getColumnModel().getColumn(5).setMaxWidth(110);
        jTable.getColumnModel().getColumn(5).setMinWidth(110);
        jTable.getColumnModel().getColumn(5).setPreferredWidth(110);
        //
        jTable.getColumnModel().getColumn(8).setMaxWidth(150);
        jTable.getColumnModel().getColumn(8).setMinWidth(150);
        jTable.getColumnModel().getColumn(8).setPreferredWidth(150);
        //
        /*  jTable.getColumnModel().getColumn(10).setMaxWidth(200);
         jTable.getColumnModel().getColumn(10).setMinWidth(200);
         jTable.getColumnModel().getColumn(10).setPreferredWidth(200);*/

    }

    public void setListenerCloseForm(ListenerCloseForm listenerCloseForm) {
        this.listenerCloseForm = listenerCloseForm;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable( )         {             @Override             public boolean isCellEditable(int row, int column)             {                 return false;             }         };
        jButtonCancel = new javax.swing.JButton();
        jButtonPrint = new javax.swing.JButton();
        jDateChooserStart = new com.toedter.calendar.JDateChooser();
        jDateChooserEnd = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButtonFill = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Заказы за указанный промежуток времени");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButtonCancel.setText("Отмена");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jButtonPrint.setText("Распечатать");
        jButtonPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrintActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("C:");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("По:");

        jButtonFill.setText("Заполнить");
        jButtonFill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFillActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 904, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButtonPrint, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                            .addComponent(jDateChooserStart, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooserEnd, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 511, Short.MAX_VALUE)
                                .addComponent(jButtonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButtonFill, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateChooserStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooserEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonFill))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonPrint)
                    .addComponent(jButtonCancel))
                .addGap(6, 6, 6))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        listenerCloseForm.event();
        this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        listenerCloseForm.event();
    }//GEN-LAST:event_formWindowClosing

    private void jButtonPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrintActionPerformed
        if (jTable1.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Нечего печатать");
            return;
        }
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String reportDateStart = df.format(dateStart);
        String reportDateEnd = df.format(dateEnd);
        try {
            boolean complete = jTable1.print(JTable.PrintMode.FIT_WIDTH,
                    new MessageFormat("Заказы с "+reportDateStart+" по "+ reportDateEnd),
                    new MessageFormat("Страница {0,number,integer}"));
        } catch (Exception p) {
            /* Printing failed, report to the user */
            JOptionPane.showMessageDialog(this, "Ошибка: Невозможно распечатать");
        }
    }//GEN-LAST:event_jButtonPrintActionPerformed

    private void jButtonFillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFillActionPerformed
        addDataInTable();
    }//GEN-LAST:event_jButtonFillActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonFill;
    private javax.swing.JButton jButtonPrint;
    private com.toedter.calendar.JDateChooser jDateChooserEnd;
    private com.toedter.calendar.JDateChooser jDateChooserStart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
