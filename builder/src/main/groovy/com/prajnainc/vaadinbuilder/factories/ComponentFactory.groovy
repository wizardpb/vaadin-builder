/*
 * Copyright (c) 2014 Prajna Inc
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.prajnainc.vaadinbuilder.factories

import com.prajnainc.vaadinbuilder.binding.DataBinding
import com.vaadin.ui.Component

/**
 *
 * A {@link Factory} that creates instances of a Vaadin {@link Component}s. It is the base class for most other component factories
 *
 * TODO - make attribute saving dynamic, and parent class dependent (use attribute delegate - one per set of attributes)
 */

class ComponentFactory extends AbstractFactory implements VaadinFactory {

    Class componentClass

    ComponentFactory(Class<? extends Component> componentClass) {
        this.componentClass = componentClass
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        def component
        if(componentClass.isAssignableFrom(value.getClass())) {
            component = value
        } else {
            component = componentClass.newInstance()
            setComponentValue(component, value, attributes)
        }
        return component
    }

    /**
     * Set any needed value into the component. Generally, this is the component caption, which can be
     * overridden by a caption attribute
     *
     * @param value - the value to set
     * @param attributes - the given attributes
     */
    protected setComponentValue(Component component, value, attributes) {
        if(value instanceof String) component.caption = value
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        setChildComponent(builder, parent, child)
    }

    protected void setChildComponent(FactoryBuilderSupport builder, Object parent, Object child) {
        // Do nothing by default
    }
}
