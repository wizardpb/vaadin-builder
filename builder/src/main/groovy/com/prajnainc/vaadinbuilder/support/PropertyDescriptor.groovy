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

/**
 * A {@link PropertyDescriptor} describes a property on a data source object such that given the instance to be bound,
 * it can create and return a {@link com.vaadin.data.Property} object binding that property on the given instance
 *
 */
class PropertyDescriptor {

    String name
    Class type
    boolean readOnly

    Property createProperty(GroovyObject instance) {
        def prop = new GroovyObjectProperty(instance,name,readOnly)
        if(prop.type != type) {
            throw new VaadinBuilderException("Incorrect property type of '$name' on $instance: wanted $type but was ${this.type}")
        }
        return prop
    }

    Property createInstance(Map instance) {
        //TBD
        null
    }
}
