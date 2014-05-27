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

import com.prajnainc.vaadinbuilder.VaadinBuilder
import com.vaadin.data.Property
import com.vaadin.data.util.ObjectProperty
import com.vaadin.server.VaadinRequest
import com.vaadin.shared.ui.MarginInfo
import com.vaadin.ui.Button
import com.vaadin.ui.Field
import com.vaadin.ui.UI;
/**
 * SamplerUI
 *
 */
class SamplerUI extends UI {

    def builder = new VaadinBuilder()

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        def view  = builder.build {
            panel(caption: "Top Panel") {
                verticalLayout() {
                    fieldGroup() {
                        textField('input',id: 'inputField', columns: 30)
                    }
//                    button('Commit', id: 'commitButton')
                }
            }
        }


        Property property = new ObjectProperty(null,Object)
        Field f =  builder.inputField
        f.setPropertyDataSource(property)

        property.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                println "Property value changed: $valueChangeEvent.property.value"
            }
        })

        Button b = builder.commitButton
        b.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent clickEvent) {
                f.commit()
            }
        })

        content = view
    }
}
