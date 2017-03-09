/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.rem.otdel;

import com.toedter.calendar.JTextFieldDateEditor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import main.rem.otdel.ListenerCloseForm;
import main.rem.otdel.RepairMobile;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Максим
 */
public class OrderUpdate extends javax.swing.JFrame {

    /**
     * Creates new form OrderUpdate
     */
    private int addOrUpdate;
    private int PK;
    private ListenerCloseForm listenerCloseForm;
    ArrayList<String> statusPk;
    ArrayList<String> status;

    public OrderUpdate(int addOrUpdate, int PK) {
        initComponents();
        this.addOrUpdate = addOrUpdate;
        this.PK = PK;

        if (addOrUpdate == 1) {
            this.setTitle("Изменить заказ");

            ResultSet resSet = null;
            try {
                resSet = RepairMobile.st.executeQuery("select myorder.PK_ORDER,"
                        + "myorder.NUMOFORDER,"
                        + "TO_CHAR(myorder.TIMETOACCEPT, 'DD.MM.YYYY'),"
                        + "TO_CHAR(myorder.TIMETODELIVERY, 'DD.MM.YYYY'),"
                        + "myorder.COSTOFORDER,myorder.TYPEOFORDER,myorder.PK_MANAGER"
                        + ",myorder.PK_STATUS,"
                        + " status.NAMEOFSTATUS,"
                        + " myorder.PK_CLIENT,"
                        + " manager.FAMOFMANAGER || ' ' || manager.NAMEOFMANAGER  || ' ' || manager.OTCOFMANAGER"
                        + " from myorder "
                        + " inner join status on status.PK_status=myorder.PK_status"
                        + " inner join manager on manager.PK_manager=myorder.PK_manager"
                        + " inner join client on client.PK_client=myorder.PK_client"
                        + " where myorder.PK_ORDER=" + PK);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Ошибка: Невозможно изменить");
                this.dispose();
            }
            try {
                if (resSet.next()) {
                    jTextFieldNumber.setText(resSet.getString(2));
                    jDateChooserCreate.setDateFormatString("dd.MM.yyyy");
                    Date thedate = new SimpleDateFormat("dd.MM.yyyy").parse(resSet.getString(3));
                    jDateChooserCreate.setDate(thedate);
                    JTextFieldDateEditor editor = (JTextFieldDateEditor) jDateChooserCreate.getDateEditor();
                    editor.setEditable(false);
                    if (resSet.getString(4) != null) {
                        jDateChooserEnd.setDateFormatString("dd.MM.yyyy");
                        Date thedate2 = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(resSet.getString(4));
                        jDateChooserEnd.setDate(thedate2);
                    }
                    JTextFieldDateEditor editor2 = (JTextFieldDateEditor) jDateChooserEnd.getDateEditor();
                    editor2.setEditable(false);
                    jTextFieldCost.setText(resSet.getString(5));
                    // тип
                    ArrayList<String> type = new ArrayList<String>();
                    type.add("Гарантийный");
                    type.add("Не гарантийный");
                    jComboBoxType.setModel(new DefaultComboBoxModel(type.toArray()));
                    if (resSet.getString(6).equals("0")) {
                        jComboBoxType.setSelectedIndex(0);
                    } else if (resSet.getString(6).equals("1")) {
                        jComboBoxType.setSelectedIndex(1);
                    } else {
                        jComboBoxType.setSelectedIndex(-1);
                    }
                    //Статусы
                    statusPk = new ArrayList<String>();
                    status = new ArrayList<String>();
                    String pkStatus = resSet.getString(8);
                    ResultSet resSet2 = RepairMobile.st.executeQuery("select * from status");
                    TableModel tableModel = DbUtils.resultSetToTableModel(resSet2);
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        statusPk.add(tableModel.getValueAt(i, 0).toString());
                        status.add(tableModel.getValueAt(i, 1).toString());
                    }
                    jComboBoxStatus.setModel(new DefaultComboBoxModel(status.toArray()));
                    for (int i = 0; i < statusPk.size(); i++) {
                        if (statusPk.get(i).equals(pkStatus)) {
                            jComboBoxStatus.setSelectedIndex(i);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Ошибка: Невозможно изменить");
                this.dispose();
            }
        }
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

        jButtonUpdate = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldNumber = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jDateChooserCreate = new com.toedter.calendar.JDateChooser();
        jDateChooserEnd = new com.toedter.calendar.JDateChooser();
        jTextFieldCost = new javax.swing.JTextField();
        jComboBoxType = new javax.swing.JComboBox();
        jComboBoxStatus = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jButtonUpdate.setText("Изменить");
        jButtonUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpdateActionPerformed(evt);
            }
        });

        jButtonCancel.setText("Отмена");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jLabel1.setText("Номер заказа");

        jLabel2.setText("Дата создания");

        jLabel3.setText("Дата завершения");

        jLabel4.setText("Стоимость заказа");

        jLabel5.setText("Тип");

        jLabel6.setText("Статус");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButtonCancel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(jButtonUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooserEnd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldCost)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooserCreate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldNumber)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(51, 51, 51))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateChooserCreate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jDateChooserEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jComboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jComboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonUpdate)
                    .addComponent(jButtonCancel))
                .addContainerGap())
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

    private void jButtonUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpdateActionPerformed
        String textNumber = jTextFieldNumber.getText();
        Date dateChooserCreate = jDateChooserCreate.getDate();
        java.sql.Date date = new java.sql.Date(dateChooserCreate.getTime());
        java.sql.Date date2= null;
        if (jDateChooserEnd.getDate() != null) {
            Date dateChooserEnd = jDateChooserEnd.getDate();
            date2 = new java.sql.Date(dateChooserEnd.getTime());
        }
        String textCost = jTextFieldCost.getText();
        //тип заказа
        int typeOfDevice = jComboBoxType.getSelectedIndex();
        //статус
        String pkStatus = statusPk.get(jComboBoxStatus.getSelectedIndex());

        if (textNumber.equals("") || textCost.equals("") || pkStatus.equals("")) {
            JOptionPane.showMessageDialog(this, "Невозможно изменить на пустое поле");
        } else {
            try {
                RepairMobile.st.executeQuery("UPDATE myorder SET "
                        + "myorder.NUMOFORDER = '" + textNumber + "', "
                        + "myorder.TIMETOACCEPT = TO_DATE('" + date + "', 'YYYY-MM-DD'),"
                        + "myorder.TIMETODELIVERY = " + (date2==null ? "null," : "TO_DATE('" + date2 + "', 'YYYY-MM-DD'),")
                       // + "myorder.TIMETODELIVERY = TO_DATE('" + date2 + "', 'YYYY-MM-DD'),"
                        + "myorder.COSTOFORDER='" + textCost + "',"
                        + "myorder.TYPEOFORDER='" + typeOfDevice + "',"
                        + "myorder.PK_STATUS='" + pkStatus + "'"
                        + " WHERE myorder.PK_ORDER=" + PK
                );
                JOptionPane.showMessageDialog(this, "Заказ успешно изменён");
                listenerCloseForm.event();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка: Невозможно изменить");
                listenerCloseForm.event();
            } finally {
                this.dispose();
            }
        }
    }//GEN-LAST:event_jButtonUpdateActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonUpdate;
    private javax.swing.JComboBox jComboBoxStatus;
    private javax.swing.JComboBox jComboBoxType;
    private com.toedter.calendar.JDateChooser jDateChooserCreate;
    private com.toedter.calendar.JDateChooser jDateChooserEnd;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField jTextFieldCost;
    private javax.swing.JTextField jTextFieldNumber;
    // End of variables declaration//GEN-END:variables
}
