package nl.jpoint.vertx.integration.java;
/*
 * Copyright 2013 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */

import nl.jpoint.vertx.HttpClusterVerticle;
import org.junit.Test;
import org.vertx.testtools.TestVerticle;

import static org.vertx.testtools.VertxAssert.*;

/**
 * Simple integration test which shows tests deploying other verticles, using the Vert.x API etc
 */
public class BasicIntegrationTest extends TestVerticle {

  @Test
  /*
  This demonstrates using the Vert.x API from within a test.
   */
  public void testHTTP() {
    // Create an HTTP server which just sends back OK response immediately
      System.out.println(HttpClusterVerticle.class.getName());
      getContainer().deployVerticle(HttpClusterVerticle.class.getName(), 1, asyncResult -> {
          assertTrue(asyncResult.succeeded());
          // The server is listening so send an HTTP request
          vertx.createHttpClient().setPort(8080).getNow("/", resp -> {
              assertEquals(200, resp.statusCode());
    /*
    If we get here, the test is complete
    You must always call `testComplete()` at the end. Remember that testing is *asynchronous* so
    we cannot assume the test is complete by the time the test method has finished executing like
    in standard synchronous tests
    */
              testComplete();
          });
    });


  }
}
