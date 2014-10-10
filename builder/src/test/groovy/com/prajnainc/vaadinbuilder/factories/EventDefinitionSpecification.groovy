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
import com.vaadin.event.ItemClickEvent
import com.vaadin.event.MouseEvents
import com.vaadin.ui.Button
import com.vaadin.ui.Table
import com.vaadin.ui.Window

import static com.vaadin.ui.HasComponents.ComponentAttachEvent
import static com.vaadin.ui.HasComponents.ComponentDetachEvent
import static com.vaadin.ui.Table.HeaderClickEvent
import static com.vaadin.ui.Table.FooterClickEvent
import static com.vaadin.ui.Table.ColumnReorderEvent
import static com.vaadin.ui.Table.ColumnResizeEvent

public class EventDefinitionSpecification extends BuilderSpecification {

    def "a Button can define all it's event actions"() {
        expect:
        def fired = null
        Button button = builder.build {
            button('Click Me', (method): { evt ->
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

    def "a Panel can define all it's event actions"() {
        expect:
        def fired = null
        def component = builder.build {
            panel('Test', (method): { evt ->
                fired = evt
            })
        }
        fireAction.call(component) == null
        fired.getClass() == event
        fired.source.is(component)

        where:
        method              | event                         | fireAction
        'onClick'           | MouseEvents.ClickEvent        | { it.@rpc.click(null)  }
        'onComponentAttach' | ComponentAttachEvent          | { it.fireComponentAttachEvent(null) }
        'onComponentDetach' | ComponentDetachEvent          | { it.fireComponentDetachEvent(null) }

    }

    def "a Window can define all it's event actions"() {
        expect:
        def fired = null
        def component = builder.build {
            window('Test', (method): { evt ->
                fired = evt
            })
        }
        fireAction.call(component) == null
        fired.getClass() == event
        fired.source.is(component)

        where:
        method              | event                         | fireAction
        'onClose'           | Window.CloseEvent             | { it.fireClose() }
        'onResize'          | Window.ResizeEvent            | { it.fireResize() }
        'onModeChange'      | Window.WindowModeChangeEvent  | { it.fireWindowWindowModeChange() }
        'onClick'           | MouseEvents.ClickEvent        | { it.@rpc.click(null)  }
        'onComponentAttach' | ComponentAttachEvent          | { it.fireComponentAttachEvent(null) }
        'onComponentDetach' | ComponentDetachEvent          | { it.fireComponentDetachEvent(null) }

    }

    def "a Table can define all it's event actions"() {
        expect:
        def fired = null
        Table component = builder.build {
            table('Test', (method): { evt ->
                fired = evt
            }) {
                tableColumn('a')
            }
        }
        component.addItem(['aContent'] as Object[],'a')

        fireAction.call(component) == null
        fired.getClass() == event
        fired.source.is(component)

        where:
        method            | event              | fireAction
        'onItemClick'     | ItemClickEvent     | { it.fireEvent(new ItemClickEvent(it, null, 'a', 'a', null))}
        'onHeaderClick'   | HeaderClickEvent   | { it.fireEvent(new HeaderClickEvent(it,'a',null)) }
        'onFooterClick'   | FooterClickEvent   | { it.fireEvent(new FooterClickEvent(it,'a',null)) }
        'onColumnResize'  | ColumnResizeEvent  | { it.fireColumnResizeEvent(null,0,0)  }
        'onColumnReorder' | ColumnReorderEvent | { it.fireEvent(new ColumnReorderEvent(it)) }

    }
}
