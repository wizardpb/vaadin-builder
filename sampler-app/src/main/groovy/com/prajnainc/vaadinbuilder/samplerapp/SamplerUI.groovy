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

package com.prajnainc.vaadinbuilder.samplerapp

import com.prajnainc.vaadinbuilder.VaadinBuilder
import com.vaadin.server.VaadinRequest
import com.vaadin.ui.UI;
/**
 * SamplerUI
 *
 */
class SamplerUI extends UI {

    def builder = new VaadinBuilder()

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        def view  = builder.build {
            panel(caption: "Top Panel") {
                verticalLayout(width: '100%', height: '100%') {
                    textArea(width: '100%', height: '100%', expandRatio: 1.0f,value: "Some text")
                }
            }
        }
        content = view
    }
}
