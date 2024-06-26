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
package org.apache.camel.processor;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.junit.jupiter.api.Test;

public class SplitterUoWIssueTest extends ContextTestSupport {

    @Test
    public void testSplitterUoWIssue() throws Exception {
        getMockEndpoint("mock:foo").expectedBodiesReceived("A", "B", "C", "D", "E");
        getMockEndpoint("mock:result").expectedBodiesReceived("A,B,C,D,E");

        template.sendBodyAndHeader(fileUri(), "A,B,C,D,E", Exchange.FILE_NAME, "splitme.txt");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testSplitterTwoFilesUoWIssue() throws Exception {
        getMockEndpoint("mock:foo").expectedBodiesReceived("A", "B", "C", "D", "E", "F", "G", "H", "I");
        getMockEndpoint("mock:result").expectedBodiesReceived("A,B,C,D,E", "F,G,H,I");

        template.sendBodyAndHeader(fileUri(), "A,B,C,D,E", Exchange.FILE_NAME, "a.txt");
        template.sendBodyAndHeader(fileUri(), "F,G,H,I", Exchange.FILE_NAME, "b.txt");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(fileUri("?initialDelay=0&delay=10&delete=true&sortBy=file:name"))
                        .split(body().tokenize(",")).to("seda:queue").end()
                        .log("End of file ${file:name}").to("mock:result");

                from("seda:queue").log("Token: ${body}").to("mock:foo");
            }
        };
    }
}
