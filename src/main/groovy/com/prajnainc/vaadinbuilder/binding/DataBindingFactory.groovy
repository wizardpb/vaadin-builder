package com.prajnainc.vaadinbuilder.binding

import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.vaadin.ui.Component

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
 * DataBindingFactory
 *
 */
class DataBindingFactory {

    Component target
    Object source
    String sourceProperty

    public DataBinding createBinding() {
        assert source != null && target != null
        DataBinding binding
        switch(source) {
            case Component: binding = createForComponent(); break;
            default: binding = createForModel(); break;
        }
        return binding
    }

    public DataBinding createBindingForTarget(Component target) {
        this.target = target
        return createBinding()
    }

    private DataBinding createForModel() {
        assert sourceProperty != null
    }

    private DataBinding createForComponent() {

    }
}
