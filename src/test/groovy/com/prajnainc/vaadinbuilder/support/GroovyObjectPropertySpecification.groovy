package com.prajnainc.vaadinbuilder.support

import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.vaadin.data.Property

/**
 * GroovyObjectPropertySpecification
 *
 *
 */
import spock.lang.Specification

import static org.hamcrest.CoreMatchers.instanceOf
import static org.hamcrest.CoreMatchers.sameInstance
import static spock.util.matcher.HamcrestSupport.that

public class GroovyObjectPropertySpecification extends Specification {

    class TestObject {
        String prop1 = 'string'
        int prop2 = 1
        private notProp
        private readOnly

        def getVirtualReadOnly() { 'readOnly' }

        def getReadOnly() { readOnly }
    }

    def "it can read the property"() {

       given:
       def prop = new GroovyObjectProperty(new TestObject(),'prop1')

       expect:
       !prop.readOnly
        prop.value == 'string'

    }

    def "it can write the property"() {

        given:
        def prop = new GroovyObjectProperty(new TestObject(),'prop1')
        prop.value = 'new string'

        expect:
        prop.value == 'new string'

    }

    def "it can read the type"() {
        given:
        def prop = new GroovyObjectProperty(new TestObject(),'prop1')

        expect:
        prop.type == String
    }

    def "it recognizes read-only properties"() {
        given:
        def prop1 = new GroovyObjectProperty(new TestObject(),'virtualReadOnly')
        def prop2 = new GroovyObjectProperty(new TestObject(),'readOnly')

        expect:
        prop1.readOnly

        and:
        prop2.readOnly
    }

    def "it throws an exception writing read-only"() {
        when:
        def prop = new GroovyObjectProperty(new TestObject(),'prop1',true)
        prop.value = ''

        then:
        thrown(Property.ReadOnlyException)
    }

    def "it can deal with non-existent properties"() {
        when:
        def prop = new GroovyObjectProperty(new TestObject(),'noProp')

        then:
        thrown(VaadinBuilderException)
    }

    def "it notifies ValueChangeListeners when value is set"() {

        given:
        def prop = new GroovyObjectProperty(new TestObject(),'prop1')
        def event
        prop.addValueChangeListener([valueChange: {evt -> event = evt}] as Property.ValueChangeListener)
        prop.value = 'updated'

        expect:
        that event, instanceOf(Property.ValueChangeEvent)
        that event.property, sameInstance(prop)
    }

}
