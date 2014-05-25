/*
 * Copyright (c) 2014 Prajna Inc
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
import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.vaadin.ui.*

import static org.hamcrest.CoreMatchers.equalTo

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
 *
 */
import static org.hamcrest.CoreMatchers.instanceOf
import static org.hamcrest.CoreMatchers.nullValue
import static spock.util.matcher.HamcrestSupport.that

public class FieldFactorySpecification extends BuilderSpecification {

    static class TestObject {
        String stringProp = 'string'
        Integer intProp = 1
        Boolean boolProp = true
        Date dateProp = new Date()
    }

    def "it should build all fields with explicit type"() {

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

    def "it should allow a null propertyId without a field group"() {

        expect:
        VerticalLayout c = (VerticalLayout)builder.build {
            verticalLayout {
                "$fieldNode"()
            }
        }
        def field = c.getComponent(0)

        that field, instanceOf(fieldClass)
        that field.caption, nullValue()

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

    def "it should require propertyId with a field group"() {

        when:
        builder.build {
            fieldGroup {
                textField()
            }
        }

        then:
        def e = thrown(RuntimeException)
        e.cause instanceof VaadinBuilderException
        e.cause.message == "Fields of a field group require a propery id"

    }

    def "it should require a non-empty propertyId with a field group"() {

        when:
        builder.build {
            fieldGroup {
                textField('')
            }
        }

        then:
        def e = thrown(RuntimeException)
        e.cause instanceof VaadinBuilderException
        e.cause.message == "Fields of a field group require a propery id"

    }
}
