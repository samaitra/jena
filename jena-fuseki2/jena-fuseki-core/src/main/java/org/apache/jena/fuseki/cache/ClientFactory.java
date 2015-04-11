/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.apache.jena.fuseki.cache;

import org.apache.jena.fuseki.Fuseki;
import org.apache.jena.fuseki.server.SystemState;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ClientFactory {



    private static Logger log = Fuseki.cacheLog ;

    private static ClientFactory instance;

    private static String memcachedHost = "localhost";

    private static Integer memcachedPort = 11211;

    private static ClientFactory getInstance(){

        if(instance==null){
            instance = new ClientFactory();
            return instance;
        }
        return instance;
    }

    public static CacheClient getClient(String clientType) {

        if (clientType == null)
            return null;
        else if (clientType.equals("guava"))
            return new GuavaCacheClient();
        else if (clientType.equals("memcached")) {
            memcachedHost = System.getProperty("memcached.host");
            log.info("memcachedHost is "+memcachedHost);
            try {
                memcachedPort = Integer.parseInt(System.getProperty("memcached.port"));
                log.info("memcachedPort is "+memcachedPort);
            }catch (NumberFormatException ex){
                log.error("Unable to parse Memcached Port ",ex);
            }

            if(memcachedHost!=null && memcachedPort!=null){
                return new SpyMemcachedClient(memcachedHost,memcachedPort);
            }


        }
        return null;

    }


}