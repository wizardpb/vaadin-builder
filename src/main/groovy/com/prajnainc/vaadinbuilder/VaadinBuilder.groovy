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

import com.prajnainc.vaadinbuilder.factories.*
import com.vaadin.ui.*

/**
 * VaadinBuilder
 *
 * The central builder class
 */

class VaadinBuilder extends FactoryBuilderSupport {

    public static final String DELEGATE_PROPERTY_OBJECT_ID = "_delegateProperty:id";
    public static final String DEFAULT_DELEGATE_PROPERTY_OBJECT_ID = "id";

    def currentNodeName

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
        registerFactory('label',new LabelFactory())
        registerFactory('embedded',new ComponentFactory(Embedded))
        // TODO - ColorPickerGrid
        // TODO - ColorPickerGradient
        registerFactory('link',new ComponentFactory(Link))
        // TODO - PopupView
        registerFactory('menuBar',new ComponentFactory(MenuBar))
        registerFactory('menuItem',new MenuItemFactory())
        registerFactory('customComponent',new CustomComponentFactory()) // CustomComponent
        registerFactory('upload',new ComponentFactory(Upload))
        registerFactory('button',new ComponentFactory(Button))
        registerFactory('calendar',new ComponentFactory(Calendar))
        // Abstract classes all taken care of by customComponent
        // AbstractMedia
        // AbstractJavaScriptComponent
        // AbstractEmbedded
        // AbstractColorPicker
    }

    def registerLayoutFactories() {
        registerFactory('gridLayout', new GridLayoutFactory())
        registerFactory('verticalLayout', new OrderedLayoutFactory(VerticalLayout))
        registerFactory('horizontalLayout', new OrderedLayoutFactory(HorizontalLayout))
        registerFactory('formLayout', new OrderedLayoutFactory(FormLayout))
    }

    def registerContainerFactories() {
        registerFactory('tabSheet', new TabSheetFactory())
        registerFactory('verticalSplitPanel', new SplitPanelFactory(VerticalSplitPanel))
        registerFactory('horizontalSplitPanel', new SplitPanelFactory(HorizontalSplitPanel))
    }

    def registerItemFieldFactories() {
        registerFactory('field',new DefaultFieldFactory())
        registerFactory('slider',new FieldFactory(Slider))                     // Slider
        registerFactory('textField',new FieldFactory(TextField))                // TextField
        registerFactory('textArea',new FieldFactory(TextArea))                  // TextArea
        registerFactory('passwordField',new FieldFactory(PasswordField))        // PasswordField
        // TODO - ProgressBar - needs threading support, maybe hide/unhide on start ?
        registerFactory('checkBox',new FieldFactory(CheckBox))                  // CheckBox
        registerFactory('richTextArea',new FieldFactory(RichTextArea))          // RichTextArea
        registerFactory('customField',new CustomComponentFactory())                     // CustomField
        registerFactory('inlineDateField',new FieldFactory(InlineDateField))    // InlineDateField
        registerFactory('popupDateField',new FieldFactory(PopupDateField))      // PopupDateField
    }

    def registerContainerFieldFactories() {
        // Table (AbstractSelect)
        // TwinColSelect
        // Tree
        // NativeSelect
        // ListSelect
        // ComboBox
        // OptionGroup
    }

    def registerUtilityFactories() {
        registerFactory('fieldGroup',new FieldGroupFactory())
        // TODO - Converter - how to add to session
        // TODO - Widget
    }

    @Override
    protected Object dispatchNodeCall(Object name, Object args) {
        currentNodeName = name
        return super.dispatchNodeCall(name, args)
    }

    public Object checkForOneOf(attrList,attributes) {
        Collection containedAttrs = (attrList as Set).intersect(attributes.keySet())
        if(containedAttrs.size() > 1) {
            throw new VaadinBuilderException("${currentNodeName}(): only one of the attributes '${attrList}' may be passed")
        }
        if(containedAttrs.size() < 1) {
            throw new VaadinBuilderException("${currentNodeName}(): one of the attributes '${attrList}' must be preset")
        }
        return containedAttrs.first()
    }
}
