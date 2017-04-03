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
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.proteanit.sql.DbUtils;
import otchet.alldetail.AllDetail;
import otchet.managerorder.ManagerOrder;
import otchet.timezakaz.TimeZakaz;
import spravochn.detail.Detail;
import spravochn.engineer.Engenieers;
import spravochn.manager.Managers;

/**
 *
 * @author tigler
 */
public class DetailsStore extends javax.swing.JFrame implements UpdatesDataInForms {

    /**
     * Creates new form DetailsStore
     */
    private int PK;
    private DefaultTableModel dtm4;
    private ArrayList<DefaultTableModel> sctructForTableses;
    private ArrayList<Integer> indsParTable;

    public DetailsStore() {

        initComponents();
        this.PK = PK;
        addDataInTable();
        sctructForTableses = new ArrayList<>();
        indsParTable = new ArrayList<>();
        jTable3.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable3.getColumnModel().getColumn(0).setMinWidth(0);
        jTable3.getColumnModel().getColumn(0).setPreferredWidth(0);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable4.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTableOrderDetail.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        dtm4 = (DefaultTableModel) jTable4.getModel();
        Object[] objects = {};
        dtm4.addColumn("Взять деталей(шт)");
        jTable4.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable4.getColumnModel().getColumn(0).setMinWidth(0);
        jTable4.getColumnModel().getColumn(0).setPreferredWidth(0);
        jTable4.getColumnModel().getColumn(1).setHeaderValue("Деталь");
        jTable4.getColumnModel().getColumn(2).setHeaderValue("Расположение");
        jTable4.getColumnModel().getColumn(3).setHeaderValue("Колличество на складе");

        jDateChooser1.setDate(new java.util.Date());
        JTextFieldDateEditor editor2 = (JTextFieldDateEditor) jDateChooser1.getDateEditor();
        editor2.setEditable(false);
    }

