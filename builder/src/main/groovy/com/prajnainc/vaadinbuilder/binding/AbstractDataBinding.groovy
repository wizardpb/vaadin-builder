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
 * <p>{@link AbstractDataBinding{ is a base class for all {@link DataBinding} classes. It decides how to bind the source and optional soure property,
 * and implement the detach listener to unbind ourselves when our target component detaches from the UI</p>
 *
 * <p>If the sourceProperty is present, the binding is assumed to be against that property on the source object. In that case, if the
 * property is {@link @Bindable}, a {@link PropertyChangeListener} is set up to update the binding when the property changes. If the source property
 * is null, then the source is treated as the (static) bound value, and is bound to the target directly</p>
 *
 */
abstract class AbstractDataBinding implements DataBinding, PropertyChangeListener {

    def source
    String sourceProperty
    def target
    List propertyDescriptors

    abstract protected void bindSourceProperty();

    abstract protected void bindSource();

    @Override
    DataBinding bind(Object target) {
        this.target = target
        return bind()
    }

    @Override
    DataBinding bind() {
        assert target != null; assert source != null

        // If there is a source property, bind it as a property on a model object ...
        if(sourceProperty) {
            if(source.metaClass.getMetaProperty(sourceProperty) == null) {
                throw new VaadinBuilderException("Source $source has no property $sourceProperty")
            }

            bindSourceProperty()
            addChangeListener()
        } else {
            // .. otherwise bind the source object directly, as a bean
            bindSource()
        }
        return this
    }

    @Override
    void unbind() {
        removeChangeListener()
    }

    public GroovyObjectPropertyDescriptor descriptorFor(String propName) {
        def pd = propertyDescriptors.find { GroovyObjectPropertyDescriptor it ->
            it.name == propName
        }
        return pd
    }

    protected addChangeListener() {
        def addListenerMethod = source.metaClass.getMetaMethod("addPropertyChangeListener",[sourceProperty,this] as Object[])
        if(addListenerMethod == null) {
            throw new VaadinBuilderException("Cannot add listener to ${source}.$sourceProperty as it is not a Bindable property")
        }
        source.addPropertyChangeListener(sourceProperty,this)
    }

    protected removeChangeListener() {
        source.removePropertyChangeListener(sourceProperty,this)
    }

}
