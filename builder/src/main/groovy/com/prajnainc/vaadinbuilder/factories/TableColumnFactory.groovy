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

import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.prajnainc.vaadinbuilder.binding.TableBinding
import com.vaadin.server.Resource
import com.vaadin.ui.Alignment
import com.vaadin.ui.Table

/**
 * A {@link TableColumnFactory} creates table columns within a table. The implementation does not create components
 * as most other factories ({@link Factory}) do, as columns are not represented as separate {@link com.vaadin.ui.Component}s.
 * <p>
 * Instead, the factory adds property ids to the parent table, including setting header, icon and alignment, etc.
 *
 *
 */
class TableColumnFactory extends AbstractFactory implements VaadinFactory {

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        return [value: value, attributes: attributes]
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {

        if(!parent instanceof Table) {
            throw new VaadinBuilderException('table columns can only be created as children of a table')
        }

        def attributes = child.attributes
        Table table = parent
        String propertyId = child.value ?: attributes.name
        if(!propertyId) {
            throw new VaadinBuilderException('Table columns must be named')
        }
        String header = attributes.header ?: propertyId
        Resource icon = attributes.icon
        Class type = attributes.type ?: Object
        def defaultValue = attributes.defaultValue

        // Builder saves general attribute 'alignment' here
        Table.Align alignment = builder.savedAttributes.alignment

        // If the data is bound, add it to the binding so we retain it across re-binds
        TableBinding binding = table.data?.binding
        if(binding) {
            binding.addDescriptor(propertyId,type,defaultValue)  // This also adds it as a container property

            // Set the column properties
            table.setColumnHeader(propertyId,header)
            table.setColumnIcon(propertyId,icon)
            table.setColumnAlignment(propertyId,alignment)
        } else {
            // Just add it to the table directly
            table.addContainerProperty(propertyId,type,null,header,icon,alignment)
        }
    }
}
