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
import com.vaadin.ui.AbstractOrderedLayout
import org.codehaus.groovy.runtime.typehandling.GroovyCastException

/**
 * OrderedLayoutFactory creates instances of subclasses of {@link AbstractOrderedLayout}
 *
 */

class OrderedLayoutFactory extends LayoutFactory {


    OrderedLayoutFactory(Class<? extends AbstractOrderedLayout> componentClass) {
        super(componentClass)
    }

    @Override
    protected void setExpandRatioFrom(VaadinBuilder childBuilder, Object parent, Object child) {
        if (childBuilder.context.savedAttributes.containsKey(VaadinBuilder.EXPAND_RATIO_ATTR)) {
            Float ratio
            Object ratioValue = childBuilder.context.savedAttributes[VaadinBuilder.EXPAND_RATIO_ATTR]
            try {
                ratio = ratioValue as Float
            } catch (GroovyCastException e) {
                def clsName = ratioValue.getClass().simpleName
                throw new VaadinBuilderException(
                    "The $clsName value '$ratioValue' cannot be converted to an expand ratio. It must be a number",
                    e)
            }
            assert parent instanceof AbstractOrderedLayout
            parent.setExpandRatio(child, ratio)
        }
    }
}
