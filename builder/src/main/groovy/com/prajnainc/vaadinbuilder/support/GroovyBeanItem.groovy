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

import com.vaadin.data.util.PropertysetItem

/**
 * A {@link GroovyBeanItem} is a Vaadin {@link com.vaadin.data.Item} that can wrap any object that implements
 * {@link GroovyObject}. It creates a {@link com.vaadin.data.Property} for all properties on the bound object
 *
 */
class GroovyBeanItem extends PropertysetItem {

    private Object bean

    private static List HIDDEN_PROPERTIES = ['class', 'propertyChangeListeners']

    GroovyBeanItem(GroovyObject groovyBean,List properties) {
        this.bean = groovyBean
        properties.each {
            addItemProperty(it,new GroovyObjectProperty(groovyBean,it))
        }
    }

    GroovyBeanItem(GroovyObject groovyBean) {
        this(groovyBean,groovyBean.metaClass.getProperties()*.name - HIDDEN_PROPERTIES)
    }

    GroovyBeanItem(Map mapBean,List properties) {
        this.bean = mapBean
        properties.each {
            addItemProperty(it,new GroovyObjectProperty(mapBean,it))
        }
    }

    GroovyBeanItem(Map mapBean) {
        this(mapBean,mapBean.keySet() as List)
    }

    public Object getBean() {
        return bean
    }
}
