/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.rem.otdel;

import com.placeholder.PlaceHolder;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
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
import javax.swing.filechooser.FileNameExtensionFilter;
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
import spravochn.status.Status;
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
        this.setEnabled(true);
        try {

            jTextFieldCost.setText("");
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

    //Обновление информации о устройстве
    public void addDeviceData() {
        this.setEnabled(true);
        try {
            ResultSet resSetProizv = null;
            pkProizv = new ArrayList<String>();
            valueProizv = new ArrayList<String>();

            ResultSet resTypeDevice = null;
            pkTypeDevice = new ArrayList<String>();
            valueTypeDevice = new ArrayList<String>();

            //производитель
            resSetProizv = RepairMobile.st.executeQuery("select * from manufacturer");
            TableModel tableModel = DbUtils.resultSetToTableModel(resSetProizv);
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

            jComboBoxModel.setSelectedIndex(-1);
            jComboBoxTypeDevice.setSelectedIndex(-1);
            jComboBoxManufacturers.setSelectedIndex(-1);
            jTextFieldIMEI.setText("");
            holder = new PlaceHolder(jTextFieldIMEI, "IMEI");

        } catch (SQLException ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Обновление информации о поломках
    public void addCrashs() {
        this.setEnabled(true);
        try {
            ResultSet resSetCrash = null;
            pkCrash = new ArrayList<String>();
            valueCrash = new ArrayList<String>();
            //был replace
            //jComboBoxModel.setEnabled(false);
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

            //
        } catch (SQLException ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Orders(int PK) {
        initComponents();

        this.PK = PK;
        flagSelDetals = false;
        modelAllCrash = new DefaultListModel<MyMap>();
        modelSelCrash = new DefaultListModel<MyMap>();
        jTable3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //
        jTextFieldNomerZakaza.setText("");
        holder = new PlaceHolder(jTextFieldNomerZakaza, "Номер заказа");
        jTableNomerZakaz.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //

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
        holder = new PlaceHolder(jTextFieldNomerZakaza, "Номер заказа");
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

        //
        addTopData();
        //
        addDeviceData();
        //
        //
        addCrashs();
        //

        try {

            ImageIcon icon = new ImageIcon(getClass().getResource("/img/mobile.png"));

            Image img = icon.getImage();
            Image newimg = img.getScaledInstance(30, 25, java.awt.Image.SCALE_SMOOTH);
            jButton1.setIcon(new ImageIcon(newimg));
        } catch (Exception ex) {
            System.out.println(ex);
        }
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

    @Override
    public void addDataInTable() {
        this.setEnabled(true);
        ResultSet resSet = null;
        ResultSet resSet2 = null;
        ResultSet resSet3 = null;

        //jComboBoxModel.setModel(new DefaultComboBoxModel<String>());
        holder = new PlaceHolder(jTextFieldAddFam, "Фамилия");
        holder = new PlaceHolder(jTextFieldAddName, "Имя");
        holder = new PlaceHolder(jTextFieldAddOtch, "Отчество");
        holder = new PlaceHolder(jTextFieldAddTelefon, "Телефон");
        holder = new PlaceHolder(jTextFieldAddress, "Адрес");

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
            //все заказы
            resSet2 = RepairMobile.st.executeQuery("select myorder.PK_ORDER,myorder.NUMOFORDER,"
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
                    + " ");
            jTable2.setModel(DbUtils.resultSetToTableModel(resSet2));

            DefaultTableModel dtm2 = (DefaultTableModel) jTable2.getModel();
            dtm2.addColumn("На замене");
            for (int i = 0; i < jTable2.getRowCount(); i++) {
                resSet = RepairMobile.st.executeQuery("select clientmobile.PK_clientmobile,clientmobile.pk_client,"
                        + " clientmobile.pk_keyofchangemobile,"
                        + " replacemobile.model, replacemobile.imeinumber from clientmobile"
                        + " inner join replacemobile on  clientmobile.pk_keyofchangemobile=replacemobile.pk_keyofchangemobile"
                        + " where pk_client=" + jTable2.getValueAt(i, 9).toString()
                );

                if (resSet.next()) {
                    jTable2.setValueAt(resSet.getString(4) + " " + resSet.getString(5), i, 12);
                } else {
                    jTable2.setValueAt("", i, 12);
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
        zakazTables(jTable2);
        //

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

        correctSizeTable(jTable2);
        correctSizeTable(jTable1);

        try {
            resSet = RepairMobile.st.executeQuery("select PK_CLIENT,FAMOFCLIENT,"
                    + "NAMEOFCLIENT,OTCOFCLIENT,NUMBEROFPHONE,address from client");

        } catch (SQLException ex) {
            Logger.getLogger(ChooseExistClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTableClients.setModel(DbUtils.resultSetToTableModel(resSet));
        jTableClients.getColumnModel().getColumn(0).setMaxWidth(0);
        jTableClients.getColumnModel().getColumn(0).setMinWidth(0);
        jTableClients.getColumnModel().getColumn(0).setPreferredWidth(0);

        jTableClients.getColumnModel().getColumn(1).setHeaderValue("Фамилия");
        jTableClients.getColumnModel().getColumn(2).setHeaderValue("Имя");
        jTableClients.getColumnModel().getColumn(3).setHeaderValue("Отчество");
        jTableClients.getColumnModel().getColumn(4).setHeaderValue("Телефон");
        jTableClients.getColumnModel().getColumn(5).setHeaderValue("Адрес");

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
        jComboBoxManufacturers = new javax.swing.JComboBox<String>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jComboBoxTypeDevice = new javax.swing.JComboBox<String>();
        jButtonAddModel = new javax.swing.JButton();
        jButtonAddManufacturer = new javax.swing.JButton();
        jComboBoxModel = new javax.swing.JComboBox<String>();
        jTextFieldIMEI = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jComboBoxType = new javax.swing.JComboBox();
        jDateChooserAddDate = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldCost = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxReplace = new javax.swing.JComboBox<String>();
        jButtonAccept = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jListAllCrash = new javax.swing.JList<MyMap>();
        jButtonAddCrash = new javax.swing.JButton();
        jButtonRetCrash = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jListSelCrash = new javax.swing.JList<MyMap>();
        jButtonAddTypeOfCrash = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable( )         {             @Override             public boolean isCellEditable(int row, int column)             {                 return false;             }         };
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
        jButtonAktRabot = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTableNomerRem = new javax.swing.JTable( )         {             @Override             public boolean isCellEditable(int row, int column)             {                 return false;             }         };
        jPanel12 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableNomerZakaz = new javax.swing.JTable( )
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        jButtonNomerShow = new javax.swing.JButton();
        jTextFieldNomerZakaza = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTableClients = new javax.swing.JTable();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTableClientsOrder = new javax.swing.JTable();
        jButtonViewClientOrders = new javax.swing.JButton();
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
        jMenuItemModel = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItemPhones = new javax.swing.JMenuItem();

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

        jTextFieldAddTelefon.setDocument((new JTextFieldLimit(12)));

        jButtonChooseExist.setText("Cуществующие клиенты");
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
                    .addComponent(jButtonChooseExist, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                    .addComponent(jTextFieldAddress, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldAddTelefon, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldAddOtch, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldAddName)
                    .addComponent(jTextFieldAddFam, javax.swing.GroupLayout.Alignment.TRAILING))
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
                            .addComponent(jTextFieldIMEI)
                            .addComponent(jComboBoxModel, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBoxManufacturers, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBoxTypeDevice, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Дата оформления");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Тип заказа");

        jComboBoxType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTypeActionPerformed(evt);
            }
        });

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Стоимость");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Телефон на замену");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDateChooserAddDate, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(57, 57, 57)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxType, 0, 164, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(78, 78, 78)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxReplace, 0, 201, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldCost, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(42, 42, 42))
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(jButtonAddCrash)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonRetCrash)
                .addGap(57, 57, 57))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
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
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonAccept, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
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
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1013, Short.MAX_VALUE)
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
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
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
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1013, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jButtonDeleteOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonDeleteOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        jButtonAktRabot.setText("Оформить акт работ");
        jButtonAktRabot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAktRabotActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1013, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonAktRabot, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonAktRabot, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Выдача", jPanel2);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Ремонты устройства"));

        jTableNomerRem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Тип", "Производитель", "Модель", "Начало ремонта", "Конец ремонта", "Тип поломки", "Статус"
            }
        ));
        jScrollPane8.setViewportView(jTableNomerRem);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Заказ по номеру"));

        jTableNomerZakaz.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane7.setViewportView(jTableNomerZakaz);
        if (jTableNomerZakaz.getColumnModel().getColumnCount() > 0) {
            jTableNomerZakaz.getColumnModel().getColumn(0).setPreferredWidth(10);
            jTableNomerZakaz.getColumnModel().getColumn(2).setPreferredWidth(130);
            jTableNomerZakaz.getColumnModel().getColumn(3).setPreferredWidth(50);
            jTableNomerZakaz.getColumnModel().getColumn(4).setPreferredWidth(25);
            jTableNomerZakaz.getColumnModel().getColumn(5).setPreferredWidth(50);
            jTableNomerZakaz.getColumnModel().getColumn(6).setPreferredWidth(60);
        }

        jButtonNomerShow.setText("Просмотр");
        jButtonNomerShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNomerShowActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 981, Short.MAX_VALUE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jTextFieldNomerZakaza, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonNomerShow, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonNomerShow, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jTextFieldNomerZakaza)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Статус ремонта устройства", jPanel10);

        jTableClients.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane9.setViewportView(jTableClients);

        jTableClientsOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Номер заказа", "Дата создания", "Дата завершения", "Стоимость", "Тип", "Статус", "Клиент", "Устройство"
            }
        ));
        jScrollPane10.setViewportView(jTableClientsOrder);

        jButtonViewClientOrders.setText("Просмотр");
        jButtonViewClientOrders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewClientOrdersActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 869, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jButtonViewClientOrders)
                        .addGap(0, 10, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(jButtonViewClientOrders)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 140, Short.MAX_VALUE)))
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("История клиентов", jPanel13);

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

        jMenuItemModel.setText("Модели");
        jMenuItemModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemModelActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemModel);

        jMenuItem2.setText("Клиенты");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuItemPhones.setText("Телефоны на замену");
        jMenuItemPhones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPhonesActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemPhones);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelFIO, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelFIO, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 569, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemManufacturerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemManufacturerActionPerformed
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setVisible(true);
    }//GEN-LAST:event_jMenuItemManufacturerActionPerformed

    private void jMenuItemStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemStatusActionPerformed
        Status status = new Status();
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
            jTextFieldAddFam.setForeground(Color.BLACK);
            jTextFieldAddName.setText(resSet.getString(2));
            jTextFieldAddName.setForeground(Color.BLACK);
            jTextFieldAddOtch.setText(resSet.getString(4));
            jTextFieldAddOtch.setForeground(Color.BLACK);
            jTextFieldAddTelefon.setText(resSet.getString(5));
            jTextFieldAddTelefon.setForeground(Color.BLACK);
            jTextFieldAddress.setText(resSet.getString(6));
            jTextFieldAddress.setForeground(Color.BLACK);
            PKClient = Integer.parseInt(resSet.getString(1));
            isCreateNew = false;

        } catch (SQLException ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

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
    private void jMenuItemModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemModelActionPerformed
        // TODO add your handling code here:
        Model model = new Model();
        model.setVisible(true);
    }//GEN-LAST:event_jMenuItemModelActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        ChooseExistClient chooseExistClient = new ChooseExistClient();
        chooseExistClient.setListenerCloseForm(new ListenerCloseForm(this));
        chooseExistClient.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

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
                RepairMobile.st.executeQuery("delete from clientmobile where pk_clientmobile=" + jTable1.getValueAt(jTable1.getSelectedRow(), 9));
                JOptionPane.showMessageDialog(this, "Товар успешно выдан");
                this.addDataInTable();
                this.addTopData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Ошибка: Невозможно изменить");
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
                            + jTable2.getValueAt(jTable2.getSelectedRow(), 9));
                    ResultSet res = RepairMobile.st.executeQuery("select pk_device from myorder where PK_order="
                            + jTable2.getValueAt(jTable2.getSelectedRow(), 0));
                    String tmpPK = "";
                    if (res.next()) {
                        tmpPK = res.getString(1);

                    }
                    RepairMobile.st.executeQuery("delete from myorder where PK_order=" + PK);
                    RepairMobile.st.executeQuery("delete from device where PK_device="
                            + tmpPK);
                    addDataInTable();
                }

                addTopData();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Удаление невозможно");
                Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButtonDeleteOrderActionPerformed

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

    private void jButtonAddTypeOfCrashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddTypeOfCrashActionPerformed
        TypeCrashAddUpdate typeCrashAddUpdate = new TypeCrashAddUpdate(0, -1);
        typeCrashAddUpdate.setListenerCloseForm(new ListenerCloseForm(this));
        typeCrashAddUpdate.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_jButtonAddTypeOfCrashActionPerformed

    private void jButtonRetCrashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRetCrashActionPerformed
        // TODO add your handling code here:
        if (jListSelCrash.getSelectedIndex() != -1) {
            modelAllCrash.addElement(modelSelCrash.elementAt(jListSelCrash.getSelectedIndex()));
            modelSelCrash.removeElementAt(jListSelCrash.getSelectedIndex());
        }
    }//GEN-LAST:event_jButtonRetCrashActionPerformed

    private void jButtonAddCrashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddCrashActionPerformed
        // TODO add your handling code here:
        if (jListAllCrash.getSelectedIndex() != -1) {
            modelSelCrash.addElement(modelAllCrash.elementAt(jListAllCrash.getSelectedIndex()));
            modelAllCrash.removeElementAt(jListAllCrash.getSelectedIndex());
        }
    }//GEN-LAST:event_jButtonAddCrashActionPerformed


    private void jButtonAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAcceptActionPerformed
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
            int numOrder = 0;
            HSSFWorkbook wb = null;
            try {
                wb = new HSSFWorkbook(new FileInputStream("AktPriema(Itog).xls"));//for earlier version use HSSF
                HSSFSheet sheet = wb.getSheetAt(0);

                /* берём номер*/
                resSet = RepairMobile.st.executeQuery("select SEQMYORDER.currval from dual");

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
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Excell", "xls");
            fileChooser.setSelectedFile(new File(numOrder + ".xls"));
            fileChooser.setFileFilter(filter);
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
            this.addDeviceData();
            this.addCrashs();
            isCreateNew = true;
            DefaultTableModel dtm = (DefaultTableModel) jTable4.getModel();
            dtm.getDataVector().removeAllElements();
        } catch (SQLException ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Ошибка: Невозможно выполнить операцию(возможно введены неверные данные)");
        }
    }//GEN-LAST:event_jButtonAcceptActionPerformed

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

    private void jComboBoxModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxModelActionPerformed
        // TODO add your handling code here:
        filtredTypedDetails();
    }//GEN-LAST:event_jComboBoxModelActionPerformed

    private void jButtonAddManufacturerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddManufacturerActionPerformed
        ManufacturerAddUpdate manufacturerAddUpdate = new ManufacturerAddUpdate(0, -1);
        manufacturerAddUpdate.setListenerCloseForm(new ListenerCloseForm(this));
        manufacturerAddUpdate.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_jButtonAddManufacturerActionPerformed

    private void jButtonAddModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddModelActionPerformed
        ModelAddUpdate modelAddUpdate = new ModelAddUpdate(0, -1);
        modelAddUpdate.setListenerCloseForm(new ListenerCloseForm(this));
        modelAddUpdate.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_jButtonAddModelActionPerformed

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

    private void jButtonChooseExistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChooseExistActionPerformed
        ChooseExistClient chooseExistClient = new ChooseExistClient(this);
        chooseExistClient.setListenerCloseForm(new ListenerCloseForm(this));
        chooseExistClient.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_jButtonChooseExistActionPerformed

    private void jTextFieldAddOtchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldAddOtchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldAddOtchActionPerformed

    private void jTextFieldAddFamFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldAddFamFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldAddFamFocusLost

    private void jButtonNomerShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNomerShowActionPerformed
        int nomerZakaza;
        try {
            nomerZakaza = Integer.parseInt(jTextFieldNomerZakaza.getText());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка: неверный ввод номера заказа!");
            return;
        }
        //извлекаем заказ по указанному номеру
        ResultSet resSet;
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
                    + " where myorder.NUMOFORDER=" + nomerZakaza);

            jTableNomerZakaz.setModel(DbUtils.resultSetToTableModel(resSet));

            DefaultTableModel dtm2 = (DefaultTableModel) jTableNomerZakaz.getModel();
            dtm2.addColumn("На замене");
            for (int i = 0; i < jTableNomerZakaz.getRowCount(); i++) {
                resSet = RepairMobile.st.executeQuery("select clientmobile.PK_clientmobile,clientmobile.pk_client,"
                        + " clientmobile.pk_keyofchangemobile,"
                        + " replacemobile.model, replacemobile.imeinumber from clientmobile"
                        + " inner join replacemobile on  clientmobile.pk_keyofchangemobile=replacemobile.pk_keyofchangemobile"
                        + " where pk_client=" + jTableNomerZakaz.getValueAt(i, 9).toString()
                );

                if (resSet.next()) {
                    jTableNomerZakaz.setValueAt(resSet.getString(4) + " " + resSet.getString(5), i, 12);
                } else {
                    jTableNomerZakaz.setValueAt("", i, 12);
                }
            }
            zakazTables(jTableNomerZakaz);
        } catch (SQLException ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Ошибка: Заказа с таким номерром не существует или он был удалён!");
            return;
        }

        //извлекаем ремонты по устройству данного заказа
        resSet = null;
        try {
            resSet = RepairMobile.st.executeQuery("select repair.PK_repair,"
                    + " myorder.numoforder,"
                    + " manufacturer.nameofmanufacturer|| ' ' ||modeldevice.nameofmodel,"
                    + " TO_CHAR(repair.startdate, 'DD.MM.YYYY'),"
                    + " TO_CHAR(repair.enddate, 'DD.MM.YYYY'),"
                    + " typeofcrash.nameofcrash ,"
                    + " repair.repairstatus "
                    + " from repair"
                    + " inner join engineer on repair.PK_engineer=engineer.PK_engineer"
                    + " inner join myorder on myorder.PK_order=repair.PK_order"
                    + " inner join device on myorder.PK_device=device.PK_device"
                    + " inner join modeldevice on modeldevice.PK_modeldevice=device.PK_modeldevice"
                    + " inner join manufacturer on modeldevice.PK_manufacturer=manufacturer.PK_manufacturer"
                    + " inner join typeofcrash on typeofcrash.PK_crash=repair.PK_crash"
                    + " where myorder.numoforder=" + nomerZakaza);

            jTableNomerRem.setModel(DbUtils.resultSetToTableModel(resSet));

            jTableNomerRem.getColumnModel().getColumn(0).setMaxWidth(0);
            jTableNomerRem.getColumnModel().getColumn(0).setMinWidth(0);
            jTableNomerRem.getColumnModel().getColumn(0).setPreferredWidth(0);
            jTableNomerRem.getColumnModel().getColumn(1).setHeaderValue("№ заказа");
            jTableNomerRem.getColumnModel().getColumn(2).setHeaderValue("Устройство");
            jTableNomerRem.getColumnModel().getColumn(3).setHeaderValue("Начало ремонта");
            jTableNomerRem.getColumnModel().getColumn(4).setHeaderValue("Конец ремонта");
            jTableNomerRem.getColumnModel().getColumn(5).setHeaderValue("Поломка");
            jTableNomerRem.getColumnModel().getColumn(6).setHeaderValue("Статус");

            for (int i = 0; i < jTableNomerRem.getRowCount(); i++) {
                if (jTableNomerRem.getValueAt(i, 6).toString().equals("0")) {
                    jTableNomerRem.setValueAt("Ремонтируется", i, 6);
                } else {
                    if (jTableNomerRem.getValueAt(i, 6).toString().equals("1")) {
                        jTableNomerRem.setValueAt("Выполнено", i, 6);
                    } else {
                        jTableNomerRem.setValueAt("Неизвестно", i, 6);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Ошибка: Невозможно извлечь ремонты по данному заказу или ремонт ещё не начался.");
            return;
        }


    }//GEN-LAST:event_jButtonNomerShowActionPerformed

    private void jButtonAktRabotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAktRabotActionPerformed
        if (jTable1.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Выделите запись для оформления акта работ.");
        } else {
            Object PK = jTable1.getValueAt(jTable1.getSelectedRow(), 0);
            int primKey = Integer.parseInt(PK.toString());
            try {
                //извлекаем данные о номере заказа и кленте
                ResultSet resSet = RepairMobile.st.executeQuery("select myorder.PK_ORDER,myorder.NUMOFORDER,"
                        + " TO_CHAR(myorder.TIMETOACCEPT, 'DD.MM.YYYY'),"
                        + " TO_CHAR(myorder.TIMETODELIVERY, 'DD.MM.YYYY'),"
                        + " myorder.COSTOFORDER,myorder.TYPEOFORDER,myorder.PK_MANAGER,"
                        + " myorder.PK_STATUS,"
                        + " status.NAMEOFSTATUS,"
                        + " myorder.PK_CLIENT,"
                        + " client.FAMOFCLIENT,"
                        + " client.NAMEOFCLIENT,"
                        + " client.OTCOFCLIENT, "
                        + " client.Address, "
                        + " client.NumberOfPhone "
                        + " from myorder "
                        + " inner join status on status.PK_status=myorder.PK_status"
                        + " inner join manager on manager.PK_manager=myorder.PK_manager"
                        + " inner join client on client.PK_client=myorder.PK_client"
                        + " where myorder.PK_ORDER=" + primKey);
                //jTable1.setModel(DbUtils.resultSetToTableModel(resSet));

                int numOrder = 0;
                String textFam = "";
                String textName = "";
                String textOtch = "";
                String textTelefon = "";
                String textAddress = "";
                if (resSet.next()) {
                    numOrder = resSet.getInt(2);
                    textFam = resSet.getString(11);
                    textName = resSet.getString(12);
                    textOtch = resSet.getString(13);
                    textAddress = resSet.getString(14);
                    textTelefon = resSet.getString(15);
                } else {
                    JOptionPane.showMessageDialog(this, "Ошибка: Невозможно извлечь заказ!");
                }

                //извлекаем ремонты по устройству данного заказа
                resSet = null;
                resSet = RepairMobile.st.executeQuery("select repair.PK_repair,"
                        + " myorder.numoforder,"
                        + " manufacturer.nameofmanufacturer|| ' ' ||modeldevice.nameofmodel,"
                        + " TO_CHAR(repair.startdate, 'DD.MM.YYYY'),"
                        + " TO_CHAR(repair.enddate, 'DD.MM.YYYY'),"
                        + " typeofcrash.nameofcrash ,"
                        + " repair.repairstatus ,"
                        + " repair.cost "
                        + " from repair"
                        + " inner join engineer on repair.PK_engineer=engineer.PK_engineer"
                        + " inner join myorder on myorder.PK_order=repair.PK_order"
                        + " inner join device on myorder.PK_device=device.PK_device"
                        + " inner join modeldevice on modeldevice.PK_modeldevice=device.PK_modeldevice"
                        + " inner join manufacturer on modeldevice.PK_manufacturer=manufacturer.PK_manufacturer"
                        + " inner join typeofcrash on typeofcrash.PK_crash=repair.PK_crash"
                        + " where myorder.PK_order=" + primKey);
                TableModel tableModel = DbUtils.resultSetToTableModel(resSet);
                if (tableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "Ошибка: Ремонтов по данному заказу не найдено");
                    return;
                }

                //проверка: Выполнены ли все ремонты?
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    if (tableModel.getValueAt(i, 6).toString().equals("0")) {
                        JOptionPane.showMessageDialog(this, "Ошибка: Один или несколько ремонтов ещё не завершены!");
                        return;
                    }
                }

                //excell
                HSSFWorkbook wb = null;
                try {
                    wb = new HSSFWorkbook(new FileInputStream("AktRabot(Itog).xls"));//for earlier version use HSSF
                    HSSFSheet sheet = wb.getSheetAt(0);

                    sheet.getRow((short) 8).getCell((short) 15).setCellValue(String.valueOf(numOrder));             //Номер заказа
                    sheet.getRow((short) 11).getCell((short) 5).setCellValue(String.valueOf(textFam));              //Фамилия    
                    sheet.getRow((short) 11).getCell((short) 16).setCellValue(String.valueOf(textName));            //Имя
                    sheet.getRow((short) 11).getCell((short) 26).setCellValue(String.valueOf(textOtch));            //Отчество
                    sheet.getRow((short) 14).getCell((short) 3).setCellValue(String.valueOf(textAddress));          //Адрес
                    sheet.getRow((short) 14).getCell((short) 20).setCellValue(String.valueOf(textTelefon));         //Номер телефона 
                    int cost = 0;

                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        cost += Integer.valueOf(tableModel.getValueAt(i, 7).toString());
                        sheet.getRow((short) 17 + i * 2).getCell((short) 1).setCellValue(String.valueOf(i + 1));          //Номер
                        sheet.getRow((short) 17 + i * 2).getCell((short) 3).setCellValue(tableModel.getValueAt(i, 5).toString());          //Название
                        sheet.getRow((short) 17 + i * 2).getCell((short) 26).setCellValue(Integer.valueOf(tableModel.getValueAt(i, 7).toString()));         //Цена                  
                    }

                    //обновим цену заказа
                    RepairMobile.st.executeQuery("UPDATE myorder SET myorder.COSTOFORDER=" + cost + " WHERE PK_ORDER=" + primKey);
                    this.addDataInTable();
                } catch (Exception e) {
                    System.out.println("Ошибка при чтении шаблона:" + e.getMessage());
                    return;
                }

                //выбор файла и сохранение
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Excell", "xls");
                fileChooser.setSelectedFile(new File("AktRabot №" + numOrder + ".xls"));
                fileChooser.setFileFilter(filter);
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

                //
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Ошибка: Невозможно составить акт работ");
            }
        }
    }//GEN-LAST:event_jButtonAktRabotActionPerformed

    private void jMenuItemPhonesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPhonesActionPerformed
        PhoneNaZamenu phoneNaZamenu = new PhoneNaZamenu();
        phoneNaZamenu.setListenerCloseForm(new ListenerCloseForm(this));
        phoneNaZamenu.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_jMenuItemPhonesActionPerformed

    private void jButtonViewClientOrdersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewClientOrdersActionPerformed
        // TODO add your handling code here:
        if (jTableClients.getSelectedRow() != -1) {
            ResultSet resSet = null;
            try {
                ResultSet resSet2 = RepairMobile.st.executeQuery("select myorder.PK_ORDER,myorder.NUMOFORDER,"
                        + "TO_CHAR(myorder.TIMETOACCEPT, 'DD.MM.YYYY'),"
                        + "TO_CHAR(myorder.TIMETODELIVERY, 'DD.MM.YYYY'),"
                        + "myorder.COSTOFORDER,myorder.TYPEOFORDER,myorder.PK_MANAGER"
                        + ",myorder.PK_STATUS,"
                        + " status.NAMEOFSTATUS,"
                        + " myorder.PK_CLIENT,"
                        + " client.FAMOFCLIENT || ' ' || client.NAMEOFCLIENT  || ' ' || client.OTCOFCLIENT,"
                        + " modeldevice.nameofmodel "
                        + " from myorder "
                        + " inner join status on status.PK_status=myorder.PK_status"
                        + " inner join manager on manager.PK_manager=myorder.PK_manager"
                        + " inner join client on client.PK_client=myorder.PK_client"
                        + " inner join device on device.PK_device=myorder.PK_device"
                        + " inner join modeldevice on modeldevice.PK_modeldevice=device.PK_modeldevice"
                        + " where client.pk_client=" + jTableClients.getValueAt(jTableClients.getSelectedRow(), 0));
                jTableClientsOrder.setModel(DbUtils.resultSetToTableModel(resSet2));

                DefaultTableModel dtm2 = (DefaultTableModel) jTableClientsOrder.getModel();
                //dtm2.addColumn("На замене");
                /*for (int i = 0; i < jTableClientsOrder.getRowCount(); i++) {
                    resSet = RepairMobile.st.executeQuery("select clientmobile.PK_clientmobile,clientmobile.pk_client,"
                            + " clientmobile.pk_keyofchangemobile,"
                            + " replacemobile.model, replacemobile.imeinumber from clientmobile"
                            + " inner join replacemobile on  clientmobile.pk_keyofchangemobile=replacemobile.pk_keyofchangemobile"
                            + " where pk_client=" + jTableClientsOrder.getValueAt(i, 9).toString()
                    );

                    
                }*/
                jTableClientsOrder.getColumnModel().getColumn(1).setHeaderValue("Номер");
                jTableClientsOrder.getColumnModel().getColumn(2).setHeaderValue("Дата создания");
                jTableClientsOrder.getColumnModel().getColumn(3).setHeaderValue("Дата завершения");
                jTableClientsOrder.getColumnModel().getColumn(4).setHeaderValue("Стоимость");
                jTableClientsOrder.getColumnModel().getColumn(5).setHeaderValue("Тип");
                jTableClientsOrder.getColumnModel().getColumn(8).setHeaderValue("Статус");
                jTableClientsOrder.getColumnModel().getColumn(10).setHeaderValue("Клиент");
                jTableClientsOrder.getColumnModel().getColumn(11).setHeaderValue("Устройство");

                //пк заказа
                jTableClientsOrder.getColumnModel().getColumn(0).setMaxWidth(0);
                jTableClientsOrder.getColumnModel().getColumn(0).setMinWidth(0);
                jTableClientsOrder.getColumnModel().getColumn(0).setPreferredWidth(0);
                //пк менеджера
                jTableClientsOrder.getColumnModel().getColumn(6).setMaxWidth(0);
                jTableClientsOrder.getColumnModel().getColumn(6).setMinWidth(0);
                jTableClientsOrder.getColumnModel().getColumn(6).setPreferredWidth(0);
                //пк статуса
                jTableClientsOrder.getColumnModel().getColumn(7).setMaxWidth(0);
                jTableClientsOrder.getColumnModel().getColumn(7).setMinWidth(0);
                jTableClientsOrder.getColumnModel().getColumn(7).setPreferredWidth(0);
                //пк клиета
                jTableClientsOrder.getColumnModel().getColumn(9).setMaxWidth(0);
                jTableClientsOrder.getColumnModel().getColumn(9).setMinWidth(0);
                jTableClientsOrder.getColumnModel().getColumn(9).setPreferredWidth(0);

                for (int i = 0; i < jTableClientsOrder.getRowCount(); i++) {
                    if (jTableClientsOrder.getValueAt(i, 5).toString().equals("0")) {
                        jTableClientsOrder.setValueAt("Гарантийный", i, 5);
                    } else {
                        if (jTableClientsOrder.getValueAt(i, 5).toString().equals("1")) {
                            jTableClientsOrder.setValueAt("Не гарантийный", i, 5);
                        } else {
                            jTableClientsOrder.setValueAt("Неизвестно", i, 5);
                        }
                    }
                }
                /*if (resSet.next()) {
                    jTableClientsOrder.setValueAt(resSet.getString(4) + " " + resSet.getString(5), i, 12);
                } else {
                    jTableClientsOrder.setValueAt("", i, 12);
                }*/
            } catch (SQLException ex) {
                Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButtonViewClientOrdersActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButtonAccept;
    private javax.swing.JButton jButtonAddCrash;
    private javax.swing.JButton jButtonAddManufacturer;
    private javax.swing.JButton jButtonAddModel;
    private javax.swing.JButton jButtonAddTypeOfCrash;
    private javax.swing.JButton jButtonAktRabot;
    private javax.swing.JButton jButtonChooseExist;
    private javax.swing.JButton jButtonDeleteOrder;
    private javax.swing.JButton jButtonNomerShow;
    private javax.swing.JButton jButtonRetCrash;
    private javax.swing.JButton jButtonViewClientOrders;
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
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItemManufacturer;
    private javax.swing.JMenuItem jMenuItemModel;
    private javax.swing.JMenuItem jMenuItemPhones;
    private javax.swing.JMenuItem jMenuItemStatus;
    private javax.swing.JMenuItem jMenuItemTypeCrash;
    private javax.swing.JMenuItem jMenuItemTypeDevice;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTableClients;
    private javax.swing.JTable jTableClientsOrder;
    private javax.swing.JTable jTableNomerRem;
    private javax.swing.JTable jTableNomerZakaz;
    private javax.swing.JTextField jTextFieldAddFam;
    private javax.swing.JTextField jTextFieldAddName;
    private javax.swing.JTextField jTextFieldAddOtch;
    private javax.swing.JTextField jTextFieldAddTelefon;
    private javax.swing.JTextField jTextFieldAddress;
    private javax.swing.JTextField jTextFieldCost;
    private javax.swing.JTextField jTextFieldIMEI;
    private javax.swing.JTextField jTextFieldNomerZakaza;
    private java.awt.Menu menu1;
    private java.awt.Menu menu2;
    private java.awt.MenuBar menuBar1;
    // End of variables declaration//GEN-END:variables

}
