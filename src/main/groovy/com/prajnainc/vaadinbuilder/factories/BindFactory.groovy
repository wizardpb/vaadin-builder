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
 */

package com.prajnainc.vaadinbuilder.factories

import com.prajnainc.vaadinbuilder.binding.DataBinding
import com.prajnainc.vaadinbuilder.binding.DataBindingFactory

/**
 * BindFactory
 *
 *
 */
class BindFactory extends AbstractFactory implements VaadinFactory {
    @Override
    Map getSavedAttributes() {
        return [:]
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {

        def source = attributes.remove('source');
        def sourceProperty = attributes.remove('sourceProperty')
        def target = attributes.remove('target')

        def factory = new DataBindingFactory(target: target, source: source, sourceProperty: sourceProperty)

        // If there is a target, create the binding , otherwise return the factory, the target will bind it later
        return target ? factory.createBinding() : factory
    }
}
