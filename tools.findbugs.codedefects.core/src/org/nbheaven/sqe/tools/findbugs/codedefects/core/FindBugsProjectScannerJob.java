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
package org.nbheaven.sqe.tools.findbugs.codedefects.core;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.queries.BinaryForSourceQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.spi.java.classpath.ClassPathProvider;
import org.openide.ErrorManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileStateInvalidException;

/**
 *
 * @author Sven Reimers
 */
public class FindBugsProjectScannerJob extends FindBugsScannerJob {

    FindBugsSession findBugsSession;

    FindBugsProjectScannerJob(Project project) {
        super(project);
        findBugsSession = project.getLookup().lookup(FindBugsSession.class);
    }


    @Override
    protected void postScan() {
        findBugsSession.setResult(getResult());
        super.postScan();
        findBugsSession.scanningDone();
    }

    protected edu.umd.cs.findbugs.Project createFindBugsProject() {
        Sources s = getProject().getLookup().lookup(org.netbeans.api.project.Sources.class);
        ClassPathProvider cpp = getProject().getLookup().lookup(org.netbeans.spi.java.classpath.ClassPathProvider.class);

        edu.umd.cs.findbugs.Project fibuProject = new edu.umd.cs.findbugs.Project();

        for (SourceGroup g : s.getSourceGroups("java")) {
            FileObject fo = g.getRootFolder();
            try {
                BinaryForSourceQuery.Result result = BinaryForSourceQuery.findBinaryRoots(fo.getURL());
                for (URL url : result.getRoots()) {
                    String file = url.getFile();
                    if ("jar".equals(url.getProtocol())) {
                        file = new URL(file).getFile();
                    }
                    // ensure this is valid for FindBugs (remove trailing !/
                    String fixedUrl = file.replace("!/", "");
                    File checkFile = new File(URLDecoder.decode(fixedUrl, "UTF-8"));
                    if (checkFile.exists()) {
                        fibuProject.addFile(URLDecoder.decode(fixedUrl, "UTF-8"));
                    }
                }
            } catch (MalformedURLException mue) {
                ErrorManager.getDefault().notify(mue);
            } catch (UnsupportedEncodingException uee) {
                Throwable t = ErrorManager.getDefault().annotate(uee,
                        "Failure decoding BinaryRoot" + fo);
                ErrorManager.getDefault().notify(t);
            } catch (FileStateInvalidException fsie) {
                ErrorManager.getDefault().notify(fsie);
            }
        }

        SourceGroup[] groups = s.getSourceGroups("java");

        for (SourceGroup g : groups) {
            FileObject fo = g.getRootFolder();
            // add source dir findbugs
            fibuProject.addSourceDir(fo.getPath());

            ClassPath cp = cpp.findClassPath(fo, ClassPath.COMPILE);

            if (null != cp) {
                for (ClassPath.Entry entry : cp.entries()) {
                    try {
                        URL url = entry.getURL();

                        if (null != entry.getRoot()) {
                            String pathName = url.getFile();

                            if (url.getProtocol().equals("jar")) {
                                pathName = pathName.substring(0,
                                        pathName.length() - 2);
                                url = new URL(pathName);
                            }

                            pathName = url.getFile();

                            try {
                                fibuProject.addAuxClasspathEntry(URLDecoder.decode(
                                        pathName, "UTF-8"));
                            } catch (UnsupportedEncodingException uee) {
                                Throwable t = ErrorManager.getDefault().annotate(uee,
                                        "Failure decoding AuxClassPath Entry" +
                                        pathName);
                                ErrorManager.getDefault().notify(t);
                            }
                        }
                    } catch (MalformedURLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        return fibuProject;
    }

}
