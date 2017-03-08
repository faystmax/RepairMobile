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

    private ArrayList<String> pkDevice;
    private ArrayList<String> valueDevice;

    private ArrayList<String> pkStatus;
    private ArrayList<String> valueStatus;

    public RepairAddUpdate(int addOrUpdate, int PK, int engPK) {
        initComponents();
        this.engPk = engPK;
        this.PK = PK;
        pkDevice = new ArrayList<>();
        valueDevice = new ArrayList<>();
        pkStatus = new ArrayList<>();
        valueStatus = new ArrayList<>();
        jDateChooserEnd.setDateFormatString("dd.MM.yyyy");
        jDateChooserEnd.setDate(new java.util.Date());
        JTextFieldDateEditor editor2 = (JTextFieldDateEditor) jDateChooserEnd.getDateEditor();
        editor2.setEditable(false);

        jDateChooserStart.setDateFormatString("dd.MM.yyyy");
        jDateChooserStart.setDate(new java.util.Date());
        JTextFieldDateEditor editor3 = (JTextFieldDateEditor) jDateChooserStart.getDateEditor();
        editor3.setEditable(false);

        this.addOrUpdate = addOrUpdate;
        ResultSet resSet = null;
        try {
            if (addOrUpdate == 0) {
                resSet = RepairMobile.st.executeQuery("select pk_device,modelofdevice,typeofdevice.nameoftype,"
                        + "manufacturer.nameofmanufacturer from device"
                        + " inner join myorder on myorder.PK_order=device.PK_order"
                        + " inner join typeofdevice on typeofdevice.PK_typeofdevice=device.PK_typeofdevice"
                        + " inner join manufacturer on manufacturer.PK_manufacturer=device.PK_manufacturer"
                        + " where myorder.PK_status=2");

                while (resSet.next()) {
                    jComboBoxDevice.addItem(resSet.getString(3) + " " + resSet.getString(4) + " " + resSet.getString(2));
                    pkDevice.add(resSet.getString(1));
                    valueDevice.add(resSet.getString(3) + " " + resSet.getString(4) + " " + resSet.getString(2));
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Ошибка: Невозможно изменить");
            Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
            this.dispose();
        }

        if (addOrUpdate == 1) {
            jButtonAddUpdate.setText("Изменить");
            this.setTitle("Изменить ремонт");
            try {
                /*resSet = MainRemOtdel.st.executeQuery("select pk_device,modelofdevice,typeofdevice.nameoftype,"
                    + "manufacturer.nameofmanufacturer from device"
                    + " inner join myorder on myorder.PK_order=device.PK_order"
                    + " inner join typeofdevice on typeofdevice.PK_typeofdevice=device.PK_typeofdevice"
                    + " inner join manufacturer on manufacturer.PK_manufacturer=device.PK_manufacturer"
                    + " where myorder.PK_status=2");

            while (resSet.next()) {
                jComboBoxDevice.addItem(resSet.getString(3) + " " + resSet.getString(4) + " " + resSet.getString(2));
                pkDevice.add(resSet.getString(1));
                valueDevice.add(resSet.getString(3) + " " + resSet.getString(4) + " " + resSet.getString(2));
            }*/
                resSet = RepairMobile.st.executeQuery("select device.pk_device,modelofdevice,typeofdevice.nameoftype,"
                        + "manufacturer.nameofmanufacturer from device"
                        + " inner join repair on repair.PK_device=device.PK_device"
                        + " inner join typeofdevice on typeofdevice.PK_typeofdevice=device.PK_typeofdevice"
                        + " inner join manufacturer on manufacturer.PK_manufacturer=device.PK_manufacturer"
                        + " where repair.PK_repair=" + PK);

                if (resSet.next()) {
                    jComboBoxDevice.addItem(resSet.getString(3) + " " + resSet.getString(4) + " " + resSet.getString(2));
                    pkDevice.add(resSet.getString(1));
                    valueDevice.add(resSet.getString(3) + " " + resSet.getString(4) + " " + resSet.getString(2));
                }

                resSet = RepairMobile.st.executeQuery("select startdate, enddate,pk_device"
                        + " from repair where pk_repair=" + PK);
                if (resSet.next()) {
                    jDateChooserStart.setDate(resSet.getDate(1));
                    jDateChooserEnd.setDate(resSet.getDate(2));
                    for (int i = 0; i < pkDevice.size(); i++) {
                        if (pkDevice.get(i).equals(resSet.getString(3))) {
                            jComboBoxDevice.setSelectedIndex(i);
                        }
                    }
                }
                resSet = RepairMobile.st.executeQuery("select pk_status,nameofstatus from status where not(pk_status=2) and not(pk_status=7)");
                while (resSet.next()) {
                    jComboBoxStatus.addItem(resSet.getString(2));
                    valueStatus.add(resSet.getString(2));
                    pkStatus.add(resSet.getString(1));
                }

                resSet = RepairMobile.st.executeQuery("select pk_status from myorder"
                        + " inner join device on device.PK_order=myorder.PK_order"
                        + " where device.Pk_device=" + pkDevice.get(jComboBoxDevice.getSelectedIndex()));
                if (resSet.next()) {
                    for (int i = 0; i < pkStatus.size(); i++) {
                        if (pkStatus.get(i).equals(resSet.getString(1))) {
                            jComboBoxStatus.setSelectedIndex(i);
                        }
                    }
                }

            } catch (SQLException ex) {
                Logger.getLogger(RepairAddUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            jLabel2.setVisible(false);
            jComboBoxStatus.setVisible(false);
            jLabel6.setVisible(false);
            jDateChooserEnd.setVisible(false);
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
        jComboBoxDevice = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jDateChooserStart = new com.toedter.calendar.JDateChooser();
        jDateChooserEnd = new com.toedter.calendar.JDateChooser();
        jButtonCancel = new javax.swing.JButton();
        jButtonAddUpdate = new javax.swing.JButton();
        jComboBoxStatus = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Добавить ремонт");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel3.setText("Устройство");

        jLabel5.setText("Дата начала");

        jLabel6.setText("Дата окончания");

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

        jComboBoxStatus.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxStatusItemStateChanged(evt);
            }
        });

        jLabel2.setText("Статус");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonAddUpdate)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jDateChooserStart, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                        .addComponent(jDateChooserEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addGap(44, 44, 44))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBoxStatus, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBoxDevice, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxDevice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateChooserStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooserEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

            if (jComboBoxDevice.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Невозможно добавить пустое поле");
            } else {
                try {
                    RepairMobile.st.executeQuery("Insert into SERVERADM.repair (startdate,PK_device,pk_engineer)"
                            + " values ("
                            + " TO_DATE('" + dateStart + "', 'YYYY-MM-DD'),'"
                            + pkDevice.get(jComboBoxDevice.getSelectedIndex()) + "'"
                            + ",'" + engPk + "')");
                    RepairMobile.st.executeQuery("update myorder  set myorder.pk_status='3'"
                            + " where  EXISTS (select device.pk_device from device"
                            + " where device.pk_order = myorder.PK_ORDER and device.pk_device ="
                            + pkDevice.get(jComboBoxDevice.getSelectedIndex()) + ")");
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
                java.sql.Date dateEnd = null;
                java.util.Date dateChooserEnd = jDateChooserEnd.getDate();
                if (dateChooserEnd != null) {
                    dateEnd = new java.sql.Date(dateChooserEnd.getTime());
                }
                if (jComboBoxDevice.getSelectedIndex() == -1) {
                    JOptionPane.showMessageDialog(this, "Невозможно изменить на пустое поле");
                } else {
                    try {
                        if (dateEnd != null) {
                            RepairMobile.st.executeQuery("UPDATE repair"
                                    + " SET startdate = TO_DATE('" + dateStart + "', 'YYYY-MM-DD'),"
                                    + " enddate = TO_DATE('" + dateEnd + "', 'YYYY-MM-DD'),"
                                    + "pk_device='"
                                    + pkDevice.get(jComboBoxDevice.getSelectedIndex())
                                    + "' WHERE PK_repair=" + PK
                            );
                        } else {
                            RepairMobile.st.executeQuery("UPDATE repair"
                                    + " SET startdate = TO_DATE('" + dateStart + "', 'YYYY-MM-DD'),"
                                    + " enddate = null,"
                                    + "pk_device='"
                                    + pkDevice.get(jComboBoxDevice.getSelectedIndex())
                                    + "' WHERE PK_repair=" + PK
                            );
                        }
                        RepairMobile.st.executeQuery("update myorder  set myorder.pk_status=" + pkStatus.get(jComboBoxStatus.getSelectedIndex())
                                + " where  EXISTS (select device.pk_device from device"
                                + " where device.pk_order = myorder.PK_ORDER and device.pk_device ="
                                + pkDevice.get(jComboBoxDevice.getSelectedIndex()) + ")"
                        );
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

    private void jComboBoxStatusItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxStatusItemStateChanged
        // TODO add your handling code here:
        if (pkStatus.size() != 0) {
            if (pkStatus.get(jComboBoxStatus.getSelectedIndex()).equals("3")
                    || pkStatus.get(jComboBoxStatus.getSelectedIndex()).equals("4")) {
                jDateChooserEnd.setDate(null);
                jDateChooserEnd.setEnabled(false);
            } else {
                jDateChooserEnd.setDate(new java.util.Date());
                jDateChooserEnd.setEnabled(true);
            }
        }
    }//GEN-LAST:event_jComboBoxStatusItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddUpdate;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JComboBox<String> jComboBoxDevice;
    private javax.swing.JComboBox<String> jComboBoxStatus;
    private com.toedter.calendar.JDateChooser jDateChooserEnd;
    private com.toedter.calendar.JDateChooser jDateChooserStart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    // End of variables declaration//GEN-END:variables
}
