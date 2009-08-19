/* Copyright 2005,2006 Sven Reimers, Florian Vogler
 *
 * This file is part of the Software Quality Environment Project.
 *
 * The Software Quality Environment Project is free software:
 * you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation,
 * either version 2 of the License, or (at your option) any later version.
 *
 * The Software Quality Environment Project is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.nbheaven.sqe.tools.pmd.codedefects.core.option;

import java.awt.BorderLayout;
import org.nbheaven.sqe.tools.pmd.codedefects.core.settings.impl.GlobalPMDSettings;

final class PMDPanel extends javax.swing.JPanel {

    private final PMDOptionsPanelController controller;
    private final ConfigureRulesPanel configureRulesPanel;

    PMDPanel(PMDOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
        configureRulesPanel = new ConfigureRulesPanel();
        // TODO listen to changes in form fields and call controller.changed()
        this.add(configureRulesPanel, BorderLayout.CENTER);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    void load() {
        configureRulesPanel.setSettings(new GlobalPMDSettings());
    }

    void store() {
        configureRulesPanel.saveSettingsToPreferences();
    }

    boolean valid() {
        // TODO check whether form is consistent and complete
        return true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
