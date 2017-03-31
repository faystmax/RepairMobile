/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spravochn.typeofcrash;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import main.rem.otdel.ListenerCloseForm;
import main.rem.otdel.Orders;
import main.rem.otdel.RepairMobile;

/**
 *
 * @author tigler
 */
public class TypeCrashAddUpdate extends javax.swing.JFrame {

    /**
     * Creates new form TypeCrashAddUpdate
     */
    private ListenerCloseForm listenerCloseForm;
    private int addOrUpdate;
    private int PK;

    public TypeCrashAddUpdate(int addOrUpdate, int PK) {
        initComponents();
        this.addOrUpdate = addOrUpdate;
        if (addOrUpdate == 1) {
            jButtonAddUpdate.setText("Изменить");
            this.setTitle("Изменить тип поломки");
        }
        this.PK = PK;

        ResultSet resSet = null;
        try {
            resSet = RepairMobile.st.executeQuery("select nameofcrash from typeofcrash where PK_crash=" + PK);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Ошибка: Невозможно изменить");

            this.dispose();
        }
        try {
            if (resSet.next()) {
                jTextArea1.setText(resSet.getString(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка: Невозможно изменить");
            this.dispose();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButtonAddUpdate = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Добавить тип поломки");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Тип поломки");

        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextArea1);

        jButtonAddUpdate.setText("Добавить");
        jButtonAddUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddUpdateActionPerformed(evt);
            }
        });

        jButtonCancel.setText("Отмена");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 188, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
                        .addComponent(jButtonAddUpdate)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAddUpdate)
                    .addComponent(jButtonCancel))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        //listenerCloseForm.event();
        updateParent();
        this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonAddUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddUpdateActionPerformed
        // TODO add your handling code here:
        if (addOrUpdate == 0) {
            String text = jTextArea1.getText();
            if (text.equals("")) {
                JOptionPane.showMessageDialog(this, "Невозможно добавить пустое поле");
            } else {
                try {
                    RepairMobile.st.executeQuery("Insert into typeofcrash (NAMEOFcrash) values ('" + text + "')");
                    JOptionPane.showMessageDialog(this, "Запись успешно добавлена");
                    //listenerCloseForm.event();
                    updateParent();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Ошибка: Невозможно добавить");

                }
                this.dispose();
            }
        } else {
            if (addOrUpdate == 1) {

                String text = jTextArea1.getText();

                if (text.equals("")) {
                    JOptionPane.showMessageDialog(this, "Невозможно изменить на пустое поле");
                } else {
                    try {
                        RepairMobile.st.executeQuery("UPDATE typeofcrash SET NAMEOFcrash = '" + text + "' WHERE PK_crash=" + PK);
                        JOptionPane.showMessageDialog(this, "Запись успешно изменена");
                        //listenerCloseForm.event();
                        updateParent();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Ошибка: Невозможно изменить");
                    }
                    this.dispose();
                }
            }
        }
    }//GEN-LAST:event_jButtonAddUpdateActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        //listenerCloseForm.event();
        updateParent();
    }//GEN-LAST:event_formWindowClosing

    public void updateParent() {

        if (listenerCloseForm.updatesDataInForms instanceof Orders) {
            ((Orders) listenerCloseForm.updatesDataInForms).addCrashs();
        } else {
            listenerCloseForm.event();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddUpdate;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
