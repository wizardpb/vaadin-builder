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

import com.prajnainc.vaadinbuilder.binding.DataBinding
import com.prajnainc.vaadinbuilder.factories.*
import com.vaadin.data.Container
import com.vaadin.data.Item
import com.vaadin.data.Property
import com.vaadin.data.fieldgroup.FieldGroup
import com.vaadin.ui.*

/**
 * VaadinBuilder is the builder class itself
 *
 * TODO - decide which components should be set immediate by default. Fields and ?
 */

class VaadinBuilder extends FactoryBuilderSupport {

    /**
     * Attributes saved for use by a parent component, usually a {@link com.vaadin.ui.Layout}
     */
    public final static String EXPAND_RATIO_ATTR    = 'expandRatio'         // Expand ration for OrderedLayouts
    public final static String ALIGNMENT_ATTR       = 'alignment'           // Component alignment in layout cell
    public final static String GRID_POSITION_ATTR   = 'position'            // position in GridLayout
    public final static String GRID_SPAN_ATTR       = 'span'                // Span of cells in GridLayout
    public final static String TAB_CAPTION          = 'tabCaption'
    public final static String TAB_ICON             = 'tabIcon'
    // TODO tab position

    private static final ATTRIBUTES_TO_SAVE = [
            EXPAND_RATIO_ATTR, ALIGNMENT_ATTR,
            GRID_POSITION_ATTR, GRID_SPAN_ATTR,
            TAB_CAPTION, TAB_ICON
    ]

    public static final String DELEGATE_PROPERTY_OBJECT_ID = "_delegateProperty:id";
    public static final String DEFAULT_DELEGATE_PROPERTY_OBJECT_ID = "id";
    public static final String GENERAL_BINDING_ATTRIBUTE = 'dataSource'
    public static final Set BINDING_ATTRIBUTES = [GENERAL_BINDING_ATTRIBUTE,'propertyDataSource','itemDataSource', 'containerDataSource'] as Set;

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
     * An attribute delegate that implements binding to sources defined as {@link BindFactory} values for specific
     * Vaadin data source attributes on a node definition. This is the case when a node is bound using the 'bind' construct
     * as the value of one of the attributes 'dataSource', 'properyDataSource', 'itemDataSource' or 'containerDataSource' e.g.
     * <code> fieldGroup(id: 'myGroup', dataSource: bind(source: someModel, sourceProperty: 'modelProeprty')) {
     * ...
     * }</code>
     *
     * <p>This delegate handles such patterns in a generic way, leveraging the BindFactories ability to correctly create and set
     * data binding objects ({@link com.vaadin.data.Item}, {@link com.vaadin.data.Property} or {@link com.vaadin.data.Container})
     * of the correct type and value for the source and target</p>
     *
     * @param builder
     * @param node
     * @param attributes
     */
    public static bindingAttributeDelegate(def builder, def node, Map attributes) {
        def bindingAttrs = new HashSet(BINDING_ATTRIBUTES)
        bindingAttrs.retainAll(attributes.keySet())

        if(bindingAttrs.size() == 0) {
            // Nothing needed
            return null
        }

        if(bindingAttrs.size() > 1) {
            throw new VaadinBuilderException("Only one of $BINDING_ATTRIBUTES may be specified")
        }

        def bindingAttr = bindingAttrs.first()
        def bindingValue = attributes.remove(bindingAttr)

        // If the node is a layout with a field group, the target is the field group, not the node itself
        def target = node instanceof Layout && node.data?.fieldGroup != null ? node.data.fieldGroup : node

        switch(bindingValue) {
            case DataBinding:
                // Store the binding in a Map (which may already exist) on the data property of the node
                (node.data ?: (node.data = [:])).binding = bindingValue.bind(target); return;
            case Property:
                bindingAttr = validateBinding(target,bindingAttr,bindingValue,'property',[Property.Viewer]); break;
            case Item:
                bindingAttr = validateBinding(target,bindingAttr,bindingValue,'item',[Item.Viewer,FieldGroup]); break;
            case Container:
                bindingAttr = validateBinding(target,bindingAttr,bindingValue,'container',[Container.Viewer]); break;
            default:
                throw new VaadinBuilderException("Cannot bind value '$bindingValue' of type ${bindingValue.getClass()} to a $bindingAttr")
        }

        /*
         * Bind actual data binding objects directly. At this point, bindingAttr should be the correct property name for the
         * node type and binding object type
         */
        target."$bindingAttr" = bindingValue
    }

    private static String validateBinding(def node,String bindingAttr,bindingValue,prefix,List targetClasses) {
        // The target must be of one of the target classes
        if(!targetClasses.any { Class k -> k.isAssignableFrom(node.getClass()) }) {
            throw new VaadinBuilderException("A $bindingValue cannot be bound to a $node")
        }
        if(bindingAttr == GENERAL_BINDING_ATTRIBUTE) {
            bindingAttr = prefix + bindingAttr.capitalize()
        }
        if(node.metaClass.getMetaProperty(bindingAttr) == null) {
            throw new VaadinBuilderException("A $node cannot be bound with $bindingAttr")
        }
        return bindingAttr
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
        addAttributeDelegate(VaadinBuilder.&bindingAttributeDelegate)
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
        registerFactory('table',new ComponentFactory(Table))
        registerFactory('comboBox',new ComponentFactory(ComboBox))
        registerFactory('twinColumnSelect',new ComponentFactory(TwinColSelect))
        registerFactory('nativeSelect',new ComponentFactory(NativeSelect))
        registerFactory('listSelect',new ComponentFactory(ListSelect))
        registerFactory('optionGroup',new ComponentFactory(OptionGroup))
        registerFactory('tree',new ComponentFactory(Tree))
    }

    def registerUtilityFactories() {
        registerFactory('fieldGroup',new FieldGroupFactory())
        registerFactory('bind', new BindFactory())
        // TODO - Converter - how to add to session
        // TODO - Widget
    }


    /**
     * Build from a String script using my classLoader
     *
     * @param script
     * @return - the built UI
     */
    public Object build(String script) {
        def loader = getClass().classLoader
        loader = loader instanceof GroovyClassLoader ? loader : new GroovyClassLoader(loader)
        build(script,loader)
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

    @Override
    protected Object createNode(Object name, Map attributes, Object value) {
        /**
         * Collect saved attributes and save them on the current context. This is made available to the factories via the builder,
         * and (because it is saved in the context) is unique to the current node being built
         */
        getContext().savedAttributes = [:]
        ATTRIBUTES_TO_SAVE.each {
            if(attributes.containsKey(it)) getContext().savedAttributes[it] = attributes.remove(it)
        }
        return super.createNode(name, attributes, value)
    }
}
