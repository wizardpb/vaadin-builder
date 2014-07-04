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

import java.beans.PropertyChangeEvent

/**
 * A {@link ContainerBinding} binds a data source for a {@link com.vaadin.data.Container.Viewer}. It treats the souce value
 * as a {@link Collection} of values that are treated as objects to be bound as {@link com.vaadin.data.Item}s, and wraps them
 * in an appropriate {@link com.vaadin.data.Container}
 *
 */
class ContainerBinding extends AbstractDataBinding {

    @Override
    protected void bindSourceProperty() {

    }

    @Override
    protected void bindSource() {

    }

    @Override
    void propertyChange(PropertyChangeEvent evt) {

    }
}
