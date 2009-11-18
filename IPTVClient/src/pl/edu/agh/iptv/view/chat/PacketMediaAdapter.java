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

import com.ericsson.icp.media.IPacketMediaListener;

public class PacketMediaAdapter extends BaseAdapter implements IPacketMediaListener
{
    public PacketMediaAdapter(TextArea area)
    {
        super(area);
    }
    
    public void processFailureReport(int aStatusCode, String aData)
    {        
        log("processFailureReport");
    }
    
    public void processMediaActivated()
    {
        log("processMediaActivated");
    }

    public void processMediaDeactivated()
    {
        log("processMediaDeactivated");
    }
    
    public void processReceivedData(String aContentType, byte[] aData, int aLength, boolean isCompleted)
    {
        log("processReceivedData");
    }
    
    public void processReceivingStarted(String aFileName, String aContentType)
    {
        log("processReceivingStarted content-type:"+aContentType+"\t file name:"+aFileName);
    }
    
    public void processSendingProgress(int aPercentage)
    {
        log("processSendingProgress" + aPercentage);
    }
    
    public void processSendingSucceed()

    {
        log("processSendingSucceed");
    }

    public void processSuccessReport(int aStatusCode, String aData)
    {
        log("processSucessReport");
    }
}
