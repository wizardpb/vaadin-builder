package com.prajnainc.vaadinbuilder.factories

import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.vaadin.ui.AbstractLayout
import com.vaadin.ui.Alignment
import com.vaadin.ui.Component
import com.vaadin.ui.Layout

/**
 * Created by paul on 4/13/14.
 */
class LayoutFactory extends ComponentContainerFactory {

    LayoutFactory(Class <? extends AbstractLayout> componentClass) {
        super(componentClass)
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        super.setChild(builder, parent, child)
        // Process appropriate saved attributes
        if(savedAttributes.containsKey('alignment')) {
            if(parent instanceof Layout.AlignmentHandler) {
                parent.setComponentAlignment((Component)child,createAlignment(savedAttributes['alignment']))
            } else {
                throw new VaadinBuilderException("Cannot set alignment of ${builder.getCurrentFactory().namedNodeString(child)}: ${namedNodeString((Component)parent)} is not an aligning container}")
            }
        }
    }

    private static Alignment createAlignment(Object alignmentValue) {
        if(!alignmentValue instanceof Alignment) {
            throw new VaadinBuilderException("'$alignmentValue' is not a valid Alignment value")
        }
        return (Alignment)alignmentValue
    }
}
