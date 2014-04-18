package com.prajnainc.vaadinbuilder.support

import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.vaadin.data.Property

/**
 * PropertyDescriptor
 *
 *
 */

/**
 * PropertyDescriptor
 *
 * A {@link PropertyDescriptor} describes a property on a data source object such that given the instance to be bound,
 * it can create and return a {@link com.vaadin.data.Property} object binding that property on the given instance
 *
 */
class PropertyDescriptor {

    String name
    Class type
    boolean readOnly

    Property createProperty(GroovyObject instance) {
        def prop = new GroovyObjectProperty(instance,name,readOnly)
        if(prop.type != type) {
            throw new VaadinBuilderException("Incorrect property type of '$name' on $instance: wanted $type but was ${this.type}")
        }
        return prop
    }

    Property createInstance(Map instance) {
        //TBD
        null
    }
}
