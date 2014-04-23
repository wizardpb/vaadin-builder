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
import com.prajnainc.vaadinbuilder.support.DynamicallyBoundFieldGroup
import com.vaadin.ui.FormLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout

import static org.hamcrest.CoreMatchers.instanceOf
import static org.hamcrest.CoreMatchers.sameInstance
import static spock.util.matcher.HamcrestSupport.that

public class OrderedLayoutSpecification extends BuilderSpecification {

    def "it builds all layouts"() {

        expect:
        def c = builder.build {
            "${node}Layout" {}
        }
        that c, instanceOf(klass)

        where:
        node            | klass
        'vertical'      | VerticalLayout
        'horizontal'    | HorizontalLayout
        'form'          | FormLayout
    }

    def "it can add components"() {

        given:
        def c = builder.build {
            verticalLayout {
                label('contained')
            }
        }

        expect:
        c.componentCount == 1
        that c.getComponent(0),instanceOf(Label)
        c.getComponent(0).value == 'contained'

    }

}
