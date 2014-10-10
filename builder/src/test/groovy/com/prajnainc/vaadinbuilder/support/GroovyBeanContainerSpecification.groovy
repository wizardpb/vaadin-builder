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

import com.vaadin.data.Item
import spock.lang.Specification

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.assertThat
import static spock.util.matcher.HamcrestSupport.that

public class GroovyBeanContainerSpecification extends Specification {

    public static class TestBean {
        String prop1
        int prop2
        Object prop3
    }

    def descriptors = [
            new GroovyObjectPropertyDescriptor(name: 'prop1', propertyType: String),
            new GroovyObjectPropertyDescriptor(name: 'prop2', propertyType: int),
            new GroovyObjectPropertyDescriptor(name: 'prop3', propertyType: Object)
    ]

    def container

    def setup() {
        container = new GroovyBeanContainer(descriptors)
    }

    def "it can be created with no properties"() {
        given:
        def container = new GroovyBeanContainer()

        expect:
        that container.containerPropertyIds, equalTo([] as Set)
    }

    def "it can be created with a list of property descriptors"() {

        given:
        def container = new GroovyBeanContainer(descriptors)

        expect:
        that container.containerPropertyIds, equalTo(['prop1', 'prop2', 'prop3'] as Set)
    }

    def "it can add an object as an item"() {

        given:
        def bean = new TestBean(prop1: 'prop1', prop2: 2, prop3: null)
        Item item = container.addBean(bean)

        expect:
        that container.size(), equalTo(1)
        that container.getItem(bean), sameInstance(item)
        that item.getItemProperty('prop1').value, equalTo('prop1')
        that item.getItemProperty('prop2').value, equalTo(2)
        that item.getItemProperty('prop3').value, nullValue()
    }

    def "it can add a collection of objects"() {
        given:
        def beans = (1..4).collect {
            new TestBean(prop1: 'prop'+it, prop2: it, prop3: [val: it])
        }
        container.addAll(beans)

        expect:
        that container.size(), equalTo(beans.size())
        (1..container.size()).each {
            def item = container.getItem(beans[it-1])
            assertThat item.getItemProperty('prop1').value, equalTo('prop'+it)
            assertThat item.getItemProperty('prop2').value, equalTo(it)
            assertThat item.getItemProperty('prop3').value, equalTo([val: it])
        }
    }

    def "it can remove items"() {

        given:
        def beans = (1..4).collect {
            new TestBean(prop1: 'prop'+it, prop2: it, prop3: [val: it])
        }
        container.addAll(beans)
        def result = container.removeItem(beans[2])

        expect:
        that result, is(true)
        that container.size(), is(3)
        that container.getItem(beans[2]), nullValue()
        that container.getItemIds(), is([0, 1, 3].collect {beans[it]})
    }

    def "it can add container properties"() {
        def bean = new TestBean(prop1: 'prop1Value')
        given:
        container = new GroovyBeanContainer()
        container.addBean(bean)
        container.addContainerProperty('prop1', String, null)

        expect:
        that container.getContainerPropertyIds() as List, is(['prop1'])
        that container.getContainerProperty(bean, 'prop1').value, is('prop1Value')
    }
}
