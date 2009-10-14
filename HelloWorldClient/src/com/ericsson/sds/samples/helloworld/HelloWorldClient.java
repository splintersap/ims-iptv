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

import com.ericsson.icp.ICPFactory;
import com.ericsson.icp.IPlatform;
import com.ericsson.icp.IProfile;
import com.ericsson.icp.IService;
import com.thoughtworks.xstream.XStream;

public class HelloWorldClient
{
    /**
     * Is set to <code>true</code> once the expected message is received.
     */
    private static boolean isResponseReceived = false;

    public static void main(String[] args)
    {
        try
        {
            // create the platform and its corresponding listener
            IPlatform platform = ICPFactory.createPlatform();
            // register client application
            platform.registerClient("HelloWorldClient");
            platform.addListener(new PlatformAdapter());

            // create the profile and its corresponding listener
            IProfile profile = platform.createProfile("IMSSetting");
            profile.addListener(new ProfileAdapter());

            // create an instance of a service. This can be used to send instant messages afterwards.
            IService ghelloworldericssoncomService = profile.createService("+g.helloworld.ericsson.com", "");
            ghelloworldericssoncomService.addListener(new ServiceAdapter()
            {
                /**
                 * When a message is received, this method will print it to the console and set the <code>isResponseReceived</code> flag to indicate
                 * success.
                 */            	
                public void processMessage(String remote, String messageType, byte[] content, int length)
                {
//                	HelloWorldMsg msg = null;
//                	try {
//						ObjectInputStream in = new ObjectInputStream(new ByteInputStream(content, 0));
//						
//						msg = (HelloWorldMsg)in.readObject(); 
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (ClassNotFoundException e) {
//						e.printStackTrace();
//					}
                	
                    String message = new String(content);
//					String message = msg.getMsg();
                    System.out.println(message);
                    
                  XStream xstream = new XStream();
                  HelloWorldMsg msg = (HelloWorldMsg)xstream.fromXML(message);
                  System.out.println(msg.getMsg());
//                  HelloWorldMsg msg = new HelloWorldMsg("Hello Michal and Ernest");
//                  String xml = xstream.toXML(msg);
//                  System.out.println(xml);
                    
                    isResponseReceived = true;
                }
            });

            // send a message through ICP
            byte[] message = "Hello, Mercury!".getBytes();
            ghelloworldericssoncomService.sendMessage(profile.getIdentity(), "sip:greetings@ericsson.com", "text/plain", message, message.length);

            long startTime = System.currentTimeMillis();
            long timeElapsed = 0;
            long DELAY = 30 * 1000;

            // wait/notify mechanism could be used as well
            while (timeElapsed < DELAY)
            {

                timeElapsed = System.currentTimeMillis() - startTime;
                Thread.sleep(100);
            }
            if(!isResponseReceived)
            {
                System.out.println("Message not received. Goodbye, World!");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
