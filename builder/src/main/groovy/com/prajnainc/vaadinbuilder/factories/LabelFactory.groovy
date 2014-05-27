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

import com.vaadin.ui.Component
import com.vaadin.ui.Label

/**
 * A {@link LayoutFactory} creates a {@link Label} object, and sets it's value to the value passed in
 * form the node call. e.g.
 * <code>
 *     label('label text')
 * </code>
 * create a label with the text 'label text
 *
 */
class LabelFactory extends ComponentFactory {

    LabelFactory() {
        super(Label)
    }

    @Override
    protected setComponentValue(Component component, value, attributes) {
        component.value = value
    }
}
