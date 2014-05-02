package com.prajnainc.vaadinbuilder.support

import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.prajnainc.vaadinbuilder.binding.ItemBinding
import com.vaadin.data.fieldgroup.FieldGroup
import groovy.beans.Bindable;/*
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

import spock.lang.*
import static org.hamcrest.CoreMatchers.*
import static spock.util.matcher.HamcrestSupport.*

public class ItemBindingSpecification extends Specification {

    static class Model {
        @Bindable def modelProp
    }

    static class TestBean {
        String prop1 = 'prop1'
        String prop2 = 'prop2'
    }

    def "it should bind a Groovy bean"() {

        given:
        def model = new Model(modelProp: new TestBean())
        def FieldGroup target = new FieldGroup()
        new ItemBinding(source: model, sourceProperty: 'modelProp').bind(target)

        expect:
        that target.itemDataSource, instanceOf(GroovyBeanItem)
        that target.itemDataSource.itemPropertyIds as Set, equalTo(['prop1','prop2'] as Set)
        ['prop1','prop2'].every { target.itemDataSource.getItemProperty(it).value == it }
    }

    def "it should bind a Groovy map"() {

        given:
        def model = new Model(modelProp: [prop1: 'prop1', prop2: 'prop2'])
        def FieldGroup target = new FieldGroup()
        new ItemBinding(source: model, sourceProperty: 'modelProp').bind(target)

        expect:
        that target.itemDataSource, instanceOf(GroovyMapItem)
        that target.itemDataSource.itemPropertyIds as Set, equalTo(['prop1','prop2'] as Set)
        ['prop1','prop2'].every { target.itemDataSource.getItemProperty(it).value == it }
    }

    def "it can set a target and bind"() {

        given:
        def model = new Model(modelProp: new TestBean())
        def FieldGroup target = new FieldGroup()
        new ItemBinding(source: model, sourceProperty: 'modelProp',target: target).bind()

        expect:
        that target.itemDataSource, instanceOf(GroovyBeanItem)
        that target.itemDataSource.itemPropertyIds as Set, equalTo(['prop1','prop2'] as Set)
        ['prop1','prop2'].every { target.itemDataSource.getItemProperty(it).value == it }
    }

    def "it can bind a specific set of properties"() {

        given:
        def model = new Model(modelProp: new TestBean())
        def FieldGroup target = new FieldGroup()
        new ItemBinding(source: model, sourceProperty: 'modelProp', itemIds: ['prop1']).bind(target)

        expect:
        that target.itemDataSource, instanceOf(GroovyBeanItem)
        that target.itemDataSource.itemPropertyIds as Set, equalTo(['prop1'] as Set)
        ['prop1'].every { target.itemDataSource.getItemProperty(it).value == it }
    }

}
