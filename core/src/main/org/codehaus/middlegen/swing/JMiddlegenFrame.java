/*
 * Copyright (c) 2001, The RdbmsTableFromJdbcMetadataProvider Group
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * - Neither the name of The RdbmsTableFromJdbcMetadataProvider Group nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.codehaus.middlegen.swing;

import org.codehaus.middlegen.swing.JSettingsTabPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author <a href="mailto:aslak.hellesoy at netcom.no">Aslak Hellesøy</a>
 * @version $Id: JMiddlegenFrame.java,v 1.5 2003/05/12 22:57:22 rinkrank Exp $
 */
public class JMiddlegenFrame extends JFrame {
    private final JDatabasePanel _databasePanel;

    private Action _generateAction =
       new AbstractAction("Generate") {
          public void actionPerformed(ActionEvent evt) {
//             try {
//                _xdoclet.start();
//             } catch (IOException e) {
//                error(e);
//             } catch (XDocletException e) {
//                error(e);
//             }
          }
       };

   public JMiddlegenFrame(String title) {
      super(title);

      // make panel with label header and tabs in center.
      JLabel header = new JLabel("", JLabel.CENTER);
      JSettingsTabPane settingsTabs = new JSettingsTabPane( null /*_xdoclet.getPlugins()*/);
      JPanel headerTabs = new JPanel(new BorderLayout());
      headerTabs.add(header, BorderLayout.NORTH);
      headerTabs.add(settingsTabs, BorderLayout.CENTER);

      _databasePanel = new JDatabasePanel(settingsTabs, header);

      JScrollPane scroll = new JScrollPane(_databasePanel);

      JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scroll, headerTabs);
      split.setDividerLocation(0.5);
      getContentPane().add(split, BorderLayout.CENTER);

      JToolBar toolBar = new JToolBar();
      toolBar.add(_generateAction);
      getContentPane().add(toolBar, BorderLayout.NORTH);

      ImageIcon icon = new ImageIcon(getClass().getResource("m.gif"));
      setIconImage(icon.getImage());

      // I have a dodgy feeling that a Mac would try to exit the VM when the
      // window closes. Try to uncomment this line on a Mac.
      // System.setSecurityManager(new NoExitSecurityManager());
   }


   /**
    * Sets the Visible attribute of the JMiddlegenFrame object
    *
    * @param flag The new Visible value
    */
   public void setVisible(boolean flag) {
       super.setVisible(flag);
/* TODO fixme
       try {
           SchemaFactory tableFactory = _middlegen.getTableFactory();
           Collection tables = tableFactory.makeTables();
           _databasePanel.reset(tables);
       } catch (DatabaseException e) {
           e.printStackTrace();  //To change body of catch statement use Options | File Templates.
       }
*/
       pack();
   }

   private void error(Throwable t) {
      t.printStackTrace();
      JOptionPane.showMessageDialog(this, t);
   }


   /**
    * This security manager will cause an ExitException to be thrown whenever
    * System.exit is called instead of terminating the VM.
    *
    * @author Aslak Hellesøy
    */
   public static class NoExitSecurityManager extends SecurityManager {
      public void checkExit(int status) {
         throw new ExitException(status);
      }

      public void checkPermission(java.security.Permission p) {
      }
   }

   public static class ExitException extends RuntimeException {
      private int _status;

      public ExitException(int status) {
         _status = status;
      }

      public int getStatus() {
         return _status;
      }
   }
}

