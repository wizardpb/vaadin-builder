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
import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.prajnainc.vaadinbuilder.binding.DataBindingFactory
import com.prajnainc.vaadinbuilder.binding.ItemBinding
import com.vaadin.data.Item
import com.vaadin.data.fieldgroup.FieldGroup
import com.vaadin.data.util.converter.DefaultConverterFactory
import com.vaadin.server.VaadinSession
import com.vaadin.ui.AbsoluteLayout
import com.vaadin.ui.FormLayout
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import groovy.beans.Bindable

import static org.hamcrest.CoreMatchers.*
import static spock.util.matcher.HamcrestSupport.that

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

public class FieldGroupFactorySpecification extends BuilderSpecification {

    static class TestObject {
        String stringProp = 'string'
        Integer intProp = 1
        boolean boolProp = true
    }

    static class TestModel {
        @Bindable TestObject modelProp = new TestObject()
        @Bindable objProp = [stringProp: 'string', intProp: 1, boolProp: true]
    }

    def testModel = new TestModel()

    def mockSession = Stub(VaadinSession) {
        getConverterFactory() >> new DefaultConverterFactory()
    }

    def setup() {
        VaadinSession.setCurrent(mockSession)
    }

    def cleanup() {
        VaadinSession.setCurrent(null)
    }

    def "it recognizes the id attribute"() {

        given:
        builder.build {
            fieldGroup(id: 'myForm')
        }
        def fieldGroup = builder.myForm

        expect:
        that fieldGroup, instanceOf(FieldGroup)
    }

    def "it can id the layout"() {
        given:
        builder.build {
            fieldGroup(id: 'myForm', layoutId: 'formLayout')
        }
        def fieldGroup = builder.myForm
        def layout = builder.formLayout
        expect:
        that fieldGroup, instanceOf(FieldGroup)
        that layout, instanceOf(FormLayout)
    }

    def "it can specify the layout"() {

        given:
        def layout = builder.build {
            fieldGroup('myForm', layout: new VerticalLayout())
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
        that layout.data.fieldGroup, sameInstance(builder.myForm)
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
        that itemDataSource.itemPropertyIds as Set, equalTo(['stringProp', 'intProp', 'boolProp'] as Set)
        that layout.data.binding, instanceOf(ItemBinding)
        that layout.data.binding.target, sameInstance(fieldGroup)
        that layout.data.binding.source, sameInstance(testModel)
    }

    def "it can bind to a bind call and imply types from the source"() {

        given:
        FormLayout layout = builder.build {
            fieldGroup('myForm', dataSource: bind(source: testModel, sourceProperty: 'modelProp' ))
        }
        def fieldGroup = layout.data.fieldGroup
        def itemDataSource = fieldGroup.itemDataSource

        expect:
        that itemDataSource, instanceOf(Item)
        that itemDataSource.itemPropertyIds as Set, equalTo(['stringProp', 'intProp', 'boolProp'] as Set)
    }

    def "it can bind fields to a source with fields of explicit model type"() {

        given:
        FormLayout layout = builder.build {
            fieldGroup('myForm', dataSource: bind(source: testModel, sourceProperty: 'objProp' )) {
                textField('stringProp', modelType: String)
                textField('intProp', modelType: Integer)
                checkBox('boolProp', modelType: Boolean)
            }
        }
        def fieldGroup = layout.data.fieldGroup
        Item itemDataSource = fieldGroup.itemDataSource

        expect:
        that itemDataSource.itemPropertyIds as Set, equalTo(['stringProp', 'intProp', 'boolProp'] as Set)
        that itemDataSource.getItemProperty('stringProp').value, equalTo(testModel.objProp.stringProp)
        that itemDataSource.getItemProperty('intProp').value, equalTo(testModel.objProp.intProp)
        that itemDataSource.getItemProperty('boolProp').value, equalTo(testModel.objProp.boolProp)
    }

    def "it detects double definition of model type"() {

        when:
        FormLayout layout = builder.build {
            fieldGroup('myForm', dataSource: bind(source: testModel, sourceProperty: 'modelProp' )) {
                textField('stringProp', modelType: String)
                textField('intProp')
                checkBox('boolProp')
            }
        }

        then:
        def e = thrown(VaadinBuilderException)
        e.message == "The type of the model property 'stringProp' is multiply defined"
    }

    def "it can bind fields to a source with fields that need converters"() {

        given:
        FormLayout layout = builder.build {
            fieldGroup('myForm', dataSource: bind(source: testModel, sourceProperty: 'modelProp' )) {
                textField('stringProp')
                textField('intProp')
                checkBox('boolProp')
            }
        }
        def fieldGroup = layout.data.fieldGroup
        Item itemDataSource = fieldGroup.itemDataSource

        expect:
        that itemDataSource.itemPropertyIds as Set, equalTo(['stringProp', 'intProp', 'boolProp'] as Set)
        that itemDataSource.getItemProperty('stringProp').value, equalTo(testModel.modelProp.stringProp)
        that itemDataSource.getItemProperty('intProp').value, equalTo(testModel.modelProp.intProp)
        that itemDataSource.getItemProperty('boolProp').value, equalTo(testModel.modelProp.boolProp)
    }

    def "it can initialize with null and re-bind fields with the correct types"() {

        given:
        testModel.modelProp = null
        FormLayout layout = builder.build {
            fieldGroup('myForm', dataSource: bind(source: testModel, sourceProperty: 'modelProp' )) {
                textField('stringProp')
                textField('intProp')
                checkBox('boolProp')
            }
        }
        testModel.modelProp = new TestObject()
        def fieldGroup = layout.data.fieldGroup
        def itemDataSource = fieldGroup.itemDataSource

        expect:
        that itemDataSource, instanceOf(Item)
        that itemDataSource.itemPropertyIds as Set, equalTo(['stringProp', 'intProp', 'boolProp'] as Set)
        that itemDataSource.getItemProperty('stringProp').value, equalTo(testModel.modelProp.stringProp)
        that itemDataSource.getItemProperty('intProp').value, equalTo(testModel.modelProp.intProp)
        that itemDataSource.getItemProperty('boolProp').value, equalTo(testModel.modelProp.boolProp)
    }

    def "it should catch non-layouts as attributes"() {
        when:
        def layout = builder.build {
            fieldGroup('myForm', layout: new Object())
        }

        then:
        def e = thrown(RuntimeException)
        e.cause instanceof VaadinBuilderException
        e.cause.message == "Unknown layout type 'java.lang.Object'"
    }

    def "it should catch unknown layout factories as attributes"() {
        when:
        def layout = builder.build {
            fieldGroup('myForm', layout: new AbsoluteLayout())
        }

        then:
        def e = thrown(RuntimeException)
        e.cause instanceof VaadinBuilderException
        e.cause.message == "No factory known for layout type '${AbsoluteLayout.name}'"
    }
}
