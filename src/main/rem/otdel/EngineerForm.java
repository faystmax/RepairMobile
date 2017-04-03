/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.rem.otdel;

import com.toedter.calendar.JTextFieldDateEditor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.TableModel;

import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author tigler
 */
public class EngineerForm extends javax.swing.JFrame implements UpdatesDataInForms {

    private int PK;
    private ArrayList<String> pkStorekeeper;
    private ArrayList<String> valueStorekeeper;
    private ArrayList<String> pkDetail;
    private ArrayList<String> valueDetail;
    private DefaultTableModel dtm;

    ArrayList<String> pkDetails;
    ArrayList<String> valueDetails;
    ArrayList<String> pkModel;
    ArrayList<String> valueModel;
    ArrayList<String> pkProizv;
    ArrayList<String> valueProizv;
    ArrayList<String> pkTypeDevice;
    ArrayList<String> valueTypeDevice;

    /**
     * Creates new form EngineerForm
     */
    public EngineerForm(int PK) {
        initComponents();
        this.PK = PK;
        addDataInTable();
        pkStorekeeper = new ArrayList<String>();
        valueStorekeeper = new ArrayList<String>();

        //jButtonAddDetail.setEnabled(false);
        ResultSet resSet = null;
        try {
            resSet = RepairMobile.st.executeQuery("select famofengineer,nameofengineer,otchofengineer from engineer"
                    + " where pk_engineer=" + PK);
            if (resSet.next()) {
                jLabelFIO.setText("Инженер:" + resSet.getString(1) + " "
                        + resSet.getString(2) + " "
                        + resSet.getString(3));
            }
        } catch (SQLException ex) {
            Logger.getLogger(EngineerForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        jDateChooser1.setDateFormatString("dd.MM.yyyy");
        jDateChooser1.setDate(new Date());
        JTextFieldDateEditor editor2 = (JTextFieldDateEditor) jDateChooser1.getDateEditor();
        editor2.setEditable(false);

        /*pkDetail = new ArrayList<String>();
        valueDetail = new ArrayList<String>();

        try {
            resSet = RepairMobile.st.executeQuery("select detail.PK_detail,"
                    + " detail.nameofdetail"
                    + " from detail");
        } catch (SQLException ex) {
            Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        TableModel tableModel = DbUtils.resultSetToTableModel(resSet);
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            pkDetail.add(tableModel.getValueAt(i, 0).toString());
            valueDetail.add(tableModel.getValueAt(i, 1).toString());
        }
        jComboBoxDetail.setModel(new DefaultComboBoxModel(valueDetail.toArray()));
        jComboBoxDetail.setSelectedIndex(-1);*/
    }

    @Override
    public void addDataInTable() {
        this.setEnabled(true);
        ResultSet resSet = null;
        try {
            resSet = RepairMobile.st.executeQuery("select repair.PK_repair,"
                    + " myorder.numoforder,"
                    + " manufacturer.nameofmanufacturer|| ' ' ||modeldevice.nameofmodel,"
                    + " TO_CHAR(repair.startdate, 'DD.MM.YYYY'),"
                    + " TO_CHAR(repair.enddate, 'DD.MM.YYYY'),"
                    + " typeofcrash.nameofcrash ,"
                    + " repair.repairstatus, "
                    + " repair.cost "
                    + " from repair"
                    + " inner join engineer on repair.PK_engineer=engineer.PK_engineer"
                    + " inner join myorder on myorder.PK_order=repair.PK_order"
                    + " inner join device on myorder.PK_device=device.PK_device"
                    + " inner join modeldevice on modeldevice.PK_modeldevice=device.PK_modeldevice"
                    + " inner join manufacturer on modeldevice.PK_manufacturer=manufacturer.PK_manufacturer"
                    + " inner join typeofcrash on typeofcrash.PK_crash=repair.PK_crash"
                    + " where engineer.PK_engineer=" + PK
            );
        } catch (SQLException ex) {
            Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTable1.setModel(DbUtils.resultSetToTableModel(resSet));

        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(0);
        jTable1.getColumnModel().getColumn(1).setHeaderValue("№ заказа");
        jTable1.getColumnModel().getColumn(2).setHeaderValue("Устройство");
        jTable1.getColumnModel().getColumn(3).setHeaderValue("Начало ремонта");
        jTable1.getColumnModel().getColumn(4).setHeaderValue("Конец ремонта");
        jTable1.getColumnModel().getColumn(5).setHeaderValue("Поломка");
        jTable1.getColumnModel().getColumn(6).setHeaderValue("Статус");
        jTable1.getColumnModel().getColumn(7).setHeaderValue("Стоимость");

        for (int i = 0; i < jTable1.getRowCount(); i++) {
            if (jTable1.getValueAt(i, 6).toString().equals("0")) {
                jTable1.setValueAt("Ремонтируется", i, 6);
            } else {
                if (jTable1.getValueAt(i, 6).toString().equals("1")) {
                    jTable1.setValueAt("Выполнено", i, 6);
                } else {
                    jTable1.setValueAt("Неизвестно", i, 6);
                }
            }
        }

        //если все ремонты заказа проведены - изменить статус заказа
        try {

            resSet = RepairMobile.st.executeQuery("select myorder.PK_ORDER,"
                    + " myorder.PK_STATUS"
                    + " from myorder "
                    + " inner join status on status.PK_status=myorder.PK_status"
                    + " where status.PK_status=12");
            ArrayList<String> orders = new ArrayList<>();
            while (resSet.next()) {
                orders.add(resSet.getString(1));
            }

            ArrayList<ArrayList<String>> listOrdCrash = new ArrayList<>();
            for (int i = 0; i < orders.size(); i++) {
                resSet = RepairMobile.st.executeQuery("select PK_ORDERcrash,pk_order,pk_crash"
                        + " from ordercrash where pk_order=" + orders.get(i)
                );
                ArrayList<String> listCrash = new ArrayList<>();
                while (resSet.next()) {
                    listCrash.add(resSet.getString(3));
                }
                listOrdCrash.add(listCrash);
            }

            ArrayList<ArrayList<String>> lstRep = new ArrayList<>();
            for (int i = 0; i < orders.size(); i++) {
                resSet = RepairMobile.st.executeQuery("select repair.PK_repair,"
                        + " myorder.pk_order,"
                        + " typeofcrash.pk_crash "
                        + " from repair"
                        + " inner join myorder on myorder.PK_order=repair.PK_order"
                        + " inner join typeofcrash on typeofcrash.PK_crash=repair.PK_crash"
                        + " inner join engineer on engineer.PK_engineer=repair.PK_engineer"
                        + " where engineer.PK_engineer=" + PK + " and repair.repairstatus=1 and myorder.pk_order=" + orders.get(i)
                );
                ArrayList<String> listCrash = new ArrayList<>();
                while (resSet.next()) {
                    listCrash.add(resSet.getString(1));
                }
                lstRep.add(listCrash);
            }

            for (int i = 0; i < orders.size(); i++) {
                if (lstRep.get(i).size() == listOrdCrash.get(i).size()) {
                    resSet = RepairMobile.st.executeQuery("update myorder set pk_status=15 where pk_order=" + orders.get(i)
                    );
                }
            }
            //
        } catch (SQLException ex) {
            Logger.getLogger(EngineerForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            resSet = RepairMobile.st.executeQuery("select repair.PK_repair,"
                    + " myorder.numoforder,"
                    + " manufacturer.nameofmanufacturer|| ' ' ||modeldevice.nameofmodel,"
                    + " TO_CHAR(repair.startdate, 'DD.MM.YYYY'),"
                    + " TO_CHAR(repair.enddate, 'DD.MM.YYYY'),"
                    + " typeofcrash.nameofcrash "
                    //+ " repair.repairstatus "

                    + " from repair"
                    + " inner join engineer on repair.PK_engineer=engineer.PK_engineer"
                    + " inner join myorder on myorder.PK_order=repair.PK_order"
                    + " inner join device on myorder.PK_device=device.PK_device"
                    + " inner join modeldevice on modeldevice.PK_modeldevice=device.PK_modeldevice"
                    + " inner join manufacturer on modeldevice.PK_manufacturer=manufacturer.PK_manufacturer"
                    + " inner join typeofcrash on typeofcrash.PK_crash=repair.PK_crash"
                    + " where engineer.PK_engineer=" + PK + " and repair.repairstatus=0"
            );
        } catch (SQLException ex) {
            Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTable2.setModel(DbUtils.resultSetToTableModel(resSet));
        jTable2.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable2.getColumnModel().getColumn(0).setMinWidth(0);
        jTable2.getColumnModel().getColumn(0).setPreferredWidth(0);
        jTable2.getColumnModel().getColumn(1).setHeaderValue("№ заказа");
        jTable2.getColumnModel().getColumn(2).setHeaderValue("Устройство");
        jTable2.getColumnModel().getColumn(3).setHeaderValue("Начало ремонта");
        jTable2.getColumnModel().getColumn(4).setHeaderValue("Конец ремонта");
        jTable2.getColumnModel().getColumn(5).setHeaderValue("Поломка");

        ResultSet resSetProizv = null;
        pkProizv = new ArrayList<String>();
        valueProizv = new ArrayList<String>();

        ResultSet resTypeDevice = null;
        pkTypeDevice = new ArrayList<String>();
        valueTypeDevice = new ArrayList<String>();

        pkDetail = new ArrayList<String>();
        valueDetail = new ArrayList<String>();

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
            }
            jComboBoxType.setModel(new DefaultComboBoxModel(valueTypeDevice.toArray()));
            jComboBoxType.setSelectedIndex(indSel);

            indSel = -1;
            resSetProizv = RepairMobile.st.executeQuery("select * from detail");
            tableModel = DbUtils.resultSetToTableModel(resSetProizv);
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                pkDetail.add(tableModel.getValueAt(i, 0).toString());
                valueDetail.add(tableModel.getValueAt(i, 1).toString());
            }
            jComboBoxDetail.setModel(new DefaultComboBoxModel(valueDetail.toArray()));
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
                    }
                    jComboBoxModel.setModel(new DefaultComboBoxModel(valueModel.toArray()));
                    jComboBoxModel.setSelectedIndex(indSel);
                } catch (SQLException ex) {
                    Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
        }
        //jTable2.getColumnModel().getColumn(6).setHeaderValue("Статус");

        /*for (int i = 0; i < jTable2.getRowCount(); i++) {
            if (jTable2.getValueAt(i, 6).toString().equals("0")) {
                jTable2.setValueAt("Ремонтируется", i, 6);
            } else {
                if (jTable2.getValueAt(i, 6).toString().equals("1")) {
                    jTable2.setValueAt("Выполнено", i, 6);
                } else {
                    jTable2.setValueAt("Неизвестно", i, 6);
                }
            }
        }*/

 /* try {
            resSet = RepairMobile.st.executeQuery("select repair.PK_repair,"
                    + " typeofdevice.nameoftype,"
                    + " manufacturer.nameofmanufacturer,"
                    + " device.modelofdevice,"
                    + " TO_CHAR(repair.startdate, 'DD.MM.YYYY'),"
                    + " TO_CHAR(repair.enddate, 'DD.MM.YYYY'),"
                    + "status.nameofStatus,"
                    + "repair.PK_device "
                    // + " engineer.FAMOFengineer || ' ' || engineer.NAMEOFengineer  || ' ' || engineer.OTChOFengineer"
                    + " from repair "
                    + " inner join engineer on repair.PK_engineer=engineer.PK_engineer"
                    + " inner join device on repair.PK_device=device.PK_device"
                    + " inner join manufacturer on device.PK_manufacturer=manufacturer.PK_manufacturer"
                    + " inner join typeofdevice on device.PK_typeofdevice=typeofdevice.PK_typeofdevice"
                    + " inner join myorder on myorder.PK_order=device.PK_order"
                    + " inner join status on status.PK_status=myOrder.PK_status"
                    + " where repair.PK_engineer=" + PK
                    + " and (status.pk_status=3 or status.pk_status=4)");
        } catch (SQLException ex) {
            Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTable2.setModel(DbUtils.resultSetToTableModel(resSet));

        jTable2.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable2.getColumnModel().getColumn(0).setMinWidth(0);
        jTable2.getColumnModel().getColumn(0).setPreferredWidth(0);
        jTable2.getColumnModel().getColumn(1).setHeaderValue("Тип");
        jTable2.getColumnModel().getColumn(2).setHeaderValue("Производитель");
        jTable2.getColumnModel().getColumn(3).setHeaderValue("Модель");
        jTable2.getColumnModel().getColumn(4).setHeaderValue("Начало ремонта");
        jTable2.getColumnModel().getColumn(5).setHeaderValue("Конец ремонта");
        jTable2.getColumnModel().getColumn(6).setHeaderValue("Статус");
        jTable2.getColumnModel().getColumn(7).setMaxWidth(0);
        jTable2.getColumnModel().getColumn(7).setMinWidth(0);
        jTable2.getColumnModel().getColumn(7).setPreferredWidth(0);
         */
        dtm = new DefaultTableModel();
        jTable3.setModel(dtm);
        dtm.addColumn("pk");
        dtm.addColumn("Деталь");
        dtm.addColumn("pk_type");
        dtm.addColumn("pk_model");
        dtm.addColumn("Устройство");
        dtm.addColumn("Колличество");
        jTable3.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable3.getColumnModel().getColumn(0).setMinWidth(0);
        jTable3.getColumnModel().getColumn(0).setPreferredWidth(0);

        jTable3.getColumnModel().getColumn(2).setMaxWidth(0);
        jTable3.getColumnModel().getColumn(2).setMinWidth(0);
        jTable3.getColumnModel().getColumn(2).setPreferredWidth(0);

        jTable3.getColumnModel().getColumn(3).setMaxWidth(0);
        jTable3.getColumnModel().getColumn(3).setMinWidth(0);
        jTable3.getColumnModel().getColumn(3).setPreferredWidth(0);
        try {
            resSet = RepairMobile.st.executeQuery("select myorder.PK_ORDER,myorder.NUMOFORDER,"
                    + "TO_CHAR(myorder.TIMETOACCEPT, 'DD.MM.YYYY'),"
                    + "TO_CHAR(myorder.TIMETODELIVERY, 'DD.MM.YYYY'),"
                    + "myorder.COSTOFORDER,myorder.TYPEOFORDER,myorder.PK_MANAGER"
                    + ",myorder.PK_STATUS,"
                    + " status.NAMEOFSTATUS,"
                    + " myorder.PK_CLIENT,"
                    + " client.FAMOFCLIENT || ' ' || client.NAMEOFCLIENT  || ' ' || client.OTCOFCLIENT,"
                    + " manufacturer.nameofmanufacturer|| ' ' ||modeldevice.nameofmodel "
                    //+ " device.pk_device "
                    // + " manager.FAMOFMANAGER || ' ' || manager.NAMEOFMANAGER  || ' ' || manager.OTCOFMANAGER"
                    + " from myorder "
                    + " inner join status on status.PK_status=myorder.PK_status"
                    + " inner join client on client.PK_client=myorder.PK_client"
                    + " inner join device on device.PK_device=myorder.PK_device"
                    + " inner join modeldevice on modeldevice.PK_modeldevice=device.PK_modeldevice"
                    + " inner join manufacturer on manufacturer.PK_manufacturer=modeldevice.PK_manufacturer"
                    + " where status.PK_status<>15 and status.PK_status<>16");
        } catch (SQLException ex) {
            Logger.getLogger(EngineerForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTableOrders.setModel(DbUtils.resultSetToTableModel(resSet));

        jTableOrders.getColumnModel().getColumn(1).setHeaderValue("Номер");
        jTableOrders.getColumnModel().getColumn(2).setHeaderValue("Дата создания");
        jTableOrders.getColumnModel().getColumn(3).setHeaderValue("Дата завершения");
        jTableOrders.getColumnModel().getColumn(4).setHeaderValue("Стоимость");
        jTableOrders.getColumnModel().getColumn(5).setHeaderValue("Тип");
        jTableOrders.getColumnModel().getColumn(8).setHeaderValue("Статус");
        jTableOrders.getColumnModel().getColumn(10).setHeaderValue("Клиент");
        jTableOrders.getColumnModel().getColumn(11).setHeaderValue("Устройство");

        //пк заказа
        jTableOrders.getColumnModel().getColumn(0).setMaxWidth(0);
        jTableOrders.getColumnModel().getColumn(0).setMinWidth(0);
        jTableOrders.getColumnModel().getColumn(0).setPreferredWidth(0);
        //пк менеджера
        jTableOrders.getColumnModel().getColumn(6).setMaxWidth(0);
        jTableOrders.getColumnModel().getColumn(6).setMinWidth(0);
        jTableOrders.getColumnModel().getColumn(6).setPreferredWidth(0);
        //пк статуса
        jTableOrders.getColumnModel().getColumn(7).setMaxWidth(0);
        jTableOrders.getColumnModel().getColumn(7).setMinWidth(0);
        jTableOrders.getColumnModel().getColumn(7).setPreferredWidth(0);
        //пк клиета
        jTableOrders.getColumnModel().getColumn(9).setMaxWidth(0);
        jTableOrders.getColumnModel().getColumn(9).setMinWidth(0);
        jTableOrders.getColumnModel().getColumn(9).setPreferredWidth(0);

        for (int i = 0; i < jTableOrders.getRowCount(); i++) {
            if (jTableOrders.getValueAt(i, 5).toString().equals("0")) {
                jTableOrders.setValueAt("Гарантийный", i, 5);
            } else {
                if (jTableOrders.getValueAt(i, 5).toString().equals("1")) {
                    jTableOrders.setValueAt("Не гарантийный", i, 5);
                } else {
                    jTableOrders.setValueAt("Неизвестно", i, 5);
                }
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane = new javax.swing.JTabbedPane();
        jPanelRemonts = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable( )         {             @Override             public boolean isCellEditable(int row, int column)             {                 return false;             }         };
        jButtonAdd = new javax.swing.JButton();
        jButtonUpdate = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableOrders = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jPanelCreateZapros = new javax.swing.JPanel();
        jButtonSend = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButtonDeleteDetail = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable( )         {             @Override             public boolean isCellEditable(int row, int column)             {                 return false;             }         };
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable( )         {             @Override             public boolean isCellEditable(int row, int column)             {                 return false;             }         };
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButtonAddDetail = new javax.swing.JButton();
        jComboBoxDetail = new javax.swing.JComboBox<String>();
        jLabel3 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jComboBoxType = new javax.swing.JComboBox<String>();
        jComboBoxManufacturer = new javax.swing.JComboBox<String>();
        jComboBoxModel = new javax.swing.JComboBox<String>();
        jLabelFIO = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Инженер");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Тип", "Производитель", "Модель", "Начало ремонта", "Конец ремонта", "Тип поломки", "Статус", "Стоимость"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButtonAdd.setText("Добавить");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jButtonUpdate.setText("Изменить");
        jButtonUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpdateActionPerformed(evt);
            }
        });

        jButtonDelete.setText("Удалить");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelRemontsLayout = new javax.swing.GroupLayout(jPanelRemonts);
        jPanelRemonts.setLayout(jPanelRemontsLayout);
        jPanelRemontsLayout.setHorizontalGroup(
            jPanelRemontsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRemontsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 944, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelRemontsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                    .addComponent(jButtonDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonAdd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelRemontsLayout.setVerticalGroup(
            jPanelRemontsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRemontsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelRemontsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelRemontsLayout.createSequentialGroup()
                        .addComponent(jButtonAdd)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonUpdate)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonDelete)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane.addTab("Ремонты", jPanelRemonts);

        jTableOrders.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTableOrders);

        jButton1.setText("Изменить статус");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 881, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane.addTab("Заказы", jPanel3);

        jButtonSend.setText("Отправить");
        jButtonSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSendActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Запрашиваемые детали"));

        jButtonDeleteDetail.setText("Удалить");
        jButtonDeleteDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteDetailActionPerformed(evt);
            }
        });

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Деталь", "Колличество"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonDeleteDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jButtonDeleteDetail)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Ремонты"));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Тип", "Производитель", "Модель", "Начало ремонта", "Конец ремонта", "", "Статус"
            }
        ));
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Дата запроса");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Добавить деталь в запрос"));

        jButtonAddDetail.setText("Добавить");
        jButtonAddDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddDetailActionPerformed(evt);
            }
        });

        jLabel3.setText("Деталь");

        jLabel1.setText("Тип устройства");

        jLabel5.setText("Производитель");

        jLabel6.setText("Модель");

        jLabel7.setText("Количество");

        jComboBoxManufacturer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxManufacturerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5)
                                .addComponent(jLabel7)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addComponent(jComboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jComboBoxDetail, 0, 217, Short.MAX_VALUE)
                                .addComponent(jComboBoxManufacturer, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jComboBoxModel, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonAddDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBoxManufacturer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxModel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addComponent(jButtonAddDetail)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanelCreateZaprosLayout = new javax.swing.GroupLayout(jPanelCreateZapros);
        jPanelCreateZapros.setLayout(jPanelCreateZaprosLayout);
        jPanelCreateZaprosLayout.setHorizontalGroup(
            jPanelCreateZaprosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCreateZaprosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCreateZaprosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelCreateZaprosLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(22, 22, 22)
                        .addGroup(jPanelCreateZaprosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButtonSend, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanelCreateZaprosLayout.setVerticalGroup(
            jPanelCreateZaprosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCreateZaprosLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanelCreateZaprosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCreateZaprosLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSend)
                        .addGap(33, 33, 33))
                    .addGroup(jPanelCreateZaprosLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelCreateZaprosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );

        jTabbedPane.addTab("Создание запроса", jPanelCreateZapros);

        jLabelFIO.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelFIO.setText("Инженер: Петров Игорь Олегович");

        jMenu1.setText("Файл");

        jMenuItem2.setText("Смена пользователя");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem1.setText("Закрыть");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelFIO, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jTabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelFIO)
                .addGap(1, 1, 1)
                .addComponent(jTabbedPane))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSendActionPerformed
        // TODO add your handling code here:
        ResultSet resSet = null;
        if (jTable2.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(rootPane, "Не выбран ремонт. Выделите запись в таблице.");
        } else {
            // if (jComboBoxStorekeeper.getSelectedIndex() == -1) {
            //   JOptionPane.showMessageDialog(rootPane, "Не выбран кладовщик");
            //} else {
            if (jTable3.getRowCount() <= 0) {
                JOptionPane.showMessageDialog(rootPane, "Не выбрана ни одна деталь");
            } else {
                try {
                    int pkRepair = Integer.parseInt(jTable2.getValueAt(jTable2.getSelectedRow(), 0).toString());
                    //int pkDevice = Integer.parseInt(jTable2.getValueAt(jTable2.getSelectedRow(), 7).toString());
                    //int idxComboBox = jComboBoxStorekeeper.getSelectedIndex();
                    // String pkStrokeeper = pkStorekeeper.get(idxComboBox);
                    java.sql.Date date = new java.sql.Date(jDateChooser1.getDateEditor().getDate().getTime());
                    RepairMobile.st.executeQuery("Insert into zapros (PK_Repair,timetoget,"
                            + "flagofcomplete) values ('" + pkRepair + "', TO_DATE('" + date + "', 'YYYY-MM-DD') ,'0')"
                    );
                    resSet = RepairMobile.st.executeQuery("select seqzapros.currval from dual");
                    int pkZapros = 0;
                    if (resSet.next()) {
                        pkZapros = resSet.getInt(1);
                    }

                    for (int i = 0; i < jTable3.getRowCount(); i++) {
                        String pkDet = (String) jTable3.getValueAt(i, 0);
                        String pkType = (String) jTable3.getValueAt(i, 2);
                        String pkModel = (String) jTable3.getValueAt(i, 3);
                        String countDetail = (String) jTable3.getValueAt(i, 5);
                        RepairMobile.st.executeQuery("Insert into concretedetail (pk_detail,pk_modeldevice,pk_typeofdevice)"
                                + " values ('" + pkDet + "','" + pkModel + "','" + pkType + "')");

                        resSet = RepairMobile.st.executeQuery("select seqconcretedetail.currval from dual");
                        int pkConcDet = 0;
                        if (resSet.next()) {
                            pkConcDet = resSet.getInt(1);
                        }

                        RepairMobile.st.executeQuery("Insert into detailofrequest"
                                + " (PK_zapros,PK_concretedetail,amount,pk_detailstatus) values ('" + pkZapros + "','" + pkConcDet + "','" + countDetail + "','1')");
                    }
                    JOptionPane.showMessageDialog(rootPane, "Запрос отправлен");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Не удалось добавить запрос");
                    Logger.getLogger(EngineerForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //}
        }

    }//GEN-LAST:event_jButtonSendActionPerformed

    private void jButtonAddDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddDetailActionPerformed
        // TODO add your handling code here:
        if (jTable2.getSelectedRow() != -1) {
            if (jComboBoxDetail.getSelectedIndex() == -1 || jComboBoxType.getSelectedIndex() == -1
                    || jComboBoxManufacturer.getSelectedIndex() == -1 || jComboBoxModel.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(rootPane, "Выберите конкретную деталь");
            } else {
                if ((int) jSpinner1.getValue() <= 0) {
                    JOptionPane.showMessageDialog(rootPane, "Деталей не может быть меньше одной");
                } else {
                    for (int i = 0; i < jTable3.getRowCount(); i++) {
                        if (pkDetail.get(jComboBoxDetail.getSelectedIndex()) == jTable3.getValueAt(i, 0)) {
                            JOptionPane.showMessageDialog(rootPane, "Такая деталь уже добавлена в список");
                            return;
                        }
                    }
                    Object[] objects = {
                        pkDetail.get(jComboBoxDetail.getSelectedIndex()),
                        valueDetail.get(jComboBoxDetail.getSelectedIndex()),
                        pkTypeDevice.get(jComboBoxDetail.getSelectedIndex()),
                        pkModel.get(jComboBoxDetail.getSelectedIndex()),
                        valueProizv.get(jComboBoxManufacturer.getSelectedIndex()) + " " + valueModel.get(jComboBoxModel.getSelectedIndex()),
                        jSpinner1.getValue().toString()
                    };
                    dtm.addRow(objects);

                }
            }

        } else {
            JOptionPane.showMessageDialog(rootPane, "Выберите ремонт");
        }

    }//GEN-LAST:event_jButtonAddDetailActionPerformed

    private void jButtonDeleteDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteDetailActionPerformed
        // TODO add your handling code here:
        if (jTable3.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(rootPane, "Выберите строку для удаления");
        } else {
            dtm.removeRow(jTable3.getSelectedRow());
        }

    }//GEN-LAST:event_jButtonDeleteDetailActionPerformed

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        // TODO add your handling code here:
        RepairAddUpdate repairAddUpdate = new RepairAddUpdate(0, -1, PK);
        repairAddUpdate.setListenerCloseForm(new ListenerCloseForm(this));
        repairAddUpdate.setVisible(true);

    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jButtonUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpdateActionPerformed
        // TODO add your handling code here:
        if (jTable1.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Выделите запись для изменения");
        } else {
            Object PKObj = jTable1.getValueAt(jTable1.getSelectedRow(), 0);
            int primKey = Integer.parseInt(PKObj.toString());
            RepairAddUpdate repairAddUpdate = new RepairAddUpdate(1, primKey, PK);
            repairAddUpdate.setListenerCloseForm(new ListenerCloseForm(this));
            repairAddUpdate.setVisible(true);
            this.setEnabled(false);
        }

    }//GEN-LAST:event_jButtonUpdateActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
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
                    RepairMobile.st.executeQuery("delete from repair where PK_repair=" + PK);
                    addDataInTable();
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Удаление невозможно");
                Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (jTableOrders.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Выделите заказ для изменения статуса!");
        } else {
            Object PK = jTableOrders.getValueAt(jTableOrders.getSelectedRow(), 0);
            int primKey = Integer.parseInt(PK.toString());
            EngineerUpdateStatus engineerUpdateStatus = new EngineerUpdateStatus(primKey);
            engineerUpdateStatus.setListenerCloseForm(new ListenerCloseForm(this));
            engineerUpdateStatus.setVisible(true);
            this.setEnabled(false);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jTable2MouseClicked

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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonAddDetail;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonDeleteDetail;
    private javax.swing.JButton jButtonSend;
    private javax.swing.JButton jButtonUpdate;
    private javax.swing.JComboBox<String> jComboBoxDetail;
    private javax.swing.JComboBox<String> jComboBoxManufacturer;
    private javax.swing.JComboBox<String> jComboBoxModel;
    private javax.swing.JComboBox<String> jComboBoxType;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabelFIO;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelCreateZapros;
    private javax.swing.JPanel jPanelRemonts;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTableOrders;
    // End of variables declaration//GEN-END:variables
}
