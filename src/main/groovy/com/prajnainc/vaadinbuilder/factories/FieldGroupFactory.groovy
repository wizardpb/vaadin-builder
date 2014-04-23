package com.prajnainc.vaadinbuilder.factories

import com.prajnainc.vaadinbuilder.VaadinBuilder
import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.prajnainc.vaadinbuilder.support.DynamicallyBoundFieldGroup

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

/**
 * FieldGroupFactory
 *
 */
class FieldGroupFactory extends AbstractFactory implements VaadinFactory {

    @Override
    Map getSavedAttributes() {
        return [:]
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if(value) {
            // Stuff the name into the attributes so the fieldGroup gets id'ed
            attributes[VaadinBuilder.DEFAULT_DELEGATE_PROPERTY_OBJECT_ID] = value
        } else {
            value = attributes[VaadinBuilder.DEFAULT_DELEGATE_PROPERTY_OBJECT_ID]
        }

        def constructorAttribute = builder.checkForOneOf(['forClass','bind','forProperties','withDescriptors'],attributes)
        return new DynamicallyBoundFieldGroup(attributes.remove(constructorAttribute))
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        parent.setData(child)
    }
}
