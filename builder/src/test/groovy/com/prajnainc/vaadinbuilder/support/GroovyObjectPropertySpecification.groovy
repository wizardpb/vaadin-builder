package com.prajnainc.vaadinbuilder.support

import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.vaadin.data.Property
import org.codehaus.groovy.runtime.typehandling.GroovyCastException
import spock.lang.Specification

/**
 * GroovyObjectPropertySpecification
 *
 *
 */
import static org.hamcrest.CoreMatchers.*
import static spock.util.matcher.HamcrestSupport.that

public class GroovyObjectPropertySpecification extends Specification {

    class TestObject {
        String prop1 = 'string'
        int prop2 = 1
        def prop3
        private notProp
        private readOnly

        def getVirtualReadOnly() { 'readOnly' }

        def getReadOnly() { readOnly }
    }

    Map propertyDescriptors

    def setup() {
        propertyDescriptors = [
                prop1          : new GroovyObjectPropertyDescriptor(name: 'prop1', propertyType: String, defaultValue: ''),
                prop2          : new GroovyObjectPropertyDescriptor(name: 'prop2', propertyType: int, defaultValue: 0),
                prop3          : new GroovyObjectPropertyDescriptor(name: 'prop3', propertyType: Object, defaultValue: null),
                virtualReadOnly: new GroovyObjectPropertyDescriptor(name: 'virtualReadOnly', propertyType: Object, defaultValue: null),
                readOnly       : new GroovyObjectPropertyDescriptor(name: 'readOnly', propertyType: Object, defaultValue: null),
        ]
    }


    def "it can read the property"() {

       given:
       def prop = new GroovyObjectProperty(new TestObject(),propertyDescriptors.prop1)

       expect:
       that prop.readOnly, is(false)
       that prop.value, equalTo('string')

    }

    def "it can write the property"() {

        given:
        def prop = new GroovyObjectProperty(new TestObject(),propertyDescriptors.prop1)
        prop.value = 'new string'

        expect:
        that prop.value, equalTo('new string')

    }

    def "it can read the type"() {
        given:
        propertyDescriptors.prop1.propertyType = null
        def prop = new GroovyObjectProperty(new TestObject(),propertyDescriptors.prop1)

        expect:
        that prop.type, equalTo(String)
    }

    def "it recognizes read-only properties"() {
        given:
        def prop1 = new GroovyObjectProperty(new TestObject(),propertyDescriptors.virtualReadOnly)
        def prop2 = new GroovyObjectProperty(new TestObject(),propertyDescriptors.readOnly)

        expect:
        that prop1.readOnly, is(true)
        that prop2.readOnly, is(true)
    }

    def "it throws an exception writing read-only"() {
        when:
        def prop = new GroovyObjectProperty(new TestObject(),propertyDescriptors.prop1,true)
        prop.value = ''

        then:
        thrown(Property.ReadOnlyException)
    }

    def "it can deal with non-existent properties"() {
        when:
        def prop = new GroovyObjectProperty(new TestObject(),new GroovyObjectPropertyDescriptor(name: 'noProp', propertyType: Object, defaultValue: null))

        then:
        thrown(VaadinBuilderException)
    }

    def "it notifies ValueChangeListeners when value is set"() {

        given:
        def prop = new GroovyObjectProperty(new TestObject(),propertyDescriptors.prop1)
        def event
        prop.addValueChangeListener([valueChange: {evt -> event = evt}] as Property.ValueChangeListener)
        prop.value = 'updated'

        expect:
        that event, instanceOf(Property.ValueChangeEvent)
        that event.property, sameInstance(prop)
    }

    def "it can read a Map entry as a property"() {

        given:
        def prop = new GroovyObjectProperty([prop1: 'prop1', prop2: 'prop2'],propertyDescriptors.prop1)

        expect:
        that prop.type, equalTo(String)
        that prop.value, equalTo('prop1')
    }

    def "it can write a Map entry as a property"() {

        given:
        def bean = [prop1: 'prop1', prop2: 'prop2']
        def prop = new GroovyObjectProperty(bean,propertyDescriptors.prop1)
        prop.value = 'new string'

        expect:
        that bean.prop1, equalTo('new string')

    }

    def "it can make a Map entry read-only"() {
        when:
        def instance = [prop1: 'prop1']
        def prop = new GroovyObjectProperty(instance,propertyDescriptors.prop1,true)
        prop.value = ''

        then:
        thrown(Property.ReadOnlyException)
        instance.prop1 == 'prop1'
    }

    def "it can detect type violations on creation"() {
        when:
        def instance = new TestObject()
        propertyDescriptors.prop1.propertyType = Integer
        def prop = new GroovyObjectProperty(instance,propertyDescriptors.prop1)

        then:
        def e = thrown(VaadinBuilderException)
        e.message == "Property prop1 of TestObject is type incompatible with class java.lang.Integer"
    }

    def "it can detect type violations on assignment"() {
        when:
        def instance = [prop1: 'prop1']
        def prop = new GroovyObjectProperty(instance,propertyDescriptors.prop1)
        prop.value = 1

        then:
        thrown(GroovyCastException)
        instance.prop1 == 'prop1'
    }
}
