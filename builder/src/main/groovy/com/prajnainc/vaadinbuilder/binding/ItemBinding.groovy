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

import com.prajnainc.vaadinbuilder.support.GroovyBeanItem
import com.prajnainc.vaadinbuilder.support.GroovyObjectPropertyDescriptor
import com.vaadin.data.Item

import java.beans.PropertyChangeEvent

/**
 * An ItemBinding binds some source object as an {@link Item}, usually to an {@link Item.Viewer}.
 *
 */
class ItemBinding extends AbstractDataBinding {

    List<GroovyObjectPropertyDescriptor> propertyDescriptors = []

    @Override
    protected void bindSourceProperty() {
        def value = source.getProperty(sourceProperty)
        Item item = null
        if(value != null) {
            item = new GroovyBeanItem(value, propertyDescriptors)
        }
        target.setItemDataSource(item)
    }

    @Override
    protected void bindSource() {
        target.setItemDataSource(new GroovyBeanItem(source))
    }

    @Override
    void unbind() {
        target.setItemDataSource(null)
        super.unbind()
    }

    public GroovyObjectPropertyDescriptor addDescriptor(String propName, Class type, Object defaultValue) {
        assert descriptorFor(propName) == null
        def descriptor = new GroovyObjectPropertyDescriptor(
            name: propName, propertyType: type, defaultValue: defaultValue
        )
        propertyDescriptors.add(descriptor)
        def dataSource = target?.itemDataSource
        if(dataSource) {
            dataSource.addItemProperty(propName, descriptor.createProperty(dataSource.bean))
        }
        return descriptor
    }

    @Override
    void propertyChange(PropertyChangeEvent evt) {
        if(evt.propertyName == sourceProperty) {
            bind()
        }
    }

}
