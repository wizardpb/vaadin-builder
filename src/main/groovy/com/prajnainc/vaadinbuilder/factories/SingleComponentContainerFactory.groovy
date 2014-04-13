package com.prajnainc.vaadinbuilder.factories

/**
 * Created by paul on 4/6/14.
 */
class SingleComponentContainerFactory extends VaadinComponentFactory {

    SingleComponentContainerFactory(Class componentClass) {
        super(componentClass)
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        parent.setContent(child)
    }

}
