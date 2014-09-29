package com.prajnainc.vaadinbuilder.support

import com.vaadin.data.Item

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
 * A {@link BeanContainer} allows adding of {@link com.vaadin.data.Item}s that wrap a Java Bean-like object. The base
 * The Vaadin {@link com.vaadin.data.Container} interface defines methods that prevents the Item
 * creation outside of the Container itself. This is insufficient for containing bean instances, so this
 * interface adds similar methods to allow this to happen.
 *
 * The interface allows arbitrary objects to be used ad the Item ids, and can also
 * default to using the objects themselves, similar to a Vaadin {@link com.vaadin.data.util.BeanItemContainer}
 *
 * While this duplicates much of the functionality of {@link com.vaadin.data.util.AbstractBeanContainer}, the
 * new implementation takes advantage of much of Groovy goodness, especially in the
 * area of property access, such as transparency allowing Maps to be used as beans with a
 * dynamically varying property set
 *
 */
public interface BeanContainer {

    /**
     * Add all these beans to the container, using the bean as the Item id. The beans
     * will be added to the end of the {@link com.vaadin.data.Container}
     *
     * @param beans
     */
    public void addAll(Collection beans);

    /**
     * Add an object to the Container. The object itself will be used as the item Id
     *
     * The object does not need to have any of the
     * container properties, but an exception will result if any of the
     * missing properties are accessed
     *
     * @param bean - the object to wrap
     *
     * @return - the {@link Item} wrapping the object
     */
    public Item addBean(Object bean);

    /**
     * Add an item after a given item id, as for {@link BeanContainer#addBean(java.lang.Object)} . This implies the implementing {@ink Container}
     * implements {@link com.vaadin.data.Container.Ordered}, and it should throw an {@link UnsupportedOperationException} if it does not.
     *
     * @param itemId - the id object
     * @param bean - the object to wrap
     *
     * @return - the {@link Item} wrapping the object
     */
    public Item addBeanAfter(Object itemId, Object bean);

    /**
     * Add an {@link Item} for the object at a given index. This implies the implementing {@ink Container}
     * implements {@link com.vaadin.data.Container.Indexed}, and it should throw an {@link UnsupportedOperationException} if it does not.
     *
     * @param index - item index
     * @param bean - the object to wrap
     *
     * @return - the {@link Item} wrapping the object
     */
    public Item addBeanAt(int index, Object bean);

}