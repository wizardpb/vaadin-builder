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

import com.vaadin.data.util.MethodProperty
import com.vaadin.data.util.PropertysetItem

/**
 * GroovyMapItem
 *
 */
class GroovyMapItem extends PropertysetItem {

    private static methodPropertyFor(data,propName) {
        return new MethodProperty(
                Object,data,'get','put',
                [propName] as Object[],[propName,null] as Object[], 1
        )
    }

    GroovyMapItem(Map data) {
        this(data, data.keySet() as List)
    }

    GroovyMapItem(Map data,List propNames) {
        propNames.each {
            if(data.keySet().contains(it)) addItemProperty(it,methodPropertyFor(data,it))
        }
    }

}
