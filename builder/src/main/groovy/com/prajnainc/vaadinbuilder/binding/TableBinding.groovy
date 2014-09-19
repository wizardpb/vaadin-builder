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
import com.prajnainc.vaadinbuilder.support.GroovyBeanContainer
import com.prajnainc.vaadinbuilder.support.GroovyObjectPropertyDescriptor
import com.vaadin.data.Container

import java.beans.PropertyChangeEvent

/**
 * TableBinding
 *
 */
class TableBinding extends AbstractDataBinding {

    def propertyDescriptors = []

    private bindData(Object data) {
        Container container = new GroovyBeanContainer(propertyDescriptors)
        if(!(data == null || data instanceof Collection)) {
            throw new VaadinBuilderException("The value of $sourceProperty of $source must be a Collection to bind to $target")
        }
        container.addAll((data == null ? [] : data))
        target.setContainerDataSource(container)
    }

    public void addDescriptor(String propertyId, Class type, Object defaultValue) {
        assert target.containerDataSource instanceof Container
        propertyDescriptors.add(new GroovyObjectPropertyDescriptor(name:propertyId, propertyType: type, defaultValue: defaultValue))
        target.containerDataSource.addContainerProperty(propertyId,type,defaultValue)
    }

    @Override
    protected void bindSourceProperty() {
        bindData(source.getProperty(sourceProperty))
    }

    @Override
    protected void bindSource() {
        bindData(source)
    }

    @Override
    void propertyChange(PropertyChangeEvent evt) {
        if(evt.propertyName == sourceProperty) {
            bind()
        }
    }
}
