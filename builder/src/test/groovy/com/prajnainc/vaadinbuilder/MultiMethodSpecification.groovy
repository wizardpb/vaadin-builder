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
package com.prajnainc.vaadinbuilder;

import spock.lang.*

import java.beans.PropertyChangeEvent

import static org.hamcrest.CoreMatchers.*
import static spock.util.matcher.HamcrestSupport.*

public class MultiMethodSpecification extends Specification {

    static abstract class TestMeSuper {

        def testMe(PropertyChangeEvent e) {
            return 'Property Super'
        }
    }

    static class TestMe extends TestMeSuper {

        def testMe(PropertyChangeEvent e) {
            return 'Property + ' + super.testMe(e)
        }

        def testMe(ObservableList.ElementEvent e) {
            return 'List'
        }

        def testMe(ObservableSet.ElementEvent e) {
            return 'Set'
        }

    }

    def "type hierarchies perform correctly"() {

        expect:
        new TestMe().testMe(e) == type

        where:
        e                                                       |   type
        new PropertyChangeEvent(this,null,null,null)            | 'Property + Property Super'
        new ObservableList.ElementAddedEvent(this,null,0)       | 'List'
        new ObservableSet.ElementAddedEvent(this,null)          | 'Set'

    }
}
