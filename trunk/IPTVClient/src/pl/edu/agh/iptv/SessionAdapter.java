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

package pl.edu.agh.iptv;

import com.ericsson.icp.ISessionListener;
import com.ericsson.icp.util.ErrorReason;
import com.ericsson.icp.util.ISessionDescription;
import com.ericsson.icp.util.MIMEContainer;

public class SessionAdapter implements ISessionListener
{

    public void processSessionAlerting()
    {   
    }

    public void processSessionCancelled()
    {   
    }

    public void processSessionEnded()
    {   
    }

    public void processSessionInformation(String arg0, byte[] arg1, int arg2)
    {   
    	System.out.println("processSessionInformation");
    }

    public void processSessionInformationFailed(ErrorReason arg0, long arg1)
    {   
    }

    public void processSessionInformationSuccessful(String arg0, byte[] arg1, int arg2)
    {   
    	System.out.println("processSessionInformationSuccessful");
    }

    public void processSessionInvitation(String arg0, boolean arg1, ISessionDescription arg2, MIMEContainer arg3)
    {   
    }

    public void processSessionMediaNegotiation(ISessionDescription arg0)
    {   
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
    }

    public void processSessionMessageSuccessful(String arg0, byte[] arg1, int arg2)
    {   
    }

    public void processSessionOptions(String arg0, ISessionDescription arg1)
    {  
    }

    public void processSessionOptionsFailed(ErrorReason arg0, long arg1)
    {   
    }

    public void processSessionOptionsSuccessful(String arg0, ISessionDescription arg1)
    {  
    }

    public void processSessionPublishFailed(String arg0, byte[] arg1, int arg2, ErrorReason arg3, long arg4)
    {   
    }

    public void processSessionPublishSuccessful(String arg0, int arg1, byte[] arg2, int arg3)
    {
    }

    public void processSessionReceivedPRACK(ISessionDescription arg0)
    {
    }

    public void processSessionReceivedPRACKResponse(ISessionDescription arg0)
    { 
    }

    public void processSessionRefer(String arg0, String arg1, byte[] arg2, int arg3)
    {  
    }

    public void processSessionReferEnded(String arg0)
    { 
    }

    public void processSessionReferFailed(String arg0, ErrorReason arg1, long arg2)
    {   
    }

    public void processSessionReferNotify(String arg0, int arg1, String arg2)
    { 
    }

    public void processSessionReferNotifyFailed(String arg0, ErrorReason arg1, long arg2)
    {
    }

    public void processSessionReferNotifySuccessful(String arg0)
    {   
    }

    public void processSessionReferSuccessful(String arg0)
    {
    }

    public void processSessionStartFailed(ErrorReason arg0, long arg1)
    { 
    }

    public void processSessionStarted(ISessionDescription arg0)
    {  
    }

    public void processSessionSubscribeDeactived(String arg0, String arg1, byte[] arg2, int arg3)
    {  
    }

    public void processSessionSubscribeFailed(String arg0, String arg1, byte[] arg2, int arg3, ErrorReason arg4, long arg5)
    {   
    }

    public void processSessionSubscribeNotification(String arg0, String arg1, byte[] arg2, int arg3)
    {  
    }

    public void processSessionSubscribeSuccessful(String arg0, String arg1, byte[] arg2, int arg3)
    {    
    }

    public void processSessionUpdate(ISessionDescription arg0)
    {   
    }

    public void processSessionUpdateFailed(ErrorReason arg0, long arg1)
    {
    }

    public void processSessionUpdateSuccessful(ISessionDescription arg0)
    {
    }

    public void processError(ErrorReason arg0)
    {   
    }

}
