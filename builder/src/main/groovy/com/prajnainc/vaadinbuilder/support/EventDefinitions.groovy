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
package com.prajnainc.vaadinbuilder.support

import com.vaadin.event.MouseEvents
import com.vaadin.ui.*

import static com.vaadin.event.FieldEvents.*
import static com.vaadin.event.ItemClickEvent.ItemClickListener
import static com.vaadin.event.ItemClickEvent.ItemClickNotifier
import static com.vaadin.ui.HasComponents.*
import static com.vaadin.ui.Table.*
import static com.vaadin.ui.Window.*

/**
 * <p>EventDefinitions is a {@link Singleton} class that defines and manages event handling listeners
 * for components built by a {@link com.prajnainc.vaadinbuilder.VaadinBuilder} and its factories,
 * notable {@link com.prajnainc.vaadinbuilder.factories.ComponentFactory}.</p>
 *
 * <p>Node definitions can contain attributes of the form 'on<EventName> which
 * specify a {@link Closure} to call when that event is triggered from the component. This class
 * takes care of translating that event handling declaration into an event handler that will
 * call that closure when the event fires.</p>
 *
 * <p>Vaadin is very regular in defining explicit interfaces for notifiers of each event, with the vast majority of
 * component classes that fire events implementing one or more of these interfaces. We can take advantage of this
 * to map node attributes to the event handlers that will eventually handle them. For each interface, we define a
 * tuple that defines the listener class for the event of that interface, along with the method that is called on the
 * listener when the event is received, and the method that adds the listener to the component class. This is then
 * indexed by the node attribute that defines the closure that should ultimately be called. Thus,
 * given a node attribute, we can create a proxy listener that calls the given closure when the component
 * fires the event.</p>
 *
 * <p>The only remaining tasks is to find out which attributes are valid for the {@link com.vaadin.ui.Component} in
 * question. We can do this concisely by having a table that links event interfaces to the node attributes (and hence
 * definition tuples) that they support. By then iterating over a Components class and interface hierarchy,
 * we can collect all possible event notifiers for that class, and hence the set of possible node attributes allowed</p>
 *
 */
@Singleton
class EventDefinitions {

    /**
     * A definition of an Vaadin event. It holds information about how to add a listener for an event, and
     * generates default names for the listener and attach methods. These follow the regular naming rules
     * implemented by Vaadin.
     */
    static class EventDefinition {

        private final Class listenerClass
        private final String listenerMethod
        private final String attachMethod

        public EventDefinition(Class listenerClass, String listenerMethod = null, String attachMethod = null) {
            /**
             * Default listener method for a istener class is the name of the class minus the ' Listener',
             * with the first letter lower case e.g 'ItemClickListener' gets 'itemClick'
             */
            def defaultListenerMethod = listenerClass.simpleName.replace('Listener', '')
            defaultListenerMethod = defaultListenerMethod[0].toLowerCase() + defaultListenerMethod[1..-1]

            this.listenerClass = listenerClass
            this.listenerMethod = listenerMethod ?: defaultListenerMethod
            this.attachMethod = attachMethod ?: 'add' + listenerClass.simpleName
        }

        public Class getListenerClass() {
            return listenerClass
        }

        public String getListenerMethod() {
            return listenerMethod
        }

        public String getAttachMethod() {
            return attachMethod
        }

        public void attach(Map attrs, Closure action) {
            def component = attrs.to
            assert component instanceof Component
            def proxy = [(listenerMethod): action].asType(listenerClass)
            component."$attachMethod"(proxy)
        }
    }

    final private static Map EVENT_TABLE = [
        (FocusNotifier)                : [
            'onFocus': new EventDefinition(FocusListener)
        ],
        (BlurNotifier)                 : [
            'onBlur': new EventDefinition(BlurListener)
        ],
        (ComponentAttachDetachNotifier): [
            'onComponentAttach': new EventDefinition(ComponentAttachListener, 'componentAttachedToContainer'),
            'onComponentDetach': new EventDefinition(ComponentDetachListener, 'componentDetachedFromContainer'),
        ],
        (Button)                       : [
            'onClick': new EventDefinition(Button.ClickListener, 'buttonClick'),
        ],
        (Panel)                        : [
            'onClick': new EventDefinition(MouseEvents.ClickListener, 'click'),
        ],
        (Window)                       : [
            'onClose'     : new EventDefinition(CloseListener, 'windowClose'),
            'onResize'    : new EventDefinition(ResizeListener, 'windowResized'),
            'onModeChange': new EventDefinition(WindowModeChangeListener, 'windowModeChanged'),
        ],
        (ItemClickNotifier)            : [
            onItemClick: new EventDefinition(ItemClickListener),
        ],
        (Table)                        : [
            onHeaderClick  : new EventDefinition(HeaderClickListener),
            onFooterClick  : new EventDefinition(FooterClickListener),
            onColumnResize : new EventDefinition(ColumnResizeListener),
            onColumnReorder: new EventDefinition(ColumnReorderListener),
        ]
    ]

    /**
     * Convenience method for instance access
     *
     * @param componentClass - the Class
     * @return - a Map of action attribute names to event definitions
     */
    public static Map definitionsFor(Class componentClass) {
        return this.instance.getDefinitionsFor(componentClass)
    }

    /**
     * Get a Map of valid node action attributes and their event definitions for the given class
     *
     * @param componentClass - the Class
     * @return - a Map of action attribute names to event definitions
     */
    public Map getDefinitionsFor(Class componentClass) {
        def definitions = [:]
        // Iterate the superclass chain..
        for (Class cls = componentClass; cls != Object; cls = cls.getSuperclass()) {
            // Add any definitions for that class..
            definitions = definitions + (EVENT_TABLE[cls] ?: [:])
            // Then add definitions for any of it's interfaces or super-interfaces.
            // Use a Set to collect to remove duplicates
            def allInterfaces = cls.getInterfaces().inject([] as Set) { sum, iFace ->
                sum.add(iFace); sum.addAll(iFace.getInterfaces()); sum
            }
            allInterfaces.each { iFace ->
                definitions = definitions + (EVENT_TABLE[iFace] ?: [:])
            }
        }
        return definitions
    }

}
