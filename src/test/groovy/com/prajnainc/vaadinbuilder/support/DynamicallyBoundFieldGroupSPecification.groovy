package com.prajnainc.vaadinbuilder.support

import apple.awt.CCheckboxMenuItem
import com.vaadin.ui.CheckBox
import com.vaadin.ui.TextField;/**
 * DynamicallyBoundFieldGroupSpecification
 *
 *
 */

import spock.lang.*
import static org.hamcrest.CoreMatchers.*
import static spock.util.matcher.HamcrestSupport.*

public class DynamicallyBoundFieldGroupSpecification extends Specification {

    static class TestObject {
        String stringProp = 'string'
        int intProp = 1
        boolean boolProp = true
    }

    DynamicallyBoundFieldGroup fieldGroup

    def "it can build and bind from an instance"() {

        given:
        fieldGroup = new DynamicallyBoundFieldGroup(new TestObject())
        fieldGroup.buildAndBind('stringProp')
        fieldGroup.buildAndBind('boolProp')

        expect:
        fieldGroup.getBoundPropertyIds() as Set == ['stringProp','boolProp'] as Set
        that fieldGroup.getField('stringProp'), instanceOf(TextField)
        that fieldGroup.getField('boolProp'), instanceOf(CheckBox)

    }

    def "it can build and bind from a type"() {

        given:
        fieldGroup = new DynamicallyBoundFieldGroup(TestObject)
        fieldGroup.buildAndBind('stringProp')
        fieldGroup.buildAndBind('boolProp')

        expect:
        fieldGroup.getBoundPropertyIds() as Set == ['stringProp','boolProp'] as Set
        that fieldGroup.getField('stringProp'), instanceOf(TextField)
        that fieldGroup.getField('boolProp'), instanceOf(CheckBox)
    }
}
