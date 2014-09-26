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

import com.prajnainc.vaadinbuilder.VaadinBuilder
import com.prajnainc.vaadinbuilder.binding.DataBinding
import com.vaadin.ui.AbstractComponentContainer
import com.vaadin.ui.Component
import com.vaadin.ui.ComponentContainer

/**
 * ComponentContainerFactory is a {@link Factory} for building Vaadin components that contain other {@link Component}s. It is the abstract base class
 * of all factories that build component containers
 *
 */

abstract class ComponentContainerFactory extends ComponentFactory {

    ComponentContainerFactory(Class<?extends ComponentContainer> componentClass) {
        super(componentClass)
    }

    @Override
    protected void setChildComponent(FactoryBuilderSupport builder, Object parent, Object child) {
        assert parent instanceof AbstractComponentContainer
        addComponent(builder.childBuilder,parent,child)
        setAlignmentFrom(builder.childBuilder,parent,child)
        setExpandRatioFrom(builder.childBuilder,parent,child)

    }

    protected void setChildComponent(FactoryBuilderSupport builder, Object parent, DataBinding child) {
        // Data binding don't get added as children
    }

    protected void addComponent(VaadinBuilder childBuilder, Component parent, Component child) {
        parent.addComponent(child)
    }

    protected void setAlignmentFrom(VaadinBuilder childBuilder, Object parent, Object child) {
        // Do nothing by default
    }

    protected void setExpandRatioFrom(VaadinBuilder childBuilder, Object parent, Object child) {
        // Do nothing by default
    }
}
