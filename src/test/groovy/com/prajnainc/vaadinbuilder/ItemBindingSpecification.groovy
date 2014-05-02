package com.prajnainc.vaadinbuilder

import com.prajnainc.vaadinbuilder.binding.ItemBinding
import com.prajnainc.vaadinbuilder.support.GroovyBeanItem
import com.vaadin.data.Item
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

    class TestBean {
        @Bindable String prop1 = 'prop1'
        @Bindable String prop2 = 'prop2'
        @Bindable String prop3 = 'prop3'
    }

    class TestModel {
        @Bindable def formValue
    }

    def "it sets the data source"() {

        given:
        def model = new TestModel(formValue: new TestBean())
        def binding = new ItemBinding(source: model, sourceProperty: 'formValue')
        def fieldGroup = new FieldGroup()
        binding.bind(fieldGroup)
        Item dataSource = fieldGroup.getItemDataSource()

        expect:
        that dataSource, instanceOf(GroovyBeanItem)
        that dataSource.itemPropertyIds as Set, equalTo(['prop1','prop2','prop3'] as Set)
        dataSource.itemPropertyIds.every { dataSource.getItemProperty(it).value == it}
    }

    def "it binds the properties"() {

        given:
        def model = new TestModel(formValue: new TestBean())
        def binding = new ItemBinding(source: model, sourceProperty: 'formValue')
        def fieldGroup = new FieldGroup()
        binding.bind(fieldGroup)
        Item dataSource = fieldGroup.getItemDataSource()
        model.formValue.prop1 = 'new-prop1';

        expect:
        dataSource.getItemProperty('prop1').value == 'new-prop1'
    }

    def "it resets the data source when the model changes"() {

        given:
        def model = new TestModel(formValue: new TestBean())
        def binding = new ItemBinding(source: model, sourceProperty: 'formValue')
        def fieldGroup = new FieldGroup()
        binding.bind(fieldGroup)
        Item oldDataSource = fieldGroup.getItemDataSource()

        model.formValue = new TestBean(prop1: 'prop1-new',prop2: 'prop2-new',prop3: 'prop3-new')
        Item dataSource = fieldGroup.getItemDataSource()

        expect:
        that oldDataSource, not(sameInstance(dataSource))
        dataSource.itemPropertyIds.every { dataSource.getItemProperty(it).value == "${it}-new"}

    }
}
