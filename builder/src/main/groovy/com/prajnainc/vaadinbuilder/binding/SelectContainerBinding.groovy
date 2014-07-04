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
 */

package com.prajnainc.vaadinbuilder.binding

import com.vaadin.data.Container
import com.vaadin.data.util.IndexedContainer

import java.beans.PropertyChangeEvent;
/**
 * A {@link SelectContainerBinding} implements a binding between a {@link Collection} of items to be displayed in a {@link com.vaadin.ui.AbstractSelect }
 * object.
 * <p>
 * It converts a list of objects into an {@link com.vaadin.data.util.IndexedContainer} whose item IDs are the objects themselves.
 *
 */
class SelectContainerBinding extends AbstractDataBinding {

    @Override
    protected void bindSourceProperty() {
        Container container = new IndexedContainer((source.getProperty(sourceProperty) ?: []) as List)
        target.setContainerDataSource(container)
    }

    @Override
    protected void bindSource() {
        Container container = new IndexedContainer((source ?: []) as List)
        target.setContainerDataSource(container)
    }

    @Override
    void propertyChange(PropertyChangeEvent evt) {
        if(evt.propertyName == sourceProperty) {
            bind()
        }
    }
}
