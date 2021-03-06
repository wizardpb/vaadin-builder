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

import com.prajnainc.vaadinbuilder.support.GroovyObjectPropertyDescriptor
import com.vaadin.data.Property
import com.vaadin.data.util.ObjectProperty

import java.beans.PropertyChangeEvent

/**
 * A {@link PropertyBinding} binds a source as a single value, usually to a {@link com.vaadin.data.Property.Viewer}
 *
 */
class PropertyBinding extends AbstractDataBinding {

    @Override
    protected void bindSourceProperty() {
        // Type and default values default to those defined by the source
        target.setPropertyDataSource(
            sourceValue instanceof Property ? sourceValue :
            new GroovyObjectPropertyDescriptor(name: sourceProperty).createProperty(source))
    }

    @Override
    protected void bindSource() {
        target.setPropertyDataSource(source instanceof Property ? source : new ObjectProperty(source))
    }

    @Override
    void unbind() {
        target.setPropertyDataSource(null)
        super.unbind()
    }

    @Override
    void propertyChange(PropertyChangeEvent evt) {
        if(evt.propertyName == sourceProperty) {
            target.propertyDataSource.updateValue()
        }
    }

}
