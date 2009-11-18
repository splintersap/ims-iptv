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

import com.ericsson.icp.IPlatformListener;
import com.ericsson.icp.util.ErrorReason;

public class PlatformAdapter extends BaseAdapter implements IPlatformListener
{
    public PlatformAdapter(TextArea area)
    {
        super(area);
    }

    public void processPlatformTerminated(ErrorReason aError)
    {
        log("processPlatformTerminated");
    }

    public void processApplicationData(String aApplication, byte[] aData, int aLength)
    {
        log("processApplicationData");
    }

    public void processIncomingProfile(String aProfileName)
    {
        log("processIncomingProfile");
    }
}
