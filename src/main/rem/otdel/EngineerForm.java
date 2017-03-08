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
public class EngineerForm extends javax.swing.JFrame implements UpdatesDataInForms{

    private int PK;
    private ArrayList<String> pkStorekeeper;
    private ArrayList<String> valueStorekeeper;
    private ArrayList<String> pkDetail;
    private ArrayList<String> valueDetail;
    private DefaultTableModel dtm;
    /**
     * Creates new form EngineerForm
     */
  

    public EngineerForm(int PK) {
        initComponents();
       this.PK = PK;
        addDataInTable();
        pkStorekeeper = new ArrayList<String>();
        valueStorekeeper = new ArrayList<String>();

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
        try {
            resSet = RepairMobile.st.executeQuery("select storekeeper.PK_storekeeper,"
                    + " storekeeper.FAMOFstorekeeper || ' ' || storekeeper.NAMEOFstorekeeper  || ' ' || storekeeper.OTCOFstorekeeper"
                    + " from storekeeper");
        } catch (SQLException ex) {
            Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        TableModel tableModel = DbUtils.resultSetToTableModel(resSet);
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            pkStorekeeper.add(tableModel.getValueAt(i, 0).toString());
            valueStorekeeper.add(tableModel.getValueAt(i, 1).toString());
        }
        //jComboBoxStorekeeper.setModel(new DefaultComboBoxModel(valueStorekeeper.toArray()));
        //jComboBoxStorekeeper.setSelectedIndex(-1);

        jDateChooser1.setDateFormatString("dd.MM.yyyy");
        jDateChooser1.setDate(new Date());
        //jDateChooser1.getDateEditor().setEnabled(false);
        JTextFieldDateEditor editor2 = (JTextFieldDateEditor) jDateChooser1.getDateEditor();
        editor2.setEditable(false);

        pkDetail = new ArrayList<String>();
        valueDetail = new ArrayList<String>();

        try {
            resSet = RepairMobile.st.executeQuery("select detail.PK_detail,"
                    + " detail.nameofdetail"
                    + " from detail");
        } catch (SQLException ex) {
            Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableModel = DbUtils.resultSetToTableModel(resSet);
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            pkDetail.add(tableModel.getValueAt(i, 0).toString());
            valueDetail.add(tableModel.getValueAt(i, 1).toString());
        }
        jComboBoxDetail.setModel(new DefaultComboBoxModel(valueDetail.toArray()));
        jComboBoxDetail.setSelectedIndex(-1);
    }
    
