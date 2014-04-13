package com.prajnainc.vaadinbuilder.factories

import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.vaadin.ui.AbstractOrderedLayout
import com.vaadin.ui.ComponentContainer
import com.vaadin.ui.Layout

/**
 * Created by paul on 4/13/14.
 */
class ComponentContainerFactory extends ComponentFactory {

    ComponentContainerFactory(Class<?extends ComponentContainer> componentClass) {
        super(componentClass)
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        parent.addComponent(child)


    }

}
