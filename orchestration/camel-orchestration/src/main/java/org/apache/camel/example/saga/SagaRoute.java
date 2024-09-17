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
package org.apache.camel.example.saga;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestParamType;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.function.BiFunction;

@Component
public class SagaRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        rest().post("/saga")
                .param().type(RestParamType.query).name("id").dataType("int").required(true).endParam()
                .to("direct:start-orchestation")
        ;


        from("direct:start-orchestation")
                //generiamo il job id
                .process(exchange -> {exchange.setProperty("job-id", "JID: "+((long)(Math.random()*10000000)));})
                .log("Impostato id ${header.job-id}")
                //avviamo la saga
                .wireTap("direct:saga")
                .setBody(simple("{\"id\":\"${header.job-id}\"}"));



        from("direct:saga")
                .saga()
                    .compensation("direct:cancelOrder")
                    .log("Executing saga #${header.id} , job-id ${header.job-id} with LRA ${header.Long-Running-Action}")

                .process(new NotifierProcessor(AsyncJobEventNotification.EVENT.STARTED))
                .log("Body: ${body}")
                .to("jms:queue:{{example.services.async-job-registry}}?exchangePattern=InOnly")
                .process(new RestoreBody())

                    .setHeader("payFor", constant("train"))
                    .to("jms:queue:{{example.services.train}}?exchangePattern=InOut" +
                            "&replyTo={{example.services.train}}.reply")
                    .log("train seat reserved for saga #${header.id} with payment transaction: ${body}")
/*                    .setHeader("payFor", constant("flight"))
                    .to("jms:queue:{{example.services.flight}}?exchangePattern=InOut" +
                            "&replyTo={{example.services.flight}}.reply")*/
                .delay(50000)
                    .asyncDelayed()
                .end()
                .to("direct:ok")
                .setBody(header("Long-Running-Action"))
                .end();

        from("direct:cancelOrder")
                .log("Transaction ${header.Long-Running-Action} has been cancelled due to flight or train failure")
                .setHeader("errore",constant("Errorissimo!!!"))
                .process(new NotifierProcessor(AsyncJobEventNotification.EVENT.FAILED))
                .to("jms:queue:{{example.services.async-job-registry}}?exchangePattern=InOnly")
        ;
        from("direct:ok")
                .log("flight booked for saga #${header.id} with payment transaction: ${body}")
                .process(new NotifierProcessor(AsyncJobEventNotification.EVENT.COMPLETED))
                .to("jms:queue:{{example.services.async-job-registry}}?exchangePattern=InOnly");
    }




}

class RestoreBody implements Processor{
    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getMessage().setBody(exchange.getProperty("previousBody"));
    }


    private static ObjectMapper om  = new ObjectMapper();
    private static String toJson(AsyncJobEventNotification not){
        try {
            return om.writeValueAsString(not);
        } catch (JsonProcessingException e) {
            return "{\"error\":\"Accpicchia!!\"}";
        }
    }

}
class NotifierProcessor implements Processor{
    private AsyncJobEventNotification.EVENT event;

    public NotifierProcessor(AsyncJobEventNotification.EVENT event) {
        this.event = event;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.setProperty("previousBody",exchange.getMessage().getBody());
        String jobId = (String) exchange.getProperty("job-id");
        String larId = (String) exchange.getMessage().getHeader("Long-Running-Action");
        String error = (String) exchange.getMessage().getHeader("error");
        exchange.getMessage().setBody(toJson(new AsyncJobEventNotification(jobId,larId,error,event)));
    }


    private static ObjectMapper om  = new ObjectMapper();
    private static String toJson(AsyncJobEventNotification not){
        try {
            return om.writeValueAsString(not);
        } catch (JsonProcessingException e) {
            return "{\"error\":\"Accpicchia!!\"}";
        }
    }

}


record AsyncJobEventNotification(
    String jobID,
    String lraID,
    String notes,
    EVENT event) {
    enum EVENT {
        STARTED,
        COMPLETED,
        FAILED;
    }

}
