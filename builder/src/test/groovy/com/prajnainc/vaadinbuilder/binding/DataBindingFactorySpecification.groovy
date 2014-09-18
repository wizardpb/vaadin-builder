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
package com.prajnainc.vaadinbuilder.binding

import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.prajnainc.vaadinbuilder.support.GroovyBeanItem
import com.vaadin.data.Container
import com.vaadin.data.Item
import com.vaadin.data.fieldgroup.FieldGroup
import com.vaadin.data.util.IndexedContainer
import com.vaadin.ui.ComboBox
import com.vaadin.ui.Label
import com.vaadin.ui.Table
import groovy.beans.Bindable
import spock.lang.Specification

public class DataBindingFactorySpecification extends Specification {

    def property = Spy(Label)
    def itemViewer = Mock(Item.Viewer)
    def fieldGroup = Mock(FieldGroup)
    def comboBox = Mock(ComboBox)
    def table = Mock(Table)

    static class TestModel {
        @Bindable
        def modelProp = [prop1: 'prop1']
    }

    TestModel testModel = new TestModel()

    def "it can create and bind a Bindable to a Property.Viewer"() {

        when:
        testModel.modelProp = 'string1'
        new DataBindingFactory(source: testModel, sourceProperty: 'modelProp').bind(property)
        testModel.modelProp = 'newValue'

        then:
        1 * property.setPropertyDataSource(_)
        1 * property.fireValueChange()
    }

    def "it can create and bind a Bindable to an Item.Viewer"() {
        when:

        new DataBindingFactory(source: testModel,sourceProperty: 'modelProp').bind(itemViewer)
        testModel.modelProp = [prop1: 'newProp1']

        then:
        // It sets the item source once when bound, then again when the bound property changes
        2 * itemViewer.setItemDataSource(_ as GroovyBeanItem)
    }

    def "it can create and bind a Bindable to a FieldGroup"() {
        when:

        new DataBindingFactory(source: testModel,sourceProperty: 'modelProp').bind(fieldGroup)
        testModel.modelProp = [prop1: 'newProp1']

        then:
        // TODO - check for call to fieldGroup.getItenIds()
        // It sets the item source once when bound, then again when the bound property changes
        2 * fieldGroup.setItemDataSource(_ as GroovyBeanItem)
    }

    def "it can create and bind a Bindable to an AbstractSelect (ComboBox)"() {
        when:
        testModel.modelProp = 1..5
        new DataBindingFactory(source: testModel,sourceProperty: 'modelProp').bind(comboBox)
        testModel.modelProp = 'a'..'d'

        then:
        // It sets the item source once when bound, then again when the bound property changes
        2 * comboBox.setContainerDataSource(_ as IndexedContainer)
    }

    def "it throws an exception on a Table (temporary)"() {
        when:
        new DataBindingFactory(source: testModel,sourceProperty: 'modelProp').bind(table)

        then:
        def e = thrown VaadinBuilderException
    }

    def "it throws an exception on a non-bindable object"() {
        when:

        new DataBindingFactory(source: testModel,sourceProperty: 'modelProp').bind([:])

        then:
        def e = thrown VaadinBuilderException
    }
}
