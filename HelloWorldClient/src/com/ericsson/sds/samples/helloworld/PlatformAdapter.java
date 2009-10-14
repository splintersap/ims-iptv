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

import com.ericsson.icp.IPlatformListener;
import com.ericsson.icp.util.ErrorReason;

public class PlatformAdapter implements IPlatformListener
{
    public void processPlatformTerminated(ErrorReason aReasonCode)
    {
    }

    public void processError(ErrorReason aError)
    {
    }
    
    public void processApplicationData(String aApplication, byte[] aData, int aLength)
    {
    }

    public void processIncomingProfile(String aProfileName)
    {
    }
}

