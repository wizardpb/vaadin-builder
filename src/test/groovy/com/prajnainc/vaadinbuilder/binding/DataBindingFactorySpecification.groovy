package com.prajnainc.vaadinbuilder.binding

import com.prajnainc.vaadinbuilder.support.GroovyBeanItem
import com.prajnainc.vaadinbuilder.support.GroovyMapItem
import com.prajnainc.vaadinbuilder.support.GroovyObjectProperty
import com.vaadin.data.Item
import com.vaadin.data.Property
import com.vaadin.ui.Label
import groovy.beans.Bindable;

import spock.lang.*
import static org.hamcrest.CoreMatchers.*
import static spock.util.matcher.HamcrestSupport.*

public class DataBindingFactorySpecification extends Specification {

    def propertyViewer = Mock(Property.Viewer)
    def itemViewer = Mock(Item.Viewer)

    static class TestModel {
        @Bindable
        def modelProp = [prop1: 'prop1']
    }

    TestModel testModel = new TestModel()

    def "it can create and bind a Bindable to a Property.Viewer"() {

        when:
        testModel.modelProp = 'string1'
        new DataBindingFactory(source: testModel, sourceProperty: 'modelProp').bind(propertyViewer)

        then:
        1 * propertyViewer.setPropertyDataSource(_ as GroovyObjectProperty)
    }

    def "it can create and bind a Bindable to an Item.Viewer"() {
        when:

        new DataBindingFactory(source: testModel,sourceProperty: 'modelProp').bind(itemViewer)
        testModel.modelProp = [prop1: 'newProp1']

        then:
        // It sets the item source once when bound, then again when the bound property changes
        2 * itemViewer.setItemDataSource(_ as GroovyMapItem)
    }
}
