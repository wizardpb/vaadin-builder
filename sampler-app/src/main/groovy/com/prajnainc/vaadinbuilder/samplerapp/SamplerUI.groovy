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

package com.prajnainc.vaadinbuilder.samplerapp

import com.prajnainc.vaadinbuilder.ui.BuilderUI
import com.vaadin.data.Item
import com.vaadin.server.BrowserWindowOpener
import com.vaadin.server.Sizeable
import com.vaadin.server.VaadinRequest
import com.vaadin.shared.ui.MarginInfo
import com.vaadin.ui.Button
import com.vaadin.ui.Component
import groovy.beans.Bindable

/**
 *
 */
class SamplerUI extends BuilderUI {

    static class Person {
        String name
        String address
        String city
        String state
        String zip
        Integer age

        public String toString() {
            return """name=$name,adddress=$address,city=$city,state=$state,zip=$zip,age=$age"""
        }
    }

    static class Model {
        @Bindable Person person = new Person()
    }

    Model model = new Model()

    @Override
    Component buildView() {
        return builder.build {
            panel(caption: 'Vaadin Builder Sampler') {
                verticalLayout(margin: new MarginInfo(true) ) {
                    tabSheet() {
                        horizontalLayout(caption: 'Form & Table', spacing: true, margin: new MarginInfo(true)  ) {
                            panel(caption: 'Add a Person') {
                                fieldGroup(id: 'personForm', dataSource: bind(source: model, sourceProperty: 'person')) {
                                    textField('name')
                                    textField('address')
                                    textField('city')
                                    textField('state')
                                    textField('zip')
                                    textField('age')
                                    horizontalLayout(width: '100%', height: Sizeable.SIZE_UNDEFINED) {
                                        button(id: 'saveButton', caption: 'Save', onClick: { evt ->
                                            builder.personForm.commit()
                                            display(model.person)
                                            model.person = new Person()
                                        } )
                                    }
                                }
                            }
                            table(id: 'people') {
                                tableColumn('name')
                                tableColumn('address')
                                tableColumn('city')
                                tableColumn('state')
                                tableColumn('zip')
                                tableColumn('age')
                            }
                        }
                    }
                }
            }
        }
    }

    private void display(Person person) {
        def table = builder.people
        Item item = table.getItem(table.addItem())
        [ 'name', 'address', 'city', 'state', 'zip', 'age'].each {
            item.getItemProperty(it).value = person."$it"
        }
    }

}
