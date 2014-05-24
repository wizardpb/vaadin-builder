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
import com.prajnainc.vaadinbuilder.support.GroovyObjectProperty
import com.vaadin.data.util.ObjectProperty

import java.beans.PropertyChangeEvent

/**
 * PropertyBinding
 *
 */
class PropertyBinding extends AbstractDataBinding {

    @Override
    DataBinding bind() {
        assert target != null; assert source != null

        // If there is a source property, bind it as a property on a model object ...
        if(sourceProperty) {
            if(source.metaClass.getMetaProperty(sourceProperty) == null) {
                throw new VaadinBuilderException("Source $source has no property $sourceProperty")
            }

            target.setPropertyDataSource(new GroovyObjectProperty(source, sourceProperty))
            addChangeListener()
        } else {
            // .. otherwise bind the source object directly, as a value
            target.setPropertyDataSource(new ObjectProperty(source))
        }
        return this
    }

    @Override
    void unbind() {
        target.setPropertyDataSource(null)
        removeChangeListener()
    }

    @Override
    void propertyChange(PropertyChangeEvent evt) {
        if(evt.propertyName == sourceProperty) {
            target.propertyDataSource.updateValue()
        }
    }

}
