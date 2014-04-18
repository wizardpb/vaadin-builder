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
import com.vaadin.ui.GridLayout

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.CoreMatchers.instanceOf
import static spock.util.matcher.HamcrestSupport.that

public class GridLayoutSpecification extends BuilderSpecification {

    def "it builds a grid layout"() {
        given:
        def c = builder.build {
            gridLayout {}
        }

        expect:
        that c, instanceOf(GridLayout)
    }

    def "it builds a grid layout of specific dimensions"() {
        given:
        def c = builder.build {
            gridLayout(columns: 3, rows: 2) {}
        }

        expect:
        that c.columns, equalTo(3)
        that c.rows, equalTo(2)
    }

    def "it adds components consecutively"() {
        given:
        def c = builder.build {
            gridLayout(columns: 2, rows: 2) {
                label('1-1')
                label('2-1')
                label('1-2')
                label('2-2')
            }
        }

        expect:
        that c.getComponent(0,0).value, equalTo('1-1')
    }
}
