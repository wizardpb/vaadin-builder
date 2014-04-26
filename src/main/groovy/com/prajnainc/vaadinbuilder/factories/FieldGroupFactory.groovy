package com.prajnainc.vaadinbuilder.factories

import com.prajnainc.vaadinbuilder.VaadinBuilder
import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.prajnainc.vaadinbuilder.support.DynamicallyBoundFieldGroup
import com.vaadin.client.ui.formlayout.FormLayoutConnector
import com.vaadin.ui.AbstractOrderedLayout
import com.vaadin.ui.FormLayout

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
 * <p>A {@link FieldGroupFactory} creates an instance of a given {@link com.vaadin.ui.Layout} and attaches a {@link DynamicallyBoundFieldGroup} to it
 * the layout is created by using a sub-factory of the correct class, and delegating all factory call to it, with the exception of the
 * {@link Factory#newInstance(groovy.util.FactoryBuilderSupport, java.lang.Object, java.lang.Object, java.util.Map)} method.</p>
 * <p>The created {@link com.vaadin.data.fieldgroup.FieldGroup} is given an id of "${layoutId}.fieldGroup"
 * and also attached to the data field of the new layout
 *
 */
class FieldGroupFactory extends AbstractFactory implements VaadinFactory {

    @Delegate LayoutFactory layoutFactory

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        def layoutName = attributes.remove('layout') ?: 'formLayout'
        layoutFactory = builder.factories[layoutName]
        if(layoutFactory == null) {
            throw new VaadinBuilderException("Unknown layout type '$layoutName'")
        }
        def modelType = attributes.remove('modelType')
        if(modelType == null) {
            throw new VaadinBuilderException("Cannot build a field group without a model type")
        }
        // Create the factory and id it
        def fieldGroup = new DynamicallyBoundFieldGroup(modelType)
        def id = attributes['id'] ?: value
        if(id != null) {
            builder.setVariable("${id}.fieldGroup",fieldGroup)
        }

        //Create the layout by delegation and return it
        def layout = layoutFactory.newInstance(builder,name,value,attributes)
        layout.data = fieldGroup
        return layout
    }


}
