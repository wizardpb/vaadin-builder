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
package com.prajnainc.vaadinbuilder.factories

import com.prajnainc.vaadinbuilder.BuilderSpecification
import com.vaadin.server.FileResource
import com.vaadin.ui.Alignment
import com.vaadin.ui.Table
import groovy.transform.NotYetImplemented;

import static org.hamcrest.CoreMatchers.*
import static spock.util.matcher.HamcrestSupport.*

public class TableColumnFactorySpecification extends BuilderSpecification {

    def "it can build a table with columns"() {

        given:
        def table = builder.build {
            table(id: 'table') {
                tableColumn('column1')
            }
        }


        expect:
        that table, instanceOf(Table)
        that builder.table, sameInstance(table)
        that table.containerPropertyIds as List,equalTo(['column1'])
    }

    @NotYetImplemented
    def "it can build a columns with headers, icons and alignment"() {

        given:
        def icon = new FileResource(new File('/some/file'))
        Table t = builder.build {
            table(id: 'table') {
                tableColumn('column1',header:'Column One',alignment: Alignment.BOTTOM_LEFT, icon: icon)
            }
        }

        expect:
        that t.getColumnAlignment('column1'),equalTo(Alignment.BOTTOM_LEFT)
        that t.getColumnHeader('column1'), equalTo('Column One')
        that t.getColumnIcon(),sameInstance(icon)
    }

    def "it can build a columns with a type"() {

        given:

        Table table = builder.build {
            table(id: 'table') {
                tableColumn('column1',type: Date)
            }
        }


        expect:
        that table.getContainerDataSource().@types['column1'], sameInstance(Date)
    }

}
