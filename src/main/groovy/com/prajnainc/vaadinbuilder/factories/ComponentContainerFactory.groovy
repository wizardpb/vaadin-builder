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

import com.vaadin.ui.AbstractComponentContainer
import com.vaadin.ui.Component
import com.vaadin.ui.ComponentContainer

/**
 * ComponentContainerFactory
 *
 * A {@link Factory} for building Vaadin components that contain other {@link Component}s
 *
 */

class ComponentContainerFactory extends ComponentFactory {

    ComponentContainerFactory(Class<?extends ComponentContainer> componentClass) {
        super(componentClass)
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        /**
         * The child factory has already added the child to my component (the parent). Now, just set
         * any alignment or expansion settings
         */
        ComponentFactory childFactory = builder.currentFactory
        setAlignmentFrom(childFactory)
        setExpandRatioFrom(childFactory)

    }

    public void doAddChild(Component child) {
        assert component instanceof AbstractComponentContainer
        component.addComponent(child)
    }

    protected void setAlignmentFrom(ComponentFactory childFactory) {
        // Do nothing by default
    }

    protected void setExpandRatioFrom(ComponentFactory childFactory) {
        // Do nothing by default
    }
}
