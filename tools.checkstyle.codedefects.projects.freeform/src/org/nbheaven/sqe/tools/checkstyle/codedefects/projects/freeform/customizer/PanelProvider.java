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
package org.nbheaven.sqe.tools.checkstyle.codedefects.projects.freeform.customizer;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import org.nbheaven.sqe.tools.checkstyle.codedefects.core.option.CheckstyleConfiguration;
import org.nbheaven.sqe.tools.checkstyle.codedefects.core.settings.CheckstyleSettings;
import org.nbheaven.sqe.tools.checkstyle.codedefects.core.settings.CheckstyleSettingsProvider;
import org.nbheaven.sqe.tools.checkstyle.codedefects.core.settings.impl.GlobalCheckstyleSettings;
import org.nbheaven.sqe.tools.checkstyle.codedefects.projects.freeform.CheckstyleSettingsProviderImpl;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.netbeans.spi.project.ui.support.ProjectCustomizer.Category;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;

/**
 *
 * @author Sven Reimers
 */
public class PanelProvider implements ProjectCustomizer.CompositeCategoryProvider {

    private String name;

    /** Creates a new instance of ProjectPanelProvider */
    private PanelProvider(String name) {
        this.name = name;
    }

    public Category createCategory(Lookup lookup) {
        return ProjectCustomizer.Category.create(this.name, "CheckStyle", null);
    }

    public JComponent createComponent(Category category, Lookup context) {
        Project p = context.lookup(Project.class);
        CheckstyleSettingsProvider checkstyleSettingsProvider = p.getLookup().lookup(CheckstyleSettingsProvider.class);
        final CheckstyleSettings checkstyleSettings = checkstyleSettingsProvider.getCheckstyleSettings();

        final CheckstyleConfiguration checkstyleConfiguration = new CheckstyleConfiguration();
        String cleanAbsoluteFile = FileUtil.toFile(checkstyleSettings.getCheckstyleConfigurationFile()).getAbsolutePath();
        checkstyleConfiguration.setConfigFilePath(cleanAbsoluteFile);
        FileObject checkstylePropertiesFile = checkstyleSettings.getPropertiesFile();
        if (null != checkstylePropertiesFile) {
            String propertiesFilePath = FileUtil.toFile(checkstylePropertiesFile).getAbsolutePath();
            checkstyleConfiguration.setPropertiesFilePath(propertiesFilePath);
        } else {
            checkstyleConfiguration.setPropertiesFilePath("");
        }
        checkstyleConfiguration.setProperties(checkstyleSettings.getPropertiesAsString());

        category.setOkButtonListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (checkstyleSettings instanceof CheckstyleSettingsProviderImpl.Settings) {
                    CheckstyleSettingsProviderImpl.Settings checkstyleSettingsImpl = (CheckstyleSettingsProviderImpl.Settings) checkstyleSettings;
                    checkstyleSettingsImpl.setCheckstyleConfigurationPath(checkstyleConfiguration.getConfigFilePath());
                    checkstyleSettingsImpl.setPropertiesPath(checkstyleConfiguration.getPropertiesFilePath());
                    checkstyleSettingsImpl.setProperties(checkstyleConfiguration.getProperties());
                }
            }
        });

        return checkstyleConfiguration;
    }

    public static PanelProvider createExample() {
        return new PanelProvider("Example");
    }
}
