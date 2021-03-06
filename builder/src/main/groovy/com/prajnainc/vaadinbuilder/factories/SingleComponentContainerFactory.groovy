/*
 *    Copyright (c) 2014 Prajna Inc
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

import com.prajnainc.vaadinbuilder.binding.DataBinding

/**
 * SingleComponentContainerFactory
 *
 */

class SingleComponentContainerFactory extends ComponentFactory {

    SingleComponentContainerFactory(Class componentClass) {
        super(componentClass)
    }

    @Override
    protected void setChildComponent(FactoryBuilderSupport builder, Object parent, Object child) {
        parent.setContent(child)
    }

    protected void setChildComponent(FactoryBuilderSupport builder, Object parent, DataBinding child) {
        // Data binding don't get added as children
    }
}
