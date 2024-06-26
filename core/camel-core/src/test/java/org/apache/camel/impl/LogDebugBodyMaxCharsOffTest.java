/*
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
package org.apache.camel.impl;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spi.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogDebugBodyMaxCharsOffTest extends ContextTestSupport {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        context.getGlobalOptions().put(Exchange.LOG_DEBUG_BODY_MAX_CHARS, "-1");
    }

    @Override
    protected Registry createCamelRegistry() throws Exception {
        Registry registry = super.createCamelRegistry();
        registry.bind("logFormatter", new TraceExchangeFormatter());
        return registry;
    }

    @Test
    public void testLogBodyMaxLengthTest() throws Exception {
        // create a big body
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            int value = i % 10;
            sb.append(value);
        }
        String body = sb.toString();

        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMessageCount(1);

        template.sendBody("direct:start", body);

        assertMockEndpointsSatisfied();

        // should be empty body as toString on the message will return an empty
        // body
        TraceExchangeFormatter myFormatter
                = context.getRegistry().lookupByNameAndType("logFormatter", TraceExchangeFormatter.class);
        String msg = myFormatter.getMessage();
        assertTrue(msg.endsWith("Body: [Body is not logged]]"));

        // but body and clipped should not be the same
        assertNotSame(msg, mock.getReceivedExchanges().get(0).getIn().getBody(String.class),
                "clipped log and real body should not be the same");
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:start").to("log:foo").to("mock:result");
            }
        };
    }
}
