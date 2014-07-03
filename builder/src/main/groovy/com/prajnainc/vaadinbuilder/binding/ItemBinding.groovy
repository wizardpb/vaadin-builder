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
import com.vaadin.data.Item

import java.beans.PropertyChangeEvent

/**
 * An ItemBinding binds some source object as an {@link Item}, usually to an {@link Item.Viewer}.
 *
 */
class ItemBinding extends AbstractDataBinding {

    List itemIds

    @Override
    protected void bindSourceProperty() {
        def value = source.getProperty(sourceProperty)
        Item item = null
        if(value) {
            item = itemIds ? new GroovyBeanItem(value, itemIds) : new GroovyBeanItem(value)
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

    @Override
    void propertyChange(PropertyChangeEvent evt) {
        if(evt.propertyName == sourceProperty) {
            bind()
        }
    }

}
