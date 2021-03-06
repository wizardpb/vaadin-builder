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
import com.vaadin.ui.Alignment
import com.vaadin.ui.GridLayout

import static org.hamcrest.CoreMatchers.*
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
        that c.getComponent(0, 0).value, equalTo('1-1')
    }

    def "it allows children to set their alignment"() {
        GridLayout c = builder.build {
            gridLayout(columns: 2, rows: 1) {
                label('0-0', alignment: Alignment.BOTTOM_LEFT)
                label('1-0', alignment: Alignment.TOP_RIGHT)
            }
        }

        expect:
        that c.getComponentAlignment(c.getComponent(0, 0)), equalTo(Alignment.BOTTOM_LEFT)
        that c.getComponentAlignment(c.getComponent(1, 0)), equalTo(Alignment.TOP_RIGHT)

    }

    def "it allows children to set their location and span with List arguments"() {

        GridLayout c = builder.build {
            gridLayout(columns: 2, rows: 2) {
                label('0-0', position: [0, 0])
                label('1-1', position: [0, 1], span: [2, 1])
            }
        }

        expect:
        that c.getComponent(0, 0).value, equalTo('0-0')
        that c.getComponent(1, 0), nullValue()
        that c.getComponent(0, 1).value, equalTo('1-1')
        that c.getComponent(1, 1).value, equalTo('1-1')
    }

    def "it allows children to set their location and span with Map arguments"() {

        GridLayout c = builder.build {
            gridLayout(columns: 2, rows: 2) {
                label('0-0', position: [column: 0, row: 0])
                label('1-1', position: [column: 0, row: 1], span: [columns: 2, rows: 1])
            }
        }

        expect:
        that c.getComponent(0, 0).value, equalTo('0-0')
        that c.getComponent(1, 0), nullValue()
        that c.getComponent(0, 1).value, equalTo('1-1')
        that c.getComponent(1, 1).value, equalTo('1-1')
    }
}
