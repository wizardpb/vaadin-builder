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

import com.prajnainc.vaadinbuilder.support.GroovyObjectProperty
import com.vaadin.ui.TextField
import groovy.beans.Bindable;
import spock.lang.*
import static org.hamcrest.CoreMatchers.*
import static spock.util.matcher.HamcrestSupport.*

public class PropertyBindingSpecification extends Specification {

    static class Model {
        @Bindable def modelProp
    }

    def "it should bind a property containing a value"() {

        given:
        def model = new Model(modelProp: 'modelValue')
        def TextField target = new TextField()
        new PropertyBinding(source: model, sourceProperty: 'modelProp').bind(target)

        expect:
        that target.propertyDataSource, instanceOf(GroovyObjectProperty)
        that target.value, equalTo('modelValue')
    }


    def "it can set a target and bind"() {

        given:
        def model = new Model(modelProp: 'modelValue')
        def TextField target = new TextField()
        new PropertyBinding(source: model, sourceProperty: 'modelProp',target: target).bind()

        expect:
        that target.propertyDataSource, instanceOf(GroovyObjectProperty)
        that target.value, equalTo('modelValue')
    }

    def "it will maintain the binding when the source property changes"() {

        given:
        def model = new Model(modelProp: 'modelValue')
        def TextField target = new TextField()
        new PropertyBinding(source: model, sourceProperty: 'modelProp').bind(target)
        model.modelProp = 'newValue'

        expect:
        that target.value, equalTo("newValue")
    }

    def "it can bind a source directly"() {

        given:
        def TextField target = new TextField()
        new PropertyBinding(source: 'targetValue').bind(target)

        expect:
        target.value == 'targetValue'
    }

}
