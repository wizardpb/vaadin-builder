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
import com.prajnainc.vaadinbuilder.binding.DataBinding
import com.vaadin.data.fieldgroup.FieldGroup
import com.vaadin.ui.FormLayout
import com.vaadin.ui.Layout

/**
 * <p>A {@link FieldGroupFactory} takes an instance of a {@link Layout} as an attribute and attaches a
 * {@link FieldGroup} to it. This then becomes a container for further nodes, usually {@link com.vaadin.ui.Field}s.
 * When building contained {@link com.vaadin.ui.Field}s, the {@link FieldFactory} looks for a {@link FieldGroup} on a
 * containing node, and arranges for these {@link com.vaadin.ui.Field}s to be bound using that {@link FieldGroup}.
 * The arrangement allows forms of varying complexity to be build using the full range of layouts and containers,
 * as well as multiple field groups.</p>
 *
 * <p>Since the {@link FieldGroupFactory} returns a {@link Layout}, but is not a {@link LayoutFactory},
 * it must appear as such to the builder for the rest of the node building process. It does this by masquerading as a
 * {@link LayoutFactory} by delegating to an instance of such that would have built the {@link Layout} passed in. It
 * does this using a {@link Delegate}, and resetting the delegate factory dynamically on every node call.</p>
 *
 * <p>The created {@link com.vaadin.data.fieldgroup.FieldGroup} is given an id of "${layoutId}.fieldGroup" and also
 * attached to the data field of the new layout, along with any {@link DataBinding} passed in via the 'itemDataSource'
 * attribute</p>
 *
 */
class FieldGroupFactory implements Factory {

    /**
     * Delegate to the layout factory for any other Factory services. This allows the factory to create both the
     * field group and the layout, and appear to the builder as a layout factory.
     *
     * Note that the delegate is dynamically updated on every node build in accordance with the layout type required
     */
    @Delegate LayoutFactory layoutFactory

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
        throws InstantiationException, IllegalAccessException
    {
        def layout = attributes.remove('layout') ?: new FormLayout()
        if(!(layout instanceof Layout)) {
            throw new VaadinBuilderException("Unknown layout type '${layout.getClass().name}'")
        }
        layoutFactory = builder.factories[getLayoutNodeName(layout)]

        if(layoutFactory == null) {
            throw new VaadinBuilderException("No factory known for layout type '${layout.getClass().name}'")
        }

        // Create the factory and id it if it's there
        def fieldGroup = new FieldGroup()
        def id = attributes.remove('id') ?: value
        if(id != null) {
            builder.setVariable(id, fieldGroup)
        }

        def layoutId = attributes.remove('layoutId')
        if(layoutId != null) {
            // Swap in the layout id as the new id so the builder id's the layout as this value
            attributes['id'] = layoutId
        }

        // Save the field group on the layout and return it - initialize it if need be
        (layout.data ?: (layout.data = [:])).fieldGroup = fieldGroup
        return layout
    }

    @Override
    void onFactoryRegistration(FactoryBuilderSupport builder, String registeredName, String registeredGroupName) {
        // No action - needed to stop a Missing Method call to the delegate
    }

    private String getLayoutNodeName(Layout layout) {
        String name = layout.getClass().simpleName
        return name[0].toLowerCase() + name[1..-1]
    }
}
