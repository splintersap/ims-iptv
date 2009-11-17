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
package pl.edu.agh.iptv.presence;

import java.awt.TextArea;

import com.ericsson.icp.util.ErrorReason;

public class BaseAdapter
{
    private TextArea area;

    protected BaseAdapter(TextArea area)
    {
        this.area = area;
    }

    public void processError(ErrorReason aError)
    {
        log("processError: " + aError.getReasonString());
    }
    
    protected void log(String message)
    {
        area.append(message + "\r\n");
    }
}
