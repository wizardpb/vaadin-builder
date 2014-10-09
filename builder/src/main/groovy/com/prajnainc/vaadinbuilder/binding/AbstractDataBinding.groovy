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
import com.prajnainc.vaadinbuilder.support.GroovyObjectPropertyDescriptor
import com.vaadin.server.ClientConnector

import java.beans.PropertyChangeListener

/**
 * <p>{@link AbstractDataBinding{ is a base class for all {@link DataBinding} classes. It decides how to bind the source
 * and optional soure property, and implement the detach listener to unbind ourselves when our target component detaches
 * from the UI</p>
 *
 * <p>If the sourceProperty is present, the binding is assumed to be against that property on the source object.
 * In that case, if the property is {@link @Bindable}, a {@link PropertyChangeListener} is set up to update the binding
 * when the property changes. If the source property is null, then the source is treated as the (static) bound value,
 * and is bound to the target directly</p>
 *
 */
abstract class AbstractDataBinding implements DataBinding, PropertyChangeListener {

    def source
    String sourceProperty
    def target
    List propertyDescriptors = []
    Boolean unTyped

    abstract protected void bindSourceProperty();
    abstract protected void bindSource();

    public Boolean isBindingToProperty() {
        return sourceProperty != null
    }

    /**
     * Return the type of the bound source property, or the source itself.
     *
     * @return - the Class of the source property
     */
    public Class getSourceType() {
        return isBindingToProperty() ? source.metaClass.getMetaProperty(sourceProperty)?.getType() : source.getClass()
    }

    /**
     * Get the value of the bound property
     *
     * @return - the Object value
     */
    public Object getSourceValue() {
        return isBindingToProperty() ? source.getProperty(sourceProperty) : source
    }

    @Override
    DataBinding bind(Object target) {
        this.target = target
        return bind()
    }

    @Override
    DataBinding bind() {
        assert target != null; assert source != null

        // If there is a source property, bind it as a property on a model object ...
        if(isBindingToProperty()) {
            if(getSourceType() == null) {
                throw new VaadinBuilderException("Source $source has no property $sourceProperty")
            }

            bindSourceProperty()
            addChangeListeners()
        } else {
            // .. otherwise bind the source object directly, as a bean
            bindSource()
        }
        return this
    }

    @Override
    void unbind() {
        removeChangeListeners()
    }

    /**
     * Return the descriptor for a name, or null if it is not there
     *
     * @param propName
     * @return
     */
    public GroovyObjectPropertyDescriptor descriptorFor(String propName) {
        // Why we have to use the getter here is unfathomable, but a direct field ref returns a null. Go figure.
        return getPropertyDescriptors().find { it.name == propName }
    }

    /**
     * Ensure that the descriptor for a property exists, and is compatible with an optional type
     * (defined by a field or column)
     *
     * @param propName
     * @param fieldType
     * @return
     */
    public GroovyObjectPropertyDescriptor ensureDescriptorFor(String propName, Class fieldType) {
        def descriptor = descriptorFor(propName)
        if(unTyped && descriptor == null) {
            // Add the descriptor with default type if we are untyped and it's not there
            descriptor = addDescriptor(propName, fieldType ?: Object, null)
        } else {
            // Valid descriptor should now be there - validate the type as well
            if (descriptor == null) {
                // No typed field
                throw new VaadinBuilderException("The model does not have a property '$propName'")
            }

            if (fieldType && descriptor.propertyType) {
                throw new VaadinBuilderException("The type of the model property '$propName' is multiply defined")
            }
        }

        return descriptor
    }

    protected GroovyObjectPropertyDescriptor addDescriptor(String propName, Class type, Object defaultValue) {
        // Should never be called
        assert false
    }

    /**
     * Add all necessary change listeners to keep the target updated when the source changes in any way. All bindings
     * need a listener to re-bind when the source property changes.
     *
     * We also add a detach listener to unbind the binding when the UI detaches
     */
    protected void addChangeListeners() {
        def addListenerMethod = source.metaClass.getMetaMethod(
            "addPropertyChangeListener",[sourceProperty,this] as Object[]
        )
        if(addListenerMethod == null) {
            throw new VaadinBuilderException(
                "Cannot add listener to ${source}.$sourceProperty as it is not a Bindable property"
            )
        }
        source.addPropertyChangeListener(sourceProperty,this)
        if(target instanceof ClientConnector) {
            target.addDetachListener([detach: { evt -> unbind() }] as ClientConnector.DetachListener)
        }
    }

    protected void removeChangeListeners() {
        source.removePropertyChangeListener(sourceProperty,this)
    }

}
