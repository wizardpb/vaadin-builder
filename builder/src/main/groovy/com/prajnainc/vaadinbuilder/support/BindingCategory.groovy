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
package com.prajnainc.vaadinbuilder.support

import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.prajnainc.vaadinbuilder.binding.DataBinding
import com.prajnainc.vaadinbuilder.binding.ItemBinding
import com.prajnainc.vaadinbuilder.binding.PropertyBinding
import com.vaadin.data.Item
import com.vaadin.data.Property
import com.vaadin.data.fieldgroup.FieldGroup

/**
 * BindingCategory
 *
 * A Category class to add polymorphic binding behavior to various source and target classes
 *
 */
class BindingCategory {

    static DataBinding bindTo(Item.Viewer self, Object source, String sourceProperty) {
        new ItemBinding(target: self, source: source, sourceProperty: sourceProperty)
    }

    static DataBinding bindTo(FieldGroup self, Object source, String sourceProperty) {
        new ItemBinding(target: self, source: source, sourceProperty: sourceProperty)
    }

    static DataBinding bindTo(Property.Viewer self, Object source, String sourceProperty) {
        new PropertyBinding(target: self, source: source, sourceProperty: sourceProperty)
    }

    static DataBinding bindTo(Object self, Object source, String sourceProperty) {
        throw new VaadinBuilderException("Cannot create a binding for target $self")
    }
}