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

import com.prajnainc.vaadinbuilder.ui.BuilderUI;
/**
 * BuiltUI is a {@link BuiltUI} that gets its view definition from the text pane of the main {@link SamplerUI}. The UI is popped up
 * by a {@link com.vaadin.ui.Button{ on teh {@link SamplerUI}, and builds it's UI dynamically from that view definition
 *
 */
class BuiltUI extends BuilderUI {

    @Override
    def getViewDefinition() {
        return SamplerUI.instance.buildScript
    }
}
