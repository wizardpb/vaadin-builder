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
 * GroovyObjectPropertyDescriptor is an implementation of {@link VaadinPropertyDescriptor} for describing Groovy properties. Flexible enought to
 * deal with property emulation on {@link Map}s. Also adds a specificiation for default values, which makes
 * it able to completely describe {@link GroovyBeanContainer} properties. Default values are only used
 * when dynamically adding properties to a {@link Map} bean that does not already contain that property.
 *
 */
class GroovyObjectPropertyDescriptor implements VaadinPropertyDescriptor<Object> {

    private String name
    private Class propertyType = Object
    private Object defaultValue

    // TODO - validate defaultValue type ?

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
        initializeDefault(bean)
        return new GroovyObjectProperty(bean, name)
    }

    private void initializeDefault(GroovyObject bean) {
        // No action for default values on GroovyObject targets - the property should be there, so use it's value
    }

    private void initializeDefault(Map bean) {
        // Only initialize with the default if the property (map key) is not there
        if(!bean.containsKey(name)) {
            bean[name] = defaultValue
        }
    }
}
