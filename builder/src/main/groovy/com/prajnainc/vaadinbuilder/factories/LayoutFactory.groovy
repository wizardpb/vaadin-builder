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

import com.prajnainc.vaadinbuilder.VaadinBuilder
import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.vaadin.ui.AbstractLayout
import com.vaadin.ui.Alignment

/**
 * LayoutFactory is the base class for all {@link Factory} classes that create {@link com.vaadin.ui.Layout}s
 *
 */

class LayoutFactory extends ComponentContainerFactory {

    LayoutFactory(Class <? extends AbstractLayout> componentClass) {
        super(componentClass)
    }

    @Override
    protected void setAlignmentFrom(VaadinBuilder childBuilder, Object parent, Object child) {
        // Apply child alignment to the parent
        def context = childBuilder.context

        if(context.savedAttributes.containsKey(VaadinBuilder.ALIGNMENT_ATTR)) {
            def alignment = context.savedAttributes[VaadinBuilder.ALIGNMENT_ATTR]
            assert parent instanceof AbstractLayout
            parent.setComponentAlignment(child,createAlignment(alignment))
        }
    }

    private static Alignment createAlignment(Object alignmentValue) {
        if(!alignmentValue instanceof Alignment) {
            throw new VaadinBuilderException("'$alignmentValue' is not a valid Alignment value")
        }
        return (Alignment)alignmentValue
    }
}