    @Override
    public void addDataInTable() {
        this.setEnabled(true);
        ResultSet resSet = null;
        try {
            resSet = RepairMobile.st.executeQuery("select repair.PK_repair,"
                    + " typeofdevice.nameoftype,"
                    + " manufacturer.nameofmanufacturer,"
                    + " device.modelofdevice,"
                    + " TO_CHAR(repair.startdate, 'DD.MM.YYYY'),"
                    + " TO_CHAR(repair.enddate, 'DD.MM.YYYY'),"
                    + "status.nameofStatus "
                    //+ " engineer.FAMOFengineer || ' ' || engineer.NAMEOFengineer  || ' ' || engineer.OTChOFengineer"
                    + " from repair "
                    + " inner join engineer on repair.PK_engineer=engineer.PK_engineer"
                    + " inner join device on repair.PK_device=device.PK_device"
                    + " inner join manufacturer on device.PK_manufacturer=manufacturer.PK_manufacturer"
                    + " inner join typeofdevice on device.PK_typeofdevice=typeofdevice.PK_typeofdevice"
                    + " inner join myorder on myorder.PK_order=device.PK_order"
                    + " inner join status on status.PK_status=myOrder.PK_status"
                    + " where engineer.PK_engineer="+ PK
            );
        } catch (SQLException ex) {
            Logger.getLogger(DetailsStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTable1.setModel(DbUtils.resultSetToTableModel(resSet));

        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(0);
        jTable1.getColumnModel().getColumn(1).setHeaderValue("Тип");
        jTable1.getColumnModel().getColumn(2).setHeaderValue("Производитель");
        jTable1.getColumnModel().getColumn(3).setHeaderValue("Модель");
        jTable1.getColumnModel().getColumn(4).setHeaderValue("Начало ремонта");
        jTable1.getColumnModel().getColumn(5).setHeaderValue("Конец ремонта");
        jTable1.getColumnModel().getColumn(6).setHeaderValue("Статус");

        try {
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

        dtm = new DefaultTableModel();
        jTable3.setModel(dtm);
        dtm.addColumn("pk");
        dtm.addColumn("Деталь");
        dtm.addColumn("Колличество");
        jTable3.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable3.getColumnModel().getColumn(0).setMinWidth(0);
        jTable3.getColumnModel().getColumn(0).setPreferredWidth(0);
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
        jPanelCreateZapros = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jButtonSend = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jButtonAddDetail = new javax.swing.JButton();
        jButtonDeleteDetail = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable( )         {             @Override             public boolean isCellEditable(int row, int column)             {                 return false;             }         };
        jComboBoxDetail = new javax.swing.JComboBox<>();
        jSpinner1 = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable( )         {             @Override             public boolean isCellEditable(int row, int column)             {                 return false;             }         };
        jLabel4 = new javax.swing.JLabel();
        jLabelFIO = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Инженер");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelRemontsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonAdd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonUpdate)
                    .addComponent(jButtonDelete, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelRemontsLayout.setVerticalGroup(
            jPanelRemontsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRemontsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanelRemontsLayout.createSequentialGroup()
                .addGap(132, 132, 132)
                .addComponent(jButtonAdd)
                .addGap(18, 18, 18)
                .addComponent(jButtonUpdate)
                .addGap(18, 18, 18)
                .addComponent(jButtonDelete)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("Ремонты", jPanelRemonts);

        jLabel2.setText("Дата создания запроса");

        jButtonSend.setText("Отправить");
        jButtonSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSendActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setText("Детали в запросе");

        jButtonAddDetail.setText("Добавить");
        jButtonAddDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddDetailActionPerformed(evt);
            }
        });

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
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 355, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jComboBoxDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSpinner1)
                                .addGap(27, 27, 27)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonAddDetail, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                            .addComponent(jButtonDeleteDetail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonDeleteDetail))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonAddDetail))
                        .addGap(19, 19, 19)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

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
        jScrollPane2.setViewportView(jTable2);

        jLabel4.setText("Ремонты");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 3, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanelCreateZaprosLayout = new javax.swing.GroupLayout(jPanelCreateZapros);
        jPanelCreateZapros.setLayout(jPanelCreateZaprosLayout);
        jPanelCreateZaprosLayout.setHorizontalGroup(
            jPanelCreateZaprosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCreateZaprosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCreateZaprosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCreateZaprosLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanelCreateZaprosLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                        .addGroup(jPanelCreateZaprosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCreateZaprosLayout.createSequentialGroup()
                                .addGroup(jPanelCreateZaprosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(31, 31, 31))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCreateZaprosLayout.createSequentialGroup()
                                .addComponent(jButtonSend, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(55, 55, 55))))))
        );
        jPanelCreateZaprosLayout.setVerticalGroup(
            jPanelCreateZaprosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCreateZaprosLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanelCreateZaprosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelCreateZaprosLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonSend)))
                .addContainerGap())
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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabelFIO, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabelFIO)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane)
                .addGap(16, 16, 16))
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
                        int pkDevice = Integer.parseInt(jTable2.getValueAt(jTable2.getSelectedRow(), 7).toString());
                        //int idxComboBox = jComboBoxStorekeeper.getSelectedIndex();
                       // String pkStrokeeper = pkStorekeeper.get(idxComboBox);
                        java.sql.Date date = new java.sql.Date(jDateChooser1.getDateEditor().getDate().getTime());
                       // RepairMobile.st.executeQuery("Insert into zapros (PK_Repair, PK_storekeeper,timetoget,"
                         //       + "flagofcomplete,pk_device) values ('" + pkRepair + "','" + pkStrokeeper + "', TO_DATE('" + date + "', 'YYYY-MM-DD') ,'0',"
                        //        + "'" + pkDevice + "')");
                        resSet = RepairMobile.st.executeQuery("select seqzapros.currval from dual");
                        int pkZapros = 0;
                        if (resSet.next()) {
                            pkZapros = resSet.getInt(1);
                        }
                        for (int i = 0; i < jTable3.getRowCount(); i++) {
                            String pkDet = (String) jTable3.getValueAt(i, 0);
                            String countDetail = (String) jTable3.getValueAt(i, 2);
                            RepairMobile.st.executeQuery("Insert into detailofrequest"
                                    + " (PK_zapros,PK_detail,amount) values ('" + pkZapros + "','" + pkDet + "','" + countDetail + "')");
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
        if (jComboBoxDetail.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(rootPane, "Выберите деталь");
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
                    jSpinner1.getValue().toString()
                };
                dtm.addRow(objects);

            }
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonAddDetail;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonDeleteDetail;
    private javax.swing.JButton jButtonSend;
    private javax.swing.JButton jButtonUpdate;
    private javax.swing.JComboBox<String> jComboBoxDetail;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelFIO;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelCreateZapros;
    private javax.swing.JPanel jPanelRemonts;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    // End of variables declaration//GEN-END:variables
}