    @Override
    public void addDataInTable() {
        ResultSet resSet = null;
        this.setEnabled(true);
        try {
            resSet = RepairMobile.st.executeQuery("select detailfromwarehouse.PK_detailfromwh,"
                    + " detail.nameofdetail,"
                    + " typeofdevice.nameoftype || ' ' || manufacturer.nameofmanufacturer || ' ' ||modeldevice.nameofmodel,"
                    + " detailfromwarehouse.amount,detailfromwarehouse.location,"
                    + " concretedetail.costofdetail"
                    + " from detailfromwarehouse"
                    + " inner join concretedetail on concretedetail.PK_concretedetail=detailfromwarehouse.PK_concretedetail"
                    + " inner join detail on detail.PK_detail=concretedetail.PK_detail"
                    + " inner join modeldevice on modeldevice.PK_modeldevice=concretedetail.PK_modeldevice"
                    + " inner join manufacturer on manufacturer.PK_manufacturer=modeldevice.PK_manufacturer"
                    + " inner join typeofdevice on typeofdevice.PK_typeofdevice=concretedetail.PK_typeofdevice"
            //+ " inner join storekeeper on storekeeper.PK_storekeeper=detailfromwarehouse.PK_storekeeper"
            //+ " where detailfromwarehouse.pk_storekeeper=" + PK
            );
        } catch (SQLException ex) {
            Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTable1.setModel(DbUtils.resultSetToTableModel(resSet));
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(0);
        jTable1.getColumnModel().getColumn(1).setHeaderValue("Деталь");
        jTable1.getColumnModel().getColumn(2).setHeaderValue("Устройство");
        jTable1.getColumnModel().getColumn(3).setHeaderValue("Колличество");
        jTable1.getColumnModel().getColumn(4).setHeaderValue("Расположение");
        jTable1.getColumnModel().getColumn(5).setHeaderValue("Цена за одну");

        try {
            resSet = RepairMobile.st.executeQuery("select PK_zapros,"
                    + " TO_CHAR(zapros.TIMETOGet, 'DD.MM.YYYY'),"
                    + " TO_CHAR(zapros.TIMETOComplete, 'DD.MM.YYYY'),"
                    + " engineer.FAMOFengineer || ' ' || engineer.NAMEOFengineer || ' ' || engineer.OTChOFengineer,"
                    + " flagofcomplete"
                    + " from zapros"
                    + " inner join repair on repair.PK_repair=zapros.PK_repair"
                    + " inner join engineer on engineer.PK_engineer=repair.PK_engineer"
            );
        } catch (SQLException ex) {
            Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTable2.setModel(DbUtils.resultSetToTableModel(resSet));
        jTable2.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable2.getColumnModel().getColumn(0).setMinWidth(0);
        jTable2.getColumnModel().getColumn(0).setPreferredWidth(0);

        jTable2.getColumnModel().getColumn(1).setHeaderValue("Дата создания");
        jTable2.getColumnModel().getColumn(2).setHeaderValue("Дата выполнения");
        jTable2.getColumnModel().getColumn(3).setHeaderValue("Инженер");
        jTable2.getColumnModel().getColumn(4).setHeaderValue("Состояние");
        for (int i = 0; i < jTable2.getRowCount(); i++) {
            if (jTable2.getValueAt(i, 4).toString().equals("0")) {
                jTable2.setValueAt("Ожидает выполнения", i, 4);
            } else {
                if (jTable2.getValueAt(i, 4).toString().equals("1")) {
                    jTable2.setValueAt("Выполнено", i, 4);
                } else {
                    jTable2.setValueAt("Неизвестно", i, 4);
                }
            }
        }

        try {
            resSet = RepairMobile.st.executeQuery("select zakazdetails.PK_zakazdetail,"
                    + " zakazdetails.PK_concretedetail,"
                    + " concretedetail.PK_detail,"
                    + " concretedetail.PK_modeldevice,"
                    + " concretedetail.PK_typeofdevice,"
                    + " zakazdetails.PK_postavshik,"
                    + " detail.nameofdetail,"
                    + " typeofdevice.nameoftype || ' ' || manufacturer.nameofmanufacturer || ' ' || modeldevice.nameofmodel,"
                    + " zakazdetails.amount,"
                    + " zakazdetails.cena,"
                    + " postavshik.nazv "
                    + " from zakazdetails"
                    + " inner join concretedetail on concretedetail.PK_concretedetail=zakazdetails.PK_concretedetail"
                    + " inner join detail on detail.PK_detail=concretedetail.PK_detail"
                    + " inner join modeldevice on modeldevice.PK_modeldevice=concretedetail.PK_modeldevice"
                    + " inner join manufacturer on manufacturer.PK_manufacturer=modeldevice.PK_manufacturer"
                    + " inner join typeofdevice on typeofdevice.PK_typeofdevice=concretedetail.PK_typeofdevice"
                    + " inner join postavshik on postavshik.PK_postavshik=zakazdetails.PK_postavshik"
            );
        } catch (SQLException ex) {
            Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTableOrderDetail.setModel(DbUtils.resultSetToTableModel(resSet));
        jTableOrderDetail.getColumnModel().getColumn(0).setMaxWidth(0);
        jTableOrderDetail.getColumnModel().getColumn(0).setMinWidth(0);
        jTableOrderDetail.getColumnModel().getColumn(0).setPreferredWidth(0);

        jTableOrderDetail.getColumnModel().getColumn(1).setMaxWidth(0);
        jTableOrderDetail.getColumnModel().getColumn(1).setMinWidth(0);
        jTableOrderDetail.getColumnModel().getColumn(1).setPreferredWidth(0);

        jTableOrderDetail.getColumnModel().getColumn(2).setMaxWidth(0);
        jTableOrderDetail.getColumnModel().getColumn(2).setMinWidth(0);
        jTableOrderDetail.getColumnModel().getColumn(2).setPreferredWidth(0);

        jTableOrderDetail.getColumnModel().getColumn(3).setMaxWidth(0);
        jTableOrderDetail.getColumnModel().getColumn(3).setMinWidth(0);
        jTableOrderDetail.getColumnModel().getColumn(3).setPreferredWidth(0);

        jTableOrderDetail.getColumnModel().getColumn(4).setMaxWidth(0);
        jTableOrderDetail.getColumnModel().getColumn(4).setMinWidth(0);
        jTableOrderDetail.getColumnModel().getColumn(4).setPreferredWidth(0);

        jTableOrderDetail.getColumnModel().getColumn(5).setMaxWidth(0);
        jTableOrderDetail.getColumnModel().getColumn(5).setMinWidth(0);
        jTableOrderDetail.getColumnModel().getColumn(5).setPreferredWidth(0);

        jTableOrderDetail.getColumnModel().getColumn(6).setHeaderValue("Деталь");
        jTableOrderDetail.getColumnModel().getColumn(7).setHeaderValue("Устройство");
        jTableOrderDetail.getColumnModel().getColumn(8).setHeaderValue("Количество");
        jTableOrderDetail.getColumnModel().getColumn(9).setHeaderValue("Стоимость");
        jTableOrderDetail.getColumnModel().getColumn(10).setHeaderValue("Поставщик");

        jDateChooser1.setDateFormatString("dd.MM.yyyy");
        jDateChooser1.setDate(new java.util.Date());
        JTextFieldDateEditor editor2 = (JTextFieldDateEditor) jDateChooser1.getDateEditor();
        editor2.setEditable(false);
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
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable( )         {             @Override             public boolean isCellEditable(int row, int column)             {                 return false;             }         };
        jButtonAdd = new javax.swing.JButton();
        jButtonUpdate = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable( )
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable( )
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        jButtonExecute = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable( )         {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                if(column==6){
                    return true;
                }
                return false;
            }
        };
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableOrderDetail = new javax.swing.JTable();
        jButtonOrderDetail = new javax.swing.JButton();
        jButtonExec = new javax.swing.JButton();
        jButtonOrderDetail1 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItemClose = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItemDetailsWarehouse = new javax.swing.JMenuItem();
        jMenuItemMenegersZakaz = new javax.swing.JMenuItem();
        jMenuItemPeriodZakaz = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Работа на складе");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Деталь", "Количество", "Расположение"
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
        jButtonUpdate.setPreferredSize(new java.awt.Dimension(119, 23));
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 325, Short.MAX_VALUE)
                        .addComponent(jButtonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAdd)
                    .addComponent(jButtonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Детали на складе", jPanel1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Дата создания", "Дата выполенения", "Инженер", "Состояние"
            }
        ));
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable2MousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Деталь", "Устройство", "Колличество"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable3MousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jButtonExecute.setText("Выполнить");
        jButtonExecute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExecuteActionPerformed(evt);
            }
        });

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Деталь", "Устройство", "Расположение", "Количество на складе", "Взять деталей(шт)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable4);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Дата выполнения");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)
                            .addComponent(jScrollPane4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonExecute, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonExecute)))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Выполнение запросов", jPanel2);

        jTableOrderDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Деталь", "Количество", "Дата заказа", "Поставщик"
            }
        ));
        jScrollPane7.setViewportView(jTableOrderDetail);

        jButtonOrderDetail.setText("Заказать деталь");
        jButtonOrderDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOrderDetailActionPerformed(evt);
            }
        });

        jButtonExec.setText("Отметить прибытие");
        jButtonExec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExecActionPerformed(evt);
            }
        });

        jButtonOrderDetail1.setText("Удалить заказанную деталь");
        jButtonOrderDetail1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOrderDetail1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButtonExec, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 246, Short.MAX_VALUE)
                        .addComponent(jButtonOrderDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonOrderDetail1)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonOrderDetail)
                    .addComponent(jButtonExec)
                    .addComponent(jButtonOrderDetail1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Заказанные детали", jPanel3);

        jMenu1.setText("Файл");

        jMenuItem2.setText("Смена пользователя");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItemClose.setText("Закрыть");
        jMenuItemClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemClose);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Справочники");

        jMenuItem1.setText("Детали");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenu4.setText("Пользователи");

        jMenuItem3.setText("Менеджеры");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem3);

        jMenuItem4.setText("Инженеры");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem4);

        jMenu2.add(jMenu4);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Отчеты");

        jMenuItemDetailsWarehouse.setText("Все детали на складе");
        jMenuItemDetailsWarehouse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDetailsWarehouseActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemDetailsWarehouse);

        jMenuItemMenegersZakaz.setText("Заказы менеджера");
        jMenuItemMenegersZakaz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemMenegersZakazActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemMenegersZakaz);

        jMenuItemPeriodZakaz.setText("Все заказы за период");
        jMenuItemPeriodZakaz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPeriodZakazActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemPeriodZakaz);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        Detail detail = new Detail();
        detail.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItemCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jMenuItemCloseActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

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
                    RepairMobile.st.executeQuery("delete from detailfromwarehouse where PK_detailfromwh=" + PK);
                    addDataInTable();
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Удаление невозможно");
                Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpdateActionPerformed
        // TODO add your handling code here:
        if (jTable1.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Выделите запись для изменения");
        } else {
            Object PKTMP = jTable1.getValueAt(jTable1.getSelectedRow(), 0);
            int primKey = Integer.parseInt(PKTMP.toString());
            AddUpdateDetailFromStore addUpdateDetailFromStore = new AddUpdateDetailFromStore(1, primKey, PK);
            addUpdateDetailFromStore.setListenerCloseForm(new ListenerCloseForm(this));
            addUpdateDetailFromStore.setVisible(true);
        }
    }//GEN-LAST:event_jButtonUpdateActionPerformed

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        // TODO add your handling code here:
        AddUpdateDetailFromStore addUpdateDetailFromStore = new AddUpdateDetailFromStore(0, -1, PK);
        addUpdateDetailFromStore.setListenerCloseForm(new ListenerCloseForm(this));
        addUpdateDetailFromStore.setVisible(true);
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jButtonExecuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExecuteActionPerformed
        // TODO add your handling code here:
        ResultSet resSet = null;
        if (jTable3.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(this, "Пустой запрос.Нечего выполнять");
        } else {

            jTable4.editCellAt(0, 0); //сбить фокус с редактируемой ячейки
            boolean flagCorrectData = true;
            ArrayList<ArrayList<String>> pkDetailsStor = new ArrayList<>();
            ArrayList<ArrayList<String>> pkDetails = new ArrayList<>();
            ArrayList<ArrayList<Integer>> amountDetails = new ArrayList<>();
            ArrayList<ArrayList<Integer>> amountDetailsRaznic = new ArrayList<>();
            for (int i = 0; i < jTable3.getRowCount(); i++) {
                int amountInReq = Integer.parseInt(jTable3.getValueAt(i, 4).toString());
                ArrayList<String> pkDetStor = new ArrayList<>();
                ArrayList<String> pkDet = new ArrayList<>();
                ArrayList<Integer> amountDet = new ArrayList<>();
                ArrayList<Integer> amountDetRaznic = new ArrayList<>();
                for (int j = 0; j < sctructForTableses.size(); j++) {
                    for (int k = 0; k < sctructForTableses.get(j).getRowCount(); k++) {
                       // if (sctructForTableses.get(j).getValueAt(k, 1).toString().equals(jTable3.getValueAt(i, 1).toString())) {
                            if (sctructForTableses.get(j).getValueAt(k, 6) != null) {
                                int countInputUser = 0;
                                int countInStore = 0;
                                try {
                                    countInputUser = Integer.parseInt(sctructForTableses.get(j).getValueAt(k, 6).toString());
                                    countInStore = Integer.parseInt(sctructForTableses.get(j).getValueAt(k, 5).toString());
                                } catch (Exception ex) {
                                    flagCorrectData = false;
                                    JOptionPane.showMessageDialog(this, "Данные в столбце 'Взять деталей' не корректные. Введите целые числа");
                                    break;
                                }

                                if (countInStore < countInputUser) {
                                    flagCorrectData = false;
                                    JOptionPane.showMessageDialog(this, "Деталей на локации меньше, чем предполагается взять\n"
                                            + sctructForTableses.get(j).getValueAt(k, 4).toString());
                                } else {
                                    if (countInStore >= countInputUser) {
                                        int countTmp = countInStore - countInputUser;
                                        pkDetStor.add(sctructForTableses.get(j).getValueAt(k, 0).toString());
                                        pkDet.add(sctructForTableses.get(j).getValueAt(k, 1).toString());
                                        amountDet.add(countInputUser);
                                        amountDetRaznic.add(countTmp);
                                    }
                                }
                            }

                      //  }

                    }
                }
                pkDetailsStor.add(pkDetStor);
                pkDetails.add(pkDet);
                amountDetails.add(amountDet);
                amountDetailsRaznic.add(amountDetRaznic);
            }
            if (flagCorrectData) {
                boolean flagAmCorrect = true;

                for (int i = 0; i < pkDetails.size(); i++) {
                    int amCount = 0;
                    for (int j = 0; j < pkDetails.get(i).size(); j++) {
                        amCount += amountDetails.get(i).get(j);
                    }
                    if (amCount != Integer.parseInt(jTable3.getValueAt(i, 4).toString())) {
                        flagAmCorrect = false;
                        JOptionPane.showMessageDialog(this, "Колличество взятых деталей не "
                                + "\nсоответствует колличеству деталей в запросе\n"
                                + jTable3.getValueAt(i, 2).toString());
                        break;
                    }
                }
                if (!flagAmCorrect) {
                    return;
                }
                for (int i = 0; i < pkDetails.size(); i++) {
                    for (int j = 0; j < pkDetails.get(i).size(); j++) {
                        try {
                            RepairMobile.st.executeQuery("update detailfromwarehouse set"
                                    + " amount=" + amountDetailsRaznic.get(i).get(j)
                                    + " where PK_detailfromwh=" + pkDetailsStor.get(i).get(j));
                        } catch (SQLException ex) {
                            Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                try {
                    java.sql.Date date = new java.sql.Date(jDateChooser1.getDateEditor().getDate().getTime());
                    RepairMobile.st.executeQuery("update zapros set"
                            + " flagofcomplete=1,"
                            + " timetocomplete = TO_DATE('" + date + "', 'YYYY-MM-DD')"
                            + " where PK_zapros=" + jTable2.getValueAt(jTable2.getSelectedRow(), 0).toString());
                    JOptionPane.showMessageDialog(this, "Запрос успешно выполнен");
                    addDataInTable();
                    jTable4.getColumnModel().getColumn(0).setMaxWidth(0);
                    jTable4.getColumnModel().getColumn(0).setMinWidth(0);
                    jTable4.getColumnModel().getColumn(0).setPreferredWidth(0);
                    jTable4.getColumnModel().getColumn(1).setHeaderValue("Деталь");
                    jTable4.getColumnModel().getColumn(2).setHeaderValue("Расположение");
                    jTable4.getColumnModel().getColumn(3).setHeaderValue("Колличество на складе");
                } catch (SQLException ex) {
                    Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }
    }//GEN-LAST:event_jButtonExecuteActionPerformed

    private void jTable3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MousePressed
        // TODO add your handling code here:
        ResultSet resSet = null;
        jTable4.editCellAt(0, 0); //уводим фокус с редактируемой ячейки
        boolean flagUseModel = false;
        int i = 0;
        for (; i < indsParTable.size(); i++) {
            if (indsParTable.get(i) == jTable3.getSelectedRow()) {
                flagUseModel = true;
                break;
            }
        }
        if (flagUseModel) {
            jTable4.setModel(sctructForTableses.get(i));
        } else {
            try {
                resSet = RepairMobile.st.executeQuery("select PK_detailfromwh, detailfromwarehouse.pk_concretedetail, detail.nameofdetail,"
                        + " typeofdevice.nameoftype || ' ' || manufacturer.nameofmanufacturer|| ' ' ||modeldevice.nameofmodel,"
                        + "location, amount from detailfromwarehouse"
                        + " inner join concretedetail on concretedetail.PK_concretedetail=detailfromwarehouse.PK_concretedetail"
                        + " inner join typeofdevice on typeofdevice.PK_typeofdevice=concretedetail.PK_typeofdevice"
                        + " inner join modeldevice on modeldevice.PK_modeldevice=concretedetail.PK_modeldevice"
                        + " inner join manufacturer on manufacturer.PK_manufacturer=modeldevice.PK_manufacturer"
                        + " inner join detail on detail.PK_detail=concretedetail.PK_detail"
                        + " where detail.PK_detail=" + jTable3.getValueAt(jTable3.getSelectedRow(), 5).toString());
            } catch (SQLException ex) {
                Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
            }
            jTable4.setModel(DbUtils.resultSetToTableModel(resSet));
            DefaultTableModel dtm4 = (DefaultTableModel) jTable4.getModel();
            Object[] objects = {};
            dtm4.addColumn("Взять деталей(шт)");
            indsParTable.add(jTable3.getSelectedRow());
            sctructForTableses.add(dtm4);
        }
        jTable4.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable4.getColumnModel().getColumn(0).setMinWidth(0);
        jTable4.getColumnModel().getColumn(0).setPreferredWidth(0);
        jTable4.getColumnModel().getColumn(1).setMaxWidth(0);
        jTable4.getColumnModel().getColumn(1).setMinWidth(0);
        jTable4.getColumnModel().getColumn(1).setPreferredWidth(0);
        jTable4.getColumnModel().getColumn(2).setHeaderValue("Деталь");
        jTable4.getColumnModel().getColumn(3).setHeaderValue("Устройство");
        jTable4.getColumnModel().getColumn(4).setHeaderValue("Расположение");
        jTable4.getColumnModel().getColumn(5).setHeaderValue("Колличество на складе");
    }//GEN-LAST:event_jTable3MousePressed

    private void jTable2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MousePressed
        // TODO add your handling code here:
        sctructForTableses = new ArrayList<>();
        indsParTable = new ArrayList<>();
        jTable4.setModel(dtm4);
        jTable4.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable4.getColumnModel().getColumn(0).setMinWidth(0);
        jTable4.getColumnModel().getColumn(0).setPreferredWidth(0);
        jTable4.getColumnModel().getColumn(1).setHeaderValue("Деталь");
        jTable4.getColumnModel().getColumn(2).setHeaderValue("Расположение");
        jTable4.getColumnModel().getColumn(3).setHeaderValue("Колличество на складе");

        int idx = jTable2.getSelectedRow();
        String pkReq = jTable2.getValueAt(idx, 0).toString();
        ResultSet resSet = null;
        try {
            resSet = RepairMobile.st.executeQuery("select PK_detailofrequest, detailofrequest.PK_concretedetail, detail.nameofdetail,"
                    + " typeofdevice.nameoftype || ' ' || manufacturer.nameofmanufacturer|| ' ' ||modeldevice.nameofmodel,amount,"
                    + " detail.pk_detail, typeofdevice.pk_typeofdevice,manufacturer.pk_manufacturer,modeldevice.pk_modeldevice"
                    + " from detailofrequest"
                    + " inner join concretedetail on concretedetail.PK_concretedetail=detailofrequest.PK_concretedetail"
                    + " inner join typeofdevice on typeofdevice.PK_typeofdevice=concretedetail.PK_typeofdevice"
                    + " inner join modeldevice on modeldevice.PK_modeldevice=concretedetail.PK_modeldevice"
                    + " inner join manufacturer on manufacturer.PK_manufacturer=modeldevice.PK_manufacturer"
                    + " inner join detail on detail.PK_detail=concretedetail.PK_detail"
                    + " where PK_Zapros=" + pkReq);
        } catch (SQLException ex) {
            Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTable3.setModel(DbUtils.resultSetToTableModel(resSet));
        jTable3.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable3.getColumnModel().getColumn(0).setMinWidth(0);
        jTable3.getColumnModel().getColumn(0).setPreferredWidth(0);
        jTable3.getColumnModel().getColumn(1).setMaxWidth(0);
        jTable3.getColumnModel().getColumn(1).setMinWidth(0);
        jTable3.getColumnModel().getColumn(1).setPreferredWidth(0);
        jTable3.getColumnModel().getColumn(2).setHeaderValue("Деталь");
        jTable3.getColumnModel().getColumn(3).setHeaderValue("Устройство");
        jTable3.getColumnModel().getColumn(4).setHeaderValue("Колличество");
        jTable3.getColumnModel().getColumn(5).setMaxWidth(0);
        jTable3.getColumnModel().getColumn(5).setMinWidth(0);
        jTable3.getColumnModel().getColumn(5).setPreferredWidth(0);
        jTable3.getColumnModel().getColumn(6).setMaxWidth(0);
        jTable3.getColumnModel().getColumn(6).setMinWidth(0);
        jTable3.getColumnModel().getColumn(6).setPreferredWidth(0);
        jTable3.getColumnModel().getColumn(7).setMaxWidth(0);
        jTable3.getColumnModel().getColumn(7).setMinWidth(0);
        jTable3.getColumnModel().getColumn(7).setPreferredWidth(0);
        jTable3.getColumnModel().getColumn(8).setMaxWidth(0);
        jTable3.getColumnModel().getColumn(8).setMinWidth(0);
        jTable3.getColumnModel().getColumn(8).setPreferredWidth(0);
        if (jTable2.getValueAt(jTable2.getSelectedRow(), 4).toString().equals("Ожидает выполнения")) {
            jButtonExecute.setEnabled(true);
        } else {
            jButtonExecute.setEnabled(false);
        }
    }//GEN-LAST:event_jTable2MousePressed

    private void jButtonOrderDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOrderDetailActionPerformed
        // TODO add your handling code here:
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setListenerCloseForm(new ListenerCloseForm(this));
        orderDetail.setVisible(true);

    }//GEN-LAST:event_jButtonOrderDetailActionPerformed

    private void jButtonExecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExecActionPerformed
        // TODO add your handling code here:
        if (jTableOrderDetail.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Выделите деталь которая пришла");
        } else {
            try {
                int result = JOptionPane.showOptionDialog(
                        null,
                        "Деталь точно прибыла?",
                        "Прибытие детали",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new Object[]{"Да", "Нет"},
                        "Да");
                if (result == JOptionPane.YES_OPTION) {

                    RepairMobile.st.executeQuery("insert into detailfromwarehouse"
                            + "(pk_concretedetail,amount)"
                            + "values('" + jTableOrderDetail.getValueAt(jTableOrderDetail.getSelectedRow(), 1) + "','"
                            + jTableOrderDetail.getValueAt(jTableOrderDetail.getSelectedRow(), 8) + "')"
                    );
                    RepairMobile.st.executeQuery("delete from zakazdetails"
                            + " where pk_zakazdetail=" + jTableOrderDetail.getValueAt(jTableOrderDetail.getSelectedRow(), 0)
                    );
                    JOptionPane.showMessageDialog(this, "Деталь успешно добавлена на склад");
                    addDataInTable();
                }

            } catch (SQLException ex) {
                Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_jButtonExecActionPerformed

    private void jButtonOrderDetail1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOrderDetail1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonOrderDetail1ActionPerformed

    private void jMenuItemDetailsWarehouseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDetailsWarehouseActionPerformed
        AllDetail allDetail = new AllDetail();
        allDetail.setListenerCloseForm(new ListenerCloseForm(this));
        allDetail.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_jMenuItemDetailsWarehouseActionPerformed

    private void jMenuItemMenegersZakazActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemMenegersZakazActionPerformed
        ManagerOrder managerOrder = new ManagerOrder();
        managerOrder.setListenerCloseForm(new ListenerCloseForm(this));
        managerOrder.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_jMenuItemMenegersZakazActionPerformed

    private void jMenuItemPeriodZakazActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPeriodZakazActionPerformed
        TimeZakaz timeZakaz = new TimeZakaz();
        timeZakaz.setListenerCloseForm(new ListenerCloseForm(this));
        timeZakaz.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_jMenuItemPeriodZakazActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        Managers managers = new Managers();
        managers.setListenerCloseForm(new ListenerCloseForm(this));
        managers.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        Engenieers engenieers = new Engenieers();
        engenieers.setListenerCloseForm(new ListenerCloseForm(this));
        engenieers.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_jMenuItem4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonExec;
    private javax.swing.JButton jButtonExecute;
    private javax.swing.JButton jButtonOrderDetail;
    private javax.swing.JButton jButtonOrderDetail1;
    private javax.swing.JButton jButtonUpdate;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItemClose;
    private javax.swing.JMenuItem jMenuItemDetailsWarehouse;
    private javax.swing.JMenuItem jMenuItemMenegersZakaz;
    private javax.swing.JMenuItem jMenuItemPeriodZakaz;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTableOrderDetail;
    // End of variables declaration//GEN-END:variables

}
