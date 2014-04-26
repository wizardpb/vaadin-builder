package com.prajnainc.vaadinbuilder.factories

import com.prajnainc.vaadinbuilder.BuilderSpecification
import com.prajnainc.vaadinbuilder.support.DynamicallyBoundFieldGroup
import com.vaadin.ui.FormLayout
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout;/*
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

public class FieldGroupFactorySpecification extends BuilderSpecification {

    static class TestObject {
        String stringProp = 'string'
        int intProp = 1
        boolean boolProp = true
    }

    interface Testable {

        String getStringProp();
        void setStringProp();

        String getReadOnlyProp();
    }

    def "it can bind to a Groovy object class"() {

        given:
        builder.build {
            fieldGroup('myForm', modelType: TestObject)
        }
        def fieldGroup = builder."myForm.fieldGroup"

        expect:
        that fieldGroup.descriptors.keySet(), equalTo(['stringProp','intProp','boolProp'] as Set)
    }

    def "it can bind to a interface"() {

        given:
        builder.build {
            fieldGroup('myForm', modelType: Testable)
        }
        def fieldGroup = builder."myForm.fieldGroup"

        expect:
        that fieldGroup.descriptors.keySet(), equalTo(['stringProp','readOnlyProp'] as Set)
    }

    def "it recognizes the id attribute"() {

        given:
        builder.build {
            fieldGroup(id: 'myForm', modelType: TestObject)
        }
        def fieldGroup = builder."myForm.fieldGroup"

        expect:
        that fieldGroup.descriptors.keySet(), equalTo(['stringProp','intProp','boolProp'] as Set)
    }

    def "it binds to a given object"() {
        given:
        def obj = new TestObject()
        builder.build {
            fieldGroup(id: 'myForm', modelType: TestObject)
        }
        def fieldGroup = builder."myForm.fieldGroup"
        def propSet = ['stringProp','intProp','boolProp'] as Set
        fieldGroup.dataSource = obj

        expect:
        that fieldGroup.itemDataSource, notNullValue()
        that fieldGroup.itemDataSource.itemPropertyIds as Set, equalTo(propSet)
        that fieldGroup.itemDataSource.getItemProperty('stringProp').value, equalTo(obj.stringProp)
        that fieldGroup.itemDataSource.getItemProperty('intProp').value, equalTo(obj.intProp)
        that fieldGroup.itemDataSource.getItemProperty('boolProp').value, equalTo(obj.boolProp)
    }

    def "it builds from a list of properties"() {
        builder.build {
            fieldGroup('myForm', modelType: [
                    [name:'stringProp',type: String],
                    [name:'intProp',type: Integer],
                    [name:'boolProp',type: Boolean]
            ])

        }
        def fieldGroup = builder."myForm.fieldGroup"

        expect:
        that fieldGroup.descriptors.keySet(), equalTo(['stringProp','intProp','boolProp'] as Set)
    }

    def "it can specify the layout"() {

        given:
        def layout = builder.build {
            fieldGroup('myForm', layout: 'verticalLayout', modelType: TestObject)
        }

        expect:
        that layout, instanceOf(VerticalLayout)
    }

    def "it can build a default layout"() {

        given:
        def layout = builder.build {
            fieldGroup('myForm', modelType: TestObject)
        }

        expect:
        that layout, instanceOf(FormLayout)
    }

    def "it can add children"() {

        given:
        FormLayout layout = builder.build {
            fieldGroup('myForm', modelType: TestObject) {
                label('SomeLabel')
            }
        }

        expect:
        layout.componentCount == 1
        that layout.getComponent(0), instanceOf(Label)
    }
}
