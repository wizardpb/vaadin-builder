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
 */
package com.prajnainc.vaadinbuilder.support

import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.vaadin.data.Property
import com.vaadin.data.util.AbstractProperty

/**
 * GroovyObjectProperty
 *
 * TODO - make it handle nested property names: 'a.b.c'
 *
 */
class GroovyObjectProperty extends AbstractProperty {

    private Class type
    private getter
    private setter
    private String readOnlyMessage

    public GroovyObjectProperty(GroovyObject instance,name,readOnly=false) {
        def metaProperty = instance.metaClass.getMetaProperty(name)
        if(!metaProperty) throw new VaadinBuilderException("$instance has no property '$name'")

        this.type = metaProperty.type
        this.getter = {-> instance.getProperty(name)}

        if(!readOnly && metaProperty.setter != null) {
            this.setter = { newValue -> instance.setProperty(name, newValue) }
        }
        this.readOnlyMessage = "$name on $instance is read-only"

        super.setReadOnly(readOnly || (metaProperty.setter == null))
    }

    public GroovyObjectProperty(Map instance,name,readOnly=false) {

        this.type = Object
        this.getter = {-> instance[name] }

        if(!readOnly) {
            this.setter = { newValue -> instance[name] = newValue }
        }
        this.readOnlyMessage = "$name on $instance is read-only"

        super.setReadOnly(readOnly)
    }

    @Override
    void setReadOnly(boolean newStatus) {
        // Only set read-only if there is a setter on the instance
        if(setter) { super.setReadOnly(newStatus) }
    }

    @Override
    Object getValue() {
        return getter()
    }

    @Override
    void setValue(Object newValue) throws Property.ReadOnlyException {
        if(readOnly) {
            throw new Property.ReadOnlyException(readOnlyMessage)
        }
        setter(newValue)
        fireValueChange()
    }

    /**
     * Fire a value change event when the underlying object property changes
     */
    void updateValue() {
        fireValueChange()
    }

    @Override
    Class getType() {
        return type
    }

}
