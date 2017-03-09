/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.rem.otdel;

import com.placeholder.PlaceHolder;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import spravochn.manufacture.ManufacturerAddUpdate;
import spravochn.typeofcrash.TypeCrashAddUpdate;
import spravochn.typeofdevice.TypeDeviceAddUpdate;
import net.proteanit.sql.DbUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import spravochn.manufacture.Manufacturer;
import spravochn.model.Model;
import spravochn.model.ModelAddUpdate;
import spravochn.typeofcrash.TypeCrash;
import spravochn.typeofdevice.TypeDevice;

/**
 *
 * @author tigler
 */
public class Orders extends javax.swing.JFrame implements UpdatesDataInForms {

    /**
     * Creates new form Orders
     */
    boolean flagSelDetals;
    PlaceHolder holder;
    private int PK;
    private int PKClient;
    private boolean isCreateNew = true;

    final int DIAGNOSTIC_COST = 300;
    final int GARANT_COST = 0;

    DefaultListModel<MyMap> modelAllCrash;
    DefaultListModel<MyMap> modelSelCrash;

    int countCost;
    int rowTypeDetail;

    //Обновление верхушки
    public void addTopData() {
        
        
        try {

            ArrayList<String> type = new ArrayList<String>();
            type.add("Гарантийный");
            type.add("Не гарантийный");
            jComboBoxType.setModel(new DefaultComboBoxModel(type.toArray()));
            jComboBoxType.setSelectedIndex(-1);

            pkReplace = new ArrayList<String>();
            valueReplace = new ArrayList<String>();
            imeiReplace = new ArrayList<String>();
            String req = "";
            boolean fl = true;
            ResultSet resSetReplace1 = RepairMobile.st.executeQuery("select clientmobile.pk_keyofchangemobile,clientmobile.datereturn from clientmobile");
            while (true) {
                if (resSetReplace1.next()) {
                    if (fl) {
                        req += "replacemobile.pk_keyofchangemobile <> " + resSetReplace1.getString(1);
                        fl = false;
                    } else {
                        req += " and replacemobile.pk_keyofchangemobile <> " + resSetReplace1.getString(1);
                    }
                } else {
                    break;
                }

            }
            ResultSet resSetReplace = null;
            if (req.equals("")) {
                resSetReplace = RepairMobile.st.executeQuery("select replacemobile.pk_keyofchangemobile,replacemobile.model,"
                        + "replacemobile.imeinumber from replacemobile ");
            } else {
                resSetReplace = RepairMobile.st.executeQuery("select replacemobile.pk_keyofchangemobile,replacemobile.model,"
                        + "replacemobile.imeinumber from replacemobile where " + req);
            }
            TableModel tableModel = DbUtils.resultSetToTableModel(resSetReplace);
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                pkReplace.add(tableModel.getValueAt(i, 0).toString());
                valueReplace.add(tableModel.getValueAt(i, 1).toString());
                imeiReplace.add(tableModel.getValueAt(i, 2).toString());
            }
            jComboBoxReplace.setModel(new DefaultComboBoxModel(valueReplace.toArray()));

            jComboBoxType.setSelectedIndex(-1);
            jComboBoxReplace.setSelectedIndex(-1);
        } catch (SQLException ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Orders(int PK) {
        initComponents();
        //
        addTopData();
        //
        this.PK = PK;
        flagSelDetals = false;
        modelAllCrash = new DefaultListModel<MyMap>();
        modelSelCrash = new DefaultListModel<MyMap>();
        addDataInTable();
        jTextFieldCost.setEditable(false);
        try {

            ImageIcon icon = new ImageIcon(getClass().getResource("/img/edit.png"));
            Image img = icon.getImage();
            Image newimg = img.getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
            jButton3.setIcon(new ImageIcon(newimg));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        try {

            ImageIcon icon = new ImageIcon(getClass().getResource("/img/delete.png"));
            Image img = icon.getImage();
            Image newimg = img.getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
            jButtonDeleteOrder.setIcon(new ImageIcon(newimg));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        holder = new PlaceHolder(jTextFieldAddFam, "Фамилия");
        holder = new PlaceHolder(jTextFieldAddName, "Имя");
        holder = new PlaceHolder(jTextFieldAddOtch, "Отчество");
        holder = new PlaceHolder(jTextFieldAddTelefon, "Телефон");
        holder = new PlaceHolder(jTextFieldAddress, "Адрес");
        holder = new PlaceHolder(jTextFieldIMEI, "IMEI");

        try {

            ImageIcon icon = new ImageIcon(getClass().getResource("/img/ok.png"));

            Image img = icon.getImage();
            Image newimg = img.getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
            jButtonAccept.setIcon(new ImageIcon(newimg));
        } catch (Exception ex) {
            System.out.println(ex);
        }

        try {

            ImageIcon icon = new ImageIcon(getClass().getResource("/img/user.png"));

            Image img = icon.getImage();
            Image newimg = img.getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
            jButtonChooseExist.setIcon(new ImageIcon(newimg));
        } catch (Exception ex) {
            System.out.println(ex);
        }

        try {

            ImageIcon icon = new ImageIcon(getClass().getResource("/img/mobile.png"));

            Image img = icon.getImage();
            Image newimg = img.getScaledInstance(30, 25, java.awt.Image.SCALE_SMOOTH);
            jButton1.setIcon(new ImageIcon(newimg));
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void addDataInTable() {
        this.setEnabled(true);
        ResultSet resSet = null;
        ResultSet resSet2 = null;
        ResultSet resSet3 = null;

        jComboBoxModel.setModel(new DefaultComboBoxModel<String>());
        jTextFieldIMEI.setText("");
        jTextFieldCost.setText("");
        
        
        holder = new PlaceHolder(jTextFieldAddFam, "Фамилия");
        holder = new PlaceHolder(jTextFieldAddName, "Имя");
        holder = new PlaceHolder(jTextFieldAddOtch, "Отчество");
        holder = new PlaceHolder(jTextFieldAddTelefon, "Телефон");
        holder = new PlaceHolder(jTextFieldAddress, "Адрес");
        holder = new PlaceHolder(jTextFieldIMEI, "IMEI");

        try {
            resSet = RepairMobile.st.executeQuery("select manager.FAMOFMANAGER,manager.NAMEOFMANAGER,manager.OTCOFMANAGER from manager"
                    + " where pk_manager=" + PK);
            if (resSet.next()) {
                jLabelFIO.setText("Менеджер: " + resSet.getString(1) + " "
                        + resSet.getString(2) + " "
                        + resSet.getString(3));
            }
        } catch (SQLException ex) {
            Logger.getLogger(EngineerForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            resSet = RepairMobile.st.executeQuery("select myorder.PK_ORDER,myorder.NUMOFORDER,"
                    + "TO_CHAR(myorder.TIMETOACCEPT, 'DD.MM.YYYY'),"
                    + "TO_CHAR(myorder.TIMETODELIVERY, 'DD.MM.YYYY'),"
                    + "myorder.COSTOFORDER,myorder.TYPEOFORDER,myorder.PK_MANAGER"
                    + ",myorder.PK_STATUS,"
                    + " status.NAMEOFSTATUS,"
                    + " myorder.PK_CLIENT,"
                    + " client.FAMOFCLIENT || ' ' || client.NAMEOFCLIENT  || ' ' || client.OTCOFCLIENT"
                    + " from myorder "
                    + " inner join status on status.PK_status=myorder.PK_status"
                    + " inner join manager on manager.PK_manager=myorder.PK_manager"
                    + " inner join client on client.PK_client=myorder.PK_client"
                    + " where myorder.PK_status=13 or myorder.PK_status=15 or myorder.PK_status=16");
            jTable1.setModel(DbUtils.resultSetToTableModel(resSet));

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.addColumn("на замене");
            dtm.addColumn("pk_rep");
            for (int i = 0; i < jTable1.getRowCount(); i++) {
                resSet2 = RepairMobile.st.executeQuery("select clientmobile.PK_clientmobile,clientmobile.pk_client,"
                        + " clientmobile.pk_keyofchangemobile,"
                        + " replacemobile.model, replacemobile.imeinumber from clientmobile"
                        + " inner join replacemobile on  clientmobile.pk_keyofchangemobile=replacemobile.pk_keyofchangemobile"
                        + " where pk_client=" + jTable1.getValueAt(i, 9).toString()
                );
                if (resSet2.next()) {

                    jTable1.setValueAt(resSet2.getString(4) + " " + resSet2.getString(5), i, 11);
                    jTable1.setValueAt(resSet2.getString(1), i, 12);
                } else {
                    jTable1.setValueAt("", i, 11);
                    jTable1.setValueAt("", i, 12);
                }
            }

            resSet2 = RepairMobile.st.executeQuery("select myorder.PK_ORDER,myorder.NUMOFORDER,"
                    + "TO_CHAR(myorder.TIMETOACCEPT, 'DD.MM.YYYY'),"
                    + "TO_CHAR(myorder.TIMETODELIVERY, 'DD.MM.YYYY'),"
                    + "myorder.COSTOFORDER,myorder.TYPEOFORDER,myorder.PK_MANAGER"
                    + ",myorder.PK_STATUS,"
                    + " status.NAMEOFSTATUS,"
                    + " myorder.PK_CLIENT,"
                    + " client.FAMOFCLIENT || ' ' || client.NAMEOFCLIENT  || ' ' || client.OTCOFCLIENT"
                    // + " manager.FAMOFMANAGER || ' ' || manager.NAMEOFMANAGER  || ' ' || manager.OTCOFMANAGER"
                    + " from myorder "
                    + " inner join status on status.PK_status=myorder.PK_status"
                    + " inner join manager on manager.PK_manager=myorder.PK_manager"
                    + " inner join client on client.PK_client=myorder.PK_client"
                    + " ");
            jTable2.setModel(DbUtils.resultSetToTableModel(resSet2));

            DefaultTableModel dtm2 = (DefaultTableModel) jTable2.getModel();
            dtm2.addColumn("на замене");
            for (int i = 0; i < jTable2.getRowCount(); i++) {
                resSet = RepairMobile.st.executeQuery("select clientmobile.PK_clientmobile,clientmobile.pk_client,"
                        + " clientmobile.pk_keyofchangemobile,"
                        + " replacemobile.model, replacemobile.imeinumber from clientmobile"
                        + " inner join replacemobile on  clientmobile.pk_keyofchangemobile=replacemobile.pk_keyofchangemobile"
                        + " where pk_client=" + jTable2.getValueAt(i, 9).toString()
                );

                if (resSet.next()) {
                    jTable2.setValueAt(resSet.getString(4) + " " + resSet.getString(5), i, 11);
                } else {
                    jTable2.setValueAt("", i, 11);
                }
            }

            resSet3 = RepairMobile.st.executeQuery("select concretedetail.PK_CONCRETEDETAIL,"
                    + " concretedetail.PK_detail,"
                    + " detail.NAMEOFdetail,"
                    + " concretedetail.costofdetail,"
                    + " concretedetail.PK_typeofdevice,"
                    + " typeofdevice.nameoftype,"
                    + " concretedetail.PK_modeldevice,"
                    + " modeldevice.nameofmodel,"
                    + " modeldevice.pk_manufacturer,"
                    + " manufacturer.nameofmanufacturer"
                    + " from concretedetail "
                    + " inner join detail on detail.PK_detail=concretedetail.PK_detail"
                    + " inner join typeofdevice on typeofdevice.PK_typeofdevice=concretedetail.PK_typeofdevice"
                    + " inner join modeldevice on modeldevice.PK_modeldevice=concretedetail.PK_modeldevice"
                    + " inner join manufacturer on manufacturer.PK_manufacturer=modeldevice.PK_manufacturer"
            );
            jTable3.setModel(DbUtils.resultSetToTableModel(resSet3));
        } catch (SQLException ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
        }

        jTable1.getColumnModel().getColumn(1).setHeaderValue("Номер");
        jTable1.getColumnModel().getColumn(2).setHeaderValue("Дата создания");
        jTable1.getColumnModel().getColumn(3).setHeaderValue("Дата завершения");
        jTable1.getColumnModel().getColumn(4).setHeaderValue("Стоимость");
        jTable1.getColumnModel().getColumn(5).setHeaderValue("Тип");
        jTable1.getColumnModel().getColumn(8).setHeaderValue("Статус");
        jTable1.getColumnModel().getColumn(10).setHeaderValue("Клиент");

        //пк заказа
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(0);
        //пк менеджера
        jTable1.getColumnModel().getColumn(6).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(6).setMinWidth(0);
        jTable1.getColumnModel().getColumn(6).setPreferredWidth(0);
        //пк статуса
        jTable1.getColumnModel().getColumn(7).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(7).setMinWidth(0);
        jTable1.getColumnModel().getColumn(7).setPreferredWidth(0);
        //пк клиета
        jTable1.getColumnModel().getColumn(9).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(9).setMinWidth(0);
        jTable1.getColumnModel().getColumn(9).setPreferredWidth(0);

        jTable1.getColumnModel().getColumn(12).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(12).setMinWidth(0);
        jTable1.getColumnModel().getColumn(12).setPreferredWidth(0);

        for (int i = 0; i < jTable1.getRowCount(); i++) {
            if (jTable1.getValueAt(i, 5).toString().equals("0")) {
                jTable1.setValueAt("Гарантийный", i, 5);
            } else {
                if (jTable1.getValueAt(i, 5).toString().equals("1")) {
                    jTable1.setValueAt("Не гарантийный", i, 5);
                } else {
                    jTable1.setValueAt("Неизвестно", i, 5);
                }
            }
        }

        //
        jTable2.getColumnModel().getColumn(1).setHeaderValue("Номер");
        jTable2.getColumnModel().getColumn(2).setHeaderValue("Дата создания");
        jTable2.getColumnModel().getColumn(3).setHeaderValue("Дата завершения");
        jTable2.getColumnModel().getColumn(4).setHeaderValue("Стоимость");
        jTable2.getColumnModel().getColumn(5).setHeaderValue("Тип");
        jTable2.getColumnModel().getColumn(8).setHeaderValue("Статус");
        jTable2.getColumnModel().getColumn(10).setHeaderValue("Клиент");

        //пк заказа
        jTable2.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable2.getColumnModel().getColumn(0).setMinWidth(0);
        jTable2.getColumnModel().getColumn(0).setPreferredWidth(0);
        //пк менеджера
        jTable2.getColumnModel().getColumn(6).setMaxWidth(0);
        jTable2.getColumnModel().getColumn(6).setMinWidth(0);
        jTable2.getColumnModel().getColumn(6).setPreferredWidth(0);
        //пк статуса
        jTable2.getColumnModel().getColumn(7).setMaxWidth(0);
        jTable2.getColumnModel().getColumn(7).setMinWidth(0);
        jTable2.getColumnModel().getColumn(7).setPreferredWidth(0);
        //пк клиета
        jTable2.getColumnModel().getColumn(9).setMaxWidth(0);
        jTable2.getColumnModel().getColumn(9).setMinWidth(0);
        jTable2.getColumnModel().getColumn(9).setPreferredWidth(0);

        for (int i = 0; i < jTable2.getRowCount(); i++) {
            if (jTable2.getValueAt(i, 5).toString().equals("0")) {
                jTable2.setValueAt("Гарантийный", i, 5);
            } else {
                if (jTable2.getValueAt(i, 5).toString().equals("1")) {
                    jTable2.setValueAt("Не гарантийный", i, 5);
                } else {
                    jTable2.setValueAt("Неизвестно", i, 5);
                }
            }
        }

        //типовые детали
        jTable3.getColumnModel().getColumn(2).setHeaderValue("Деталь");
        jTable3.getColumnModel().getColumn(3).setHeaderValue("Цена");
        jTable3.getColumnModel().getColumn(5).setHeaderValue("Тип устройства");
        jTable3.getColumnModel().getColumn(7).setHeaderValue("Модель");
        jTable3.getColumnModel().getColumn(9).setHeaderValue("Производитель");
        //jTable3.getColumnModel().getColumn(6).setHeaderValue("Наличие");

        //пк типовой детали
        jTable3.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable3.getColumnModel().getColumn(0).setMinWidth(0);
        jTable3.getColumnModel().getColumn(0).setPreferredWidth(0);

        jTable3.getColumnModel().getColumn(1).setMaxWidth(0);
        jTable3.getColumnModel().getColumn(1).setMinWidth(0);
        jTable3.getColumnModel().getColumn(1).setPreferredWidth(0);

        jTable3.getColumnModel().getColumn(4).setMaxWidth(0);
        jTable3.getColumnModel().getColumn(4).setMinWidth(0);
        jTable3.getColumnModel().getColumn(4).setPreferredWidth(0);

        jTable3.getColumnModel().getColumn(6).setMaxWidth(0);
        jTable3.getColumnModel().getColumn(6).setMinWidth(0);
        jTable3.getColumnModel().getColumn(6).setPreferredWidth(0);

        jTable3.getColumnModel().getColumn(8).setMaxWidth(0);
        jTable3.getColumnModel().getColumn(8).setMinWidth(0);
        jTable3.getColumnModel().getColumn(8).setPreferredWidth(0);

        jDateChooserAddDate.setDateFormatString("dd.MM.yyyy");
        jDateChooserAddDate.setDate(new Date());
        JTextFieldDateEditor editor2 = (JTextFieldDateEditor) jDateChooserAddDate.getDateEditor();
        editor2.setEditable(false);

        ResultSet resSetCrash = null;
        pkCrash = new ArrayList<String>();
        valueCrash = new ArrayList<String>();

        ResultSet resSetProizv = null;
        pkProizv = new ArrayList<String>();
        valueProizv = new ArrayList<String>();

        ResultSet resTypeDevice = null;
        pkTypeDevice = new ArrayList<String>();
        valueTypeDevice = new ArrayList<String>();
        try {

            //был replace
            jComboBoxModel.setEnabled(false);
            //тип поломки
            resSetCrash = RepairMobile.st.executeQuery("select * from typeofcrash");
            TableModel tableModel = DbUtils.resultSetToTableModel(resSetCrash);
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                pkCrash.add(tableModel.getValueAt(i, 0).toString());
                valueCrash.add(tableModel.getValueAt(i, 1).toString());
            }
            modelAllCrash = new DefaultListModel<>();
            modelSelCrash = new DefaultListModel<>();
            for (int i = 0; i < valueCrash.size(); i++) {
                MyMap mymap = new MyMap(pkCrash.get(i), valueCrash.get(i));
                modelAllCrash.addElement(mymap);
            }
            jListAllCrash.setModel(modelAllCrash);
            jListSelCrash.setModel(modelSelCrash);
            jListAllCrash.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jListSelCrash.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            //jComboBoxTypeCrash.setModel(new DefaultComboBoxModel(valueCrash.toArray()));
            //производитель
            resSetProizv = RepairMobile.st.executeQuery("select * from manufacturer");
            tableModel = DbUtils.resultSetToTableModel(resSetProizv);
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                pkProizv.add(tableModel.getValueAt(i, 0).toString());
                valueProizv.add(tableModel.getValueAt(i, 1).toString());
            }
            jComboBoxManufacturers.setModel(new DefaultComboBoxModel(valueProizv.toArray()));

            //тип устройства
            resSetProizv = RepairMobile.st.executeQuery("select * from typeofdevice");
            tableModel = DbUtils.resultSetToTableModel(resSetProizv);
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                pkTypeDevice.add(tableModel.getValueAt(i, 0).toString());
                valueTypeDevice.add(tableModel.getValueAt(i, 1).toString());
            }
            jComboBoxTypeDevice.setModel(new DefaultComboBoxModel(valueTypeDevice.toArray()));

            //jComboBoxTypeCrash.setSelectedIndex(-1);
            jComboBoxTypeDevice.setSelectedIndex(-1);
            jComboBoxManufacturers.setSelectedIndex(-1);

            //
            correctSizeTable(jTable2);
            correctSizeTable(jTable1);
        } catch (SQLException ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    ArrayList<String> pkReplace;
    ArrayList<String> valueReplace;
    ArrayList<String> imeiReplace;
    ArrayList<String> pkModel;
    ArrayList<String> valueModel;
    ArrayList<String> pkCrash;
    ArrayList<String> valueCrash;
    ArrayList<String> pkProizv;
    ArrayList<String> valueProizv;
    ArrayList<String> pkTypeDevice;
    ArrayList<String> valueTypeDevice;

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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        menuBar1 = new java.awt.MenuBar();
        menu1 = new java.awt.Menu();
        menu2 = new java.awt.Menu();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jTextFieldAddFam = new javax.swing.JTextField();
        jTextFieldAddName = new javax.swing.JTextField();
        jTextFieldAddOtch = new javax.swing.JTextField();
        jTextFieldAddTelefon = new javax.swing.JTextField();
        jButtonChooseExist = new javax.swing.JButton();
        jTextFieldAddress = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jComboBoxManufacturers = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jComboBoxTypeDevice = new javax.swing.JComboBox<>();
        jButtonAddModel = new javax.swing.JButton();
        jButtonAddManufacturer = new javax.swing.JButton();
        jComboBoxModel = new javax.swing.JComboBox<>();
        jTextFieldIMEI = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jComboBoxType = new javax.swing.JComboBox();
        jDateChooserAddDate = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldCost = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxReplace = new javax.swing.JComboBox<>();
        jButtonAccept = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jListAllCrash = new javax.swing.JList<>();
        jButtonAddCrash = new javax.swing.JButton();
        jButtonRetCrash = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jListSelCrash = new javax.swing.JList<>();
        jButtonAddTypeOfCrash = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jButton7 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable( )
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        jButton3 = new javax.swing.JButton();
        jButtonDeleteOrder = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable( )
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        jButton1 = new javax.swing.JButton();
        jLabelFIO = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuClose = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItemStatus = new javax.swing.JMenuItem();
        jMenuItemManufacturer = new javax.swing.JMenuItem();
        jMenuItemTypeCrash = new javax.swing.JMenuItem();
        jMenuItemTypeDevice = new javax.swing.JMenuItem();

        jMenu1.setText("jMenu1");

        menu1.setLabel("File");
        menuBar1.add(menu1);

        menu2.setLabel("Edit");
        menuBar1.add(menu2);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Заказы");

        jTabbedPane1.setToolTipText("");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Клиент"));

        jTextFieldAddFam.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldAddFamFocusLost(evt);
            }
        });

        jTextFieldAddOtch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldAddOtchActionPerformed(evt);
            }
        });

        jButtonChooseExist.setText("Cуществующие пользователи");
        jButtonChooseExist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonChooseExistActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldAddFam, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                            .addComponent(jTextFieldAddName)
                            .addComponent(jTextFieldAddOtch)
                            .addComponent(jTextFieldAddTelefon)
                            .addComponent(jTextFieldAddress)))
                    .addComponent(jButtonChooseExist, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextFieldAddFam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextFieldAddName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextFieldAddOtch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jTextFieldAddTelefon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextFieldAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonChooseExist)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Устройство"));

        jComboBoxManufacturers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxManufacturersActionPerformed(evt);
            }
        });

        jLabel5.setText("Производитель");

        jLabel6.setText("Модель");

        jLabel7.setText("Тип устройства");

        jComboBoxTypeDevice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTypeDeviceActionPerformed(evt);
            }
        });

        jButtonAddModel.setText("Добавить модель");
        jButtonAddModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddModelActionPerformed(evt);
            }
        });

        jButtonAddManufacturer.setText("Добавить производителя");
        jButtonAddManufacturer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddManufacturerActionPerformed(evt);
            }
        });

        jComboBoxModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxModelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonAddManufacturer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonAddModel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jComboBoxModel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jComboBoxManufacturers, 0, 200, Short.MAX_VALUE)
                                    .addComponent(jComboBoxTypeDevice, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jTextFieldIMEI))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jComboBoxTypeDevice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBoxManufacturers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxModel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldIMEI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonAddModel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonAddManufacturer)
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Заказ"));

        jLabel9.setText("Дата оформления");

        jLabel10.setText("Тип заказа");

        jComboBoxType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTypeActionPerformed(evt);
            }
        });

        jLabel11.setText("Стоимость");

        jLabel1.setText("Телефон на замену");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jLabel9))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jDateChooserAddDate, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(102, 102, 102)
                        .addComponent(jLabel10))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jComboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(jComboBoxReplace, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextFieldCost, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel11)
                        .addGap(85, 85, 85))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooserAddDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFieldCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBoxReplace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 13, Short.MAX_VALUE))
        );

        jButtonAccept.setText("Оформить");
        jButtonAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAcceptActionPerformed(evt);
            }
        });

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Типы поломки"));

        jScrollPane5.setViewportView(jListAllCrash);

        jButtonAddCrash.setText("↓");
        jButtonAddCrash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddCrashActionPerformed(evt);
            }
        });

        jButtonRetCrash.setText("↑");
        jButtonRetCrash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRetCrashActionPerformed(evt);
            }
        });

        jScrollPane6.setViewportView(jListSelCrash);

        jButtonAddTypeOfCrash.setText("Добавить тип поломки");
        jButtonAddTypeOfCrash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddTypeOfCrashActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(jButtonAddCrash)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonRetCrash)
                .addGap(59, 59, 59))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addComponent(jScrollPane6)
                .addContainerGap())
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonAddTypeOfCrash, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonRetCrash)
                    .addComponent(jButtonAddCrash))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonAddTypeOfCrash)
                .addContainerGap())
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Используемые детали"));

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Деталь", "Цена"
            }
        ));
        jScrollPane4.setViewportView(jTable4);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonAccept, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jButtonAccept, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        jTabbedPane1.addTab("Добавление", jPanel1);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Деталь", "Стоимость", "Наличие", "Производитель", "Тип", "Модель", "Наличие"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        jButton7.setText("Включить в стоимость");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 989, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jButton7)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Типовые детали", jPanel7);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "№", "Статус", "Клиент", "Тип", "Стоимость", "Дата создания", "Дата завершения", "На замене"
            }
        ));
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(10);
            jTable2.getColumnModel().getColumn(2).setPreferredWidth(130);
            jTable2.getColumnModel().getColumn(3).setPreferredWidth(50);
            jTable2.getColumnModel().getColumn(4).setPreferredWidth(25);
            jTable2.getColumnModel().getColumn(5).setPreferredWidth(50);
            jTable2.getColumnModel().getColumn(6).setPreferredWidth(60);
        }

        jButton3.setText("Редактировать");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButtonDeleteOrder.setText("Удалить");
        jButtonDeleteOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteOrderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 989, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonDeleteOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jButtonDeleteOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Просмотр всех заказов", jPanel6);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "№", "Статус", "Клиент", "Тип", "Стоимость", "Дата создания", "Дата завершения", "На замене"
            }
        ));
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(10);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(30);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(130);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(25);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(6).setPreferredWidth(60);
        }

        jButton1.setText("Выдать");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 989, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Выдача", jPanel2);

        jLabelFIO.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelFIO.setText("Менеджер: Петрова Екатерина Сергеевна");
        jLabelFIO.setToolTipText("");

        jMenu2.setText("Файл");

        jMenuItem1.setText("Смена пользователя");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuClose.setText("Закрыть");
        jMenuClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuCloseActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuClose);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Справочники");

        jMenuItemStatus.setText("Статусы");
        jMenuItemStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemStatusActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemStatus);

        jMenuItemManufacturer.setText("Производители");
        jMenuItemManufacturer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemManufacturerActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemManufacturer);

        jMenuItemTypeCrash.setText("Типы поломок");
        jMenuItemTypeCrash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemTypeCrashActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemTypeCrash);

        jMenuItemTypeDevice.setText("Типы устройств");
        jMenuItemTypeDevice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemTypeDeviceActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemTypeDevice);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelFIO, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1018, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabelFIO, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemManufacturerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemManufacturerActionPerformed
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setVisible(true);
    }//GEN-LAST:event_jMenuItemManufacturerActionPerformed

    private void jMenuItemStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemStatusActionPerformed
        Model status = new Model();
        status.setVisible(true);
    }//GEN-LAST:event_jMenuItemStatusActionPerformed

    private void jMenuItemTypeDeviceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTypeDeviceActionPerformed
        TypeDevice typeDevice = new TypeDevice();
        typeDevice.setVisible(true);
    }//GEN-LAST:event_jMenuItemTypeDeviceActionPerformed

    private void jMenuItemTypeCrashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTypeCrashActionPerformed
        TypeCrash typeCrash = new TypeCrash();
        typeCrash.setVisible(true);
    }//GEN-LAST:event_jMenuItemTypeCrashActionPerformed

    private void jMenuCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_jMenuCloseActionPerformed

    public void resetElements() {
        jTextFieldIMEI.setText("");
        jTextFieldAddress.setText("");
        jComboBoxManufacturers.setSelectedIndex(-1);
        //jComboBoxTypeCrash.setSelectedIndex(-1);
        jComboBoxTypeDevice.setSelectedIndex(-1);

        jTextFieldAddFam.setText("");
        jTextFieldAddName.setText("");
        jTextFieldAddOtch.setText("");
        jTextFieldAddTelefon.setText("");
        jComboBoxModel.setSelectedIndex(-1);
        countCost = 0;
        rowTypeDetail = 0;
        DefaultTableModel dtm = (DefaultTableModel) jTable4.getModel();
        dtm.getDataVector().removeAllElements();
    }

    public void setUser(int pkClient) {
        try {
            ResultSet resSet = RepairMobile.st.executeQuery("SELECT PK_Client,NameOfClient,"
                    + "FamOfClient,OtcOfClient,NUMBEROFPHONE,address "
                    + " from client "
                    + " WHERE PK_Client= '" + pkClient + "'"
            );
            resSet.next();
            jTextFieldAddFam.setText(resSet.getString(3));
            jTextFieldAddName.setText(resSet.getString(2));
            jTextFieldAddOtch.setText(resSet.getString(4));
            jTextFieldAddTelefon.setText(resSet.getString(5));
            jTextFieldAddress.setText(resSet.getString(6));
            PKClient = Integer.parseInt(resSet.getString(1));
            isCreateNew = false;

        } catch (SQLException ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void jButtonAcceptActionPerformed(java.awt.event.ActionEvent evt) {
        //
        if (jTextFieldAddFam.getText().equals("") || jTextFieldAddName.getText().equals("") || jTextFieldAddOtch.getText().equals("")
                || jTextFieldAddTelefon.getText().equals("") || jTextFieldAddress.getText().equals("") || jTextFieldIMEI.getText().equals("")
                || jComboBoxTypeDevice.getSelectedIndex() == -1
                || jComboBoxManufacturers.getSelectedIndex() == -1
                || jComboBoxModel.getSelectedIndex() == -1
                || jComboBoxType.getSelectedIndex() == -1 || jListSelCrash.getModel().getSize() == 0) {
            JOptionPane.showMessageDialog(this, "Пожалуйста заполните все поля");
            return;
        }

        if (jTextFieldAddTelefon.getText().length() != 12 || jTextFieldAddTelefon.getText().charAt(0) != '+') {
            JOptionPane.showMessageDialog(this, "Неверный номер телефона");
            return;
        }

        String textFam = jTextFieldAddFam.getText();
        String textName = jTextFieldAddName.getText();
        String textOtch = jTextFieldAddOtch.getText();
        String textTelefon = jTextFieldAddTelefon.getText();
        String textAddress = jTextFieldAddress.getText();
        //проверка по номеру телефона
        try {
            ResultSet resSet = RepairMobile.st.executeQuery("SELECT PK_Client,NameOfClient,FamOfClient,OtcOfClient  "
                    + " from client "
                    + " WHERE NUMBEROFPHONE= '" + textTelefon + "'"
            );

            if (resSet.next() && isCreateNew == true) {
                int reply = JOptionPane.showConfirmDialog(null, "Пользователь с данным номером телефона уже существует:\n"
                        + " " + resSet.getString(3) + " " + resSet.getString(2) + " " + resSet.getString(4) + "\n"
                        + " Хотите выбрать его?", "Выбор", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    jTextFieldAddFam.setText(resSet.getString(3));
                    jTextFieldAddName.setText(resSet.getString(2));
                    jTextFieldAddOtch.setText(resSet.getString(4));
                    PKClient = Integer.parseInt(resSet.getString(1));
                    isCreateNew = false;
                }
            }
            //добавление клиента
            if (isCreateNew == true) {
                resSet = RepairMobile.st.executeQuery("INSERT INTO CLIENT (NAMEOFCLIENT, FAMOFCLIENT, OTCOFCLIENT, NUMBEROFPHONE,address) "
                        + "VALUES ('" + textName + "', '" + textFam + "', '" + textOtch + "', '" + textTelefon + "','" + textAddress + "')"
                );
                resSet = RepairMobile.st.executeQuery("select SEQCLIENT.currval from dual");
                int pkZapros = 0;
                if (resSet.next()) {
                    PKClient = resSet.getInt(1);
                }
            }

            //добавление заказа
            int typeOfOrder = jComboBoxType.getSelectedIndex();
            Date dateChooserAddDate = jDateChooserAddDate.getDate();
            java.sql.Date date = new java.sql.Date(dateChooserAddDate.getTime());

            //добавление записи о выдаче телефона на замену
            if (pkReplace.size() > 0 && jComboBoxReplace.getSelectedIndex() != -1) {
                resSet = RepairMobile.st.executeQuery("INSERT INTO CLIENTMOBILE "
                        + "(PK_CLIENT, PK_KEYOFCHANGEMOBILE, DATEDILIVERY)"
                        + "VALUES ("
                        + "'" + PKClient + "', "
                        + "'" + pkReplace.get(jComboBoxReplace.getSelectedIndex()) + "', "
                        + " TO_DATE('" + date + "', 'YYYY-MM-DD')"
                        + ")"
                );
            }

            //добавление устройства
            resSet = RepairMobile.st.executeQuery("INSERT INTO DEVICE "
                    + "(PK_MODELDEVICE, PK_TYPEOFDEVICE,imei) "
                    + "VALUES ("
                    + "'" + pkModel.get(jComboBoxModel.getSelectedIndex()) + "', "
                    + " '" + pkTypeDevice.get(jComboBoxTypeDevice.getSelectedIndex()) + "',"
                    + " '" + jTextFieldIMEI.getText() + "'"
                    + ")"
            );

            resSet = RepairMobile.st.executeQuery("select SEQDEVICE.currval from dual");
            int currvalDevice = 0;
            if (resSet.next()) {
                currvalDevice = resSet.getInt(1);
            }

            resSet = RepairMobile.st.executeQuery("INSERT INTO MYORDER "
                    + "(NUMOFORDER, TIMETOACCEPT, TIMETODELIVERY, COSTOFORDER, TYPEOFORDER, PK_STATUS, PK_MANAGER,PK_device, PK_CLIENT) "
                    + "VALUES (SeqOrderNumber.nextval,"
                    + "TO_DATE('" + date + "', 'YYYY-MM-DD'),"
                    + "null,"
                    + " '" + countCost + "',"
                    + " '" + typeOfOrder + "',"
                    + " '10',"
                    + " '" + PK + "',"
                    + " '" + currvalDevice + "',"
                    + " '" + PKClient + "')"
            );
            resSet = RepairMobile.st.executeQuery("select SEQMYORDER.currval from dual");
            int pkOrder = 0;
            if (resSet.next()) {
                pkOrder = resSet.getInt(1);
            }

            for (int i = 0; i < jListSelCrash.getModel().getSize(); i++) {
                resSet = RepairMobile.st.executeQuery("INSERT INTO ordercrash "
                        + "(PK_crash, PK_order) "
                        + "VALUES ("
                        + "'" + jListSelCrash.getModel().getElementAt(i).getKey() + "', "
                        + " '" + pkOrder + "'"
                        + ")"
                );
            }

            //excell
            HSSFWorkbook wb = null;
            try {
                wb = new HSSFWorkbook(new FileInputStream("AktPriema(Itog).xls"));//for earlier version use HSSF
                HSSFSheet sheet = wb.getSheetAt(0);

                /* берём номер*/
                resSet = RepairMobile.st.executeQuery("select SEQMYORDER.currval from dual");
                int numOrder = 0;
                if (resSet.next()) {
                    numOrder = resSet.getInt(1);
                }

                sheet.getRow((short) 7).getCell((short) 19).setCellValue(String.valueOf(numOrder));           //Номер заказа
                sheet.getRow((short) 10).getCell((short) 5).setCellValue(String.valueOf(textFam));            //Фамилия    
                sheet.getRow((short) 10).getCell((short) 16).setCellValue(String.valueOf(textName));          //Имя
                sheet.getRow((short) 10).getCell((short) 21).setCellValue(String.valueOf(textOtch));          //Отчество
                sheet.getRow((short) 13).getCell((short) 3).setCellValue(String.valueOf(textAddress));           //Адрес
                sheet.getRow((short) 13).getCell((short) 20).setCellValue(String.valueOf(textTelefon));       //Номер телефона 
                sheet.getRow((short) 16).getCell((short) 5).setCellValue(String.valueOf(valueModel.get(jComboBoxModel.getSelectedIndex())));     //модель
                sheet.getRow((short) 17).getCell((short) 5).setCellValue(String.valueOf(jTextFieldIMEI.getText()));                               //imei
                String polomki = new String();
                for (int i = 0; i < jListSelCrash.getModel().getSize(); i++) {
                    polomki += jListSelCrash.getModel().getElementAt(i).getValue() + "\n ";

                }
                sheet.getRow((short) 20).getCell((short) 5).setCellValue(polomki);

                if (valueReplace.size() > 0 && jComboBoxReplace.getSelectedIndex() != -1) {
                    sheet.getRow((short) 25).getCell((short) 5).setCellValue(valueReplace.get(jComboBoxReplace.getSelectedIndex()));
                    sheet.getRow((short) 27).getCell((short) 5).setCellValue(imeiReplace.get(jComboBoxReplace.getSelectedIndex()));
                }

                /* телефон на замену*/
// /* */
                sheet.getRow((short) 43).getCell((short) 16).setCellValue(date.toString());        //дата передачи

            } catch (Exception e) {
                System.out.println("Ошибка при чтении шаблона:" + e.getMessage());
                return;
            }

            //выбор файла и сохранение
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                // save to file
                try {
                    //запись в файл
                    FileOutputStream fileOut = new FileOutputStream(file);
                    wb.write(fileOut);
                    fileOut.flush();
                    fileOut.close();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            JOptionPane.showMessageDialog(this, "Заказ успешно создан!");
            addTopData();
            resetElements();
            this.addDataInTable();
            isCreateNew = true;
            DefaultTableModel dtm = (DefaultTableModel) jTable4.getModel();
            dtm.getDataVector().removeAllElements();
        } catch (SQLException ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Ошибка: Невозможно выполнить операцию(возможно введены неверные данные)");
        }
    }

    //именить статус заказа на выдан и поставить сегодняшнюю дату
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (jTable1.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Выделите запись для выдачи");
        } else {
            Object PK = jTable1.getValueAt(jTable1.getSelectedRow(), 0);
            int primKey = Integer.parseInt(PK.toString());
            try {
                if (jTable1.getValueAt(jTable1.getSelectedRow(), 7).toString().equals("16")) {
                    JOptionPane.showMessageDialog(this, "Товар уже выдан!");
                    return;
                }
                RepairMobile.st.executeQuery("UPDATE myorder SET  PK_STATUS= " + 16 + " WHERE PK_ORDER=" + PK);
                Date datenow = new Date();
                java.sql.Date date = new java.sql.Date(datenow.getTime());
                RepairMobile.st.executeQuery("UPDATE myorder SET  myorder.TIMETODELIVERY= TO_DATE('" + date + "', 'YYYY-MM-DD') WHERE PK_ORDER=" + PK);
                RepairMobile.st.executeQuery("delete from clientmobile where pk_clientmobile=" + jTable1.getValueAt(jTable1.getSelectedRow(), 12));
                JOptionPane.showMessageDialog(this, "Запись успешно изменена");
                this.addDataInTable();
                this.addTopData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Ошибка: Невозможно изменить");
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (jTable2.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Выделите заказ для изменения!");
        } else {
            Object PK = jTable2.getValueAt(jTable2.getSelectedRow(), 0);
            int primKey = Integer.parseInt(PK.toString());
            OrderUpdate orderUpdate = new OrderUpdate(1, primKey);
            orderUpdate.setListenerCloseForm(new ListenerCloseForm(this));
            orderUpdate.setVisible(true);
            this.setEnabled(false);

        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextFieldAddOtchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldAddOtchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldAddOtchActionPerformed

    private void jButtonAddModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddModelActionPerformed
        ModelAddUpdate modelAddUpdate = new ModelAddUpdate(0, -1);
        modelAddUpdate.setListenerCloseForm(new ListenerCloseForm(this));
        modelAddUpdate.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_jButtonAddModelActionPerformed

    private void jButtonAddTypeOfCrashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddTypeOfCrashActionPerformed
        TypeCrashAddUpdate typeCrashAddUpdate = new TypeCrashAddUpdate(0, -1);
        typeCrashAddUpdate.setListenerCloseForm(new ListenerCloseForm(this));
        typeCrashAddUpdate.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_jButtonAddTypeOfCrashActionPerformed

    private void jButtonAddManufacturerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddManufacturerActionPerformed
        ManufacturerAddUpdate manufacturerAddUpdate = new ManufacturerAddUpdate(0, -1);
        manufacturerAddUpdate.setListenerCloseForm(new ListenerCloseForm(this));
        manufacturerAddUpdate.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_jButtonAddManufacturerActionPerformed

    private void jButtonChooseExistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChooseExistActionPerformed
        ChooseExistClient chooseExistClient = new ChooseExistClient(this);
        chooseExistClient.setListenerCloseForm(new ListenerCloseForm(this));
        chooseExistClient.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_jButtonChooseExistActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

//GEN-FIRST:event_jButtonAcceptActionPerformed
 
//GEN-LAST:event_jButtonAcceptActionPerformed

    private void jTextFieldAddFamFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldAddFamFocusLost
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextFieldAddFamFocusLost

    private void jButtonAddCrashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddCrashActionPerformed
        // TODO add your handling code here:
        if (jListAllCrash.getSelectedIndex() != -1) {
            modelSelCrash.addElement(modelAllCrash.elementAt(jListAllCrash.getSelectedIndex()));
            modelAllCrash.removeElementAt(jListAllCrash.getSelectedIndex());
        }
    }//GEN-LAST:event_jButtonAddCrashActionPerformed

    private void jButtonRetCrashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRetCrashActionPerformed
        // TODO add your handling code here:
        if (jListSelCrash.getSelectedIndex() != -1) {
            modelAllCrash.addElement(modelSelCrash.elementAt(jListSelCrash.getSelectedIndex()));
            modelSelCrash.removeElementAt(jListSelCrash.getSelectedIndex());
        }
    }//GEN-LAST:event_jButtonRetCrashActionPerformed

    private void jComboBoxTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTypeActionPerformed
        // TODO add your handling code here:
        if (!flagSelDetals) {
            if (jComboBoxType.getSelectedIndex() == 0) {
                jTextFieldCost.setText(String.valueOf(GARANT_COST));
            }
            if (jComboBoxType.getSelectedIndex() == 1) {
                jTextFieldCost.setText(String.valueOf(DIAGNOSTIC_COST));
            }
        }
    }//GEN-LAST:event_jComboBoxTypeActionPerformed

    private void filtredTypedDetails() {
        ResultSet resSet = null;
        if (jComboBoxTypeDevice.getSelectedIndex() != -1 && jComboBoxManufacturers.getSelectedIndex() != -1 && jComboBoxModel.getSelectedIndex() != -1) {
            try {
                resSet = RepairMobile.st.executeQuery("select concretedetail.PK_CONCRETEDETAIL,"
                        + " concretedetail.PK_detail,"
                        + " detail.NAMEOFdetail,"
                        + " concretedetail.costofdetail,"
                        + " concretedetail.PK_typeofdevice,"
                        + " typeofdevice.nameoftype,"
                        + " concretedetail.PK_modeldevice,"
                        + " modeldevice.nameofmodel,"
                        + " modeldevice.pk_manufacturer,"
                        + " manufacturer.nameofmanufacturer"
                        + " from concretedetail "
                        + " inner join detail on detail.PK_detail=concretedetail.PK_detail"
                        + " inner join typeofdevice on typeofdevice.PK_typeofdevice=concretedetail.PK_typeofdevice"
                        + " inner join modeldevice on modeldevice.PK_modeldevice=concretedetail.PK_modeldevice"
                        + " inner join manufacturer on manufacturer.PK_manufacturer=modeldevice.PK_manufacturer"
                        + " where concretedetail.pk_typeofdevice=" + pkTypeDevice.get(jComboBoxTypeDevice.getSelectedIndex())
                        + " and concretedetail.pk_modeldevice=" + pkModel.get(jComboBoxModel.getSelectedIndex())
                        + " and modeldevice.PK_manufacturer=" + pkProizv.get(jComboBoxManufacturers.getSelectedIndex())
                );
                jTable3.setModel(DbUtils.resultSetToTableModel(resSet));
            } catch (SQLException ex) {
                Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            if (jComboBoxManufacturers.getSelectedIndex() != -1 && jComboBoxModel.getSelectedIndex() != -1) {
                try {
                    resSet = RepairMobile.st.executeQuery("select concretedetail.PK_CONCRETEDETAIL,"
                            + " concretedetail.PK_detail,"
                            + " detail.NAMEOFdetail,"
                            + " concretedetail.costofdetail,"
                            + " concretedetail.PK_typeofdevice,"
                            + " typeofdevice.nameoftype,"
                            + " concretedetail.PK_modeldevice,"
                            + " modeldevice.nameofmodel,"
                            + " modeldevice.pk_manufacturer,"
                            + " manufacturer.nameofmanufacturer"
                            + " from concretedetail "
                            + " inner join detail on detail.PK_detail=concretedetail.PK_detail"
                            + " inner join typeofdevice on typeofdevice.PK_typeofdevice=concretedetail.PK_typeofdevice"
                            + " inner join modeldevice on modeldevice.PK_modeldevice=concretedetail.PK_modeldevice"
                            + " inner join manufacturer on manufacturer.PK_manufacturer=modeldevice.PK_manufacturer"
                            + " where concretedetail.pk_modeldevice=" + pkModel.get(jComboBoxModel.getSelectedIndex())
                            + " and modeldevice.PK_manufacturer=" + pkProizv.get(jComboBoxManufacturers.getSelectedIndex())
                    );
                    jTable3.setModel(DbUtils.resultSetToTableModel(resSet));
                } catch (SQLException ex) {
                    Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                if (jComboBoxTypeDevice.getSelectedIndex() != -1 && jComboBoxManufacturers.getSelectedIndex() != -1) {
                    try {
                        resSet = RepairMobile.st.executeQuery("select concretedetail.PK_CONCRETEDETAIL,"
                                + " concretedetail.PK_detail,"
                                + " detail.NAMEOFdetail,"
                                + " concretedetail.costofdetail,"
                                + " concretedetail.PK_typeofdevice,"
                                + " typeofdevice.nameoftype,"
                                + " concretedetail.PK_modeldevice,"
                                + " modeldevice.nameofmodel,"
                                + " modeldevice.pk_manufacturer,"
                                + " manufacturer.nameofmanufacturer"
                                + " from concretedetail "
                                + " inner join detail on detail.PK_detail=concretedetail.PK_detail"
                                + " inner join typeofdevice on typeofdevice.PK_typeofdevice=concretedetail.PK_typeofdevice"
                                + " inner join modeldevice on modeldevice.PK_modeldevice=concretedetail.PK_modeldevice"
                                + " inner join manufacturer on manufacturer.PK_manufacturer=modeldevice.PK_manufacturer"
                                + " where concretedetail.pk_typeofdevice=" + pkTypeDevice.get(jComboBoxTypeDevice.getSelectedIndex())
                                + " and modeldevice.PK_manufacturer=" + pkProizv.get(jComboBoxManufacturers.getSelectedIndex())
                        );
                        jTable3.setModel(DbUtils.resultSetToTableModel(resSet));
                    } catch (SQLException ex) {
                        Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    if (jComboBoxTypeDevice.getSelectedIndex() != -1) {
                        try {
                            resSet = RepairMobile.st.executeQuery("select concretedetail.PK_CONCRETEDETAIL,"
                                    + " concretedetail.PK_detail,"
                                    + " detail.NAMEOFdetail,"
                                    + " concretedetail.costofdetail,"
                                    + " concretedetail.PK_typeofdevice,"
                                    + " typeofdevice.nameoftype,"
                                    + " concretedetail.PK_modeldevice,"
                                    + " modeldevice.nameofmodel,"
                                    + " modeldevice.pk_manufacturer,"
                                    + " manufacturer.nameofmanufacturer"
                                    + " from concretedetail "
                                    + " inner join detail on detail.PK_detail=concretedetail.PK_detail"
                                    + " inner join typeofdevice on typeofdevice.PK_typeofdevice=concretedetail.PK_typeofdevice"
                                    + " inner join modeldevice on modeldevice.PK_modeldevice=concretedetail.PK_modeldevice"
                                    + " inner join manufacturer on manufacturer.PK_manufacturer=modeldevice.PK_manufacturer"
                                    + " where concretedetail.pk_typeofdevice=" + pkTypeDevice.get(jComboBoxTypeDevice.getSelectedIndex())
                            );
                            jTable3.setModel(DbUtils.resultSetToTableModel(resSet));
                        } catch (SQLException ex) {
                            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        if (jComboBoxManufacturers.getSelectedIndex() != -1) {
                            try {
                                resSet = RepairMobile.st.executeQuery("select concretedetail.PK_CONCRETEDETAIL,"
                                        + " concretedetail.PK_detail,"
                                        + " detail.NAMEOFdetail,"
                                        + " concretedetail.costofdetail,"
                                        + " concretedetail.PK_typeofdevice,"
                                        + " typeofdevice.nameoftype,"
                                        + " concretedetail.PK_modeldevice,"
                                        + " modeldevice.nameofmodel,"
                                        + " modeldevice.pk_manufacturer,"
                                        + " manufacturer.nameofmanufacturer"
                                        + " from concretedetail "
                                        + " inner join detail on detail.PK_detail=concretedetail.PK_detail"
                                        + " inner join typeofdevice on typeofdevice.PK_typeofdevice=concretedetail.PK_typeofdevice"
                                        + " inner join modeldevice on modeldevice.PK_modeldevice=concretedetail.PK_modeldevice"
                                        + " inner join manufacturer on manufacturer.PK_manufacturer=modeldevice.PK_manufacturer"
                                        + " where modeldevice.PK_manufacturer=" + pkProizv.get(jComboBoxManufacturers.getSelectedIndex())
                                );
                                jTable3.setModel(DbUtils.resultSetToTableModel(resSet));
                            } catch (SQLException ex) {
                                Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {

                        }
                    }
                }
            }
        }
        //типовые детали
        jTable3.getColumnModel().getColumn(2).setHeaderValue("Деталь");
        jTable3.getColumnModel().getColumn(3).setHeaderValue("Цена");
        jTable3.getColumnModel().getColumn(5).setHeaderValue("Тип устройства");
        jTable3.getColumnModel().getColumn(7).setHeaderValue("Модель");
        jTable3.getColumnModel().getColumn(9).setHeaderValue("Производитель");
        //jTable3.getColumnModel().getColumn(6).setHeaderValue("Наличие");

        //пк типовой детали
        jTable3.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable3.getColumnModel().getColumn(0).setMinWidth(0);
        jTable3.getColumnModel().getColumn(0).setPreferredWidth(0);

        jTable3.getColumnModel().getColumn(1).setMaxWidth(0);
        jTable3.getColumnModel().getColumn(1).setMinWidth(0);
        jTable3.getColumnModel().getColumn(1).setPreferredWidth(0);

        jTable3.getColumnModel().getColumn(4).setMaxWidth(0);
        jTable3.getColumnModel().getColumn(4).setMinWidth(0);
        jTable3.getColumnModel().getColumn(4).setPreferredWidth(0);

        jTable3.getColumnModel().getColumn(6).setMaxWidth(0);
        jTable3.getColumnModel().getColumn(6).setMinWidth(0);
        jTable3.getColumnModel().getColumn(6).setPreferredWidth(0);

        jTable3.getColumnModel().getColumn(8).setMaxWidth(0);
        jTable3.getColumnModel().getColumn(8).setMinWidth(0);
        jTable3.getColumnModel().getColumn(8).setPreferredWidth(0);
    }
    private void jComboBoxTypeDeviceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTypeDeviceActionPerformed
        // TODO add your handling code here:
        filtredTypedDetails();
    }//GEN-LAST:event_jComboBoxTypeDeviceActionPerformed

    private void jComboBoxManufacturersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxManufacturersActionPerformed
        if (jComboBoxManufacturers.getSelectedIndex() != -1) {
            jComboBoxModel.setEnabled(true);
            ResultSet resSetModel = null;
            pkModel = new ArrayList<String>();
            valueModel = new ArrayList<String>();
            try {
                // TODO add your handling code here:
                resSetModel = RepairMobile.st.executeQuery("select pk_modeldevice,nameofmodel"
                        + " from modeldevice where pk_manufacturer =" + pkProizv.get(jComboBoxManufacturers.getSelectedIndex()));
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
        filtredTypedDetails();
    }//GEN-LAST:event_jComboBoxManufacturersActionPerformed

    private void jComboBoxModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxModelActionPerformed
        // TODO add your handling code here:
        filtredTypedDetails();
    }//GEN-LAST:event_jComboBoxModelActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        if (jTable3.getSelectedRow() != -1) {
            countCost += Integer.parseInt(jTable3.getValueAt(jTable3.getSelectedRow(), 3).toString());
            jTextFieldCost.setText(String.valueOf(countCost));
            JOptionPane.showMessageDialog(this, "Добавлено к стоимости");
            DefaultTableModel dtm = (DefaultTableModel) jTable4.getModel();
            dtm.addRow(new Object[]{jTable3.getValueAt(jTable3.getSelectedRow(), 2).toString(), jTable3.getValueAt(jTable3.getSelectedRow(), 3).toString()});
        } else {
            JOptionPane.showMessageDialog(this, "Выделите запись");
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButtonDeleteOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteOrderActionPerformed
        // TODO add your handling code here:
        if (jTable2.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Выделите строку для удаления");
        } else {
            try {
                Object PK = jTable2.getValueAt(jTable2.getSelectedRow(), 0);
                int primKey = Integer.parseInt(PK.toString());

                int option = JOptionPane.showConfirmDialog(this, "Вы уверены что хотите удалить запись",
                        "Удаление записи", JOptionPane.YES_NO_CANCEL_OPTION);
                if (option == 0) {
                    RepairMobile.st.executeQuery("delete from clientmobile where PK_client="
                            +jTable2.getValueAt(jTable2.getSelectedRow(), 9));
                    RepairMobile.st.executeQuery("delete from myorder where PK_order=" + PK);                  
                    addDataInTable();
                }
                
                addTopData();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Удаление невозможно");
                Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButtonDeleteOrderActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButtonAccept;
    private javax.swing.JButton jButtonAddCrash;
    private javax.swing.JButton jButtonAddManufacturer;
    private javax.swing.JButton jButtonAddModel;
    private javax.swing.JButton jButtonAddTypeOfCrash;
    private javax.swing.JButton jButtonChooseExist;
    private javax.swing.JButton jButtonDeleteOrder;
    private javax.swing.JButton jButtonRetCrash;
    private javax.swing.JComboBox<String> jComboBoxManufacturers;
    private javax.swing.JComboBox<String> jComboBoxModel;
    private javax.swing.JComboBox<String> jComboBoxReplace;
    private javax.swing.JComboBox jComboBoxType;
    private javax.swing.JComboBox<String> jComboBoxTypeDevice;
    private com.toedter.calendar.JDateChooser jDateChooserAddDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelFIO;
    private javax.swing.JList<MyMap> jListAllCrash;
    private javax.swing.JList<MyMap> jListSelCrash;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuClose;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemManufacturer;
    private javax.swing.JMenuItem jMenuItemStatus;
    private javax.swing.JMenuItem jMenuItemTypeCrash;
    private javax.swing.JMenuItem jMenuItemTypeDevice;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextField jTextFieldAddFam;
    private javax.swing.JTextField jTextFieldAddName;
    private javax.swing.JTextField jTextFieldAddOtch;
    private javax.swing.JTextField jTextFieldAddTelefon;
    private javax.swing.JTextField jTextFieldAddress;
    private javax.swing.JTextField jTextFieldCost;
    private javax.swing.JTextField jTextFieldIMEI;
    private java.awt.Menu menu1;
    private java.awt.Menu menu2;
    private java.awt.MenuBar menuBar1;
    // End of variables declaration//GEN-END:variables

}
