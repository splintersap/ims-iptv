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

package com.ericsson.sds.samples.helloworld;

import com.ericsson.icp.IProfileListener;
import com.ericsson.icp.IProfile.State;
import com.ericsson.icp.util.ErrorReason;

public class ProfileAdapter implements IProfileListener
{

    public void processApplicationData(String aApplication, byte[] aData, int aLength)
    {
    }

    public void processEvent(String aEvent, String aSource, ErrorReason aReasonCode)
    {
    }

    public void processStateChanged(State aState)
    {
    }

    public void processError(ErrorReason aError)
    {
    }
}
