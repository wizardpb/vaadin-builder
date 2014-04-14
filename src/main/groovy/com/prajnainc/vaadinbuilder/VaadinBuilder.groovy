/*
 *    Copyright (c) 2014 Prajna Inc.
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

import com.prajnainc.vaadinbuilder.factories.LayoutFactory
import com.prajnainc.vaadinbuilder.factories.OrderedLayoutFactory
import com.prajnainc.vaadinbuilder.factories.SingleComponentContainerFactory
import com.prajnainc.vaadinbuilder.factories.ComponentFactory
import com.vaadin.ui.Button
import com.vaadin.ui.Calendar
import com.vaadin.ui.Component
import com.vaadin.ui.Embedded
import com.vaadin.ui.FormLayout
import com.vaadin.ui.GridLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Link
import com.vaadin.ui.Panel
import com.vaadin.ui.Upload
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Window

/**
 * Created by paul on 4/6/14.
 *
 *
 */
class VaadinBuilder extends FactoryBuilderSupport {

    public static final String DELEGATE_PROPERTY_OBJECT_ID = "_delegateProperty:id";
    public static final String DEFAULT_DELEGATE_PROPERTY_OBJECT_ID = "id";

    VaadinBuilder(boolean init=true) {
        super(init)
        this[DELEGATE_PROPERTY_OBJECT_ID] = DEFAULT_DELEGATE_PROPERTY_OBJECT_ID
    }

    public static objectIDAttributeDelegate(def builder, def node, def attributes) {
        def idAttr = builder.getAt(DELEGATE_PROPERTY_OBJECT_ID) ?: DEFAULT_DELEGATE_PROPERTY_OBJECT_ID
        def theID = attributes.remove(idAttr)
        if (theID) {
            builder.setVariable(theID, node)
            if(node instanceof Component && !node.id) node.id = theID
        }
    }
    /**
     * Compatibility API.
     *
     * @param c run this closure in the builder
     */
    public Object build(Closure c) {
        c.setDelegate(this)
        return c.call()
    }

    def registerSupportNodes() {
        addAttributeDelegate(VaadinBuilder.&objectIDAttributeDelegate)
    }

    def registerSingleComponentFactories() {
        registerFactory('panel',new SingleComponentContainerFactory(Panel))
        registerFactory('window', new SingleComponentContainerFactory(Window))
    }

    def registerComponentFactories() {
        registerFactory('label',new ComponentFactory(Label))
        // AbstractMedia
        registerFactory('embedded',new ComponentFactory(Embedded))
        // ColorPickerGrid
        registerFactory('link',new ComponentFactory(Link))
        // PopupView
        // MenuBar && MenuItem
        // CustomComponent
        // ColorPickerGradient
        // AbstractJavaScriptComponent
        // AbstractEmbedded
        // AbstractColorPicker
        registerFactory('upload',new ComponentFactory(Upload))
        registerFactory('button',new ComponentFactory(Button))
        registerFactory('calendar',new ComponentFactory(Calendar))
    }

    def registerLayoutFactories() {
        registerFactory('gridLayout',new LayoutFactory(GridLayout))
        registerFactory('verticalLayout',new OrderedLayoutFactory(VerticalLayout))
        registerFactory('horizontalLayout',new OrderedLayoutFactory(HorizontalLayout))
        registerFactory('formLayout',new OrderedLayoutFactory(FormLayout))
        // TabLayout & Accordion
        // SplitLayouts
    }

    def registerFieldFactories() {
        // Slider
        // TextField
        // TextArea
        // PasswordField
        // ProgressBar ?
        // CheckBox
        // RichTextArea
        // CustomField
        // Table (AbstractSelect)
        // TwinColSelect
        // Tree
        // OptionGroup
        // NativeSelect
        // ListSelect
        // ComboBox
        // InlineDateField
        // PopupDateField

    }

    def registerUtilityFactories() {
        // Converter
        // Widget ?
    }
}
