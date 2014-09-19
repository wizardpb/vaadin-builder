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
package com.prajnainc.vaadinbuilder.support

import com.vaadin.data.Container
import com.vaadin.data.Item
import com.vaadin.data.Property
import com.vaadin.data.util.*
import com.vaadin.data.util.filter.SimpleStringFilter
import com.vaadin.data.util.filter.UnsupportedFilterException

/**
 * GroovyBeanContainer
 *
 * Modeled on {@link AbstractBeanContainer}, a {@link GroovyBeanContainer} allows a more flexible containment
 * of object values. As long as the contained objects can be contained in a {@link @GroovyBeanItem), they
 * can be added to this {@link Container}. The allowable set of {@link Item} properties is defined by
 * a set of {@link GroovyObjectPropertyDescriptor}s, which can be defined at container creation time, or
 * added dynamically.
 *
 */
class GroovyBeanContainer extends AbstractInMemoryContainer<Object,Object, GroovyBeanItem>
implements BeanContainer
{

    /**
     * Link property ids to their description
     */
    Map<String,GroovyObjectPropertyDescriptor> model

    Map<Object,Item> itemIdsToItem

    GroovyBeanContainer() {
        this.model = [:]
        this.itemIdsToItem = [:]
    }

    GroovyBeanContainer(List<GroovyObjectPropertyDescriptor> descriptors) {
        this()
        this.model = descriptors.collectEntries {
            [it.name, it]
        }
    }

    @Override
    protected GroovyBeanItem getUnfilteredItem(Object itemId) {
        return itemIdsToItem[itemId]
    }

    @Override
    Collection<?> getContainerPropertyIds() {
        return model.keySet()
    }

    @Override
    Property getContainerProperty(Object itemId, Object propertyId) {
        return itemIdsToItem[itemId]?.getItemProperty(propertyId)
    }

    @Override
    Class<?> getType(Object propertyId) {
        return null
    }

    @Override
    void addAll(Collection beans) {
        beans.each { addBean(it) }
    }

    @Override
    Item addBean(Object bean) {
        return internalAddItemAtEnd(bean, createItem(bean),true)
    }

    @Override
    Item addBeanAfter(Object itemId, Object bean) {
        return internalAddItemAfter(itemId,bean,createItem(bean),true)
    }

    @Override
    Item addBenAt(int index, Object bean) {
        return internalAddItemAt(index,bean,createItem(bean),true)
    }

    @Override
    protected void registerNewItem(int position, Object itemId, GroovyBeanItem item) {
        super.registerNewItem(position, itemId, item)
        itemIdsToItem[itemId] = item
    }

    @Override
    boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue) throws UnsupportedOperationException {

        if(!propertyId instanceof String) {
            throw new UnsupportedOperationException("Groovy container property ids must be Strings")
        }

        if (null == propertyId) {
            return false;
        }


        // Fails if the Property is already present
        if (model.containsKey(propertyId)) {
            return false;
        }

        def propertyDescriptor = new GroovyObjectPropertyDescriptor(
                name: propertyId,propertyType: type, defaultValue: defaultValue
        )

        model.put(propertyId, propertyDescriptor);
        itemIdsToItem.values().each {
            it.addItemProperty(propertyId, propertyDescriptor.createProperty(it.getBean()));
        }

        // Sends a change event
        fireContainerPropertySetChange();

        return true;
    }

    @Override
    boolean removeItem(Object itemId) throws UnsupportedOperationException {
        int origSize = size();
//        Item item = getItem(itemId);
        int position = indexOfId(itemId);

        def itemRemoved = internalRemoveItem(itemId)
        if (itemRemoved) {
            // detach listeners from Item
//            removeAllValueChangeListeners(item);

            // remove item
            itemIdsToItem.remove(itemId);

            // fire event only if the visible view changed, regardless of
            // whether filtered out items were removed or not
            if (size() != origSize) {
                fireItemRemoved(position, itemId);
            }
        }
        return itemRemoved
    }

    private GroovyBeanItem createItem(bean) {
        return new GroovyBeanItem(bean,model.keySet())
    }
}


