/*
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
 */

package com.prajnainc.vaadinbuilder

import com.vaadin.ui.VerticalLayout
import org.codehaus.groovy.tools.GroovyClass;
import spock.lang.*
import static org.hamcrest.CoreMatchers.*
import static spock.util.matcher.HamcrestSupport.*

public class VaadinBuilderSpecification extends BuilderSpecification {

    def "it can build from a Closure"() {
        given:
        def ui = builder.build {
            verticalLayout {
                label('Label')
            }
        }

        expect:
        ui instanceof VerticalLayout
        ui.componentCount == 1
    }

    def "it can build from a Script object"() {
        given:
        def scriptText = "verticalLayout { label('Label') }"
        def script = new GroovyClassLoader(getClass().classLoader).parseClass(scriptText)
        def ui = builder.build (script)

        expect:
        ui instanceof VerticalLayout
        ui.componentCount == 1
    }

    def "it can build from a String script"() {
        given:
        def ui = builder.build("verticalLayout { label('Label') }")

        expect:
        ui instanceof VerticalLayout
        ui.componentCount == 1

    }
}
