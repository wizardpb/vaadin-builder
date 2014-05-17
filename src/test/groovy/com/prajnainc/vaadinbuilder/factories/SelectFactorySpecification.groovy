package com.prajnainc.vaadinbuilder.factories

import com.prajnainc.vaadinbuilder.BuilderSpecification
import com.vaadin.ui.ComboBox
import com.vaadin.ui.ListSelect
import com.vaadin.ui.NativeSelect
import com.vaadin.ui.OptionGroup
import com.vaadin.ui.Table
import com.vaadin.ui.Tree
import com.vaadin.ui.TwinColSelect;/*
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

import spock.lang.*
import static org.hamcrest.CoreMatchers.*
import static spock.util.matcher.HamcrestSupport.*

public class SelectFactorySpecification extends BuilderSpecification {

    def "it builds the correct component"() {

        expect:

        def component = builder.build {
            "$node"()
        }

        that component, instanceOf(nodeClass)

        where:

        node                | nodeClass
        'comboBox'          | ComboBox
        'table'             | Table
        'twinColumnSelect'  | TwinColSelect
        'nativeSelect'      | NativeSelect
        'listSelect'        | ListSelect
        'optionGroup'       | OptionGroup
        'tree'              | Tree
    }

}
