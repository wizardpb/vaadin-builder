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
package com.prajnainc.vaadinbuilder.factories

import com.prajnainc.vaadinbuilder.BuilderSpecification
import com.prajnainc.vaadinbuilder.binding.DataBindingFactory
import com.prajnainc.vaadinbuilder.binding.ItemBinding
import com.vaadin.data.Item
import com.vaadin.data.fieldgroup.FieldGroup
import com.vaadin.ui.FormLayout
import com.vaadin.ui.VerticalLayout
import groovy.beans.Bindable

import static org.hamcrest.CoreMatchers.*
import static spock.util.matcher.HamcrestSupport.that

public class BindFactorySpecification extends BuilderSpecification {

    static class TestBean {
        String prop1
        Integer prop2
        def prop3
    }

    static class TestModel {
        @Bindable
        TestBean data
    }

    def "it creates factories when there is no target and no type information"() {

        given:
        def source = new TestBean()
         def binding = builder.build {
            bind(source: source)
        }

        expect:
        that binding, instanceOf(DataBindingFactory)
        that binding.source, sameInstance(source)

    }

    def "it creates binding factories when there is no target but adds type information from the source"() {

        given:
        def source = new TestModel()
        def binding = builder.build {
            bind(source: source, sourceProperty: 'data')
        }
        def descriptors = binding.propertyDescriptors.sort { it.name }

        expect:
        that binding, instanceOf(DataBindingFactory)
        that descriptors*.name, equalTo(['prop1', 'prop2', 'prop3'])
        that descriptors*.propertyType, equalTo([String, Integer, Object])
        that descriptors*.defaultValue, equalTo( [null]*3)
    }


    def "it creates bindings when there is a target"() {

        given:
        def (source, target) = [new TestBean(), new FieldGroup()]
        def binding = builder.build {
            bind(source: source, target: target)
        }

        expect:
        that binding, instanceOf(ItemBinding)
        that binding.source, sameInstance(source)
        that binding.target, sameInstance(target)

    }

    def "it binds to a direct target"() {

        given:
        FieldGroup target = new FieldGroup()
        def source = new TestBean()
        def binding = builder.build {
            bind(source: source, target: target)
        }
        binding.bind()

        expect:
        that target.itemDataSource, instanceOf(Item)
        that target.itemDataSource.getItemPropertyIds().sort(), equalTo(['prop1', 'prop2', 'prop3'])
    }

    def "it binds a target to a null source property"() {
        given:
        def source = new TestModel()
        FieldGroup target = new FieldGroup()
        def binding = builder.build {
            bind(source: source, sourceProperty: 'data')
        }
        def descriptors = binding.propertyDescriptors.sort { it.name }
        binding = binding.bind(target)

        expect:
        that binding, instanceOf(ItemBinding)
        that descriptors*.name, equalTo(['prop1', 'prop2', 'prop3'])
        that descriptors*.propertyType, equalTo([String, Integer, Object])
        that descriptors*.defaultValue, equalTo( [null]*3)
        that target.itemDataSource, nullValue()
    }

    def "it binds a target to a non-null source property"() {
        given:
        def source = new TestModel(data: new TestBean(prop1: 'p1', prop2: 1, prop3: new Object()))
        FieldGroup target = new FieldGroup()
        def binding = builder.build {
            bind(source: source, sourceProperty: 'data')
        }
        def descriptors = binding.propertyDescriptors.sort { it.name }
        binding = binding.bind(target)

        expect:
        that binding, instanceOf(ItemBinding)
        that descriptors*.name, equalTo(['prop1', 'prop2', 'prop3'])
        that descriptors*.propertyType, equalTo([String, Integer, Object])
        that descriptors*.defaultValue, equalTo( [null]*3)
        def values = (1..3).collect { target.itemDataSource.getItemProperty('prop'+it).value }
        that values, equalTo((1..3).collect { source.data."prop$it" })
    }

    def "it binds to a @Bindable and re-binds on change"() {

        given:
        def source = new TestModel()
        FieldGroup target = new FieldGroup()
        def binding = builder.build {
            bind(source: source, sourceProperty: 'data')
        }
        def descriptors = binding.propertyDescriptors.sort { it.name }
        binding = binding.bind(target)
        source.data = new TestBean(prop1: 'p1', prop2: 1, prop3: new Object())

        expect:
        that binding, instanceOf(ItemBinding)
        that descriptors*.name, equalTo(['prop1', 'prop2', 'prop3'])
        that descriptors*.propertyType, equalTo([String, Integer, Object])
        that descriptors*.defaultValue, equalTo( [null]*3)
        def values = (1..3).collect { target.itemDataSource.getItemProperty('prop'+it).value }
        that values, equalTo((1..3).collect { source.data."prop$it" })
    }

    def "it can bind a source to a node target in a single component container"() {
        given:
        def source = new TestModel(data: new TestBean())
        FieldGroup target = new FieldGroup()
        def panel = builder.build {
            panel() {
                fieldGroup(dataSource: bind(source: source, sourceProperty: 'data'))
            }
        }

        expect:
        that panel.content, instanceOf(FormLayout)
        def fieldGroup = panel.content.data.fieldGroup
        def binding = panel.content.data.binding
        that fieldGroup.itemDataSource, notNullValue()
        that binding, notNullValue()
        that binding.source, sameInstance(source)
        that binding.sourceProperty, is('data')
    }

    def "it can bind a source to a node target in a component container"() {
        given:
        def source = new TestModel(data: new TestBean())
        FieldGroup target = new FieldGroup()
        VerticalLayout layout = builder.build {
            verticalLayout() {
                fieldGroup(dataSource: bind(source: source, sourceProperty: 'data'))
            }
        }
        def child = layout.getComponent(0)
        
        expect:
        that child, instanceOf(FormLayout)
        def fieldGroup = child.data.fieldGroup
        def binding = child.data.binding
        that fieldGroup.itemDataSource, notNullValue()
        that binding, notNullValue()
        that binding.source, sameInstance(source)
        that binding.sourceProperty, is('data')
    }
}
