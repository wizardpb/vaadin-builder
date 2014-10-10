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

import com.vaadin.data.Item
import spock.lang.Specification

import static org.hamcrest.CoreMatchers.equalTo
import static spock.util.matcher.HamcrestSupport.that

public class GroovyBeanItemSpecification extends Specification {

    public static class GroovyBean {
        def objectProp = [value: 1]
        String stringProp = 'string'
        Integer intProp = 1

        public void setDummyProp(val) {}
        def getDummyProp() { 'dummy' }

        public boolean isDummyBool() { return false}
        public void setDummyBool(boolean bool) {}

        def getReadOnly() { 'readOnly' }
    }

    def "it can bind Groovy object"() {

       given:
       def item = new GroovyBeanItem(new GroovyBean())

       expect:
       that item.getItemPropertyIds() as Set, equalTo(['objectProp', 'stringProp', 'intProp', 'dummyProp', 'dummyBool', 'readOnly'] as Set)

    }

    def "it binds the correct types on a GroovyObject"() {
        given:
        def item = new GroovyBeanItem(new GroovyBean())

        expect:
        that item.getItemPropertyIds().collectEntries { [it, item.getItemProperty(it)?.type]}, equalTo(
                [objectProp: Object, stringProp: String, intProp: Integer, dummyProp: Object, dummyBool: boolean, readOnly: Object]
        )

    }

    def "it can access property values"() {
        expect:
        Item item = new GroovyBeanItem(new GroovyBean())
        item.getItemProperty(propName).value == propValue

        where:
        propName        | propValue
        'objectProp'    | [value: 1]
        'stringProp'    | 'string'
        'intProp'       | 1
        'dummyProp'     | 'dummy'
        'dummyBool'     | false
        'readOnly'      | 'readOnly'

    }

    def "it can set property values"() {

        expect:
        Item item = new GroovyBeanItem(new GroovyBean())
        item.getItemProperty(propName).setValue(setValue)
        item.getItemProperty(propName).value == getValue

        where:
        propName        | setValue      | getValue
        'objectProp'    | [value: 2]    | [value: 2]
        'stringProp'    | 'newString'   | 'newString'
        'intProp'       | 2             | 2
        'dummyProp'     | 'newDummy'    | 'dummy'
        'dummyBool'     | true          | false
    }

    def "it recognizes read-only properties"() {

        given:
        def item = new GroovyBeanItem(new GroovyBean())

        expect:
        item.getItemProperty('readOnly').readOnly

    }

    def "it can specify the properties to bind" () {

        given:
        def item = new GroovyBeanItem(new GroovyBean(), [
                new GroovyObjectPropertyDescriptor(name: 'objectProp', propertyType: Object),
                new GroovyObjectPropertyDescriptor(name: 'stringProp', propertyType: String)
        ])

        expect:
        that item.getItemPropertyIds() as List, equalTo(['objectProp', 'stringProp'])
        that item.getItemPropertyIds().collectEntries { [it, item.getItemProperty(it)?.type]}, equalTo(
                [objectProp: Object, stringProp: String]
        )

    }

    def "it can bind to a Map"() {
        expect:
        Item item = new GroovyBeanItem([
                objectProp:[value: 1],
                stringProp: 'string',
                intProp: 1,
        ])
        item.getItemProperty(propName).value == propValue

        where:
        propName        | propValue
        'objectProp'    | [value: 1]
        'stringProp'    | 'string'
        'intProp'       | 1
    }

    def "it binds to a Map with specific types"() {
        given:
        def mapBean = [
                objectProp:[value: 1],
                stringProp: 'string',
                intProp: 1,
        ]
        Item item = new GroovyBeanItem(mapBean, [
                new GroovyObjectPropertyDescriptor(name: 'objectProp', propertyType: Map, defaultValue: [:]),
                new GroovyObjectPropertyDescriptor(name: 'stringProp', propertyType: String, defaultValue: ''),
                new GroovyObjectPropertyDescriptor(name: 'intProp', propertyType: Integer, defaultValue: null)
        ])

        expect:
        that item.getItemPropertyIds().collectEntries { [it, item.getItemProperty(it)?.type]}, equalTo(
                [objectProp: Map, stringProp: String, intProp: Integer]
        )


    }

    def "it can detect types exceptions"() {
        // TODO - implement
    }
}
