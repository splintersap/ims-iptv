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

package pl.edu.agh.ims.video.client;

import com.ericsson.icp.ISessionListener;
import com.ericsson.icp.util.ErrorReason;
import com.ericsson.icp.util.ISessionDescription;
import com.ericsson.icp.util.MIMEContainer;

public class SessionAdapter implements ISessionListener
{

    public void processSessionAlerting()
    {   
    	System.out.println("processSessionAlerting");
    }

    public void processSessionCancelled()
    {   
    	System.out.println("processSessionCancelled");
    }

    public void processSessionEnded()
    {  
    	System.out.println("processSessionEnded");
    }

    public void processSessionInformation(String arg0, byte[] arg1, int arg2)
    {   
    	System.out.println("processSessionInformation");
    }

    public void processSessionInformationFailed(ErrorReason arg0, long arg1)
    {   
    	System.out.println("processSessionInformationFailed");
    }

    public void processSessionInformationSuccessful(String arg0, byte[] arg1, int arg2)
    {   
    	System.out.println("processSessionInformationSuccessful");
    }

    public void processSessionInvitation(String arg0, boolean arg1, ISessionDescription arg2, MIMEContainer arg3)
    {   
    	System.out.println("processSessionInvitation");
    }

    public void processSessionMediaNegotiation(ISessionDescription arg0)
    {   
    	System.out.println("processSessionMediaNegotiation");
    }

    public void processSessionMessage(String arg0, byte[] arg1, int arg2)
    {   
    	System.out.println("processSessionMessage");
    }

    public void processSessionMessage(String arg0, byte[] arg1, int arg2, String arg3)
    {   
    	System.out.println("processSessionMessage");
    }

    public void processSessionMessageFailed(ErrorReason arg0, long arg1)
    {   
    	System.out.println("processSessionMessageFailed");
    }

    public void processSessionMessageSuccessful(String arg0, byte[] arg1, int arg2)
    {   
    	System.out.println("processSessionMessageSuccessful");
    }

    public void processSessionOptions(String arg0, ISessionDescription arg1)
    {  
    	System.out.println("processSessionOptions");
    }

    public void processSessionOptionsFailed(ErrorReason arg0, long arg1)
    {   
    	System.out.println("processSessionOptionsFailed");
    }

    public void processSessionOptionsSuccessful(String arg0, ISessionDescription arg1)
    {  
    	System.out.println("processSessionOptionsSuccessful");
    }

    public void processSessionPublishFailed(String arg0, byte[] arg1, int arg2, ErrorReason arg3, long arg4)
    {   
    	System.out.println("processSessionPublishFailed");
    }

    public void processSessionPublishSuccessful(String arg0, int arg1, byte[] arg2, int arg3)
    {
    	System.out.println("processSessionPublishSuccessful");
    }

    public void processSessionReceivedPRACK(ISessionDescription arg0)
    {
    	System.out.println("processSessionReceivedPRACK");
    }

    public void processSessionReceivedPRACKResponse(ISessionDescription arg0)
    { 
    	System.out.println("processSessionReceivedPRACKResponse");
    }

    public void processSessionRefer(String arg0, String arg1, byte[] arg2, int arg3)
    {  
    	System.out.println("processSessionRefer");
    }

    public void processSessionReferEnded(String arg0)
    {
    	System.out.println("processSessionReferEnded");
    }

    public void processSessionReferFailed(String arg0, ErrorReason arg1, long arg2)
    {   
    	System.out.println("processSessionReferFailed");
    }

    public void processSessionReferNotify(String arg0, int arg1, String arg2)
    { 
    	System.out.println("processSessionReferNotify");
    }

    public void processSessionReferNotifyFailed(String arg0, ErrorReason arg1, long arg2)
    {
    	System.out.println("processSessionReferNotifyFailed");
    }

    public void processSessionReferNotifySuccessful(String arg0)
    {   
    	System.out.println("processSessionReferNotifySuccessful");
    }

    public void processSessionReferSuccessful(String arg0)
    {
    	System.out.println("processSessionReferSuccessful");
    }

    public void processSessionStartFailed(ErrorReason arg0, long arg1)
    { 
    	System.out.println("processSessionStartFailed");
    }

    public void processSessionStarted(ISessionDescription arg0)
    {  
    	System.out.println("processSessionStarted");
    	System.out.println(ISessionDescription.FieldType.SessionName);
    }

    public void processSessionSubscribeDeactived(String arg0, String arg1, byte[] arg2, int arg3)
    {  
    	System.out.println("processSessionSubscribeDeactived");
    }

    public void processSessionSubscribeFailed(String arg0, String arg1, byte[] arg2, int arg3, ErrorReason arg4, long arg5)
    {   
    	System.out.println("processSessionSubscribeFailed");
    }

    public void processSessionSubscribeNotification(String arg0, String arg1, byte[] arg2, int arg3)
    {  
    	System.out.println("processSessionSubscribeNotification");
    }

    public void processSessionSubscribeSuccessful(String arg0, String arg1, byte[] arg2, int arg3)
    {    
    	System.out.println("processSessionSubscribeSuccessful");
    }

    public void processSessionUpdate(ISessionDescription arg0)
    {   
    	System.out.println("processSessionUpdate");
    }

    public void processSessionUpdateFailed(ErrorReason arg0, long arg1)
    {
    	System.out.println("processSessionUpdateFailed");
    }

    public void processSessionUpdateSuccessful(ISessionDescription arg0)
    {
    	System.out.println("processSessionUpdateSuccessful");
    }

    public void processError(ErrorReason arg0)
    {   
    	System.out.println("processError");
    }

}
