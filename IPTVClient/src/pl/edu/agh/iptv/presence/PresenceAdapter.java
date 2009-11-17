package pl.edu.agh.iptv.presence;

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

//package com.ericsson;
import java.awt.TextArea;

import com.ericsson.icp.services.PGM.IPresenceListener;
import com.ericsson.icp.util.IIterator;
import com.ericsson.icp.util.IPresentity;

public class PresenceAdapter extends BaseAdapter implements IPresenceListener
{
    protected PresenceAdapter(TextArea area)
    {
        super(area);
    }

    public void processAllowWatcherResult(boolean aSuccess)
    {
        log("processAllowWatcherResult");
    }

    public void processBlockWatcherResult(boolean aSuccess)
    {
        log("processBlockWatcherResult");
    }

    public void processSubscribeNotification(String arg0, IPresentity arg1)
    {
        log("processSubscribeNotification");
    }

    public void processSubscribeResult(boolean aStatus, String aRemote, String aEvent)
    {
        log("processSubscribeResult");
    }

    public void processUnsubscribeResult(boolean aStatus, String aRemote, String aEvent)
    {
        log("processUnsubscribeResult");
    }

    public void processUpdatePresentityResult(boolean aSuccess)
    {
        log("processUpdatePresentityResult");
    }

    public void processWatcherInfo(String aRemote, IIterator aWatcherIterator)
    {
        log("processWatcherInfo");
    }
}
