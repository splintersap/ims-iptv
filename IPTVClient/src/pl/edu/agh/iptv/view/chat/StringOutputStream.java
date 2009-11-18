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

import java.io.IOException;
import java.io.OutputStream;

/**
 * This class is used to convert an output stream to a string. The content can be retrieved by calling the getValue() method.
 */
public class StringOutputStream extends OutputStream
{
    private StringBuffer buffer = new StringBuffer();

    /**
     * {@inheritDoc}
     */
    public void write(int b) throws IOException
    {
        buffer.append(new String(new byte[] { (byte) b }));
    }

    /**
     * {@inheritDoc}
     */
    public void clear()
    {
        buffer = new StringBuffer();
    }

    /**
     * {@inheritDoc}
     */
    public String getValue()
    {
        return buffer.toString();
    }
}
