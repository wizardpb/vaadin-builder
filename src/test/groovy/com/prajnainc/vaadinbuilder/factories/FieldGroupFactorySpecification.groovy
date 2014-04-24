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

    def "it builds and binds to a Groovy object class"() {

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
        DynamicallyBoundFieldGroup fieldGroup = c.data

        expect:
        that fieldGroup, notNullValue()
        that builder.thisGroup, sameInstance(fieldGroup)
        that c.data.descriptors.keySet(), equalTo(['stringProp','intProp','boolProp'] as Set)
    }

    def "it binds to a given object"() {
        given:
        def obj = new TestObject()
        def c = builder.build {
            verticalLayout {
                fieldGroup(id: 'thisGroup', bind: obj )
            }
        }
        DynamicallyBoundFieldGroup fieldGroup = c.data
        def propSet = ['stringProp','intProp','boolProp'] as Set

        expect:
        that fieldGroup, notNullValue()
        that builder.thisGroup, sameInstance(fieldGroup)
        that fieldGroup.descriptors.keySet(), equalTo(propSet)
        that fieldGroup.itemDataSource, notNullValue()
        that fieldGroup.itemDataSource.itemPropertyIds as Set, equalTo(propSet)
        that fieldGroup.itemDataSource.getItemProperty('stringProp').value, equalTo(obj.stringProp)
        that fieldGroup.itemDataSource.getItemProperty('intProp').value, equalTo(obj.intProp)
        that fieldGroup.itemDataSource.getItemProperty('boolProp').value, equalTo(obj.boolProp)
    }

    def "it builds from a list of properties"() {
        def c = builder.build {
            verticalLayout {
                fieldGroup('thisGroup', forProperties: [
                        [name:'stringProp',type: String],
                        [name:'intProp',type: Integer],
                        [name:'boolProp',type: Boolean]
                ])
            }
        }

        expect:
        that builder.thisGroup, instanceOf(DynamicallyBoundFieldGroup)
        that c.data, sameInstance(builder.thisGroup)
        that c.data.descriptors.keySet(), equalTo(['stringProp','intProp','boolProp'] as Set)
    }
}
