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
package com.prajnainc.vaadinbuilder.binding

import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.prajnainc.vaadinbuilder.support.BindingCategory
import com.prajnainc.vaadinbuilder.support.GroovyBeanItem
import com.vaadin.data.Item
import com.vaadin.data.Property
import com.vaadin.data.fieldgroup.FieldGroup
import com.vaadin.server.ClientConnector
import com.vaadin.ui.Component

/**
 * A {@link DataBindingFactory} creates bindings for later binding to a target.
 *
 * TODO - binding for Container targets. Does Table need a special one ?
 *
 */
class DataBindingFactory implements DataBinding {

    def source
    String sourceProperty
    def target
    List propertyDescriptors = []

    public DataBinding createBinding() {
        assert source != null
        DataBinding binding
        if(target != null) {
            // If there is a target, we can create the binding
            binding = use(BindingCategory) { target.bindTo(source,sourceProperty,propertyDescriptors) }
        } else {
            // If there is no target, create any target type information we can, and return ourseleves
            if(sourceProperty) {
                Class bindingType = source.metaClass.getMetaProperty(sourceProperty).type
                propertyDescriptors = GroovyBeanItem.bindablePropertyDescriptorsFor(bindingType)
            }
            binding = this
        }
        return binding
    }

    @Override
    DataBinding bind() {
        assert target != null
        return createBinding().bind()
    }

    @Override
    DataBinding bind(Object target) {
        this.target = target
        return bind()
    }

    @Override
    void unbind() {
        throw new UnsupportedOperationException("Binding factories cannot unbind()")
    }

}
