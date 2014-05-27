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

import com.prajnainc.vaadinbuilder.ui.BuilderUI
import com.vaadin.server.BrowserWindowOpener
import com.vaadin.server.VaadinRequest
import com.vaadin.shared.ui.MarginInfo
import com.vaadin.ui.Button

/**
 * <p>{@link SamplerUI} is the main {@link com.vaadin.ui.UI} for the Sampler app. It implements a Vaadin Buidle rsmapler application that
 * allows textural builder scripts to be entered and built on teh fly, allowing experimentation and exploration of using
 * {@link com.prajnainc.vaadinbuilder.VaadinBuilder}</p>
 * <p>It has a larger text area in which a textual
 * builder definition can be typed, and a 'Build' {@link Button} that opens a new UI (a {@link BuiltUI} that uses that
 * definition to build its content.</p>
 *
 */
class SamplerUI extends BuilderUI {

    public static SamplerUI instance

    @Override
    def getViewDefinition() {
        return {
            verticalLayout(width: '100%', height: '100%', margin: new MarginInfo(true), spacing: true) {
                panel(caption: "UI Builder", width: '100%', height: '100%') {
                    verticalLayout( width: '100%', height: '100%', margin: new MarginInfo(true), spacing: true) {
                        textArea(caption: 'Build Script',id: 'buildScriptText', width: '100%', height: '100%', expandRatio: 1.0f)
                        button('Build!', id: 'buildButton')
                    }
                }
            }
        }
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        super.init(vaadinRequest)

        Button b = builder.buildButton

        // Create an opener extension
        BrowserWindowOpener opener = new BrowserWindowOpener(BuiltUI.class);
        opener.setFeatures("height=200,width=300,resizable");

        // Attach it to a button
        opener.extend(b);

        instance = this

    }

    String getBuildScript() {
        builder.buildScriptText.value
    }
}
