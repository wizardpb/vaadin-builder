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
package com.prajnainc.vaadinbuilder.factories

import com.prajnainc.vaadinbuilder.VaadinBuilderException
import com.prajnainc.vaadinbuilder.support.MenuItemCommand
import com.vaadin.ui.MenuBar
import com.vaadin.ui.MenuBar.MenuItem

/**
 *
 * <p>A {@link MenuItemFactory} creates and adds {@link MenuItem}s to a {@link MenuBar}. This departs from the normal
 * pattern of creating a component then separately adding it, since {@link MenuBar} and (@link MenuItem} do
 * not possess methods to do this. Instead, they create and add the items in a single action. The
 * factory must therefore create and add in {@link Factory#newInstance}. It gets the parent from
 * the builder to do this.</p>
 *
 * <p>Separators also need special handling.</p>
 *
 */

class MenuItemFactory extends ComponentFactory {

    MenuItemFactory() {
        super(MenuItem)
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
        throws InstantiationException, IllegalAccessException
    {
        def parent = builder.getCurrent()
        def menuItem = null
        if(name == 'separator') {
            if(parent instanceof MenuBar) {
                throw new VaadinBuilderException("Cannot add separators to a menu bar")
            }
            parent.addSeparator()
        } else {
            def icon = attributes.remove('icon')
            def action = attributes.remove('action')
            menuItem = parent.addItem(value, icon, new MenuItemCommand(action: action))
        }
        return menuItem
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        // Nothing to do
    }
}
