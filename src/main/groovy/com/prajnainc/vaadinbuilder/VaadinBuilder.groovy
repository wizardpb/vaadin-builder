package com.prajnainc.vaadinbuilder

import com.prajnainc.vaadinbuilder.factories.SingleComponentContainerFactory
import com.vaadin.ui.Panel
import com.vaadin.ui.Window

/**
 * Created by paul on 4/6/14.
 */
class VaadinBuilder extends FactoryBuilderSupport {

    def registerSingleComponentFactories() {
        registerFactory('panel',new SingleComponentContainerFactory(componentClass: Panel))
        registerFactory('window', new SingleComponentContainerFactory(componentClass: Window))
    }

}
