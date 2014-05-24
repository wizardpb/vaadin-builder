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
import com.vaadin.server.ClientConnector

import java.beans.PropertyChangeListener

/**
 * AbstractDataBinding is a base class for all {@link DataBinding} classes. It implement the detach listener to unbind ourselves when our target
 * component detaches from the UI
 *
 */
abstract class AbstractDataBinding implements DataBinding, PropertyChangeListener {

    def source
    String sourceProperty
    def target

    @Override
    void detach(ClientConnector.DetachEvent event) {
        unbind()
    }

    @Override
    DataBinding bind(Object target) {
        this.target = target
        return bind()
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
