package com.prajnainc.vaadinbuilder.support

import com.vaadin.ui.VerticalLayout
import org.junit.Test

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

public class ReflectionTest extends GroovyTestCase {

    static List withAllSuperclasses(Class cls) {
        def clsList = []
        for(; cls != null; cls = cls.getSuperclass()) {
            clsList << cls
        }
        return clsList
    }

    @Test
    void testMe() {
        withAllSuperclasses(VerticalLayout).each { cls ->
            def allInterfaces = cls.getInterfaces().inject(new HashSet<Class>()) { l, Class iface ->
                l << iface
                l.addAll(iface.getInterfaces())
                l
            }
            println "${cls.getSimpleName()}: implements: ${allInterfaces. collect { it.getSimpleName()}}"
        }
    }
}
