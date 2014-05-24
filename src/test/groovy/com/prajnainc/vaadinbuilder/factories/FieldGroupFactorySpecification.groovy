/*
 * Copyright (c) 2014 Prajna Inc
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
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import groovy.beans.Bindable

import static org.hamcrest.CoreMatchers.*

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
import static spock.util.matcher.HamcrestSupport.that

public class FieldGroupFactorySpecification extends BuilderSpecification {

    static class TestObject {
        String stringProp = 'string'
        int intProp = 1
        boolean boolProp = true
    }

    static class TestModel {
        @Bindable def modelProp = new TestObject()
    }

    def testModel = new TestModel()

    def "it recognizes the id attribute"() {

        given:
        builder.build {
            fieldGroup(id: 'myForm')
        }
        def fieldGroup = builder."myForm.fieldGroup"

        expect:
        that fieldGroup, instanceOf(FieldGroup)
    }

    def "it can specify the layout"() {

        given:
        def layout = builder.build {
            fieldGroup('myForm', layout: 'verticalLayout')
        }

        expect:
        that layout, instanceOf(VerticalLayout)
    }

    def "it can build a default layout"() {

        given:
        def layout = builder.build {
            fieldGroup('myForm')
        }

        expect:
        that layout, instanceOf(FormLayout)
    }

    def "it can add children to the layout"() {

        given:
        FormLayout layout = builder.build {
            fieldGroup('myForm') {
                label('SomeLabel')
            }
        }

        expect:
        layout.componentCount == 1
        that layout.getComponent(0), instanceOf(Label)
    }

    def "it adds the field group to the layouts data property"() {

        given:
        FormLayout layout = builder.build {
            fieldGroup('myForm')
        }

        expect:
        that layout.data.fieldGroup, sameInstance(builder."myForm.fieldGroup")
    }

    def "it can bind a binding factory"() {

        given:
        FormLayout layout = builder.build {
            fieldGroup('myForm', itemDataSource: new DataBindingFactory(source: testModel, sourceProperty: 'modelProp' ))
        }
        def fieldGroup = layout.data.fieldGroup
        def itemDataSource = fieldGroup.itemDataSource

        expect:
        that itemDataSource, instanceOf(Item)
        that itemDataSource.itemPropertyIds as Set, equalTo(['stringProp','intProp','boolProp'] as Set)
        that layout.data.binding, instanceOf(ItemBinding)
        that layout.data.binding.target, sameInstance(fieldGroup)
        that layout.data.binding.source, sameInstance(testModel)
    }
}
