/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spravochn.engineer;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import main.rem.otdel.ListenerCloseForm;
import main.rem.otdel.RepairMobile;

/**
 *
 * @author tigler
 */
public class EngenieersAddUpdate extends javax.swing.JFrame {

    /**
     * Creates new form EngenieersAddUpdate
     */
    private int addOrUpdate;
    private int PK;
    private ListenerCloseForm listenerCloseForm;

    public EngenieersAddUpdate(int addOrUpdate, int PK) {
        initComponents();
        this.addOrUpdate = addOrUpdate;
        this.PK = PK;
        jCheckBoxChangeParol.setVisible(false);
        if (addOrUpdate == 1) {
            jButtonAddUpdate.setText("Изменить");
            this.setTitle("Изменить инженера");
            ChangePassword(false);
            jCheckBoxChangeParol.setVisible(true);
            ResultSet resSet = null;
            try {
                resSet = RepairMobile.st.executeQuery("select nameofengineer,otchofengineer,famofengineer,login,password"
                        + " from engineer where engineer.PK_engineer=" + PK);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Ошибка: Невозможно изменить");

                this.dispose();
            }
            try {
                if (resSet.next()) {
                    jTextFieldName.setText(resSet.getString(1));
                    jTextFieldOtch.setText(resSet.getString(2));
                    jTextFieldFam.setText(resSet.getString(3));
                    jTextFieldLogin.setText(resSet.getString(4));
                    //jTextFieldPassword.setText(resSet.getString(5));
                }
            } catch (SQLException ex) {
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButtonCancel = new javax.swing.JButton();
        jButtonAddUpdate = new javax.swing.JButton();
        jTextFieldFam = new javax.swing.JTextField();
        jTextFieldName = new javax.swing.JTextField();
        jTextFieldOtch = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldLogin = new javax.swing.JTextField();
        jCheckBoxParol = new javax.swing.JCheckBox();
        jTextFieldPassword = new javax.swing.JPasswordField();
        jCheckBoxChangeParol = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Добавление инженера");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Фамилия");

        jLabel2.setText("Имя");

        jLabel3.setText("Отчество");

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

        jLabel4.setText("Логин");

        jLabel5.setText("Пароль");

        jCheckBoxParol.setText("Показать пароль");
        jCheckBoxParol.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxParolItemStateChanged(evt);
            }
        });

        jCheckBoxChangeParol.setText("Изменить пароль");
        jCheckBoxChangeParol.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxChangeParolItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jButtonCancel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonAddUpdate))
                        .addComponent(jCheckBoxParol, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel4)
                                .addComponent(jLabel5)
                                .addComponent(jLabel2)
                                .addComponent(jLabel1))
                            .addGap(24, 24, 24)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextFieldFam)
                                .addComponent(jTextFieldName)
                                .addComponent(jTextFieldOtch, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                                .addComponent(jTextFieldLogin)
                                .addComponent(jTextFieldPassword))))
                    .addComponent(jCheckBoxChangeParol))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldFam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldOtch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxChangeParol)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextFieldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBoxParol)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancel)
                    .addComponent(jButtonAddUpdate))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        listenerCloseForm.event();
        this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonAddUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddUpdateActionPerformed
        // TODO add your handling code here:
        if (addOrUpdate == 0) {
            String textLogin = jTextFieldLogin.getText();
            String textPassword = jTextFieldPassword.getText();
            String textFam = jTextFieldFam.getText();
            String textName = jTextFieldName.getText();
            String textOcth = jTextFieldOtch.getText();
            if (textLogin.equals("") || textPassword.equals("") || textFam.equals("")
                    || textName.equals("") || textOcth.equals("")) {
                JOptionPane.showMessageDialog(this, "Невозможно добавить пустое поле");
            } else {
                try {
                    RepairMobile.st.executeQuery("Insert into engineer (famofengineer,nameofengineer,otchofengineer,login,password) values "
                            + "('" + textFam + "','" + textName + "','" + textOcth + "','" + textLogin + "','" + textPassword.hashCode() + "')");
                    JOptionPane.showMessageDialog(this, "Запись успешно добавлена");
                    listenerCloseForm.event();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Ошибка: Невозможно добавить");

                }
                this.dispose();
            }
        } else {
            if (addOrUpdate == 1) {

                String textLogin = jTextFieldLogin.getText();
                String textPassword = jTextFieldPassword.getText();
                String textFam = jTextFieldFam.getText();
                String textName = jTextFieldName.getText();
                String textOcth = jTextFieldOtch.getText();
                if (textLogin.equals("") || (textPassword.equals("") && jCheckBoxChangeParol.isSelected() == true) || textFam.equals("")
                        || textName.equals("") || textOcth.equals("")) {
                    JOptionPane.showMessageDialog(this, "Невозможно изменить на пустое поле");
                } else {
                    try {
                        if (jCheckBoxChangeParol.isSelected() == true) {
                            RepairMobile.st.executeQuery("UPDATE engineer SET famofengineer = '" + textFam + "', "
                                    + "nameofengineer = '" + textName + "', otchofengineer = '" + textOcth + "',"
                                    + " login='" + textLogin + "',password='" + textPassword.hashCode() + "' WHERE PK_engineer=" + PK
                            );
                        } else {
                            RepairMobile.st.executeQuery("UPDATE engineer SET famofengineer = '" + textFam + "', "
                                    + "nameofengineer = '" + textName + "', otchofengineer = '" + textOcth + "',"
                                    + " login='" + textLogin + "' WHERE PK_engineer=" + PK
                            );
                        }
                        JOptionPane.showMessageDialog(this, "Запись успешно изменена");
                        listenerCloseForm.event();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Ошибка: Невозможно изменить");
                    }
                    this.dispose();
                }
            }

        }
    }//GEN-LAST:event_jButtonAddUpdateActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        listenerCloseForm.event();
    }//GEN-LAST:event_formWindowClosing

    private void jCheckBoxParolItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxParolItemStateChanged
        if (jCheckBoxParol.isSelected() == true) {
            jTextFieldPassword.setEchoChar((char) 0);
        } else {
            jTextFieldPassword.setEchoChar((char) 8226);
        }
    }//GEN-LAST:event_jCheckBoxParolItemStateChanged

    private void jCheckBoxChangeParolItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxChangeParolItemStateChanged
        if (jCheckBoxChangeParol.isSelected() == true) {
            ChangePassword(true);
        } else {
            ChangePassword(false);
        }
    }//GEN-LAST:event_jCheckBoxChangeParolItemStateChanged

    public void ChangePassword(boolean value) {
        jTextFieldPassword.setEnabled(value);
        jCheckBoxParol.setEnabled(value);
        jLabel5.setEnabled(value);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddUpdate;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JCheckBox jCheckBoxChangeParol;
    private javax.swing.JCheckBox jCheckBoxParol;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField jTextFieldFam;
    private javax.swing.JTextField jTextFieldLogin;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldOtch;
    private javax.swing.JPasswordField jTextFieldPassword;
    // End of variables declaration//GEN-END:variables
}
