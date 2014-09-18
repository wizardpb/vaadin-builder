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
package com.prajnainc.vaadinbuilder.support

import com.vaadin.data.Property
import com.vaadin.data.util.VaadinPropertyDescriptor

/**
 * GroovyObjectPropertyDescriptor
 *
 */
class GroovyObjectPropertyDescriptor implements VaadinPropertyDescriptor<Object> {

    private String name
    private Class propertyType = Object
    private Object defaultValue

    @Override
    String getName() {
        return name
    }

    @Override
    Class<?> getPropertyType() {
        return propertyType
    }

    Object getDefaultValue() {
        return defaultValue
    }

    @Override
    Property<?> createProperty(Object bean) {
        return new GroovyObjectProperty(bean,name)
    }
}
