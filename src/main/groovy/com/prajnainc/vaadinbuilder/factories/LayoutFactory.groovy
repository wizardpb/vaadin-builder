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

import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.vaadin.ui.AbstractLayout
import com.vaadin.ui.Alignment

/**
 * LayoutFactory
 *
 */

class LayoutFactory extends ComponentContainerFactory {

    LayoutFactory(Class <? extends AbstractLayout> componentClass) {
        super(componentClass)
    }

    @Override
    protected void setAlignmentFrom(ComponentFactory childFactory) {
        // Apply child alignment via my component
        if(childFactory.savedAttributes.containsKey('alignment')) {
            assert component instanceof AbstractLayout
            component.setComponentAlignment(childFactory.component,createAlignment(savedAttributes['alignment']))
        }
    }

    private static Alignment createAlignment(Object alignmentValue) {
        if(!alignmentValue instanceof Alignment) {
            throw new VaadinBuilderException("'$alignmentValue' is not a valid Alignment value")
        }
        return (Alignment)alignmentValue
    }
}
