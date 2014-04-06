package com.prajnainc.vaadinbuilder.factories

import com.vaadin.ui.Component

/**
 * Created by paul on 4/6/14.
 *
 * VaadinComponentFactory
 *
 * A basic {@link Factory} that creates instances of a Vaadin {@link Component}
 */
class VaadinComponentFactory extends AbstractFactory {

    private Class componentClass

//    VaadinComponentFactory(Class componentClass) {
//        this.componentClass = componentClass
//    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        return componentClass.newInstance()
    }
}
