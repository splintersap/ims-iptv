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

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This dialog can be used to ask the user a question that can be answered by 'yes' or 'no'. The <code>isAccepted</code> method can be used to
 * retrieve the answer.
 */
public class ConfirmDialog extends Dialog
{
    private static final long serialVersionUID = -4079780110198517768L;

    /**
     * <code>true</code> if the user clicked on the 'accept' button, false otherwise.
     */
    private boolean isAccepted;

    /**
     * Creates a new dialog that can be used to ask the user a yes/no question.
     * 
     * @param parent
     * @param message
     */
    public ConfirmDialog(Frame parent, String message)
    {
        super(parent, true);
        initGui(message);
    }

    /**
     * Returns <code>true</code> if the user clicked on the 'accept' button, false otherwise.
     */
    public boolean hasAccepted()
    {
        return isAccepted;
    }

    /**
     * Creates the GUI for the dialog.
     * 
     * @param message
     */
    private void initGui(String message)
    {
        setLayout(new BorderLayout());
        TextArea messageArea = new TextArea(message);
        messageArea.setEditable(false);
        add(messageArea, BorderLayout.CENTER);

        // create the yes/no buttons
        Panel buttonPanel = new Panel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        Button yesButton = new Button("Yes");
        yesButton.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent e)
            {
                isAccepted = true;
                setVisible(false);
            }
        });
        Button noButton = new Button("No");
        noButton.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent e)
            {
                isAccepted = false;
                setVisible(false);
            }
        });
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        add(buttonPanel, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }
}
