/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.rem.otdel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import main.rem.otdel.ListenerCloseForm;
import main.rem.otdel.RepairMobile;
import net.proteanit.sql.DbUtils;
import javax.swing.table.TableModel;

/**
 *
 * @author tigler
 */
public class AddUpdateDetailFromStore extends javax.swing.JFrame {

    /**
     * Creates new form AddUpdateDetailFromStore
     */
    private int addOrUpdate;
    private int PK;
    private int PKStorekeeper;
    private ListenerCloseForm listenerCloseForm;

    ArrayList<String> pkDetails;
    ArrayList<String> valueDetails;
    ArrayList<String> pkModel;
    ArrayList<String> valueModel;
    ArrayList<String> pkProizv;
    ArrayList<String> valueProizv;
    ArrayList<String> pkTypeDevice;
    ArrayList<String> valueTypeDevice;

    public AddUpdateDetailFromStore(int addOrUpdate, int PK, int PKStorekeeper) {
        initComponents();
        this.addOrUpdate = addOrUpdate;
        this.PK = PK;
        this.PKStorekeeper = PKStorekeeper;
        String pkConcreteDetail = null;
        String pkManuf = null;
        String pkType = null;
        String pkMod = null;
        String pkDet = null;
        String cost = null;
        try {
            ResultSet resSet = null;

            resSet = RepairMobile.st.executeQuery("select pk_detailFromwh,pk_concretedetail,amount,location from detailfromwarehouse"
                    + " where pk_detailFromwh=" + PK);

            try {
                if (resSet.next()) {
                    pkConcreteDetail = resSet.getString(2);
                    jSpinnerAmount.setValue(resSet.getInt(3));
                    jTextFieldLocation.setText(resSet.getString(4));
                }
                if (pkConcreteDetail != null) {
                    resSet = RepairMobile.st.executeQuery("select pk_concretedetail,pk_detail,pk_modeldevice,pk_typeofdevice,costofdetail from concretedetail"
                            + " where pk_concretedetail=" + pkConcreteDetail);
                }
                if (resSet.next()) {
                    pkMod = resSet.getString(3);
                    pkType = resSet.getString(4);
                    pkDet = resSet.getString(2);
                    cost = resSet.getString(5);
                    jSpinnerCostOne.setValue(Integer.parseInt(cost));
                }
                if (pkMod != null) {
                    resSet = RepairMobile.st.executeQuery("select pk_modeldevice,pk_manufacturer from modeldevice"
                            + " where pk_modeldevice=" + pkMod);
                }
                if (resSet.next()) {
                    pkManuf = resSet.getString(2);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Ошибка: Невозможно изменить");
                this.dispose();
            }

            /*resSet = RepairMobile.st.executeQuery("select pk_detail,nameofdetail from detail");
            int indCombobox = 0;
            while (resSet.next()) {
                pkDetail.add(resSet.getString(1));
                valuedetail.add(resSet.getString(2));
                jComboBoxDetail.addItem(resSet.getString(2));
                if (resSet.getString(1).equals(pkDetailForUpdate)) {
                    jComboBoxDetail.setSelectedIndex(indCombobox);
                }
                indCombobox++;
            }*/
        } catch (SQLException ex) {
            Logger.getLogger(AddUpdateDetailFromStore.class.getName()).log(Level.SEVERE, null, ex);
        }

        ResultSet resSet = null;
        ResultSet resSetProizv = null;
        pkProizv = new ArrayList<String>();
        valueProizv = new ArrayList<String>();

        ResultSet resTypeDevice = null;
        pkTypeDevice = new ArrayList<String>();
        valueTypeDevice = new ArrayList<String>();

        pkDetails = new ArrayList<String>();
        valueDetails = new ArrayList<String>();

        pkModel = new ArrayList<String>();
        valueModel = new ArrayList<String>();

        try {

            //был replace
            jComboBoxModel.setEnabled(false);

            //производитель
            int indSel = -1;
            resSetProizv = RepairMobile.st.executeQuery("select * from manufacturer");
            TableModel tableModel = DbUtils.resultSetToTableModel(resSetProizv);
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                pkProizv.add(tableModel.getValueAt(i, 0).toString());
                valueProizv.add(tableModel.getValueAt(i, 1).toString());
                if (pkProizv.get(i).equals(pkManuf)) {
                    indSel = i;

                }
            }
            jComboBoxManufacturer.setModel(new DefaultComboBoxModel(valueProizv.toArray()));
            jComboBoxManufacturer.setSelectedIndex(indSel);

            indSel = -1;
            //тип устройства
            resSetProizv = RepairMobile.st.executeQuery("select * from typeofdevice");
            tableModel = DbUtils.resultSetToTableModel(resSetProizv);
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                pkTypeDevice.add(tableModel.getValueAt(i, 0).toString());
                valueTypeDevice.add(tableModel.getValueAt(i, 1).toString());
                if (pkTypeDevice.get(i).equals(pkType)) {
                    indSel = i;

                }
            }
            jComboBoxType.setModel(new DefaultComboBoxModel(valueTypeDevice.toArray()));
            jComboBoxType.setSelectedIndex(indSel);

            indSel = -1;
            resSetProizv = RepairMobile.st.executeQuery("select * from detail");
            tableModel = DbUtils.resultSetToTableModel(resSetProizv);
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                pkDetails.add(tableModel.getValueAt(i, 0).toString());
                valueDetails.add(tableModel.getValueAt(i, 1).toString());
                if (pkDetails.get(i).equals(pkDet)) {
                    indSel = i;

                }
            }
            jComboBoxDetail.setModel(new DefaultComboBoxModel(valueDetails.toArray()));
            jComboBoxDetail.setSelectedIndex(indSel);

