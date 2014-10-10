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
class TableBinding extends ContainerBinding {

    List<GroovyObjectPropertyDescriptor> propertyDescriptors = []

    private bindData(Object data) {
        Container container = new GroovyBeanContainer(propertyDescriptors)
        if(!(data == null || data instanceof Collection)) {
            throw new VaadinBuilderException(
                "The value of $sourceProperty of $source must be a Collection to bind to $target"
            )
        }
        container.addAll((data == null ? [] : data))
        target.setContainerDataSource(container)
    }

    public GroovyObjectPropertyDescriptor addDescriptor(String propertyId, Class type, Object defaultValue) {
        def descriptor = new GroovyObjectPropertyDescriptor(
            name: propertyId, propertyType: type, defaultValue: defaultValue
        )
        propertyDescriptors.add(descriptor)
        if(target.containerDataSource) {
            target.containerDataSource.addContainerProperty(propertyId, type, defaultValue)
        }
        return descriptor
    }

    @Override
    protected void bindSourceProperty() {
        bindData(source.getProperty(sourceProperty))
    }

    @Override
    protected void bindSource() {
        bindData(source)
        super.bindSource()
    }

    @Override
    void propertyChange(PropertyChangeEvent evt) {
        collectionPropertyChange(evt)
    }

    void collectionPropertyChange(PropertyChangeEvent evt) {
        if(evt.propertyName == sourceProperty) {
            bind()
            super.propertyChange(evt)
        }
    }

    void collectionPropertyChange(ObservableList.ElementEvent evt) {
        Container dataSource = target.containerDataSource
        if(evt.source == ObservableList.SIZE_PROPERTY) return;          // Ignore size change events
        switch(evt) {
            case ObservableList.ElementAddedEvent:
                dataSource.addBeanAt(evt.index, evt.newValue)
                break;
            case ObservableList.ElementUpdatedEvent:
                dataSource.removeItem(evt.oldValue)
                dataSource.addBeanAt(evt.index, evt.newValue)
                break;
            case ObservableList.ElementClearedEvent:
                dataSource.removeAllItems()
                break;
            case ObservableList.ElementRemovedEvent:
                dataSource.removeItem(evt.oldValue)
                break;
            case ObservableList.MultiElementAddedEvent:
                evt.values.eachWithIndex { bean, offset ->
                    dataSource.addBeanAt(evt.index+offset, bean)
                }
                break;
            case ObservableList.MultiElementRemovedEvent:
                evt.values.each { bean ->
                    dataSource.removeItem(bean)
                }
                break;
        }
    }

    void collectionPropertyChange(ObservableSet.ElementEvent evt) {
        if(e.source == ObservableList.SIZE_PROPERTY) return;          // Ignore size change events
        switch(evt) {
            case ObservableSet.ElementAddedEvent:
                dataSource.addBean(evt.newValue)
                break;
            case ObservableSet.ElementClearedEvent:
                dataSource.removeAllItems()
                break;
            case ObservableSet.ElementRemovedEvent:
                dataSource.removeItem(evt.oldValue)
                break;
            case ObservableSet.MultiElementAddedEvent:
                evt.values.eachWithIndex { bean ->
                    dataSource.addBean(bean)
                }
                break;
            case ObservableSet.MultiElementRemovedEvent:
                evt.values.each { bean ->
                    dataSource.removeItem(bean)
                }
                break;
        }
    }
}
