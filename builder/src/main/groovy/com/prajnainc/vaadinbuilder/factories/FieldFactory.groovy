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
package com.prajnainc.vaadinbuilder.factories

import com.prajnainc.vaadinbuilder.VaadinBuilder
import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.prajnainc.vaadinbuilder.binding.ItemBinding
import com.prajnainc.vaadinbuilder.support.GroovyObjectPropertyDescriptor
import com.vaadin.data.Property
import com.vaadin.data.fieldgroup.FieldGroup
import com.vaadin.ui.AbstractComponent
import com.vaadin.ui.AbstractField
import com.vaadin.ui.Component
import com.vaadin.ui.DefaultFieldFactory

/**
 * <p>A {@link FieldFactory} builds fields of explicit types, given by the node name that was used to invoke the
 * builder. If there is a field group in the enclosing component hierarchy (an instance of {@link FieldGroup},
 * it will use that to bind the field to a {@link Property} of the appropriate type (supplied by the field group),
 * and whose property Id is the value argument passed to the builder. This Property will then be bound to a Groovy
 * property of that same name on the model type contained in the {@link FieldGroup}. See {@link FieldGroupFactory}
 * and {@link com.prajnainc.vaadinbuilder.binding.DataBinding} for details of how {@link FieldGroup} nodes
 * can be bound to data sources</p>
 *
 * <p>fieldGroup nodes can be nested, with fields binding to the most locally-enclosing group.</p>
 *
 * <p>If there is no {@link FieldGroup}, the field will be attached to a lone {@link Property} of the type given by
 * an explicit 'dataType' attribute, or {@link Object} if that is not supplied, again with a property Id given by the
 * nodes value argument</p>
 *
 *
 *
 */

/*
 * TODO - validation
 */
class FieldFactory extends ComponentFactory {

    FieldFactory(Class fieldClass) {
        super(fieldClass)
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
        throws InstantiationException, IllegalAccessException
    {

        AbstractComponent container = findFieldGroupContainer(builder.getCurrent())
        FieldGroup fieldGroup = container?.data?.fieldGroup

        if(fieldGroup && !value) {
            throw new VaadinBuilderException("Fields of a field group require a property id")
        }

        AbstractField component = super.newInstance(builder, name, value, attributes)

        // Save propName, modelType, fieldGroup and binding for later
        def modelType = builder.savedAttributes[VaadinBuilder.MODEL_TYPE_ATTR]

        if(modelType && !(modelType instanceof Class)) {
            throw new VaadinBuilderException("Model type must be a class")
        }

        component.data = [
                propName: value,
                modelType: modelType,
                fieldGroup: fieldGroup, binding: container?.data?.binding
        ]

        return component
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        super.setParent(builder, parent, child)

        String propName = child.data?.propName
        FieldGroup fieldGroup = child.data?.fieldGroup

        if(fieldGroup) {
            assert propName as boolean
            ItemBinding binding = child.data.binding

            def modelType = child.data?.modelType
            if(binding) {
                // Get the model type from the attributes, and ensure the descriptor exists. This throws an exception
                // if the types are in conflict
                GroovyObjectPropertyDescriptor descriptor = binding.ensureDescriptorFor(propName, modelType )
                modelType = descriptor.propertyType
            } else {
                // Extract any type info from the data source. If it's not there, the bind will fail
                modelType = fieldGroup.itemDataSource?.getItemProperty(propName)?.type
            }


            if(!child.converter && modelType && !(modelType in [String, Object])) {
                // We need a converter - look for a default
                child.setConverter(modelType)
            }

            fieldGroup.bind(child, propName)
        }
    }
/**
     * For a field, the factory value argument is humanized and used to set the component caption
     *
     * @param value
     * @param attributes
     * @return
     */
    @Override
    protected setComponentValue(Component component, value, attributes) {
        return super.setComponentValue(component,
            value != null ? DefaultFieldFactory.createCaptionByPropertyId(value) : value, attributes
        )
    }

    protected AbstractComponent findFieldGroupContainer(Component currentComponent) {
        // Recurse up the parent chain until we find a field group or no parent
        if(currentComponent == null) return null
        if(currentComponent.data?.fieldGroup instanceof FieldGroup) return currentComponent
        return findFieldGroupContainer(currentComponent.parent)
    }
}
