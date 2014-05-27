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
import com.prajnainc.vaadinbuilder.support.GroovyBeanItem
import com.vaadin.ui.AbstractField
import com.vaadin.ui.CheckBox
import com.vaadin.ui.PopupDateField
import com.vaadin.ui.TextField

import static org.hamcrest.CoreMatchers.*

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
import static spock.util.matcher.HamcrestSupport.that

public class DefaultFieldFactorySpecification extends BuilderSpecification {

    static class TestObject {
        String stringProp = 'string'
        Integer intProp = 1
        Boolean boolProp = true
        Date dateProp = new Date()
    }

    def "it should throw an exception if there is no field group"() {

        when:
        builder.build {
            verticalLayout {
                field('thisProp')
            }
        }

        then:
        RuntimeException e = thrown()
        that e.cause, instanceOf(VaadinBuilderException)
        e.cause.message == "Cannot use a field node without a field group"
    }

    def "with a field factory and bound object, it should imply the data type" () {


        def obj = new TestObject()
        def c = builder.build {
            fieldGroup(id: 'thisGroup', itemDataSource: new GroovyBeanItem(new TestObject())) {
                field(propName)
            }
        }
        AbstractField field = c.getComponent(0)

        expect:
        that field, instanceOf(fieldType)
        that field.propertyDataSource.type, sameInstance(dataType)
        that field.propertyDataSource.value, equalTo(obj."$propName")

        where:
        propName        | fieldType         | dataType
        'stringProp'    | TextField         | String
        // Needs mock for session to provide converter
//        'intProp'       | TextField         | Integer
//        'boolProp'      | CheckBox          | Boolean
//        'dateProp'      | PopupDateField    | Date

    }
}
