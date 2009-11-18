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

import java.io.PrintStream;

import javax.swing.JTextArea;

import com.ericsson.icp.util.ErrorReason;

public class BaseAdapter
{
    private JTextArea area;

    protected BaseAdapter(JTextArea area)
    {
        this.area = area;
    }

    public void processError(ErrorReason aError)
    {
        area.setText("processError: " + aError.getReasonString());
    }
    
    public void log(String message)
    {
        log(message, null);
    }
    
    public void log(String description, Exception e)
    {
        StringOutputStream stringos = new StringOutputStream();
        if(e != null)
        {
            PrintStream stream = new PrintStream(stringos);
            e.printStackTrace(stream);
        }

        area.append(description + " " + stringos.getValue() + "\r\n");
    }
}
