package com.prajnainc.vaadinbuilder.factories

import com.vaadin.ui.Component

/**
 * Created by paul on 4/6/14.
 *
 * ComponentFactory
 *
 * A basic {@link Factory} that creates instances of a Vaadin {@link Component}
 */
class ComponentFactory extends AbstractFactory {

    private static final ATTRIBUTES_TO_SAVE = ['expandRatio','alignment']

    Class componentClass
    Map savedAttributes
    String nodeName

    ComponentFactory(Class<? extends Component> componentClass) {
        this.componentClass = componentClass
    }

    /**
     * Save any attributes for later processing. They are removed from the passed in attributes
     */
    protected void extractSavedAttributes(Map attributes) {
        savedAttributes = [:]
        ATTRIBUTES_TO_SAVE.each { savedAttributes[it] = attributes.remove(it) }
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {

        // Save our node name for exception reporting
        nodeName = name

        // Do the attribute saved, then return a new instance of the component class. The builder will set all bean (Component) properties set in the remaining attributes
        extractSavedAttributes(attributes)
        return componentClass.newInstance()
    }

    /**
     * Return a {@link String} attaching the nodeName from this {@link ComponentFactory} to the given node ({@link Component} object
     *
     * @param Component
     */
    public String namedNodeString(Component node) {
        return "$nodeName($node)" as String
    }
}
