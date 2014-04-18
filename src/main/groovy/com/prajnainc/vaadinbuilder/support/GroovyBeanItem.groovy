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

package com.prajnainc.vaadinbuilder.support

import com.vaadin.data.util.BeanItem
import com.vaadin.data.util.PropertysetItem

/**
 * GroovyBeanItem
 *
 * A {@link GroovyBeanItem} is a Vaadin {@link com.vaadin.data.Item} that can wrap any object that implements
 * {@link GroovyObject}.
 *
 */
class GroovyBeanItem extends BeanItem {

    /**
     * Build a list of {@link com.vaadin.data.util.VaadinPropertyDescriptor}s for the given bean. We make
     * sure that extraneous properties (currently the metaClass property) are removed
     *
     * @param groovyBean
     * @return a List of descriptors for this object
     */
    private static List<String> getPropertyDescriptors(Object groovyBean) {
        def descriptors = groovyBean.metaClass.properties*.name
        descriptors.remove('metaClass')
        return descriptors
    }

    GroovyBeanItem(Object groovyBean) {
        super(groovyBean,getPropertyDescriptors(groovyBean))
    }

}
