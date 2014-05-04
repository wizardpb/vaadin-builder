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
import com.vaadin.ui.*

import static org.hamcrest.CoreMatchers.*
import static spock.util.matcher.HamcrestSupport.that

public class ComponentFactorySpecification extends BuilderSpecification {

    def "it creates the correct instance"() {

        expect:
        def c = builder.build {
            "$node"()
        }
        that c, instanceOf(klass)

        where:
        node        | klass
        'label'     | Label
        'embedded'  | Embedded
        'link'      | Link
        'upload'    | Upload
        'button'    | Button
        'calendar'  | com.vaadin.ui.Calendar
    }

    def "it sets property values"() {

        expect:
        def c = builder.build {
            "$node"(caption: node)
        }
        that c.caption, equalTo(node)

        where:
        node        | klass
        'label'     | Label
        'embedded'  | Embedded
        'link'      | Link
        'upload'    | Upload
        'button'    | Button
        'calendar'  | com.vaadin.ui.Calendar
    }

    def "it passes through a value instance"() {

        expect:
        def inst = klass.newInstance()
        def c = builder.build {
            "$node"(inst)
        }
        that c, sameInstance(inst)

        where:
        node        | klass
        'label'     | Label
        'embedded'  | Embedded
        'link'      | Link
        'upload'    | Upload
        'button'    | Button
        'calendar'  | com.vaadin.ui.Calendar
    }

}
