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

package com.prajnainc.vaadinbuilder.ui

import com.prajnainc.vaadinbuilder.VaadinBuilder
import com.vaadin.server.VaadinRequest
import com.vaadin.ui.UI

/**
 * BuilderUI attaches a {@link VaadinBuilder} and a view definition (in the form that the builder can read} to a Vaadin
 * {@link UI}, the arranges to builds that view definition into a {@link com.vaadin.ui.Component} tree at init time,
 * and then attaches to  the UI
 *
 */
abstract class BuilderUI extends UI {

    VaadinBuilder builder

    /**
     * Return the view definition used by the builder to build the UI. This can be a {@link Closure},
     * a {@link Script} or a {@link String}
     *
     * @return
     */
    abstract def getViewDefinition();

    /**
     * Initialize myself by building the UI from my view definition, and setting the result as the content
     *
     * @param request
     */
    @Override
    protected void init(VaadinRequest request) {
        builder = new VaadinBuilder()
        content = builder.build(viewDefinition)
    }

}
