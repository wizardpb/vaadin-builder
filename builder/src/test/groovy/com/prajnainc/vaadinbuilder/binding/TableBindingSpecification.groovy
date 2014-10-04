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
package com.prajnainc.vaadinbuilder.binding

import com.prajnainc.vaadinbuilder.support.GroovyBeanContainer
import com.vaadin.ui.Table
import groovy.beans.Bindable;
import spock.lang.*
import static org.hamcrest.CoreMatchers.*
import static spock.util.matcher.HamcrestSupport.*

public class TableBindingSpecification extends Specification {

    static class Model {
        @Bindable def modelProp
    }

    def "it should bind a property containing a value"() {

        given:
        def data = (1..5).collect { [data: it]}
        def model = new Model(modelProp: data)
        def Table target = new Table()
        new TableBinding(source: model, sourceProperty: 'modelProp').bind(target)

        expect:
        that target.containerDataSource, instanceOf(GroovyBeanContainer)
        that target.containerDataSource.itemIds, equalTo(data)
    }


    def "it can set a target and bind"() {

        given:
        def data = (1..5).collect { [data: it]}
        def model = new Model(modelProp: data)
        def Table target = new Table()
        new TableBinding(source: model, sourceProperty: 'modelProp',target: target).bind()

        expect:
        that target.containerDataSource, instanceOf(GroovyBeanContainer)
        that target.containerDataSource.itemIds, equalTo(data)
    }

    def "it will maintain the binding when the source property changes"() {

        given:
        def data = (1..5).collect { [data: it]}
        def model = new Model(modelProp: data)
        def Table target = new Table()
        new TableBinding(source: model, sourceProperty: 'modelProp').bind(target)
        model.modelProp = ('a'..'d').collect { [data: it]}

        expect:
        that target.containerDataSource.itemIds, equalTo(model.modelProp)
    }

    def "it can bind a source directly"() {

        given:
        def data = (1..5).collect { [data: it]}
        def Table target = new Table()
        new TableBinding(source: data).bind(target)

        expect:
        that target.containerDataSource.itemIds, equalTo(data)
    }

    def "it adds when the source property contents is observable and adds"() {
        given:
        def data = (1..5).collect { [data: it]}
        def model = new Model(modelProp: new ObservableList(data))
        def Table target = new Table()
        new TableBinding(source: model, sourceProperty: 'modelProp').bind(target)
        model.modelProp.add([data: 6])

        expect:
        that target.containerDataSource, instanceOf(GroovyBeanContainer)
        that target.containerDataSource.itemIds, equalTo(model.modelProp)
    }

    def "it adds when the source contents is observable and adds"() {
        given:
        def model = new ObservableList((1..5).collect { [data: it]})
        def Table target = new Table()
        new TableBinding(source: model).bind(target)
        model.add([data: 6])

        expect:
        that target.containerDataSource, instanceOf(GroovyBeanContainer)
        that target.containerDataSource.itemIds, equalTo(model)
    }

    def "it removes when the source property contents is observable and changes"() {
        given:
        def data = (1..5).collect { [data: it]}
        def model = new Model(modelProp: new ObservableList(data))
        def Table target = new Table()
        new TableBinding(source: model, sourceProperty: 'modelProp').bind(target)
        model.modelProp.remove([data: 3])

        expect:
        that target.containerDataSource, instanceOf(GroovyBeanContainer)
        that target.containerDataSource.itemIds, equalTo([1,2,4,5].collect{[data: it]})
    }

    def "it removes when the source contents is observable and changes"() {
        given:
        def data = (1..5).collect { [data: it]}
        def model = new ObservableList(data)
        def Table target = new Table()
        new TableBinding(source: model).bind(target)
        model.remove([data: 3])

        expect:
        that target.containerDataSource, instanceOf(GroovyBeanContainer)
        that target.containerDataSource.itemIds, equalTo([1,2,4,5].collect{[data: it]})
    }
}
