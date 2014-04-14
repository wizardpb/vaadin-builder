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

package com.prajnainc.vaadinbuilder

import com.vaadin.ui.Panel
import com.vaadin.ui.Window
import org.junit.*

import static org.junit.Assert.*
import static org.hamcrest.CoreMatchers.*

/**
 * Created by paul on 4/6/14.
 */
class SingleComponentFactoryTests extends GroovyTestCase {

    @Test
    public void testSingleComponents() {
        def builder = new VaadinBuilder()
        def component = builder.build {
            panel(id: 'testPanel',caption: 'myPanel')
        }

        assertThat(component,instanceOf(Panel))
        assertThat(component.id,equalTo('testPanel'))
        assertThat(component.caption,equalTo('myPanel'))
        assertThat(builder.testPanel, sameInstance(component))

        component = builder.build {
            window(id: 'testWindow', caption: 'myWindow') {
                panel(id: 'content', caption: 'windowPanel')
            }
        }

        assertThat(component,instanceOf(Window))
        assertThat(component.id,equalTo('testWindow'))
        assertThat(component.caption,equalTo('myWindow'))
        assertThat(builder.testWindow, sameInstance(component))

        assertThat(builder.testWindow.content,sameInstance(builder.content))
        assertThat(builder.content.parent,sameInstance(builder.testWindow))

    }

    @Test
    public void testComponentFactory() {

    }
}
