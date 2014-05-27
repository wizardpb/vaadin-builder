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

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.CoreMatchers.instanceOf
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


    def "it can add recursively"() {

        given:
        def c = builder.build {
            verticalLayout {
                verticalLayout() {
                    textField('input',id: 'inputField', columns: 30)
                }
                button('Commit', id: 'commitButton')
            }
        }

        expect:
        that c, instanceOf(VerticalLayout)
        c.componentCount == 2
        that c.getComponent(0),instanceOf(VerticalLayout)
        that c.getComponent(1),instanceOf(Button)

    }

    def "it can set alignment for a child"() {
        VerticalLayout c = builder.build {
            verticalLayout {
                label('a', alignment: Alignment.BOTTOM_LEFT)
                label('b', alignment: Alignment.TOP_RIGHT)
            }
        }

        expect:
        that c.getComponentAlignment(c.getComponent(0)), equalTo(Alignment.BOTTOM_LEFT)
        that c.getComponentAlignment(c.getComponent(1)), equalTo(Alignment.TOP_RIGHT)
    }

    def "it can set expand ration for a child"() {

        VerticalLayout c = builder.build {
            verticalLayout {
                label('a', expandRatio: 0.5f)
                label('b', expandRatio: 0.5f)
            }
        }

        expect:
        that c.getExpandRatio(c.getComponent(0)), equalTo(0.5f)
        that c.getExpandRatio(c.getComponent(1)), equalTo(0.5f)
    }

}
