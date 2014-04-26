package com.prajnainc.vaadinbuilder.factories

import com.prajnainc.vaadinbuilder.BuilderSpecification
import com.vaadin.ui.CheckBox
import com.vaadin.ui.InlineDateField
import com.vaadin.ui.PasswordField
import com.vaadin.ui.PopupDateField
import com.vaadin.ui.RichTextArea
import com.vaadin.ui.TextArea
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout;/*
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

public class FieldFactorySpecification extends BuilderSpecification {

    static class TestObject {
        String stringProp = 'string'
        Integer intProp = 1
        Boolean boolProp = true
        Date dateProp = new Date()
    }

    def "without a field group, it should build all fields with explicit type"() {

        expect:
        VerticalLayout c = (VerticalLayout)builder.build {
            verticalLayout {
                "$fieldNode"('thisProp')
            }
        }
        def field = c.getComponent(0)

        that field, instanceOf(fieldClass)
        that field.caption, equalTo('This Prop')

        where:
        fieldNode           | fieldClass
        'textField'         | TextField
        'textArea'          | TextArea
        'passwordField'     | PasswordField
        'checkBox'          | CheckBox
        'richTextArea'      | RichTextArea
        'inlineDateField'   | InlineDateField
        'popupDateField'    | PopupDateField
    }

    def "with a field group, it should build all fields with explicit type and bind to a given data"() {

        expect:
        VerticalLayout c = (VerticalLayout)builder.build {
            verticalLayout {
                "$fieldNode"('thisProp')
            }
        }
        def field = c.getComponent(0)

        that field, instanceOf(fieldClass)
        that field.caption, equalTo('This Prop')

        where:
        fieldNode           | fieldClass
        'textField'         | TextField
        'textArea'          | TextArea
        'passwordField'     | PasswordField
        'checkBox'          | CheckBox
        'richTextArea'      | RichTextArea
        'inlineDateField'   | InlineDateField
        'popupDateField'    | PopupDateField
    }

}
