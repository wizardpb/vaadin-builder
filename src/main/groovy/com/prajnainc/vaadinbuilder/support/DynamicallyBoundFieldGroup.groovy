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

    final static Set EXCLUDED_PROPERTIES = ['class'] as Set

    Map<String,PropertyDescriptor>descriptors = null

    private static Map buildDescriptorsFor(Class klass) {
        return klass.metaClass.getProperties()
                .grep { !EXCLUDED_PROPERTIES.contains(it.name) }
                .collectEntries {
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

    /**
     * Create a {@link DynamicallyBoundFieldGroup} for a given bean. The field descriptors are build from
     * properties on the objects class. Data bound to the form is wrapped in an Item with those property ids
     *
     * @param bean - a {@link GroovyObject} that will be bound to the {@link FieldGroup}
     */
    DynamicallyBoundFieldGroup(GroovyObject bean) {
        super(new GroovyBeanItem(bean))
        descriptors = buildDescriptorsFor(bean.getClass())
    }

    /**
     * Create a {@link DynamicallyBoundFieldGroup} for a given bean class. The field descriptors are build from
     * properties defined by the class. Data bound to the form is wrapped in an Item with those property ids
     * *
     * @param itemType - a {@link Class} giving the type that will be bound to the {@link FieldGroup}
     */
    DynamicallyBoundFieldGroup(Class itemType) {
        this(buildDescriptorsFor(itemType))
    }

    /**
     * Create a {@link DynamicallyBoundFieldGroup} with a literal property set description. The description is
     * a {@link List} of [@link Map}s of the form:
     * <code>
     *     [
     *          [name: <propName>, type: <propClass>, readOnly: <boolean>]
     *     ]
     * </code>
     * e.g.:
     * <code>
     *     [
     *          [name: 'someProp', type: Object, readOnly: false]
     *     ]
     * </code>     *
     *
     * @param descriptors - a {@link List} of in the form above
     */
    DynamicallyBoundFieldGroup(List descriptors) {
        this(descriptors.collectEntries {
            [it.name, new PropertyDescriptor(it)]
        })
    }

    /**
     * Create a {@link DynamicallyBoundFieldGroup} from an explicit {@link List} of {@link PropertyDescriptor}s
     *
     * @param descriptors - the descriptors
     */
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
