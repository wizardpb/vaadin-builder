package com.prajnainc.vaadinbuilder.factories

import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.vaadin.client.ui.Field
import com.vaadin.data.fieldgroup.FieldGroup
import com.vaadin.data.util.ObjectProperty
import com.vaadin.data.Property
import com.vaadin.ui.AbstractField
import com.vaadin.ui.Component
import com.vaadin.ui.DefaultFieldFactory

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
 * A {@link FieldFactory} builds fields of explicit types, given by the node name that was used to invoke the builder. If there is
 * a field group in the enclosing component hierarchy (an instance of {@link com.prajnainc.vaadinbuilder.support.DynamicallyBoundFieldGroup},
 * it will use that to bind the field to a {@link Property} supplied by the field group of the appropriate type.
 *
 * If not, the field will be bound to a lone {@link Property} of the type given by an explicit 'dataType' attribute, or {@link Object} if
 * that is not supplied
 *
 */
class FieldFactory extends ComponentFactory {

    FieldFactory(Class fieldClass) {
        super(fieldClass)
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
//        FieldGroup fieldGroup = findFieldGroup(builder.getCurrent())
//        def fieldType = attributes.remove('fieldType')
        def dataType = attributes.remove('dataType') ?: Object
//
//        component = componentClass.newInstance()
//        return component

        super.newInstance(builder,name,value, attributes)
        AbstractField field = component
        field.caption = DefaultFieldFactory.createCaptionByPropertyId(value)
        field.setPropertyDataSource(new ObjectProperty(null,dataType))
        return field
    }

    protected FieldGroup findFieldGroup(Component currentComponent) {
        // Recurse up the parent chain until we find a field group or no parent
        if(currentComponent == null) return null
        if(currentComponent.data instanceof FieldGroup) return currentComponent.data
        return findFieldGroup(currentComponent.parent)
    }
}
