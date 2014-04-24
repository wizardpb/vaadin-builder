package com.prajnainc.vaadinbuilder.factories

import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.vaadin.client.ui.Field
import com.vaadin.data.fieldgroup.FieldGroup

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

/**
 * DefaultFieldFactory
 *
 */
class DefaultFieldFactory extends FieldFactory {

    def DefaultFieldFactory() {
        super(null)
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {

        FieldGroup fieldGroup = findFieldGroup(builder.current)

        if(!fieldGroup) {
            throw new VaadinBuilderException("Cannot use a $name node without a field group")
        }

    }

}
