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
package org.nbheaven.sqe.tools.findbugs.codedefects.projects.freeform.customizer;

import edu.umd.cs.findbugs.config.UserPreferences;
import org.nbheaven.sqe.tools.findbugs.codedefects.core.option.ConfigureDetectorsPanel;
import org.nbheaven.sqe.tools.findbugs.codedefects.core.option.ConfigureFeaturesPanel;
import org.nbheaven.sqe.tools.findbugs.codedefects.core.settings.FindBugsSettingsProvider;

import org.netbeans.api.project.Project;

import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.netbeans.spi.project.ui.support.ProjectCustomizer.Category;

import org.openide.util.Lookup;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


/**
 *
 * @author sven
 */
public class PanelProvider implements ProjectCustomizer.CompositeCategoryProvider {
    private String name;

    /** Creates a new instance of ProjectPanelProvider */
    private PanelProvider(String name) {
        this.name = name;
    }

    public Category createCategory(Lookup context) {
        return ProjectCustomizer.Category.create(this.name, "FindBugs", null);
    }

    public JComponent createComponent(Category category, Lookup context) {
        Project p = (Project) context.lookup(Project.class);
        final FindBugsSettingsProvider fibuSettingsProvider = (FindBugsSettingsProvider) p.getLookup()
                                                                            .lookup(FindBugsSettingsProvider.class);

        final UserPreferences findBugsSettings = fibuSettingsProvider.getFindBugsSettings();
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTabbedPane jTabbedPane = new JTabbedPane();
        final ConfigureDetectorsPanel detectorsPanel = new ConfigureDetectorsPanel(findBugsSettings);
        jTabbedPane.addTab("Configure Detectors", detectorsPanel);

        JPanel featuresPanel = new ConfigureFeaturesPanel();
        jTabbedPane.addTab("Configure Features", featuresPanel);
        panel.add(jTabbedPane, BorderLayout.CENTER);

        category.setOkButtonListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                detectorsPanel.applyDetectorChangesToUserPreferences(findBugsSettings);
                fibuSettingsProvider.setFindBugsSettings(findBugsSettings);
            }
        });        
        
        return panel;
    }

    public static PanelProvider createExample() {
        return new PanelProvider("Example");
    }
}
