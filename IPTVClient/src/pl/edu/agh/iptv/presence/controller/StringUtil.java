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
package pl.edu.agh.iptv.presence.controller;

public class StringUtil
{
    private StringUtil()
    {
        // prevent instantiation of utility class 
    }
    
    /**
     * Returns <code>true</code> if the string is null or empty.
     * @param str
     * @return
     */
    public static boolean isEmpty(String str)
    {
        return str == null || str.equals("");
    }
    /**
     * Retirve a file extension
     * @param fileName The file name from which to retirve the extension
     * @return The extension
     */
    public static String getFileExtension(String fileName)
    {
        String extension = null; 
    
        int extensionIndex = fileName.indexOf(".");
        if (extensionIndex >= 0)
        {
            extension = fileName.substring(extensionIndex + 1);
        }
        return extension;

    }
}
