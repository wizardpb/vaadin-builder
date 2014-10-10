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

import spock.lang.Specification

import static org.hamcrest.CoreMatchers.*
import static spock.util.matcher.HamcrestSupport.that

public class GroovyObjectPropertyDescriptorSpecification extends Specification {

    def "it should be creatable with the Map constructor"() {

        given:
        def descriptor = new GroovyObjectPropertyDescriptor(name: 'prop', propertyType: Object, defaultValue: '')

        expect:
        that descriptor.name, equalTo('prop')
        that descriptor.propertyType, sameInstance(Object)
        that descriptor.defaultValue, equalTo('')
    }

    def "it should be creatable with a partial Map constructor name only"() {

        given:
        def descriptor = new GroovyObjectPropertyDescriptor(name: 'prop')

        expect:
        that descriptor.name, equalTo('prop')
        that descriptor.propertyType, sameInstance(Object)
        that descriptor.defaultValue, equalTo(null)
    }

    def "it should be creatable with a partial Map constructor name and type"() {

        given:
        def descriptor = new GroovyObjectPropertyDescriptor(name: 'prop', propertyType: String)

        expect:
        that descriptor.name, equalTo('prop')
        that descriptor.propertyType, sameInstance(String)
        that descriptor.defaultValue, equalTo(null)
    }

    def "it should create GroovyObjectProperties"() {

        given:
        def descriptor = new GroovyObjectPropertyDescriptor(name: 'prop', propertyType: Object, defaultValue: '')
        def prop = descriptor.createProperty([prop: 'value'])

        expect:
        that prop, instanceOf(GroovyObjectProperty)
        that prop.value, equalTo('value')
    }

    def "it should initialize default properties in Maps without the property"() {
        given:
        def descriptor = new GroovyObjectPropertyDescriptor(name: 'prop', propertyType: Object, defaultValue: '')
        def bean = [:]
        def prop = descriptor.createProperty(bean)

        expect:
        that prop.value, equalTo('')
        that bean, equalTo([prop: ''])

    }

    def "it should not initialize default properties in Maps with the property"() {
        given:
        def descriptor = new GroovyObjectPropertyDescriptor(name: 'prop', propertyType: Object, defaultValue: '')
        def bean = [prop: 'value']
        def prop = descriptor.createProperty(bean)

        expect:
        that prop.value, equalTo('value')
        that bean, equalTo([prop: 'value'])
    }

    static class TestObject {
        def prop = 'objProp'
    }

    def "it should not initialize default properties on GroovyObjects"() {
        given:
        def descriptor = new GroovyObjectPropertyDescriptor(name: 'prop', propertyType: Object, defaultValue: '')
        def bean = new TestObject()
        def prop = descriptor.createProperty(bean)

        expect:
        that prop.value, equalTo('objProp')
    }
}
