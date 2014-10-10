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
 * A {@link SelectContainerBinding} implements a binding between a {@link Collection} of items to be displayed in a
 * {@link com.vaadin.ui.AbstractSelect } object. <p>
 *
 * <p>It converts a list of objects into an {@link com.vaadin.data.util.IndexedContainer} whose item IDs are the
 * objects themselves.
 *
 */
class SelectContainerBinding extends ContainerBinding {

    // TODO - make the container type variable? Limitation of IndexedContainer item id uniqueness?

    @Override
    protected void bindSourceProperty() {
        Container container = new IndexedContainer((source.getProperty(sourceProperty) ?: []) as List)
        target.setContainerDataSource(container)
    }

    @Override
    protected void bindSource() {
        Container container = new IndexedContainer((source ?: []) as List)
        target.setContainerDataSource(container)
        super.bindSource()

    }

    @Override
    void propertyChange(PropertyChangeEvent evt) {
        /**
         * Only Groovy can do multi-method dispatch, so all property changes come here, and are fed to a local
         * implementation
         */
        collectionPropertyChange(evt)
    }

    void collectionPropertyChange(PropertyChangeEvent evt) {
        if(evt.propertyName == sourceProperty) {
            bind()
            super.propertyChange(evt)
        }
    }

    private void addItem(value, int index) {
        if(index < target.containerDataSource.size()){
            target.containerDataSource.addItemAt(index, value)
        } else {
            target.containerDataSource.addItem(value)
        }
    }

    void collectionPropertyChange(ObservableList.ElementEvent evt) {
        if(evt.source == 'size') return;          // Ignore size change events
        IndexedContainer dataSource = target.containerDataSource
        switch(evt) {
            case ObservableList.ElementAddedEvent:
                addItem(evt.newValue, evt.index)
                break;
            case ObservableList.ElementUpdatedEvent:
                dataSource.removeItem(evt.oldValue)
                addItem(evt.newValue, evt.index)
                break;
            case ObservableList.ElementClearedEvent:
                dataSource.removeAllItems()
                break;
            case ObservableList.ElementRemovedEvent:
                dataSource.removeItem(evt.oldValue)
                break;
            case ObservableList.MultiElementAddedEvent:
                /**
                 * Since the indicated add index from an add and addAt operation at the end of the
                 * list is the same, it is not possible to tell what the operation was. However,
                 * we need to know this, because the operation to add the items will be different
                 * for each case.
                 *
                 * Until {@link ObservableList} is fixed, this is the only way to tell. If the operation
                 * was an 'add', the last element in the collection will be the last element in the added values
                 */
                def wasAddOp = getSourceValue().last() == evt.values.last()
                if(wasAddOp) {
                    evt.values.each{ item ->
                        target.containerDataSource.addItem(item)
                    }
                } else {
                    evt.values.eachWithIndex { item, offset ->
                        addItem(item, evt.index + offset)
                    }
                }
                break;
            case ObservableList.MultiElementRemovedEvent:
                evt.values.each { item ->
                    dataSource.removeItem(item)
                }
                break;
        }
    }

    void collectionPropertyChange(ObservableSet.ElementEvent evt) {
        Container dataSource = target.containerDataSource
        if(evt.source == 'size') return;          // Ignore size change events
        switch(evt) {
            case ObservableSet.ElementAddedEvent:
                dataSource.addItem(evt.newValue)
                break;
            case ObservableSet.ElementClearedEvent:
                dataSource.removeAllItems()
                break;
            case ObservableSet.ElementRemovedEvent:
                dataSource.removeItem(evt.oldValue)
                break;
            case ObservableSet.MultiElementAddedEvent:
                evt.values.each { bean ->
                    dataSource.addItem(bean)
                }
                break;
            case ObservableSet.MultiElementRemovedEvent:
                evt.values.each { bean ->
                    dataSource.removeItem(bean)
                }
                break;
        }
    }
}
