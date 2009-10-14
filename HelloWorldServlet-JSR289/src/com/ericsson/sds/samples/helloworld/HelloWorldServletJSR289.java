/* **********************************************************************
 * Copyright (c) Ericsson 2006. All Rights Reserved.
 * Reproduction in whole or in part is prohibited without the 
 * written consent of the copyright owner. 
 * 
 * ERICSSON MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY 
 * OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE, OR NON-INFRINGEMENT. ERICSSON SHALL NOT BE LIABLE FOR ANY 
 * DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR 
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. 
 * 
 * **********************************************************************/
package com.ericsson.sds.samples.helloworld;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;

import com.thoughtworks.xstream.XStream;
@javax.servlet.sip.annotation.SipServlet
public class HelloWorldServletJSR289 extends SipServlet
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 6906586311780779946L;

    /**
     * The SIP Factory. Can be used to create URI and requests.
     */
    @Resource
    private SipFactory sipFactory;

    /**
     * @inheritDoc
     */
    protected void doMessage(SipServletRequest req) throws ServletException,
            IOException {
        
        // This servlet is triggered by a MESSAGE to sip:greetings@ericsson.com.
        // Ignore the request if URI is not sip:greetings@ericsson.com.
        if (!"sip:greetings@ericsson.com"
                .equals(req.getRequestURI().toString())) {
            return;
        }

        // Send 200 OK to acknowledge request was received.
        req.createResponse(200).send();

        // Create a greeting MESSAGE to send back to the user.
        SipServletRequest messageRequest = sipFactory
                .createRequest(req.getApplicationSession(), "MESSAGE", req
                        .getTo(), req.getFrom());
        messageRequest.pushRoute(sipFactory
                .createSipURI(null, "127.0.0.1:5082"));
        messageRequest.addHeader("Accept-Contact", req
                .getHeader("Accept-Contact"));
        messageRequest.addHeader("User-Agent", req.getHeader("User-Agent"));

        // Set the message content.
        
        XStream xstream = new XStream();
        HelloWorldMsg msg = new HelloWorldMsg("Hello Michal and Ernest");
        String xml = xstream.toXML(msg);
        log(xml);
//        String xml = "Hello World brothers and sisters!";
//        String xml = "<com.ericsson.sds.samples.helloworld.HelloWorldMsg> <msg>Hello Michal and Ernest</msg> </com.ericsson.sds.samples.helloworld.HelloWorldMsg>";
        
        messageRequest.setContent(xml, "application/xml");
//        log("SENDING MESSAGE **************########################################");
//        messageRequest.setContent(new HelloWorldMsg(new String("Hello World !")), "application/x-java-serialized-object");
        

        // S-CSCF processes only asserted requests. When a request is sent 
        // without a P-Asserted-Identity header, it is rejected with a 
        // 403 Forbidden. This header asserts that the request comes from a 
        // trusted domain.
        messageRequest.addHeader("p-asserted-identity",
                "sip:helloworld@ericsson.com");

        // Send the request
        messageRequest.send();
    }
}
