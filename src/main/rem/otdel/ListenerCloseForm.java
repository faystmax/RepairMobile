/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.rem.otdel;

/**
 *
 * @author tigler
 */
public class ListenerCloseForm {

    public UpdatesDataInForms updatesDataInForms;

    public ListenerCloseForm(UpdatesDataInForms updatesDataInForms) {
        this.updatesDataInForms = updatesDataInForms;
    }

    public void event() {
        updatesDataInForms.addDataInTable();
    }
}
