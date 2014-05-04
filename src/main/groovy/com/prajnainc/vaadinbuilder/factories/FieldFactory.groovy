package com.prajnainc.vaadinbuilder.factories

import com.vaadin.data.Property
import com.vaadin.data.fieldgroup.FieldGroup
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
 * a field group in the enclosing component hierarchy (an instance of {@link FieldGroup},
 * it will use that to bind the field to a {@link Property} of the appropriate type (supplied by the field group), and whose property Id is
 * the value argument passed to the builder. This Property will then be bound to a Groovy property of that same name on the model type contained in the
 * {@link FieldGroup}. See {@link FieldGroupFactory} and {@link com.prajnainc.vaadinbuilder.binding.DataBinding} for details of how {@link FieldGroup} nodes
 * can be bound to data sources
 *
 * fieldGroup nodes can be nested, with fields binding to the most locally-enclosing group.
 *
 * If there is no {@link FieldGroup}, the field will be attached to a lone {@link Property} of the type given by an explicit 'dataType' attribute, or {@link Object} if
 * that is not supplied, again with a property Id given by the nodes value argument
 *
 *
 *
 */
class FieldFactory extends ComponentFactory {

    FieldFactory(Class fieldClass) {
        super(fieldClass)
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        FieldGroup fieldGroup = findFieldGroup(builder.getCurrent())
        super.newInstance(builder, name, value, attributes)
        if(fieldGroup) {
            fieldGroup.bind(component,value)
        }
        return component
    }

    /**
     * For a field, the factory value argument is humanized and used to set the component caption
     *
     * @param value
     * @param attributes
     * @return
     */
    @Override
    protected setComponentValue(Object value, Object attributes) {
        return super.setComponentValue(DefaultFieldFactory.createCaptionByPropertyId(value), attributes)
    }

    protected FieldGroup findFieldGroup(Component currentComponent) {
        // Recurse up the parent chain until we find a field group or no parent
        if(currentComponent == null) return null
        if(currentComponent.data?.fieldGroup instanceof FieldGroup) return currentComponent.data.fieldGroup
        return findFieldGroup(currentComponent.parent)
    }
}
