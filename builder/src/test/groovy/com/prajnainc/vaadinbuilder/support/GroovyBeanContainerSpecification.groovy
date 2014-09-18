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
 *
 *
 */
package com.prajnainc.vaadinbuilder.support;

import spock.lang.*
import static org.hamcrest.CoreMatchers.*
import static spock.util.matcher.HamcrestSupport.*

public class GroovyBeanContainerSpecification extends Specification {

    def "it can be created with a list of Map definitions"() {

        given:
        def container = new GroovyBeanContainer([
                new GroovyObjectPropertyDescriptor(name: 'prop1'),
                new GroovyObjectPropertyDescriptor(name: 'prop2', propertyType: String),
                new GroovyObjectPropertyDescriptor(name: 'prop3', propertyType: String, defaultValue: '')
        ])

        expect:
        that container.containerPropertyIds, equalTo(['prop1','prop2','prop3'] as Set)
    }
}
