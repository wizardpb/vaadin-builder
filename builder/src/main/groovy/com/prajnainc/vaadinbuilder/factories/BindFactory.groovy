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

import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.prajnainc.vaadinbuilder.binding.DataBindingFactory

/**
 * BindFactory
 *
 *
 */
class BindFactory extends AbstractFactory {

    Map getSavedAttributes() {
        return [:]
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
        throws InstantiationException, IllegalAccessException {

        def source = attributes.remove('source');
        def sourceProperty = attributes.remove('sourceProperty')
        def target = attributes.remove('target')

        if(!source) {
            throw new VaadinBuilderException("No source provided for property $sourceProperty")
        }

        def factory = new DataBindingFactory(target: target, source: source, sourceProperty: sourceProperty)
        return factory.createBinding()

    }
}
