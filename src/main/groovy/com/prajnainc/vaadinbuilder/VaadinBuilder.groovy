package com.prajnainc.vaadinbuilder

import com.prajnainc.vaadinbuilder.factories.SingleComponentContainerFactory
import com.prajnainc.vaadinbuilder.factories.VaadinComponentFactory
import com.vaadin.ui.Button
import com.vaadin.ui.Calendar
import com.vaadin.ui.Component
import com.vaadin.ui.Embedded
import com.vaadin.ui.Label
import com.vaadin.ui.Link
import com.vaadin.ui.Panel
import com.vaadin.ui.Upload
import com.vaadin.ui.Window

/**
 * Created by paul on 4/6/14.
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
        registerFactory('button',new VaadinComponentFactory(Button))
        registerFactory('link',new VaadinComponentFactory(Link))
        registerFactory('upload',new VaadinComponentFactory(Upload))
        registerFactory('embedded',new VaadinComponentFactory(Embedded))
        registerFactory('calendar',new VaadinComponentFactory(Calendar))
        registerFactory('label',new VaadinComponentFactory(Label))
    }

    def registerLayoutFactories() {

    }

    def registerFormFactories() {

    }
}
