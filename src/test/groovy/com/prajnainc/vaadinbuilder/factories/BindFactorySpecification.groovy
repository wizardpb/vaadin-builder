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
import com.prajnainc.vaadinbuilder.binding.DataBinding
import com.prajnainc.vaadinbuilder.binding.DataBindingFactory
import com.vaadin.data.Item
import com.vaadin.data.fieldgroup.FieldGroup
import groovy.beans.Bindable;
import spock.lang.*
import static org.hamcrest.CoreMatchers.*
import static spock.util.matcher.HamcrestSupport.*

public class BindFactorySpecification extends BuilderSpecification {

    static class TestBean {
        String prop1
    }

    def "it creates factories when there is no target"() {

        given:
        def source = new TestBean()
         def factory = builder.build {
            bind(source: source)
        }

        expect:
        that factory,instanceOf(DataBindingFactory)
        that factory.source, sameInstance(source)

    }

    def "it creates bindings when there is a target"() {

        given:
        def (source, target) = [new TestBean(), new FieldGroup()]
        def factory = builder.build {
            bind(source: source, target: target)
        }

        expect:
        that factory, instanceOf(DataBinding)
        that factory.source, sameInstance(source)
        that factory.target, sameInstance(target)

    }
}
