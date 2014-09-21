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
package com.prajnainc.vaadinbuilder.binding

import com.vaadin.data.Container
import com.vaadin.data.Item
import com.vaadin.data.Property
import com.vaadin.server.ClientConnector


/**
 * A {@link DataBinding} instance is responsible for binding a data source to a UI {@link com.vaadin.ui.Component} such
 * that the component will update it's view when the data source changes. It encapsulates all knowledge
 * necessary to create a Vaadin data binding object (a {@link Property}, {@link Item} or a {@link Container} ),
 * from a source object, connect it to a target component, and react to value changes from the source, propagating
 * these to the UI
 */
interface DataBinding {

    /**
     * Create and activate the binding. The target should be set
     *
     * @return the binding that has been bound.
     *
     */
    public DataBinding bind();

    /**
     * Set and bind the target
     *
     * @param target
     * @return the binding that has been bound
     */
    public DataBinding bind(Object target);

    /**
     * Undo the binding, releasing any resources and observer connections in use
     *
     */
    public void unbind();

    /**
     * Set the target
     *
     * @param target
     */
    public void setTarget(Object target);

    /**
     * Target getter
     *
     * @return - the target
     */
    public Object getTarget();
}
