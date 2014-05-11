package com.prajnainc.vaadinbuilder.factories

import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.prajnainc.vaadinbuilder.binding.DataBinding
import com.vaadin.data.Item
import com.vaadin.data.fieldgroup.FieldGroup

/*
 * Copyright (c) 2014 Prajna Inc.
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
 *
 *
 */

/**
 * FieldGroupFactory
 *
 * <p>A {@link FieldGroupFactory} creates an instance of a given {@link com.vaadin.ui.Layout} and attaches a {@link FieldGroup} to it
 * the layout is created by using a sub-factory of the correct class, and delegating all factory call to it, with the exception of the
 * {@link Factory#newInstance(groovy.util.FactoryBuilderSupport, java.lang.Object, java.lang.Object, java.util.Map)} method.</p>
 *
 * <p>The created {@link com.vaadin.data.fieldgroup.FieldGroup} is given an id of "${layoutId}.fieldGroup"
 * and also attached to the data field of the new layout,along with any {@link DataBinding} passed in via the 'itemDataSource' attribute
 *
 */
class FieldGroupFactory implements VaadinFactory {

    /**
     * Delegate to the layout factory for any other Factory services. This allows the factory to create both the field group and thelayout, and
     * appear to the builder as a layout factory.
     *
     * Note that the delegate is dynamically updated on every node build in accordance with the layout type required
     */
    @Delegate LayoutFactory layoutFactory

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        def layoutName = attributes.remove('layout') ?: 'formLayout'
        layoutFactory = builder.factories[layoutName]
        if(layoutFactory == null) {
            throw new VaadinBuilderException("Unknown layout type '$layoutName'")
        }

        // Create the factory and id it
        def fieldGroup = new FieldGroup()
        def id = attributes.remove('id') ?: value
        if(id != null) {
            builder.setVariable("${id}.fieldGroup",fieldGroup)
        }

        //Create the layout by delegation and return it
        def layout = layoutFactory.newInstance(builder,name,value,attributes)
        layout.data = [fieldGroup: fieldGroup]
        return layout
    }

    void onFactoryRegistration(FactoryBuilderSupport builder, String registeredName, String registeredGroupName) {
        // No action
    }

}
