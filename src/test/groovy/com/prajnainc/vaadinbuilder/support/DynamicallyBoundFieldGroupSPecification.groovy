/**
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

import com.vaadin.ui.CheckBox
import com.vaadin.ui.TextField;

/**
 * DynamicallyBoundFieldGroupSpecification
 *
 *
 */

import spock.lang.*
import static org.hamcrest.CoreMatchers.*
import static spock.util.matcher.HamcrestSupport.*

public class DynamicallyBoundFieldGroupSpecification extends Specification {

    static class TestObject {
        String stringProp = 'string'
        int intProp = 1
        boolean boolProp = true
    }

    DynamicallyBoundFieldGroup fieldGroup

    def "it can build and bind from an instance"() {

        given:
        fieldGroup = new DynamicallyBoundFieldGroup(new TestObject())
        fieldGroup.buildAndBind('stringProp')
        fieldGroup.buildAndBind('boolProp')

        expect:
        fieldGroup.getBoundPropertyIds() as Set == ['stringProp','boolProp'] as Set
        that fieldGroup.getField('stringProp'), instanceOf(TextField)
        that fieldGroup.getField('boolProp'), instanceOf(CheckBox)

        that fieldGroup.getField('stringProp').value, equalTo('string')
        that fieldGroup.getField('boolProp').value, equalTo(true)

    }

    def "it can build and bind from a type"() {

        given:
        fieldGroup = new DynamicallyBoundFieldGroup(TestObject)
        fieldGroup.buildAndBind('stringProp')
        fieldGroup.buildAndBind('boolProp')

        expect:
        fieldGroup.getBoundPropertyIds() as Set == ['stringProp','boolProp'] as Set
        that fieldGroup.getField('stringProp'), instanceOf(TextField)
        that fieldGroup.getField('boolProp'), instanceOf(CheckBox)

        // Fields initialized to default values
        that fieldGroup.getField('stringProp').value, equalTo("")
        that fieldGroup.getField('boolProp').value, equalTo(false)
    }

    def "it can be bound to a model"() {

        given:
        fieldGroup = new DynamicallyBoundFieldGroup(TestObject)
        fieldGroup.buildAndBind('stringProp')
//        fieldGroup.buildAndBind('intProp')
        fieldGroup.buildAndBind('boolProp')
        fieldGroup.setDataSource(new TestObject(stringProp: 'newString', intProp: 2, boolProp: true))

        expect:
        that fieldGroup.getField('stringProp').value, equalTo('newString')
        that fieldGroup.getField('boolProp').value, equalTo(true)
    }

    def "it can be bound to a Map"() {
        given:
        fieldGroup = new DynamicallyBoundFieldGroup(TestObject)
        fieldGroup.buildAndBind('stringProp')
        fieldGroup.buildAndBind('boolProp')
        fieldGroup.setDataSource([stringProp: 'newString', intProp: 2, boolProp: true])

        expect:
        that fieldGroup.getField('stringProp').value, equalTo('newString')
        that fieldGroup.getField('boolProp').value, equalTo(true)
    }
}
