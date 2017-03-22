/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.rem.otdel;

import com.toedter.calendar.JTextFieldDateEditor;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import main.rem.otdel.ListenerCloseForm;
import main.rem.otdel.RepairMobile;
import main.rem.otdel.DetailsStore;

/**
 *
 * @author tigler
 */
public class RepairAddUpdate extends javax.swing.JFrame {

    /**
     * Creates new form RepairAddUpdate
     */
    private ListenerCloseForm listenerCloseForm;
    private int addOrUpdate;
    private int PK;
    private int engPk;

    private ArrayList<String> pkOrder;
    private ArrayList<String> valueOrder;

    private ArrayList<String> pkCrash;
    private ArrayList<String> valueCrash;
    private boolean flag;

    public RepairAddUpdate(int addOrUpdate, int PK, int engPK) {
        flag = false;
        initComponents();

        this.engPk = engPK;
        this.PK = PK;
        pkOrder = new ArrayList<>();
        valueOrder = new ArrayList<>();
        pkCrash = new ArrayList<>();
        valueCrash = new ArrayList<>();

        jDateChooserStart.setDateFormatString("dd.MM.yyyy");
        jDateChooserStart.setDate(new java.util.Date());
        JTextFieldDateEditor editor3 = (JTextFieldDateEditor) jDateChooserStart.getDateEditor();
        editor3.setEditable(false);

        this.addOrUpdate = addOrUpdate;
        ResultSet resSet = null;
        String pkOrd = null;
        String pkCrsh = null;
        String repStat = null;
        try {
            if (addOrUpdate == 0) {

                resSet = RepairMobile.st.executeQuery("select myorder.pk_order,"
                        + " '№' ||myorder.numoforder || ' ' || manufacturer.nameofmanufacturer || ' ' || modeldevice.nameofmodel"
                        + " from myorder"
                        + " inner join device on device.PK_device=myorder.PK_device"
                        + " inner join modeldevice on device.PK_modeldevice=modeldevice.PK_modeldevice"
                        + " inner join manufacturer on manufacturer.PK_manufacturer=modeldevice.PK_manufacturer"
                        + " where myorder.PK_status=12");

                while (resSet.next()) {
                    jComboBoxDevice.addItem(resSet.getString(2));
                    pkOrder.add(resSet.getString(1));
                    valueOrder.add(resSet.getString(2));

                }
                jComboBoxDevice.setSelectedIndex(-1);
                jComboBoxCrash.setSelectedIndex(-1);
                jComboBoxCrash.setEnabled(false);

            } else {
                resSet = RepairMobile.st.executeQuery("select pk_order,repairstatus,pk_crash from repair where pk_repair=" + PK);
                if (resSet.next()) {
                    pkOrd = resSet.getString(1);
                    repStat = resSet.getString(2);
                    pkCrsh = resSet.getString(3);
                }

                resSet = RepairMobile.st.executeQuery("select myorder.pk_order,"
                        + " '№' ||myorder.numoforder || ' ' || manufacturer.nameofmanufacturer || ' ' || modeldevice.nameofmodel"
                        + " from myorder"
                        + " inner join device on device.PK_device=myorder.PK_device"
                        + " inner join modeldevice on device.PK_modeldevice=modeldevice.PK_modeldevice"
                        + " inner join manufacturer on manufacturer.PK_manufacturer=modeldevice.PK_manufacturer"
                        + " where myorder.PK_status=12");

                int idxOrd = -1;
                int i = 0;
                while (resSet.next()) {
                    jComboBoxDevice.addItem(resSet.getString(2));
                    pkOrder.add(resSet.getString(1));
                    valueOrder.add(resSet.getString(2));
                    if (pkOrd != null) {
                        if (pkOrd.equals(resSet.getString(1))) {
                            idxOrd = i;
                        }
                    }
                    i++;
                }

                jComboBoxDevice.setSelectedIndex(idxOrd);

                pkOrd = pkOrder.get(jComboBoxDevice.getSelectedIndex());
                ArrayList<String> listAlreadyUseCrash = new ArrayList<>();
                String usedCrashForReq = "";
                String curCrash = "";
                resSet = RepairMobile.st.executeQuery("select pk_crash from repair where pk_repair=" + PK);
                if (resSet.next()) {
                    curCrash = resSet.getString(1);
                }

                resSet = RepairMobile.st.executeQuery("select pk_crash from repair where pk_order=" + pkOrd);
                while (resSet.next()) {
                    if (!curCrash.equals(resSet.getString(1))) {
                        listAlreadyUseCrash.add(resSet.getString(1));
                    }
                }
                for (int j = 0; j < listAlreadyUseCrash.size(); j++) {
                    if (j == listAlreadyUseCrash.size() - 1) {
                        usedCrashForReq += "typeofcrash.pk_crash<>" + listAlreadyUseCrash.get(j);
                    } else {
                        usedCrashForReq += "typeofcrash.pk_crash<>" + listAlreadyUseCrash.get(j) + " and ";
                    }

                }

                if (!usedCrashForReq.equals("")) {
                    resSet = RepairMobile.st.executeQuery("select typeofcrash.pk_crash,"
                            + " typeofcrash.nameofcrash"
                            + " from typeofcrash"
                            + " inner join ordercrash on ordercrash.PK_crash=typeofcrash.pk_crash"
                            + " where ordercrash.PK_order=" + pkOrder.get(jComboBoxDevice.getSelectedIndex()) + " and " + usedCrashForReq);
                } else {
                    resSet = RepairMobile.st.executeQuery("select typeofcrash.pk_crash,"
                            + " typeofcrash.nameofcrash"
                            + " from typeofcrash"
                            + " inner join ordercrash on ordercrash.PK_crash=typeofcrash.pk_crash"
                            + " where ordercrash.PK_order=" + pkOrder.get(jComboBoxDevice.getSelectedIndex()));
                }

                int idxCr = -1;
                i = 0;
                while (resSet.next()) {
                    jComboBoxCrash.addItem(resSet.getString(2));
                    pkCrash.add(resSet.getString(1));
                    valueCrash.add(resSet.getString(2));
                    if (pkCrsh != null) {
                        if (pkCrsh.equals(resSet.getString(1))) {
                            idxCr = i;
                        }
                    }
                    i++;
                }

                if (repStat != null) {
                    if (repStat.equals("0")) {
                        jComboBoxStatus.setSelectedIndex(0);
                    } else {
                        jComboBoxStatus.setSelectedIndex(1);
                        jDateChooserStart.setVisible(true);
                        jLabel5.setVisible(true);
                    }
                }
                if (jComboBoxStatus.getSelectedIndex() != 1) {
                    jDateChooserStart.setVisible(false);
                    jLabel5.setVisible(false);
                }
                jLabel5.setText("Дата завершения");

                jComboBoxCrash.setSelectedIndex(idxCr);
            }

            flag = true;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Ошибка: Невозможно изменить");
            Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
            this.dispose();
        }

        if (addOrUpdate == 1) {
            jButtonAddUpdate.setText("Изменить");
            this.setTitle("Изменить ремонт");

        } else {
            jDateChooserStart.setDate(new java.util.Date());
            jComboBoxDevice.setSelectedIndex(-1);
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

        jLabel1 = new javax.swing.JLabel();
        jComboBoxDevice = new javax.swing.JComboBox<String>();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jDateChooserStart = new com.toedter.calendar.JDateChooser();
        jButtonCancel = new javax.swing.JButton();
        jButtonAddUpdate = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jComboBoxCrash = new javax.swing.JComboBox<String>();
        jLabel6 = new javax.swing.JLabel();
        jComboBoxStatus = new javax.swing.JComboBox<String>();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Добавить ремонт");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jComboBoxDevice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxDeviceActionPerformed(evt);
            }
        });

        jLabel3.setText("Заказ");

        jLabel5.setText("Дата начала");

        jButtonCancel.setText("Отмена");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jButtonAddUpdate.setText("Добавить");
        jButtonAddUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddUpdateActionPerformed(evt);
            }
        });

        jLabel4.setText("Поломка");

        jLabel6.setText("Статус");

        jComboBoxStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ремонтируется", "Выполнено" }));
        jComboBoxStatus.setSelectedIndex(-1);
        jComboBoxStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxStatusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButtonCancel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
                                .addComponent(jButtonAddUpdate))
                            .addComponent(jComboBoxDevice, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBoxCrash, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateChooserStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(10, 10, 10))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxDevice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBoxCrash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jComboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55)
                        .addComponent(jLabel5))
                    .addComponent(jDateChooserStart, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancel)
                    .addComponent(jButtonAddUpdate))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAddUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddUpdateActionPerformed
        // TODO add your handling code here:
        int devicePk = jComboBoxDevice.getSelectedIndex();
        java.util.Date dateChooserStart = jDateChooserStart.getDate();
        java.sql.Date dateStart = new java.sql.Date(dateChooserStart.getTime());

        if (addOrUpdate == 0) {

            if (jComboBoxDevice.getSelectedIndex() == -1 || jComboBoxStatus.getSelectedIndex() == -1 || jComboBoxCrash.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Заполните поля");
            } else {
                try {
                    int stat = 0;
                    if (jComboBoxStatus.getSelectedIndex() == 0) {
                        RepairMobile.st.executeQuery("Insert into repair (startdate,PK_order,pk_engineer,pk_crash,repairstatus)"
                                + " values ("
                                + " TO_DATE('" + dateStart + "', 'YYYY-MM-DD'),'"
                                + pkOrder.get(jComboBoxDevice.getSelectedIndex()) + "'"
                                + ",'" + engPk + "'"
                                + ",'" + pkCrash.get(jComboBoxCrash.getSelectedIndex()) + "','0')");
                    } else {
                        RepairMobile.st.executeQuery("Insert into repair (startdate,enddate,PK_order,pk_engineer,pk_crash,repairstatus)"
                                + " values ("
                                + " TO_DATE('" + dateStart + "', 'YYYY-MM-DD'),"
                                + " TO_DATE('" + dateStart + "', 'YYYY-MM-DD'),'"
                                + pkOrder.get(jComboBoxDevice.getSelectedIndex()) + "'"
                                + ",'" + engPk + "'"
                                + ",'" + pkCrash.get(jComboBoxCrash.getSelectedIndex()) + "','1')");
                    }

                    //
                    /**
                     * сделать проверку на все выполненые работы
                     */
                    JOptionPane.showMessageDialog(this, "Запись успешно добавлена");
                    listenerCloseForm.event();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Ошибка: Невозможно добавить");
                    Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
                }
                this.dispose();
            }
        } else {
            if (addOrUpdate == 1) {
                if (jComboBoxDevice.getSelectedIndex() == -1) {
                    JOptionPane.showMessageDialog(this, "Невозможно изменить на пустое поле");
                } else {
                    try {
                        String repStat = null;
                        if (jComboBoxStatus.getSelectedIndex() == 0) {
                            repStat = "0";
                        } else {
                            repStat = "1";
                        }

                        String date = "null";
                        if (repStat.equals("1")) {
                            date = "TO_DATE('" + dateStart + "', 'YYYY-MM-DD')";
                        }
                        RepairMobile.st.executeQuery("UPDATE repair"
                                + " SET "
                                + " enddate =" + date + ","
                                + " pk_order='"
                                + pkOrder.get(jComboBoxDevice.getSelectedIndex())
                                + "', pk_crash='" + pkCrash.get(jComboBoxCrash.getSelectedIndex())
                                + "',repairstatus='" + repStat + "'"
                                + " WHERE PK_repair=" + PK
                        );


                        /*RepairMobile.st.executeQuery("update myorder  set myorder.pk_status=" + pkCrash.get(jComboBoxStatus.getSelectedIndex())
                                + " where  EXISTS (select device.pk_device from device"
                                + " where device.pk_order = myorder.PK_ORDER and device.pk_device ="
                                + pkOrder.get(jComboBoxDevice.getSelectedIndex()) + ")"
                        );*/
                        JOptionPane.showMessageDialog(this, "Запись успешно изменена");
                        listenerCloseForm.event();
                    } catch (SQLException ex) {
                        Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(this, "Ошибка: Невозможно изменить");
                    }
                    this.dispose();
                }
            }
        }
    }//GEN-LAST:event_jButtonAddUpdateActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        // TODO add your handling code here:
        listenerCloseForm.event();
        this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        listenerCloseForm.event();
    }//GEN-LAST:event_formWindowClosing

    private void jComboBoxDeviceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxDeviceActionPerformed
        // TODO add your handling code here:
        if (jComboBoxDevice.getSelectedIndex() != -1 && flag) {
            jComboBoxCrash.setEnabled(true);
            ResultSet resSet = null;
            pkCrash = new ArrayList<>();
            valueCrash = new ArrayList<>();
            jComboBoxCrash.removeAllItems();
            String pkOrd = null;
            try {
                if (addOrUpdate == 0) {

                    pkOrd = pkOrder.get(jComboBoxDevice.getSelectedIndex());

                    ArrayList<String> listAlreadyUseCrash = new ArrayList<>();
                    String usedCrashForReq = "";
                    resSet = RepairMobile.st.executeQuery("select pk_crash from repair where pk_order=" + pkOrd);
                    while (resSet.next()) {
                        listAlreadyUseCrash.add(resSet.getString(1));
                    }
                    for (int j = 0; j < listAlreadyUseCrash.size(); j++) {
                        if (j == listAlreadyUseCrash.size() - 1) {
                            usedCrashForReq += "typeofcrash.pk_crash<>" + listAlreadyUseCrash.get(j);
                        } else {
                            usedCrashForReq += "typeofcrash.pk_crash<>" + listAlreadyUseCrash.get(j) + " and ";
                        }

                    }
                    if (!usedCrashForReq.equals("")) {
                        resSet = RepairMobile.st.executeQuery("select typeofcrash.pk_crash,"
                                + " typeofcrash.nameofcrash"
                                + " from typeofcrash"
                                + " inner join ordercrash on ordercrash.PK_crash=typeofcrash.pk_crash"
                                + " where ordercrash.PK_order=" + pkOrder.get(jComboBoxDevice.getSelectedIndex()) + " and " + usedCrashForReq);
                    } else {
                        resSet = RepairMobile.st.executeQuery("select typeofcrash.pk_crash,"
                                + " typeofcrash.nameofcrash"
                                + " from typeofcrash"
                                + " inner join ordercrash on ordercrash.PK_crash=typeofcrash.pk_crash"
                                + " where ordercrash.PK_order=" + pkOrder.get(jComboBoxDevice.getSelectedIndex()));
                    }
                    while (resSet.next()) {
                        jComboBoxCrash.addItem(resSet.getString(2));
                        pkCrash.add(resSet.getString(1));
                        valueCrash.add(resSet.getString(2));
                    }
                    jComboBoxCrash.setSelectedIndex(-1);
                }

                if (addOrUpdate == 1) {

                    pkOrd = pkOrder.get(jComboBoxDevice.getSelectedIndex());

                    ArrayList<String> listAlreadyUseCrash = new ArrayList<>();
                    String usedCrashForReq = "";
                    resSet = RepairMobile.st.executeQuery("select pk_crash from repair where pk_order=" + pkOrd);
                    while (resSet.next()) {
                        listAlreadyUseCrash.add(resSet.getString(1));
                    }
                    for (int j = 0; j < listAlreadyUseCrash.size(); j++) {
                        if (j == listAlreadyUseCrash.size() - 1) {
                            usedCrashForReq += "typeofcrash.pk_crash<>" + listAlreadyUseCrash.get(j);
                        } else {
                            usedCrashForReq += "typeofcrash.pk_crash<>" + listAlreadyUseCrash.get(j) + " and ";
                        }

                    }
                    if (!usedCrashForReq.equals("")) {
                        resSet = RepairMobile.st.executeQuery("select typeofcrash.pk_crash,"
                                + " typeofcrash.nameofcrash"
                                + " from typeofcrash"
                                + " inner join ordercrash on ordercrash.PK_crash=typeofcrash.pk_crash"
                                + " where ordercrash.PK_order=" + pkOrder.get(jComboBoxDevice.getSelectedIndex()) + " and " + usedCrashForReq);
                    } else {
                        resSet = RepairMobile.st.executeQuery("select typeofcrash.pk_crash,"
                                + " typeofcrash.nameofcrash"
                                + " from typeofcrash"
                                + " inner join ordercrash on ordercrash.PK_crash=typeofcrash.pk_crash"
                                + " where ordercrash.PK_order=" + pkOrder.get(jComboBoxDevice.getSelectedIndex()));
                    }
                    while (resSet.next()) {
                        jComboBoxCrash.addItem(resSet.getString(2));
                        pkCrash.add(resSet.getString(1));
                        valueCrash.add(resSet.getString(2));
                    }
                    jComboBoxCrash.setSelectedIndex(-1);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Ошибка: Невозможно изменить");
                Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
                this.dispose();
            }
        }
    }//GEN-LAST:event_jComboBoxDeviceActionPerformed

    private void jComboBoxStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxStatusActionPerformed
        // TODO add your handling code here:
        if (jComboBoxStatus.getSelectedIndex() == 1 && addOrUpdate == 1) {
            jDateChooserStart.setVisible(true);
            jLabel5.setVisible(true);
        } else if (addOrUpdate == 1) {
            jDateChooserStart.setVisible(false);
            jLabel5.setVisible(false);
        }
    }//GEN-LAST:event_jComboBoxStatusActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddUpdate;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JComboBox<String> jComboBoxCrash;
    private javax.swing.JComboBox<String> jComboBoxDevice;
    private javax.swing.JComboBox<String> jComboBoxStatus;
    private com.toedter.calendar.JDateChooser jDateChooserStart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    // End of variables declaration//GEN-END:variables
}
