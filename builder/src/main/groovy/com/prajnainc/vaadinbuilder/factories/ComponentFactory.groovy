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

import com.prajnainc.vaadinbuilder.support.EventDefinitions
import com.vaadin.ui.Component

/**
 *
 * A {@link Factory} that creates instances of a Vaadin {@link Component}s. It is the base class for most other
 * component factories
 *
 * TODO - make attribute saving dynamic, and parent class dependent (use attribute delegate - one per set of attributes)
 */

class ComponentFactory extends AbstractFactory {

    Class componentClass

    ComponentFactory(Class<? extends Component> componentClass) {
        this.componentClass = componentClass
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
        throws InstantiationException, IllegalAccessException
    {
        def component
        if(componentClass.isAssignableFrom(value.getClass())) {
            component = value
        } else {
            component = componentClass.newInstance()
            setComponentValue(component, value, attributes)
        }
        attachListeners(component, attributes)
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

    /**
     * <p>Create and attach listeners defined by a node attributes and a {@link Component}s event table</p>
     *
     * <p>The event table defines all actionable events for a component, and consists of three elements:
     * <ul>
     *     <li>the listener class or interface</li>
     *     <li>the method to call on that interface when the event fires</li>
     *     <li>the attach method that adds a listener to the component</li>
     * </ul>
     * <p>The table is keyed by the name of a node attribute that will contain an action closure to call when the
     * event fires</p>
     *
     * <p>Using this information, the method extracts any event definition that matches a node attribute, and constructs
     * a listener proxy that will call the action closure when the event fires</p>
     *
     */
    public void attachListeners(Component component, Map attributes) {
        EventDefinitions.definitionsFor(component.getClass()).each { attr, eventDefinition->
            if(attributes.containsKey(attr)) {
                def action = attributes.remove(attr)
                def proxy = [(eventDefinition.listenerMethod): action].asType(eventDefinition.listenerClass)
                def method = eventDefinition.attachWith?: 'addListener'
                component."$method"(proxy)
            }
        }
    }

}
