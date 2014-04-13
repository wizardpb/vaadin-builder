package com.prajnainc.vaadinbuilder.factories

import com.prajnainc.vaadinbuilder.support.Form
import com.vaadin.ui.Component

/**
 * Created by paul on 4/11/14.
 */
class FormFactory extends VaadinComponentFactory {

    FormFactory(Class componentClass) {
        super(componentClass)
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        Form thisForm = (Form)parent
        thisForm.layout = (Component)child
    }
}
