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
 */
class GroovyBeanContainer extends AbstractInMemoryContainer<Object,Object, GroovyBeanItem> implements
        Container.Filterable, Container.SimpleFilterable, Container.Sortable, Property.ValueChangeListener,
        Container.PropertySetChangeNotifier{

    /**
     * Maps all item ids in the container (including filtered) to their
     * corresponding GroovyBeanItem.
     */
    private final Map itemIdToItem = [:]

    /**
     * The type of the beans in the container.
     */
    private final Class type;

    /**
     * A description of the properties found in beans of type {@link #type}.
     * Determines the property ids that are present in the container.
     */
    private LinkedHashMap<String,GroovyObjectPropertyDescriptor> model;

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container#getType(java.lang.Object)
     */
    @Override
    public Class<?> getType(Object propertyId) {
        return model.get(propertyId).getPropertyType();
    }

    /**
     * Returns the type of beans this Container can contain.
     *
     * This comes from the bean type constructor parameter, and bean metadata
     * (including container properties) is based on this.
     *
     * @return
     */
    public Class getBeanType() {
        return type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container#getContainerPropertyIds()
     */
    @Override
    public Collection<String> getContainerPropertyIds() {
        return model.keySet();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container#removeAllItems()
     */
    @Override
    public boolean removeAllItems() {
        int origSize = size();

        internalRemoveAllItems();

        // detach listeners from all Items
        for (Item item : itemIdToItem.values()) {
            removeAllValueChangeListeners(item);
        }
        itemIdToItem.clear();

        // fire event only if the visible view changed, regardless of whether
        // filtered out items were removed or not
        if (origSize != 0) {
            fireItemSetChange();
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container#getItem(java.lang.Object)
     */
    @Override
    public GroovyBeanItem getItem(Object itemId) {
        // TODO return only if visible?
        return getUnfilteredItem(itemId);
    }

    @Override
    protected GroovyBeanItem getUnfilteredItem(Object itemId) {
        return itemIdToItem.get(itemId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container#getItemIds()
     */
    @Override
    @SuppressWarnings("unchecked")
    public List getItemIds() {
        return (List) super.getItemIds();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container#getContainerProperty(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public Property getContainerProperty(Object itemId, Object propertyId) {
        Item item = getItem(itemId);
        if (item == null) {
            return null;
        }
        return item.getItemProperty(propertyId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container#removeItem(java.lang.Object)
     */
    @Override
    public boolean removeItem(Object itemId) {
        // TODO should also remove items that are filtered out
        int origSize = size();
        Item item = getItem(itemId);
        int position = indexOfId(itemId);

        if (internalRemoveItem(itemId)) {
            // detach listeners from Item
            removeAllValueChangeListeners(item);

            // remove item
            itemIdToItem.remove(itemId);

            // fire event only if the visible view changed, regardless of
            // whether filtered out items were removed or not
            if (size() != origSize) {
                fireItemRemoved(position, itemId);
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Re-filter the container when one of the monitored properties changes.
     */
    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        // if a property that is used in a filter is changed, refresh filtering
        filterAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vaadin.data.Container.Filterable#addContainerFilter(java.lang.Object,
     * java.lang.String, boolean, boolean)
     */
    @Override
    public void addContainerFilter(Object propertyId, String filterString,
                                   boolean ignoreCase, boolean onlyMatchPrefix) {
        try {
            addFilter(new SimpleStringFilter(propertyId, filterString,
                    ignoreCase, onlyMatchPrefix));
        } catch (UnsupportedFilterException e) {
            // the filter instance created here is always valid for in-memory
            // containers
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container.Filterable#removeAllContainerFilters()
     */
    @Override
    public void removeAllContainerFilters() {
        if (!getFilters().isEmpty()) {
            for (Item item : itemIdToItem.values()) {
                removeAllValueChangeListeners(item);
            }
            removeAllFilters();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vaadin.data.Container.Filterable#removeContainerFilters(java.lang
     * .Object)
     */
    @Override
    public void removeContainerFilters(Object propertyId) {
        Collection<Container.Filter> removedFilters = super.removeFilters(propertyId);
        if (!removedFilters.isEmpty()) {
            // stop listening to change events for the property
            for (Item item : itemIdToItem.values()) {
                removeValueChangeListener(item, propertyId);
            }
        }
    }

    @Override
    public void addContainerFilter(Container.Filter filter)
            throws UnsupportedFilterException {
        addFilter(filter);
    }

    @Override
    public void removeContainerFilter(Container.Filter filter) {
        removeFilter(filter);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.util.AbstractInMemoryContainer#hasContainerFilters()
     */
    @Override
    public boolean hasContainerFilters() {
        return super.hasContainerFilters();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.util.AbstractInMemoryContainer#getContainerFilters()
     */
    @Override
    public Collection<Container.Filter> getContainerFilters() {
        return super.getContainerFilters();
    }

    /**
     * Make this container listen to the given property provided it notifies
     * when its value changes.
     *
     * @param item
     *            The {@link Item} that contains the property
     * @param propertyId
     *            The id of the property
     */
    private void addValueChangeListener(Item item, Object propertyId) {
        Property<?> property = item.getItemProperty(propertyId);
        if (property instanceof Property.ValueChangeNotifier) {
            // avoid multiple notifications for the same property if
            // multiple filters are in use
            Property.ValueChangeNotifier notifier = (Property.ValueChangeNotifier) property;
            notifier.removeListener(this);
            notifier.addListener(this);
        }
    }

    /**
     * Remove this container as a listener for the given property.
     *
     * @param item
     *            The {@link Item} that contains the property
     * @param propertyId
     *            The id of the property
     */
    private void removeValueChangeListener(Item item, Object propertyId) {
        Property<?> property = item.getItemProperty(propertyId);
        if (property instanceof Property.ValueChangeNotifier) {
            ((Property.ValueChangeNotifier) property).removeListener(this);
        }
    }

    /**
     * Remove this contains as a listener for all the properties in the given
     * {@link Item}.
     *
     * @param item
     *            The {@link Item} that contains the properties
     */
    private void removeAllValueChangeListeners(Item item) {
        for (Object propertyId : item.getItemPropertyIds()) {
            removeValueChangeListener(item, propertyId);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container.Sortable#getSortableContainerPropertyIds()
     */
    @Override
    public Collection<?> getSortableContainerPropertyIds() {
        return getSortablePropertyIds();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container.Sortable#sort(java.lang.Object[],
     * boolean[])
     */
    @Override
    public void sort(Object[] propertyId, boolean[] ascending) {
        sortContainer(propertyId, ascending);
    }

    @Override
    public ItemSorter getItemSorter() {
        return super.getItemSorter();
    }

    @Override
    public void setItemSorter(ItemSorter itemSorter) {
        super.setItemSorter(itemSorter);
    }

    @Override
    protected void registerNewItem(int position, Object itemId,
                                   GroovyBeanItem item) {
        itemIdToItem.put(itemId, item);

        // add listeners to be able to update filtering on property
        // changes
        for (Container.Filter filter : getFilters()) {
            for (String propertyId : getContainerPropertyIds()) {
                if (filter.appliesToProperty(propertyId)) {
                    // addValueChangeListener avoids adding duplicates
                    addValueChangeListener(item, propertyId);
                }
            }
        }
    }

    /**
     * Check that a bean can be added to the container (is of the correct type
     * for the container).
     *
     * @param bean
     * @return
     */
    private boolean validateBean(GroovyObject bean) {
        return bean != null && getBeanType().isAssignableFrom(bean.getClass());
    }

    /**
     * Adds the bean to the Container.
     *
     * Note: the behavior of this method changed in Vaadin 6.6 - now items are
     * added at the very end of the unfiltered container and not after the last
     * visible item if filtering is used.
     *
     * @see com.vaadin.data.Container#addItem(Object)
     */
    protected GroovyBeanItem addItem(Object itemId, GroovyObject bean) {
        if (!validateBean(bean)) {
            return null;
        }
        return internalAddItemAtEnd(itemId, createBeanItem(bean), true);
    }

    /**
     * Adds the bean after the given bean.
     *
     * @see com.vaadin.data.Container.Ordered#addItemAfter(Object, Object)
     */
    protected GroovyBeanItem addItemAfter(Object previousItemId,
                                              Object newItemId, GroovyObject bean) {
        if (!validateBean(bean)) {
            return null;
        }
        return internalAddItemAfter(previousItemId, newItemId,
                createBeanItem(bean), true);
    }

    /**
     * Adds a new bean at the given index.
     *
     * The bean is used both as the item contents and as the item identifier.
     *
     * @param index
     *            Index at which the bean should be added.
     * @param newItemId
     *            The item id for the bean to add to the container.
     * @param bean
     *            The bean to add to the container.
     *
     * @return Returns the new GroovyBeanItem or null if the operation fails.
     */
    protected GroovyBeanItem addItemAt(int index, Object newItemId,
                                           GroovyObject bean) {
        if (!validateBean(bean)) {
            return null;
        }
        return internalAddItemAt(index, newItemId, createBeanItem(bean), true);
    }

    /**
     * Adds a bean to the container using the bean item id resolver to find its
     * identifier.
     *
     * A bean id resolver must be set before calling this method.
     *
     * @see #addItem(Object, GroovyObject)
     *
     * @param bean
     *            the bean to add
     * @return GroovyBeanItem item added or null
     * @throws IllegalStateException
     *             if no bean identifier resolver has been set
     * @throws IllegalArgumentException
     *             if an identifier cannot be resolved for the bean
     */
    protected GroovyBeanItem addBean(GroovyObject bean)
            throws IllegalStateException, IllegalArgumentException {
        if (bean == null) {
            return null;
        }
        Object itemId = resolveBeanId(bean);
        if (itemId == null) {
            throw new IllegalArgumentException(
                    "Resolved identifier for a bean must not be null");
        }
        return addItem(itemId, bean);
    }

    /**
     * Adds a bean to the container after a specified item identifier, using the
     * bean item id resolver to find its identifier.
     *
     * A bean id resolver must be set before calling this method.
     *
     * @see #addItemAfter(Object, Object, GroovyObject)
     *
     * @param previousItemId
     *            the identifier of the bean after which this bean should be
     *            added, null to add to the beginning
     * @param bean
     *            the bean to add
     * @return GroovyBeanItem item added or null
     * @throws IllegalStateException
     *             if no bean identifier resolver has been set
     * @throws IllegalArgumentException
     *             if an identifier cannot be resolved for the bean
     */
    protected GroovyBeanItem addBeanAfter(Object previousItemId,
                                              GroovyObject bean) throws IllegalStateException,
            IllegalArgumentException {
        if (bean == null) {
            return null;
        }
        Object itemId = resolveBeanId(bean);
        if (itemId == null) {
            throw new IllegalArgumentException(
                    "Resolved identifier for a bean must not be null");
        }
        return addItemAfter(previousItemId, itemId, bean);
    }

    /**
     * Adds a bean at a specified (filtered view) position in the container
     * using the bean item id resolver to find its identifier.
     *
     * A bean id resolver must be set before calling this method.
     *
     * @see #addItemAfter(Object, Object, GroovyObject)
     *
     * @param index
     *            the index (in the filtered view) at which to add the item
     * @param bean
     *            the bean to add
     * @return GroovyBeanItem item added or null
     * @throws IllegalStateException
     *             if no bean identifier resolver has been set
     * @throws IllegalArgumentException
     *             if an identifier cannot be resolved for the bean
     */
    protected GroovyBeanItem addBeanAt(int index, GroovyObject bean)
            throws IllegalStateException, IllegalArgumentException {
        if (bean == null) {
            return null;
        }
        Object itemId = resolveBeanId(bean);
        if (itemId == null) {
            throw new IllegalArgumentException(
                    "Resolved identifier for a bean must not be null");
        }
        return addItemAt(index, itemId, bean);
    }

    /**
     * Adds all the beans from a {@link Collection} in one operation using the
     * bean item identifier resolver. More efficient than adding them one by
     * one.
     *
     * A bean id resolver must be set before calling this method.
     *
     * Note: the behavior of this method changed in Vaadin 6.6 - now items are
     * added at the very end of the unfiltered container and not after the last
     * visible item if filtering is used.
     *
     * @param collection
     *            The collection of beans to add. Must not be null.
     * @throws IllegalStateException
     *             if no bean identifier resolver has been set
     * @throws IllegalArgumentException
     *             if the resolver returns a null itemId for one of the beans in
     *             the collection
     */
    protected void addAll(Collection<? extends GroovyObject> collection)
            throws IllegalStateException, IllegalArgumentException {
        boolean modified = false;
        for (GroovyObject bean : collection) {
            // TODO skipping invalid beans - should not allow them in javadoc?
            if (bean == null
                    || !getBeanType().isAssignableFrom(bean.getClass())) {
                continue;
            }
            Object itemId = resolveBeanId(bean);
            if (itemId == null) {
                throw new IllegalArgumentException(
                        "Resolved identifier for a bean must not be null");
            }

            if (internalAddItemAtEnd(itemId, createBeanItem(bean), false) != null) {
                modified = true;
            }
        }

        if (modified) {
            // Filter the contents when all items have been added
            if (isFiltered()) {
                filterAll();
            } else {
                fireItemSetChange();
            }
        }
    }

    /**
     * Use the bean resolver to get the identifier for a bean.
     *
     * @param bean
     * @return resolved bean identifier, null if could not be resolved
     * @throws IllegalStateException
     *             if no bean resolver is set
     */
    protected Object resolveBeanId(GroovyObject bean) {
        if (beanIdResolver == null) {
            throw new IllegalStateException(
                    "Bean item identifier resolver is required.");
        }
        return beanIdResolver.getIdForBean(bean);
    }

    /**
     * @deprecated As of 7.0, replaced by {@link #addPropertySetChangeListener}
     **/
    @Deprecated
    @Override
    public void addListener(Container.PropertySetChangeListener listener) {
        addPropertySetChangeListener(listener);
    }

    @Override
    public void addPropertySetChangeListener(
            Container.PropertySetChangeListener listener) {
        super.addPropertySetChangeListener(listener);
    }

    /**
     * @deprecated As of 7.0, replaced by
     *             {@link #removePropertySetChangeListener(com.vaadin.data.Container.PropertySetChangeListener)}
     **/
    @Deprecated
    @Override
    public void removeListener(Container.PropertySetChangeListener listener) {
        removePropertySetChangeListener(listener);
    }

    @Override
    public void removePropertySetChangeListener(
            Container.PropertySetChangeListener listener) {
        super.removePropertySetChangeListener(listener);
    }

    @Override
    public boolean addContainerProperty(Object propertyId, Class<?> type,
                                        Object defaultValue) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "Use addNestedContainerProperty(String) to add container properties to a "
                        + getClass().getSimpleName());
    }

    /**
     * Adds a property for the container and all its items.
     *
     * Primarily for internal use, may change in future versions.
     *
     * @param propertyId
     * @param propertyDescriptor
     * @return true if the property was added
     */
    protected final boolean addContainerProperty(String propertyId,
                                                 VaadinPropertyDescriptor<GroovyObject> propertyDescriptor) {
        if (null == propertyId || null == propertyDescriptor) {
            return false;
        }

        // Fails if the Property is already present
        if (model.containsKey(propertyId)) {
            return false;
        }

        model.put(propertyId, propertyDescriptor);
        for (GroovyBeanItem item : itemIdToItem.values()) {
            item.addItemProperty(propertyId,
                    propertyDescriptor.createProperty(item.getBean()));
        }

        // Sends a change event
        fireContainerPropertySetChange();

        return true;
    }

    /**
     * Adds a nested container property for the container, e.g.
     * "manager.address.street".
     *
     * All intermediate getters must exist and should return non-null values
     * when the property value is accessed. If an intermediate getter returns
     * null, a null value will be returned.
     *
     * @see com.vaadin.data.util.NestedMethodProperty
     *
     * @param propertyId
     * @return true if the property was added
     */
    public boolean addNestedContainerProperty(String propertyId) {
        return addContainerProperty(propertyId, new NestedPropertyDescriptor(
                propertyId, type));
    }

    /**
     * Adds a nested container properties for all sub-properties of a named
     * property to the container. The named property itself is removed from the
     * model as its subproperties are added.
     *
     * All intermediate getters must exist and should return non-null values
     * when the property value is accessed. If an intermediate getter returns
     * null, a null value will be returned.
     *
     * @see com.vaadin.data.util.NestedMethodProperty
     * @see #addNestedContainerProperty(String)
     *
     * @param propertyId
     */
    @SuppressWarnings("unchecked")
    public void addNestedContainerBean(String propertyId) {
        Class<?> propertyType = getType(propertyId);
        LinkedHashMap<String, VaadinPropertyDescriptor<Object>> pds = GroovyBeanItem
                .getPropertyDescriptors((Class<Object>) propertyType);
        for (String subPropertyId : pds.keySet()) {
            String qualifiedPropertyId = propertyId + "." + subPropertyId;
            NestedPropertyDescriptor<GroovyObject> pd = new NestedPropertyDescriptor<GroovyObject>(
                    qualifiedPropertyId, (Class<GroovyObject>) type);
            model.put(qualifiedPropertyId, pd);
            model.remove(propertyId);
            for (GroovyBeanItem item : itemIdToItem.values()) {
                item.addItemProperty(propertyId,
                        pd.createProperty(item.getBean()));
                item.removeItemProperty(propertyId);
            }
        }

        // Sends a change event
        fireContainerPropertySetChange();
    }

    @Override
    public boolean removeContainerProperty(Object propertyId)
            throws UnsupportedOperationException {
        // Fails if the Property is not present
        if (!model.containsKey(propertyId)) {
            return false;
        }

        // Removes the Property to Property list and types
        model.remove(propertyId);

        // If remove the Property from all Items
        for (final Iterator i = getAllItemIds().iterator(); i.hasNext();) {
            getUnfilteredItem(i.next()).removeItemProperty(propertyId);
        }

        // Sends a change event
        fireContainerPropertySetChange();

        return true;
    }

}


