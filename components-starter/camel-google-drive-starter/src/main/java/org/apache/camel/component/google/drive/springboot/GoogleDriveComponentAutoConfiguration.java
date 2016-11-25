/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.google.drive.springboot;

import java.util.HashMap;
import java.util.Map;
import org.apache.camel.CamelContext;
import org.apache.camel.component.google.drive.GoogleDriveComponent;
import org.apache.camel.util.IntrospectionSupport;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Generated by camel-package-maven-plugin - do not edit this file!
 */
@Configuration
@ConditionalOnBean(type = "org.apache.camel.springboot.CamelAutoConfiguration")
@AutoConfigureAfter(name = "org.apache.camel.springboot.CamelAutoConfiguration")
@EnableConfigurationProperties(GoogleDriveComponentConfiguration.class)
public class GoogleDriveComponentAutoConfiguration {

    @Bean(name = "google-drive-component")
    @ConditionalOnClass(CamelContext.class)
    @ConditionalOnMissingBean(GoogleDriveComponent.class)
    public GoogleDriveComponent configureGoogleDriveComponent(
            CamelContext camelContext,
            GoogleDriveComponentConfiguration configuration) throws Exception {
        GoogleDriveComponent component = new GoogleDriveComponent();
        component.setCamelContext(camelContext);
        Map<String, Object> parameters = new HashMap<>();
        IntrospectionSupport.getProperties(configuration, parameters, null,
                false);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            Object value = entry.getValue();
            Class<?> paramClass = value.getClass();
            if (paramClass.getName().endsWith("NestedConfiguration")) {
                Class nestedClass = null;
                try {
                    nestedClass = (Class) paramClass.getDeclaredField(
                            "CAMEL_NESTED_CLASS").get(null);
                    HashMap<String, Object> nestedParameters = new HashMap<>();
                    IntrospectionSupport.getProperties(value, nestedParameters,
                            null, false);
                    Object nestedProperty = nestedClass.newInstance();
                    IntrospectionSupport.setProperties(camelContext,
                            camelContext.getTypeConverter(), nestedProperty,
                            nestedParameters);
                    entry.setValue(nestedProperty);
                } catch (NoSuchFieldException e) {
                }
            }
        }
        IntrospectionSupport.setProperties(camelContext,
                camelContext.getTypeConverter(), component, parameters);
        return component;
    }
}