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
import com.vaadin.data.util.IndexedContainer
import com.vaadin.server.FileResource
import com.vaadin.ui.Table
import groovy.beans.Bindable

import static org.hamcrest.CoreMatchers.*
import static spock.util.matcher.HamcrestSupport.that

public class TableColumnFactorySpecification extends BuilderSpecification {

    static class Model {
        @Bindable data
    }

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
        that table.containerPropertyIds as List, equalTo(['column1'])
    }

    def "it can build a columns with headers, icons and alignment"() {

        given:
        def icon = new FileResource(new File('/some/file'))
        Table t = builder.build {
            table(id: 'table') {
                tableColumn('column1', header:'Column One', alignment: Table.Align.CENTER , icon: icon)
            }
        }

        expect:
        that t.getColumnAlignment('column1'), equalTo(Table.Align.CENTER )
        that t.getColumnHeader('column1'), equalTo('Column One')
        that t.getColumnIcon('column1'), sameInstance(icon)
    }

    def "it can build a columns with a type"() {

        given:

        Table table = builder.build {
            table(id: 'table') {
                tableColumn('column1', modelType: Date)
            }
        }


        expect:
        that table.getContainerDataSource().@types['column1'], sameInstance(Date)
    }

    def "it can bind to an explicit data source"() {
        given:
        def dataSource =  new IndexedContainer()
        Table table = builder.build {
            table(id: 'table', containerDataSource: dataSource) {
                tableColumn('column1', modelType: Date)
            }
        }

        expect:
        that table.getContainerPropertyIds() as List, is(['column1'])
        that dataSource.getContainerPropertyIds() as List, is(['column1'])

    }

    def "it can bind to a Map data source"() {
        given:
        def bean = [column1: 'column1', column2: 'column2']
        def model = new Model(data: [bean])
        Table table = builder.build {
            table(id: 'table', dataSource: bind(source: model, sourceProperty:'data')) {
                tableColumn('column1')
                tableColumn('column2')
            }
        }

        expect:
        that table.getContainerPropertyIds() as List, is(['column1', 'column2'])
        that table.getItem(bean).getItemProperty('column1').value, is('column1')
        that table.getItem(bean).getItemProperty('column2').value, is('column2')

    }

    static class TestBean {
        def prop1
        def prop2
    }
    
    def "it can bind to a bean data source"() {
        given:
        def bean = new TestBean(prop1: 'val1', prop2: 2)
        def model = new Model(data: [bean])
        Table table = builder.build {
            table(id: 'table', dataSource: bind(source: model, sourceProperty:'data')) {
                tableColumn('prop1')
                tableColumn('prop2')
            }
        }

        expect:
        that table.size(), is(1)
        that table.getContainerPropertyIds() as List, is(['prop1', 'prop2'])
        that table.getItem(bean).getItemProperty('prop1').value, is('val1')
        that table.getItem(bean).getItemProperty('prop2').value, is(2)

    }

    def "it can rebind the table when the source changes"() {
        given:
        def bean = new TestBean(prop1: 'val1', prop2: 2)
        def model = new Model(data: [bean])
        Table table = builder.build {
            table(id: 'table', dataSource: bind(source: model, sourceProperty:'data')) {
                tableColumn('prop1')
                tableColumn('prop2')
            }
        }
        model.data = [
                new TestBean(prop1: 'newVal1-1', prop2: 'newVal1-2'),
                new TestBean(prop1: 'newVal2-1', prop2: 'newVal2-2'),
        ]

        expect:
        that table.size(), is(2)
        that table.getContainerPropertyIds() as List, is(['prop1', 'prop2'])
        that table.getItem(model.data[0]).getItemProperty('prop1').value, is('newVal1-1')
        that table.getItem(model.data[0]).getItemProperty('prop2').value, is('newVal1-2')
        that table.getItem(model.data[1]).getItemProperty('prop1').value, is('newVal2-1')
        that table.getItem(model.data[1]).getItemProperty('prop2').value, is('newVal2-2')    }
}
