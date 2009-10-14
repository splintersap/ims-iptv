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

import com.ericsson.icp.IServiceListener;
import com.ericsson.icp.ISession;
import com.ericsson.icp.util.ErrorReason;

public class ServiceAdapter implements IServiceListener
{

    public void processMessage(String arg0, String arg1, byte[] arg2, int arg3, String arg4)
    {   
    }

    public void processIncomingSession(ISession aSession)
    {
    }

    public void processMessage(String aRemote, String aMsgType, byte[] aMessage, int aLength)
    {
    }

    public void processOptions(String aPreferedContact, String aRemote, String aType, byte[] aContent, int aLength)
    {
    }

    public void processPublishResult(boolean aStatus, String aRemote, String aEvent, long retryAfter)
    {
    }

    public void processRefer(String aReferID, String aRemote, String aThirdParty, String aContentType, byte[] aContent, int aLength)
    {
    }

    public void processReferEnded(String aReferID)
    {
    }

    public void processReferNotification(String aReferId, int aState)
    {
    }

    public void processReferNotifyResult(boolean status, String aReferID, long retryAfter)
    {
    }

    public void processReferResult(boolean aStatus, String aReferId, long retryAfter)
    {
    }

    public void processSendMessageResult(boolean aStatus, long retryAfter)
    {
    }

    public void processSendOptionsResult(boolean aStatus, String aPreferedContact, String aRemote, String aType, byte[] aContent, int aLength, long retryAfter)
    {
    }

    public void processSubscribeEnded(String aPreferedContact, String aRemote, String aEvent)
    {
    }

    public void processSubscribeNotification(String aRemote, String aContact, String aEvent, String aType, byte[] aContent, int aLength)
    {
    }

    public void processSubscribeResult(boolean aStatus, String aRemote, String aEvent, long retryAfter)
    {
    }

    public void processUnsubscribeResult(boolean aStatus, String aRemote, String aEvent, long retryAfter)
    {
    }

    public void processError(ErrorReason aError)
    {
    }
}