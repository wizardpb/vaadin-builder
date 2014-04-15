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

import com.vaadin.ui.Label;/**
 * Created by Paul Bennett on 4/14/14.
 *
 * SingleComponentContainerSpecification
 *
 */

import spock.lang.*
import static org.hamcrest.CoreMatchers.*

public class SingleComponentContainerSpecification extends Specification {

    def "it sets the content"() {

        expect:
        def c = new VaadinBuilder().build {
            "$node"() {
                label('contained')
            }
        }
        def content = c.content
        content instanceOf(Label)
        content.value == 'contained'

        where:

        node        | caption
        'panel'     | ''
        'window'    | ''
    }
}
