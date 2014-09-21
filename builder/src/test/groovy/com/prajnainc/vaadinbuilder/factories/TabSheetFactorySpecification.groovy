/*
 * Copyright (c) 2014 Prajna Inc
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
package com.prajnainc.vaadinbuilder.factories

import com.prajnainc.vaadinbuilder.BuilderSpecification
import com.vaadin.ui.Label
import com.vaadin.ui.TabSheet

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
import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.CoreMatchers.instanceOf
import static spock.util.matcher.HamcrestSupport.that

public class TabSheetFactorySpecification extends BuilderSpecification {

    def "it creates a tab sheet"() {

        given:
        def tabSheet = builder.build {
            tabSheet()
        }

        expect:
        that tabSheet, instanceOf(TabSheet)
    }

    def "it can add a tab"() {

        given:
        TabSheet tabSheet = builder.build {
            tabSheet {
                label('tab 1')
            }
        }

        expect:
        tabSheet.componentCount == 1
        that tabSheet.getTab(0), instanceOf(TabSheet.Tab)
        that tabSheet.getTab(0).component, instanceOf(Label)
    }

    def "it sets the caption"() {

        given:
        TabSheet tabSheet = builder.build {
            tabSheet {
                label('tab 1', caption: 'Label1')
            }
        }

        expect:
        tabSheet.componentCount == 1
        that tabSheet.getTab(0).caption, equalTo('Label1')
    }
}
