package com.prajnainc.vaadinbuilder.support

import com.vaadin.data.fieldgroup.FieldGroup
import com.vaadin.ui.Component
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.FormLayout


/**
 * Created by paul on 4/11/14.
 */
class BasicForm extends CustomComponent implements Form {

    Component layout = new FormLayout()
    FieldGroup fieldGroup = new FieldGroup()

}
