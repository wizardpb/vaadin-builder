package com.prajnainc.vaadinbuilder.factories

import com.prajnainc.vaadinbuilder.BuilderSpecification
import com.prajnainc.vaadinbuilder.support.DynamicallyBoundFieldGroup;/*
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

    def "it builds the group"() {

        given:
        def c = builder.build {
            verticalLayout {
                fieldGroup('thisGroup', forClass: TestObject)
                label('new label')
            }
        }

        expect:
        that builder.thisGroup, instanceOf(DynamicallyBoundFieldGroup)
        that c.data, sameInstance(builder.thisGroup)
        that c.data.descriptors.keySet(), equalTo(['stringProp','intProp','boolProp'] as Set)
    }

    def "it recognizes the id attribute"() {

        given:
        def c = builder.build {
            verticalLayout {
                fieldGroup(id: 'thisGroup', forClass: TestObject)
                label('new label')
            }
        }

        expect:
        that builder.thisGroup, instanceOf(DynamicallyBoundFieldGroup)
        that c.data, sameInstance(builder.thisGroup)
        that c.data.descriptors.keySet(), equalTo(['stringProp','intProp','boolProp'] as Set)
    }
}
