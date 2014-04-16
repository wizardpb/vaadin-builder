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
 */

package com.prajnainc.vaadinbuilder

import com.prajnainc.vaadinbuilder.menuSupport.MenuItemCommand
import com.vaadin.ui.MenuBar

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.CoreMatchers.instanceOf
import static spock.util.matcher.HamcrestSupport.that

public class MenuSpecification extends BuilderSpecification {

    def "it builds a empty menu bar"() {

        given:
        def result = builder.build {
            menuBar {}
        }

        expect:
        result instanceOf(MenuBar)

    }

    def "it can set menu properties"() {
        given:
        def result = builder.build {
            menuBar(autoOpen: true) {}
        }

        expect:
        result.autoOpen
    }

    def "it can add menu items"() {
        given:
        def result = builder.build {
            menuBar() {
                menuItem('File')
            }
        }

        expect:
        that result.items.size(), equalTo(1)
        that result.items.first(), instanceOf(MenuBar.MenuItem)
        that result.items.first().text,equalTo('File')
    }

    def "it can add menu item hierarchies"() {
        given:
        def result = builder.build {
            menuBar() {
                menuItem('File') {
                    menuItem('Save', action: { println it })
                    menuItem('Open', action: { println it })
                    menuItem('Recent') {
                        menuItem('A File', checked: true)
                    }
                }
            }
        }

        expect:
        that result.items.first().children.size(), equalTo(3)
        that result.items.first().children[2].children.size(), equalTo(1)
    }

    def "it adds menu commands"() {

        given:
        def result = builder.build {
            menuBar() {
                menuItem('File') {
                    menuItem('Save', action: { println it })
                    menuItem('Open', action: { println it })
                    menuItem('Recent') {
                        menuItem('A File', checked: true)
                    }
                }
            }
        }

        expect:
        that result.items.first().children[0].command, instanceOf(MenuItemCommand)
        that result.items.first().children[0].command.action, instanceOf(Closure)

        and:
        result.items.first().children[2].children.checked
    }
}
