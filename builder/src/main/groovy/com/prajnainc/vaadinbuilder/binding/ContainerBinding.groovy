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

import java.beans.PropertyChangeEvent

/**
 * A {@link ContainerBinding} binds a data source for a {@link com.vaadin.data.Container.Viewer}. It treats the
 * source value as a {@link Collection} of values that are treated as objects to be bound as  {@link com.vaadin.data
 * .Item}s, and wraps them in an appropriate {@link com.vaadin.data.Container}
 *
 */
abstract class ContainerBinding extends AbstractDataBinding {


    @Override
    protected void bindSource() {
        addCollectionListener(sourceValue)
    }

    /**
     * Add collection change listeners if the source is a collection
     *
     */
    @Override
    protected void addChangeListeners() {
        super.addChangeListeners()
        if(isBindingToProperty() && (isObservable(sourceType) || isObservable(sourceValue.getClass()))) {
            addCollectionListener(sourceValue)
        }
    }

    protected void addCollectionListener(Collection observable) {
        if(isObservable(observable.getClass())) {
            observable.addPropertyChangeListener(this)
        }
    }

    protected void removeCollectionListener(Collection observable) {
        if(isObservable(observable.getClass())) {
            observable.addPropertyChangeListener(this)
        }
    }

    protected Boolean isObservable(Class cls) {
        return cls in [ObservableList, ObservableSet]
    }
    
    @Override
    void propertyChange(PropertyChangeEvent evt) {
        // The source value has changed - remove old listener, they will have been already added by the new bind
        removeCollectionListener(evt.oldValue)
    }

}
