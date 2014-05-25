/*
 *    Copyright (c) 2014 Prajna Inc
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

package com.prajnainc.vaadinbuilder.widgets

import com.prajnainc.vaadinbuilder.VaadinBuilder
import com.vaadin.ui.Component

/**
 * Widget
 *
 * A {@link Widget} is a composable Vaadin UI component. It implements a Model-View-Presenter artifact, with the widget
 * itself playing the role of presenter. It contains a definition of the view, implemented as a {@link Closure} that can
 * be passed to a {@link VaadinBuilder} to create the hierarchy of Vaadin {@link com.vaadin.ui.Component}s that actually implement and display the UI.
 *
 * Each instance of a {@link Widget} can have a unique name, which is used to identify the component by id, as other {@link com.vaadin.ui.Component}s
 * built by a builder are.
 *
 * Widgets can be introduced into a containing view by the use of the builder 'widget' node. When encountered. this node creates an instance of the
 * {@link Widget} and incorporates it's view into the view hierarchy that that point.
 *
 * Because the view definition is contained withing the presenter (the widget instance itself), event generated by components in the view can
 * easily be directed to it by defining an event action that simply references 'this' in the view definition.
 *
 * The builder will also take steps to bind any event-generating  components to the {@link Widget} automatically, by looking for methods
 * with a defined signature on the widget itself. For a component with an id of Id, generating an event of type E, the builder
 * will look for a method definition
 * <code>void on<E>From<Id>(E event) ... </code>
 * where <E> is the event type and <Id> is the capitalized id
 * e.g for a {@link com.vaadin.ui.Button} with an id of 'save', clicks from this button would cause a method
 * <code> void onButtonClickFromCommit(Button.ClickEvent e)</code>
 * to be called with the event generated by the click
 *
 */
interface Widget {

    /**
     * Return a groovy {@link Object} that can be passed to a {@link VaadinBuilder} to build the view for this {@link Widget}. This
     * can be one of a {@link Script}, a {@link String} of script text, or a {@link Closure}
     *
     * @return
     */
    Closure getViewSpec();

    /**
     * Build the view defined by {@link Widget#getView(VaadinBuilder build)} using the given {@link VaadinBuilder}
     *
     * @param builder
     * @return the view top-level component
     */
    Component getView(VaadinBuilder build);

    /**
     * Get this {@link Widget}s id {@link String}
     *
     * @return
     */
    String getId();

    /**
     * Set the {@link Widget}s id
     *
     * @param id
     * @return
     */
    void setId(String id);
}