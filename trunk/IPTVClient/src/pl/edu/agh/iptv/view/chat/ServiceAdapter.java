/* **********************************************************************
 * Copyright (c) Ericsson 2007. All Rights Reserved.
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
package pl.edu.agh.iptv.view.chat;

import java.awt.TextArea;

import com.ericsson.icp.IServiceListener;
import com.ericsson.icp.ISession;

public class ServiceAdapter extends BaseAdapter implements IServiceListener
{
    public ServiceAdapter(TextArea area)
    {
        super(area);
    }
    public void processIncomingSession(ISession aSession)
    {
        log("processIncomingSession");
    }

    public void processSendMessageResult(boolean aStatus, long retryAfter)
    {
        log("processSendMessageResult");
    }

    public void processMessage(String aRemote, String aMsgType, byte[] aMessage, int aLength)
    {
        log("processMessage-1");
    }
    
    public void processMessage(String aRemote, String aMsgType, byte[] aMessage, int aLength, String aContentEncoding)
    {
        log("processMessage-2");
    }

    public void processSubscribeResult(boolean aStatus, String aRemote, String aEvent, long retryAfter)
    {
        log("processSubscribeResult");
    }

    public void processUnsubscribeResult(boolean aStatus, String aRemote, String aEvent, long retryAfter)
    {
        log("processUnsubscribeResult");
    }

    public void processSubscribeNotification(String aRemote, String aContact, String aEvent, String aType, byte[] aContent, int aLength)
    {
        log("processSubscribeNotification");
    }

    public void processPublishResult(boolean aStatus, String aRemote, String aEvent, long retryAfter)
    {
        log("processPublishResult");
    }

    public void processReferResult(boolean aStatus, String aReferId, long retryAfter)
    {
        log("processReferResult");
    }

    public void processReferEnded(String aReferID)
    {
        log("processReferEnded");
    }

    public void processSubscribeEnded(String aPreferedContact, String aRemote, String aEvent)
    {
        log("processSubscribeEnded");
    }

    public void processReferNotification(String aReferId, int aState)
    {
        log("processReferNotification");
    }

    public void processRefer(String aReferID, String aRemote, String aThirdParty, String aContentType, byte[] aContent, int aLength)
    {
        log("processRefer");
    }

    public void processReferNotifyResult(boolean status, String aReferID, long retryAfter)
    {
        log("processReferNotifyResult");
    }

    public void processSendOptionsResult(boolean aStatus, String aPreferedContact, String aRemote, String aType, byte[] aContent, int aLength, long retryAfter)
    {
        log("processSendOptionsResult");
    }

    public void processOptions(String aPreferedContact, String aRemote, String aType, byte[] aContent, int aLength)
    {
        log("processOptions");
    }}
