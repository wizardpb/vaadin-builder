package com.prajnainc.vaadinbuilder.factories

import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.vaadin.ui.Component
import com.vaadin.ui.GridLayout
import org.codehaus.groovy.runtime.typehandling.GroovyCastException

/**
 * GridLayoutFactory
 *
 *
 */

/**
 * GridLayoutFactory
 *
 */
class GridLayoutFactory extends LayoutFactory {

    GridLayoutFactory() {
       super(GridLayout)
    }

    @Override
    protected void addComponent(ComponentFactory childFactory, Component child) {
        component.addComponent(child,*validateArguments(childFactory))

    }

    private List validateArguments(ComponentFactory childFactory) {
        def position = childFactory.savedAttributes.remove(GRID_POSITION_ATTR)
        def span = childFactory.savedAttributes.remove(GRID_SPAN_ATTR)

        if(!position && span) {
            // We need specific position information for a span - get it from the component
            position = [component.cursorX, component.cursorY]
        }

        position = validateAndCollect(position,['column','row'],GRID_POSITION_ATTR)
        span = validateAndCollect(span,['columns','rows'],GRID_SPAN_ATTR)

        // If there is a span, calculate lower bottom corner coordinates
        return span ? position + [position.first() + span.first() - 1, position.last() + span.last() - 1] : position
    }

    private validateAndCollect(attributeValue,keySet,name) {
        def result = []
        if(attributeValue != null) {
            switch(attributeValue) {
                case List:
                    if(attributeValue.size() != 2) {
                        throw new VaadinBuilderException("$name value must have two values")
                    }
                    result = attributeValue; break
                case Map:
                    if(attributeValue.keySet() != keySet as Set) {
                        throw new VaadinBuilderException("$name has incorrect keys: should be $keySet")
                    }
                    result = keySet.collect { attributeValue[it] }
                    break;
                default: throw new VaadinBuilderException("$name value must be a Map or List")
            }
            try {
                result = result.collect { it as Integer }
            } catch(GroovyCastException e) {
                throw new VaadinBuilderException("$name has an incorrect value type",e)
            }
        }

        assert result.isEmpty() || result.size() == 2
        assert result.every { it instanceof Integer }

        return result
    }
}
