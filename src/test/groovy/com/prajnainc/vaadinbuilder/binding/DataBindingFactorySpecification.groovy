package com.prajnainc.vaadinbuilder.binding

import com.prajnainc.vaadinbuilder.support.GroovyBeanItem
import com.prajnainc.vaadinbuilder.support.GroovyMapItem
import com.vaadin.data.Item
import groovy.beans.Bindable;/**
 * DataBindingFactorySpecification
 *
 *
 */

import spock.lang.*
import static org.hamcrest.CoreMatchers.*
import static spock.util.matcher.HamcrestSupport.*

public class DataBindingFactorySpecification extends Specification {

    def itemViewer = Mock(Item.Viewer)

    static class TestModel {
        @Bindable
        def modelProp = [prop1: 'prop1']
    }

    TestModel testModel = new TestModel()

    def "it can create bindings for an Item.Viewer"() {
        when:

        new DataBindingFactory(source: testModel,sourceProperty: 'modelProp').bind(itemViewer)

        then:
        1 * itemViewer.setItemDataSource(_ as GroovyMapItem)
    }
}
