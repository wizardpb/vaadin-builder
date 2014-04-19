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

package com.prajnainc.vaadinbuilder.support

import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.vaadin.data.Item
import com.vaadin.data.fieldgroup.FieldGroup
import com.vaadin.ui.Field

/**
 * DynamicallyBoundFieldGroup
 *
 * A {@link DynamicallyBoundFieldGroup} is a Vaadin {@link FieldGroup} that can have it's data source dynamically bound. The object can
 * be created with a description of the properties to bind (either explicitly, or through adding a set of fields), and these are
 * dynamically bound to a data source when the source is set.
 *
 * The source can also be an unadorned {@link GroovyObject} i.e a model object not yet wrapped in an {@link Item). In this
 * case, an appropriate {@link Item} class is choosen and automatically wraps the supplied model. The class currently supports
 * regular Groovy beans, and treats {@link Map}s as a special case, a collection of keyed properties.
 *
 *
 *
 */
class DynamicallyBoundFieldGroup extends FieldGroup {

    Map<String,PropertyDescriptor>descriptors = null

    private static Map buildDescriptorsFor(Class klass) {
        return klass.metaClass.getProperties().collectEntries {
            assert it instanceof MetaBeanProperty
            [it.name, new PropertyDescriptor(name: it.name, type: it.type, readOnly: it.setter == null)]
        }
    }

    private static Item buildItem(GroovyObject dataSource) {
        return new GroovyBeanItem(dataSource)
    }

    private static Item buildItem(Map dataSource) {
        return new GroovyMapItem(dataSource)
    }

    DynamicallyBoundFieldGroup(GroovyObject bean) {
        super(new GroovyBeanItem(bean))
        descriptors = buildDescriptorsFor(bean.getClass())
    }

    DynamicallyBoundFieldGroup(Class itemType) {
        this(buildDescriptorsFor(itemType))
    }

    DynamicallyBoundFieldGroup(List descriptors) {
        this(descriptors.collectEntries { [it.name, new PropertyDescriptor(it)] })
    }

    DynamicallyBoundFieldGroup(Map descriptors) {
        this.descriptors = descriptors
    }

    @Override
    protected Class<?> getPropertyType(Object propertyId) throws BindException {
        if(itemDataSource) {
            return super.getPropertyType(propertyId)
        } else {
            def descriptor = descriptors[propertyId]
            if(!descriptor) throw new BindException("Property type for '$propertyId' could not be determined. No property with that id was found.")
            return descriptor.type
        }
    }

    public setDataSource(Object dataSource) {
        setItemDataSource(buildItem(dataSource))
    }
}
