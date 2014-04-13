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
