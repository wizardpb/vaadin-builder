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

import com.vaadin.event.FieldEvents
import com.vaadin.event.MouseEvents
import com.vaadin.ui.Button
import com.vaadin.ui.Panel
import com.vaadin.ui.Window

import static com.vaadin.ui.HasComponents.*

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

    final private static Map EVENT_TABLE = [
        (FieldEvents.FocusNotifier): [
            'onFocus':  [
                listenerClass: FieldEvents.FocusListener, listenerMethod: 'focus', attachMethod: 'addFocusListener'],
        ],
        (FieldEvents.BlurNotifier): [
            'onBlur': [
                listenerClass: FieldEvents.BlurListener, listenerMethod: 'blur', attachMethod: 'addBlurListener'],
        ],
        (ComponentAttachDetachNotifier): [
            'onComponentAttach': [
                listenerClass: ComponentAttachListener,
                listenerMethod: 'componentAttachedToContainer',
                attachWith: 'addComponentAttachListener'],
            'onComponentDetach': [
                listenerClass: ComponentDetachListener,
                listenerMethod: 'componentDetachedFromContainer',
                attachWith: 'addComponentDetachListener'],
        ],
        (Button): [
            'onClick': [
                listenerClass: Button.ClickListener, listenerMethod: 'buttonClick', attachWith: 'addClickListener'],
        ],
        (Panel) : [
            'onClick': [
                listenerClass: MouseEvents.ClickListener, listenerMethod: 'click', attachWith: 'addClickListener'],
        ],
        (Window) : [
            'onClose': [
                listenerClass: Window.CloseListener, listenerMethod: 'windowClose', attachWith: 'addCloseListener'],
            'onResize': [
                listenerClass: Window.ResizeListener, listenerMethod: 'windowResized', attachWith: 'addResizeListener'],
            'onModeChange': [
                listenerClass: Window.WindowModeChangeListener,
                listenerMethod: 'windowModeChanged',
                attachWith: 'addWindowModeChangeListener'],
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
        for(Class cls = componentClass; cls != Object; cls = cls.getSuperclass()) {
            // Add any definitions for that class..
            definitions = definitions + (EVENT_TABLE[cls] ?: [:])
            // Then add definitions for any of it's interfaces or super-interfaces.
            // Use a Set to collect to remove duplicates
            def allInterfaces = cls.getInterfaces().inject([] as Set) { sum, iFace ->
                sum.add(iFace); sum.addAll(iFace.getInterfaces()); sum
            }
            allInterfaces.each { iFace ->
                definitions =  definitions + (EVENT_TABLE[iFace] ?: [:])
            }
        }
        return definitions
    }

}
