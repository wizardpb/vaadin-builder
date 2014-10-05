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
package com.prajnainc.vaadinbuilder.factories

import com.prajnainc.vaadinbuilder.BuilderSpecification
import com.vaadin.event.FieldEvents
import com.vaadin.ui.Button;
import spock.lang.*
import static org.hamcrest.CoreMatchers.*
import static spock.util.matcher.HamcrestSupport.*

public class EventDefinitionSpecification extends BuilderSpecification {

    def "a Button can define event actions"() {
        expect:
        def fired = null
        Button button = builder.build {
            button('Click Me',(method): { evt ->
                fired = evt
            })
        }
        fireAction.call(button) == null
        fired.getClass() == event
        fired.source.is(button)

        where:
        method      | event                  | fireAction
        'onClick'   | Button.ClickEvent      | { it.fireClick() }
        'onBlur'    | FieldEvents.BlurEvent  | { it.@focusBlurRpc.blur() }
        'onFocus'   | FieldEvents.FocusEvent | { it.@focusBlurRpc.focus() }
    }
}
