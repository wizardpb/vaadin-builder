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

package com.prajnainc.vaadinbuilder.binding

import com.vaadin.data.util.IndexedContainer
import com.vaadin.ui.ComboBox
import groovy.beans.Bindable
import spock.lang.Specification

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.CoreMatchers.instanceOf
import static spock.util.matcher.HamcrestSupport.that

public class SelectContainerBindingSpecification extends Specification {

    static class Model {
        @Bindable def modelProp
    }

    def "it should bind a property containing a value"() {

        given:
        def model = new Model(modelProp: 1..5)
        def ComboBox target = new ComboBox()
        new SelectContainerBinding(source: model, sourceProperty: 'modelProp').bind(target)

        expect:
        that target.containerDataSource, instanceOf(IndexedContainer)
        that target.containerDataSource.itemIds, equalTo(1..5 as List)
    }


    def "it can set a target and bind"() {

        given:
        def model = new Model(modelProp: 1..5)
        def ComboBox target = new ComboBox()
        new SelectContainerBinding(source: model, sourceProperty: 'modelProp', target: target).bind()

        expect:
        that target.containerDataSource, instanceOf(IndexedContainer)
        that target.containerDataSource.itemIds, equalTo(1..5 as List)
    }

    def "it will maintain the binding when the source property changes"() {

        given:
        def model = new Model(modelProp: 1..5)
        def ComboBox target = new ComboBox()
        new SelectContainerBinding(source: model, sourceProperty: 'modelProp').bind(target)
        model.modelProp = 'a'..'d'

        expect:
        that target.containerDataSource.itemIds, equalTo('a'..'d' as List)
    }

    def "it can bind a source directly"() {

        given:
        def ComboBox target = new ComboBox()
        new SelectContainerBinding(source: 1..5 as List).bind(target)

        expect:
        that target.containerDataSource.itemIds, equalTo(1..5 as List)
    }

    def "it updates the target when an observable source value changes"() {
        expect:
        def ComboBox target = new ComboBox()
        new SelectContainerBinding(source: model).bind(target)
        model."$op"(*params)
        target.containerDataSource.itemIds == itemIds

        where:

        model                                     | op           | params          | itemIds
        new ObservableList('a'..'e' as ArrayList) | "add"        | ['f']           | 'a'..'f' as List
        new ObservableList('a'..'e' as ArrayList) | "add"        | [1, 'f']        | ['a', 'f', 'b', 'c', 'd', 'e']
        new ObservableList('a'..'e' as ArrayList) | "set"        | [1, 'f']        | ['a', 'f', 'c', 'd', 'e']
        new ObservableList('a'..'e' as ArrayList) | "remove"     | [3]             | ['a', 'b', 'c', 'e']
        new ObservableList('a'..'e' as ArrayList) | "remove"     | ['d']           | ['a', 'b', 'c', 'e']
        new ObservableList('a'..'e' as ArrayList) | "addAll"     | ['f', 'g']      | 'a'..'g' as List
        new ObservableList('a'..'e' as ArrayList) | "addAll"     | [1, ['f', 'g']] | ['a', 'f', 'g', 'b', 'c', 'd', 'e']
        new ObservableList('a'..'e' as ArrayList) | "removeAll"  | ['d', 'e']      | ['a', 'b', 'c']
        new ObservableSet('a'..'e' as Set)        | "add"        | ['f']           | 'a'..'f' as List
        new ObservableSet('a'..'e' as Set)        | "remove"     | ['d']           | ['a', 'b', 'c', 'e']
        new ObservableSet('a'..'e' as Set)        | "addAll"     | ['f', 'g']      | 'a'..'g' as List
        new ObservableSet('a'..'e' as Set)        | "removeAll"  | ['d', 'e']      | ['a', 'b', 'c']
        new ObservableSet('a'..'e' as Set)        | "retainAll"  | ['d', 'e']      | ['d', 'e']
    }

    def "it updates the target when an observable source property value changes"() {
        expect:
        def model = new Model(modelProp: propertyValue)
        def ComboBox target = new ComboBox()
        new SelectContainerBinding(source: model, sourceProperty: 'modelProp').bind(target)
        model.modelProp."$op"(*params)
        target.containerDataSource.itemIds == itemIds

        where:

        propertyValue                               | op           | params        | itemIds
        new ObservableList('a'..'e' as ArrayList)   | "add"        | ['f']         | 'a'..'f' as ArrayList
        new ObservableList('a'..'e' as ArrayList)   | "add"        | [1, 'f']       | ['a', 'f', 'b', 'c', 'd', 'e']
        new ObservableList('a'..'e' as ArrayList)   | "set"        | [1, 'f']       | ['a', 'f', 'c', 'd', 'e']
        new ObservableList('a'..'e' as ArrayList)   | "remove"     | [3]           | ['a', 'b', 'c', 'e']
        new ObservableList('a'..'e' as ArrayList)   | "remove"     | ['d']         | ['a', 'b', 'c', 'e']
        new ObservableList('a'..'e' as ArrayList)   | "addAll"     | ['f', 'g']     | 'a'..'g' as ArrayList
        new ObservableList('a'..'e' as ArrayList)   | "addAll"     | [1, ['f', 'g']] | ['a', 'f', 'g', 'b', 'c', 'd', 'e']
        new ObservableList('a'..'e' as ArrayList)   | "removeAll"  | ['d', 'e']     | ['a', 'b', 'c']
        new ObservableSet('a'..'e' as Set)          | "add"        | ['f']         | 'a'..'f' as ArrayList
        new ObservableSet('a'..'e' as Set)          | "remove"     | ['d']         | ['a', 'b', 'c', 'e']
        new ObservableSet('a'..'e' as Set)          | "addAll"     | ['f', 'g']     | 'a'..'g' as ArrayList
        new ObservableSet('a'..'e' as Set)          | "removeAll"  | ['d', 'e']     | ['a', 'b', 'c']
        new ObservableSet('a'..'e' as Set)          | "retainAll"  | ['d', 'e']     | ['d', 'e']
    }
}
