package com.prajnainc.vaadinbuilder.factories

import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.vaadin.ui.AbstractOrderedLayout
import org.codehaus.groovy.runtime.typehandling.GroovyCastException

/**
 * Created by paul on 4/13/14.
 */
class OrderedLayoutFactory extends LayoutFactory {


    OrderedLayoutFactory(Class<? extends AbstractOrderedLayout> componentClass) {
        super(componentClass)
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        super.setChild(builder, parent, child)
        Double ratio
        Object ratioValue = savedAttributes['expandRatio']
        try {
            ratio = ratioValue as Double
        } catch (GroovyCastException e) {
            throw new VaadinBuilderException("The ${ratioValue.getClass().simpleName} value '$ratioValue' cannot be converted to anexpand ration. It must be a number",e)
        }
        if(savedAttributes.containsKey('expandRatio')) parent.setExpandRatio(child,ratio)
    }
}
