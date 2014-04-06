package com.prajnainc.vaadinbuilder.factories

/**
 * Created by paul on 4/6/14.
 */
class SingleComponentContainerFactory extends VaadinComponentFactory {

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        parent.setContent(child)
    }

}