            indSel = -1;
            if (jComboBoxManufacturer.getSelectedIndex() != -1) {
                jComboBoxModel.setEnabled(true);
                ResultSet resSetModel = null;
                pkModel = new ArrayList<String>();
                valueModel = new ArrayList<String>();
                try {
                    // TODO add your handling code here:
                    resSetModel = RepairMobile.st.executeQuery("select pk_modeldevice,nameofmodel"
                            + " from modeldevice where pk_manufacturer =" + pkProizv.get(jComboBoxManufacturer.getSelectedIndex()));
                    tableModel = DbUtils.resultSetToTableModel(resSetModel);
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        pkModel.add(tableModel.getValueAt(i, 0).toString());
                        valueModel.add(tableModel.getValueAt(i, 1).toString());
                        if (pkModel.get(i).equals(pkMod)) {
                            indSel = i;
                        }
                    }
                    jComboBoxModel.setModel(new DefaultComboBoxModel(valueModel.toArray()));
                    jComboBoxModel.setSelectedIndex(indSel);
                } catch (SQLException ex) {
                    Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (addOrUpdate == 1) {
                jButtonAddUpdate.setText("Изменить");
                this.setTitle("Изменить деталь на складе");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
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

        jComboBoxDetail = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSpinnerAmount = new javax.swing.JSpinner();
        jTextFieldLocation = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButtonCancel = new javax.swing.JButton();
        jButtonAddUpdate = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jComboBoxType = new javax.swing.JComboBox<>();
        jComboBoxManufacturer = new javax.swing.JComboBox<>();
        jComboBoxModel = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jSpinnerCostOne = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Добавить детали на склад");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Деталь");

        jLabel2.setText("Колличество");

        jLabel3.setText("Расположение");

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

        jLabel4.setText("Тип устройства");

        jLabel5.setText("Производитель");

        jLabel6.setText("Модель");

        jComboBoxManufacturer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxManufacturerActionPerformed(evt);
            }
        });

        jLabel7.setText("Стоимость одной");

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
                        .addComponent(jButtonAddUpdate))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel1)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jComboBoxType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBoxDetail, 0, 205, Short.MAX_VALUE)
                                    .addComponent(jComboBoxManufacturer, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBoxModel, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jSpinnerAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jSpinnerCostOne, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 29, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBoxManufacturer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jComboBoxModel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jSpinnerCostOne, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jSpinnerAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancel)
                    .addComponent(jButtonAddUpdate))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        // TODO add your handling code here:
        listenerCloseForm.event();
        this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonAddUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddUpdateActionPerformed
        // TODO add your handling code here:
        if (addOrUpdate == 0) {
            String textLoc = jTextFieldLocation.getText();
            int indComboDetail = jComboBoxDetail.getSelectedIndex();
            int amount = Integer.parseInt(jSpinnerAmount.getValue().toString());
            if (textLoc.equals("") || indComboDetail == -1 || amount <= 0) {
                JOptionPane.showMessageDialog(this, "Некорректные данные");
            } else {
                try {
                    ResultSet resSet = RepairMobile.st.executeQuery("select pk_concretedetail from concretedetail"
                            + " inner join modeldevice on modeldevice.PK_modeldevice=concretedetail.PK_modeldevice"
                            + " inner join manufacturer on manufacturer.PK_manufacturer=modeldevice.PK_manufacturer"
                            + " where "
                            + " concretedetail.pk_detail=" + pkDetails.get(jComboBoxDetail.getSelectedIndex())
                            + "and concretedetail.pk_modelDevice=" + pkModel.get(jComboBoxModel.getSelectedIndex())
                            + "and concretedetail.pk_typeofdevice=" + pkTypeDevice.get(jComboBoxType.getSelectedIndex())
                            + "and manufacturer.pk_manufacturer=" + pkProizv.get(jComboBoxManufacturer.getSelectedIndex())
                    );
                    String pkConcretDetail = "";
                    if (resSet.next()) {
                        pkConcretDetail = resSet.getString(1);
                    } else {
                        RepairMobile.st.executeQuery("insert into concretedetail (pk_detail,pk_modeldevice,pk_typeofdevice,costofdetail)"
                                + "values('" + pkDetails.get(jComboBoxDetail.getSelectedIndex())
                                + "','" + pkModel.get(jComboBoxModel.getSelectedIndex())
                                + "','" + pkTypeDevice.get(jComboBoxType.getSelectedIndex()) + "','" + jSpinnerCostOne.getValue().toString() + "')"
                        );
                        resSet = RepairMobile.st.executeQuery("select SEQConcretedetail.currval from dual");
                        if (resSet.next()) {
                            pkConcretDetail = resSet.getString(1);
                        }
                    }
                    if (pkConcretDetail != null) {
                        RepairMobile.st.executeQuery("Insert into detailfromwarehouse"
                                + " (pk_concretedetail,amount,location) values"
                                + " ('" + pkConcretDetail + "', " + "'" + amount + "','" + textLoc + "')");
                        JOptionPane.showMessageDialog(this, "Запись успешно добавлена");
                        listenerCloseForm.event();
                    } else {
                        JOptionPane.showMessageDialog(this, "Ошибка: Невозможно добавить");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Ошибка: Невозможно добавить");
                    Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
                }
                this.dispose();
            }
        } else {
            if (addOrUpdate == 1) {
                String textLoc = jTextFieldLocation.getText();
                int indComboDetail = jComboBoxDetail.getSelectedIndex();
                int amount = Integer.parseInt(jSpinnerAmount.getValue().toString());
                if (textLoc.equals("") || indComboDetail == -1 || amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Некорректные данные");
                } else {
                    try {
                        ResultSet resSet = RepairMobile.st.executeQuery("select pk_concretedetail from concretedetail"
                                + " inner join modeldevice on modeldevice.PK_modeldevice=concretedetail.PK_modeldevice"
                                + " inner join manufacturer on manufacturer.PK_manufacturer=modeldevice.PK_manufacturer"
                                + " where "
                                + " concretedetail.pk_detail=" + pkDetails.get(jComboBoxDetail.getSelectedIndex())
                                + "and concretedetail.pk_modelDevice=" + pkModel.get(jComboBoxModel.getSelectedIndex())
                                + "and concretedetail.pk_typeofdevice=" + pkTypeDevice.get(jComboBoxType.getSelectedIndex())
                                + "and manufacturer.pk_manufacturer=" + pkProizv.get(jComboBoxManufacturer.getSelectedIndex())
                        );
                        String pkConcretDetail = "";
                        if (resSet.next()) {
                            pkConcretDetail = resSet.getString(1);
                        } else {
                            RepairMobile.st.executeQuery("insert into concretedetail (pk_detail,pk_modeldevice,pk_typeofdevice,costofdetail)"
                                    + "values('" + pkDetails.get(jComboBoxDetail.getSelectedIndex())
                                    + "','" + pkModel.get(jComboBoxModel.getSelectedIndex())
                                    + "','" + pkTypeDevice.get(jComboBoxType.getSelectedIndex()) + "','" + jSpinnerCostOne.getValue().toString() + "')"
                            );
                            resSet = RepairMobile.st.executeQuery("select SEQConcretedetail.currval from dual");
                            if (resSet.next()) {
                                pkConcretDetail = resSet.getString(1);
                            }
                        }
                        if (pkConcretDetail != null) {
                            RepairMobile.st.executeQuery("UPDATE detailfromwarehouse"
                                    + " SET PK_concretedetail = '" + pkConcretDetail + "',"
                                    + " amount = '" + amount + "',"
                                    + " location = '" + textLoc + "'"
                                    + " WHERE PK_detailfromwh=" + PK);
                            RepairMobile.st.executeQuery("UPDATE concretedetail"
                                    + " SET costofdetail = '" + jSpinnerCostOne.getValue().toString() + "'"
                                    + " WHERE PK_concretedetail=" + pkConcretDetail);
                            JOptionPane.showMessageDialog(this, "Запись успешно изменена");
                            listenerCloseForm.event();
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Ошибка: Невозможно изменить");
                        Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.dispose();
                }
            }

        }
    }//GEN-LAST:event_jButtonAddUpdateActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        listenerCloseForm.event();
    }//GEN-LAST:event_formWindowClosing

    private void jComboBoxManufacturerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxManufacturerActionPerformed
        // TODO add your handling code here:
        if (jComboBoxManufacturer.getSelectedIndex() != -1) {
            jComboBoxModel.setEnabled(true);
            ResultSet resSetModel = null;
            pkModel = new ArrayList<String>();
            valueModel = new ArrayList<String>();
            try {
                // TODO add your handling code here:
                resSetModel = RepairMobile.st.executeQuery("select pk_modeldevice,nameofmodel"
                        + " from modeldevice where pk_manufacturer =" + pkProizv.get(jComboBoxManufacturer.getSelectedIndex()));
                TableModel tableModel = DbUtils.resultSetToTableModel(resSetModel);
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    pkModel.add(tableModel.getValueAt(i, 0).toString());
                    valueModel.add(tableModel.getValueAt(i, 1).toString());
                }
                jComboBoxModel.setModel(new DefaultComboBoxModel(valueModel.toArray()));
                jComboBoxModel.setSelectedIndex(-1);
            } catch (SQLException ex) {
                Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jComboBoxManufacturerActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddUpdate;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JComboBox<String> jComboBoxDetail;
    private javax.swing.JComboBox<String> jComboBoxManufacturer;
    private javax.swing.JComboBox<String> jComboBoxModel;
    private javax.swing.JComboBox<String> jComboBoxType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JSpinner jSpinnerAmount;
    private javax.swing.JSpinner jSpinnerCostOne;
    private javax.swing.JTextField jTextFieldLocation;
    // End of variables declaration//GEN-END:variables
}
