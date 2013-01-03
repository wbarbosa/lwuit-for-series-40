/*
 * Copyright (c) 2008, 2010, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores
 * CA 94065 USA or visit www.oracle.com if you need additional information or
 * have any questions.
 */

package com.sun.lwuit.resources.editor;

import com.sun.lwuit.resources.editor.editors.RunOnDevice;
import com.sun.lwuit.util.EditableResources;
import java.awt.Desktop;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.util.Properties;
import java.util.TreeMap;
import java.util.prefs.Preferences;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Static class to contain code generation logic
 *
 * @author Shai Almog
 */
public class GenerateHelper {
    static final String[] GENERATED_PROJECT_FOLDER_NAMES = {
        /*"/GeneratedProject/DeviceX",
        "/GeneratedProject/DeviceX/assets",
        "/GeneratedProject/DeviceX/bin",
        "/GeneratedProject/DeviceX/libs",
        "/GeneratedProject/DeviceX/nbproject",
        "/GeneratedProject/DeviceX/res",
        "/GeneratedProject/DeviceX/src",
        "/GeneratedProject/DeviceX/nbproject/private",
        "/GeneratedProject/DeviceX/res/layout",
        "/GeneratedProject/DeviceX/res/values",
        "/GeneratedProject/DeviceX/src/com/mycompany",*/
        "/GeneratedProject/Desktop",
        "/GeneratedProject/MIDP",
        "/GeneratedProject/nbproject",
//        "/GeneratedProject/RIM",
        "/GeneratedProject/src",
        "/GeneratedProject/Desktop/nbproject",
        "/GeneratedProject/Desktop/src",
        "/GeneratedProject/Desktop/test",
        "/GeneratedProject/Desktop/nbproject/private",
        "/GeneratedProject/Desktop/src/desktop",
        "/GeneratedProject/MIDP/nbproject",
        "/GeneratedProject/MIDP/src",
        "/GeneratedProject/MIDP/nbproject/private",
        "/GeneratedProject/MIDP/src/userclasses",
        "/GeneratedProject/nbproject/private",
//        "/GeneratedProject/RIM/nbproject",
//        "/GeneratedProject/RIM/src",
//        "/GeneratedProject/RIM/nbproject/private",
//        "/GeneratedProject/RIM/src/userclasses",
        "/GeneratedProject/src/generated",
        "/GeneratedProject/src/userclasses",
    };
    static final String[] GENERATED_PROJECT_FILE_NAMES = {
        "/GeneratedProject/build.xml",
        "/GeneratedProject/IO.jar",
        "/GeneratedProject/IO_SE.jar",
        "/GeneratedProject/README.TXT",
        "/GeneratedProject/UI.jar",
        "/GeneratedProject/S40.jar",
//        "/GeneratedProject/UI_RIM.jar",
//        "/GeneratedProject/UI_RIM_Touch.jar",
        "/GeneratedProject/UI_SE.jar",
        /*"/GeneratedProject/DeviceX/DeviceXManifest.xml",
        "/GeneratedProject/DeviceX/build.properties",
        "/GeneratedProject/DeviceX/build.xml",
        "/GeneratedProject/DeviceX/default.properties",
        "/GeneratedProject/DeviceX/local.properties",
        "/GeneratedProject/DeviceX/proguard.cfg",
        "/GeneratedProject/DeviceX/libs/IO_DeviceX.jar",
        "/GeneratedProject/DeviceX/libs/UI_DeviceX.jar",
        "/GeneratedProject/DeviceX/nbproject/project.xml",
        "/GeneratedProject/DeviceX/nbproject/private/private.xml",
        "/GeneratedProject/DeviceX/res/layout/main.xml",
        "/GeneratedProject/DeviceX/res/values/strings.xml",
        "/GeneratedProject/DeviceX/src/com/mycompany/MainActivity.java",
        "/GeneratedProject/DeviceX/src/com/mycompany/R.java",*/
        "/GeneratedProject/Desktop/build.xml",
        "/GeneratedProject/Desktop/manifest.mf",
        "/GeneratedProject/Desktop/nbproject/build-impl.xml",
        "/GeneratedProject/Desktop/nbproject/genfiles.properties",
        "/GeneratedProject/Desktop/nbproject/project.properties",
        "/GeneratedProject/Desktop/nbproject/project.xml",
        "/GeneratedProject/Desktop/src/desktop/LWUITApplet.java",
        "/GeneratedProject/Desktop/src/desktop/Main.java",
        "/GeneratedProject/MIDP/build.xml",
        "/GeneratedProject/MIDP/nbproject/build-impl.xml",
        "/GeneratedProject/MIDP/nbproject/genfiles.properties",
        "/GeneratedProject/MIDP/nbproject/project.properties",
        "/GeneratedProject/MIDP/nbproject/project.xml",
        "/GeneratedProject/MIDP/src/userclasses/MainMIDlet.java",
        "/GeneratedProject/nbproject/build-impl.xml",
        "/GeneratedProject/nbproject/genfiles.properties",
        "/GeneratedProject/nbproject/project.properties",
        "/GeneratedProject/nbproject/project.xml",
//        "/GeneratedProject/RIM/build.xml",
//        "/GeneratedProject/RIM/nbproject/build-impl.xml",
//        "/GeneratedProject/RIM/nbproject/genfiles.properties",
//        "/GeneratedProject/RIM/nbproject/project.properties",
//        "/GeneratedProject/RIM/nbproject/project.xml",
//        "/GeneratedProject/RIM/src/userclasses/MainMIDlet.java",
        "/GeneratedProject/src/userclasses/StateMachine.java"
    };
    private static final TreeMap<String, String[]> SELECTABLE_SDKS = new TreeMap<String, String[]>();
    

    public GenerateHelper() {
        //In project.properties file:
        //platform.active.description, platform.active, platform.device
        SELECTABLE_SDKS.put("Nokia SDK 2.0 for Java", new String[]{"Nokia_SDK_2_0_for_Java", "Nokia_SDK_2_0_Java"});
        SELECTABLE_SDKS.put("Nokia SDK 1.1 for Java", new String[]{"Nokia_SDK_1_1_for_Java", "Nokia_SDK_1_1_Java"});
        SELECTABLE_SDKS.put("Series 40 6th Edition SDK", new String[]{"Series_40_6th_Edition_SDK", "S40_6th_Edition_SDK"});
    }

    private void replaceStringInFile(File destinationFile, String sourceValue, String newValue) throws IOException {
        DataInputStream i = new DataInputStream(new FileInputStream(destinationFile));
        byte[] b = new byte[(int)destinationFile.length()];
        i.readFully(b);
        i.close();
        String val = new String(b);
        val = val.replaceAll(sourceValue, newValue);

        Writer out = new FileWriter(destinationFile);
        out.write(val);
        out.close();
    }

    private void replaceStringInFiles(String oldString, String newString, File... files) throws IOException {
        for(File f : files) {
            replaceStringInFile(f, oldString, newString);
        }
    }

    Properties generateNetbeansProject(ResourceEditorView v, JComponent mainPanel, EditableResources loadedResources, File loadedFile) {
        try {
            if(loadedResources == null) {
                return null;
            }
            String uiResourceName = v.pickMainScreenForm();
            if(uiResourceName == null) {
                JOptionPane.showMessageDialog(mainPanel, "This feature is designed for use with the GUI Builder", "Add A UI Form", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            JTextField projectName = new JTextField("NewProject");
            int res = JOptionPane.showConfirmDialog(mainPanel, projectName, "Enter Name For Project", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(res != JOptionPane.OK_OPTION) {
                return null;
            }
            JComboBox selectSDK = new JComboBox(SELECTABLE_SDKS.keySet().toArray());
            int result = JOptionPane.showConfirmDialog(mainPanel, selectSDK, "Select target SDK", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result != JOptionPane.OK_OPTION) {
                return null;
            }
            File[] f = ResourceEditorView.showSaveDirFileChooser();
            if(f != null && f.length == 1) {
                File destFolder;
                if(!f[0].exists()) {
                    f[0].mkdirs();
                }
                if(f[0].getName().equalsIgnoreCase(projectName.getText())) {
                    destFolder = f[0];
                } else {
                    destFolder = new File(f[0], projectName.getText());
                    destFolder.mkdirs();
                }

                File srcDir = new File(destFolder, "src");
                File generatedDir = new File(srcDir, "generated");
                File nbprojectDir = new File(destFolder, "nbproject");

                srcDir.mkdirs();
                generatedDir.mkdirs();
                nbprojectDir.mkdirs();

                File midletFolder = new File(destFolder, "MIDP");
                File midletUserclassesDir = new File(midletFolder, "src/userclasses");

//                File rimFolder = new File(destFolder, "RIM");
//                File rimUserclassesDir = new File(rimFolder, "src/userclasses");

                if(loadedFile == null) {
                    loadedFile = new File(srcDir, ResourceEditorView.normalizeFormName(projectName.getText()) + ".res");
                } else {
                    loadedFile = new File(srcDir, loadedFile.getName());
                }
                v.setLoadedFile(loadedFile);
                OutputStream out = new FileOutputStream(loadedFile);
                loadedResources.save(out);
                out.close();
                v.addToRecentMenu(loadedFile);

                String packageName = v.generateStateMachineCode(uiResourceName, new File(generatedDir, "StateMachineBase.java"), false);

                Properties projectGeneratorSettings = new Properties();
                projectGeneratorSettings.load(getClass().getResourceAsStream("/GeneratedProject/lwuit_resource_editor_settings.properties"));
                projectGeneratorSettings.put("mainForm", uiResourceName);
                projectGeneratorSettings.put("package", packageName);
                File lwuitPropertiesFile = new File(destFolder, "lwuit_resource_editor_settings.properties");
                out = new FileOutputStream(lwuitPropertiesFile);
                projectGeneratorSettings.store(out, "Generated by the LWUIT resource editor");
                projectGeneratorSettings.put("userClassAbs",
                        new File(destFolder, projectGeneratorSettings.getProperty("userClass")).getAbsolutePath());
                out.close();

                for(String folderName : GENERATED_PROJECT_FOLDER_NAMES) {
                    new File(destFolder, folderName.replace("/GeneratedProject/", "")).mkdirs();
                }
                for(String fileName : GENERATED_PROJECT_FILE_NAMES) {
                    createFileInDir(fileName, new File(destFolder, fileName.replace("/GeneratedProject/", "")));
                }

                replaceStringInFiles("GeneratedProject", projectName.getText(), new File(destFolder, "build.xml"),
                        new File(nbprojectDir, "build-impl.xml"),
                        new File(nbprojectDir, "project.properties"),
                        new File(nbprojectDir, "project.xml"));
                String description = (String)selectSDK.getSelectedItem();
                String[] value = SELECTABLE_SDKS.get(description);
                String active = value[0];
                String device = value[1];
                //replace active
                replaceStringInFiles("Nokia_SDK_1_1_for_Java", active, new File(nbprojectDir, "project.properties"));
                //replace description
                replaceStringInFiles("Nokia SDK 1.1 for Java", description, new File(nbprojectDir, "project.properties"));
                //replace device
                replaceStringInFiles("Nokia_SDK_1_1_Java", device, new File(nbprojectDir, "project.properties"));
                
                

                replaceStringInFiles("DesktopGenerated", projectName.getText() + "_Desktop", new File(destFolder, "Desktop/build.xml"),
                        new File(destFolder, "Desktop/nbproject/build-impl.xml"),
                        new File(destFolder, "Desktop/nbproject/project.properties"),
                        new File(destFolder, "Desktop/nbproject/project.xml"));

                replaceStringInFiles("GeneratedProject", projectName.getText(), new File(destFolder, "Desktop/build.xml"),
                        new File(destFolder, "Desktop/nbproject/build-impl.xml"),
                        new File(destFolder, "Desktop/nbproject/project.properties"),
                        new File(destFolder, "Desktop/nbproject/project.xml"));

                replaceStringInFiles("MIDPGenerated", projectName.getText() + "_MIDP", new File(destFolder, "MIDP/build.xml"),
                        new File(destFolder, "MIDP/nbproject/build-impl.xml"),
                        new File(destFolder, "MIDP/nbproject/project.properties"),
                        new File(destFolder, "MIDP/nbproject/project.xml"));

                replaceStringInFiles("GeneratedProject", projectName.getText(), new File(destFolder, "MIDP/build.xml"),
                        new File(destFolder, "MIDP/nbproject/build-impl.xml"),
                        new File(destFolder, "MIDP/nbproject/project.properties"),
                        new File(destFolder, "MIDP/nbproject/project.xml"));


                replaceStringInFiles("res_file.res", loadedFile.getName(), 
                        new File(midletUserclassesDir, "MainMIDlet.java"),
                        new File(destFolder, "Desktop/src/desktop/Main.java"),
                        new File(destFolder, "Desktop/src/desktop/LWUITApplet.java"));

                ResourceEditorView.openInIDE(midletFolder, -1);
                ResourceEditorView.openInIDE(new File(destFolder, "Desktop"), -1);
                ResourceEditorView.openInIDE(destFolder, -1);
                return projectGeneratorSettings;
            }
        } catch(IOException err) {
            err.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel, "IO Error occured during creation: " + err, "IO Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    void createFileInDir(String resourceName, File destinationFile) throws IOException {
        InputStream i = getClass().getResourceAsStream(resourceName);
        OutputStream out = new FileOutputStream(destinationFile);
        byte[] buffer = new byte[65536];
        int size = i.read(buffer);
        while(size > -1) {
            out.write(buffer, 0, size);
            size = i.read(buffer);
        }
        out.close();
        i.close();
    }

    private RunOnDevice generatePreviewActivity(JComponent mainPanel, EditableResources loadedResources, RunOnDevice rd, File selection, String deviceXDir) {
        try {
            final Process p = new ProcessBuilder(deviceXDir + "/tools/deviceX.bat", "create", "project", "--target", "2", "--name", "LWUITPreview",
                    "--path", selection.getAbsolutePath(), "--activity", "PreviewActivity", "--package", "com.oracle.lwuit.preview").
                    redirectErrorStream(true).start();
            rd.waitForProcess(p, false, "Building Activity");
            File libsFolder = new File(selection, "libs");
            libsFolder.mkdirs();
            createFileInDir("/GeneratedProject/DeviceX/libs/IO_DeviceX.jar", new File(libsFolder, "IO_DeviceX.jar"));
            createFileInDir("/GeneratedProject/DeviceX/libs/UI_DeviceX.jar", new File(libsFolder, "UI_DeviceX.jar"));
            File srcFolder = new File(selection, "src/com/oracle/lwuit/preview");
            srcFolder.mkdirs();
            createFileInDir("/PreviewActivity.java", new File(srcFolder, "PreviewActivity.java"));
            createFileInDir("/DeviceXManifest.xml", new File(selection, "DeviceXManifest.xml"));
            File assetsFolder = new File(selection, "assets");
            assetsFolder.mkdirs();
            FileOutputStream fo = new FileOutputStream(new File(assetsFolder, "r.res"));
            loadedResources.save(fo);
            fo.close();
            return rd;
        } catch(Exception err) {
            err.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel, "Error when generating Activity " + err, "IO Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public void xDevicePreview(final JComponent mainPanel, final EditableResources loadedResources) {
        try {
            String node = Preferences.userNodeForPackage(ResourceEditorView.class).get("deviceXSDK", null);
            if (node == null || !new File(node).exists()) {
                node = pickDeviceXSDK();
                if (node != null) {
                    Preferences.userNodeForPackage(ResourceEditorView.class).put("deviceXSDK", node);
                } else {
                    File[] result = ResourceEditorView.showOpenFileChooserWithTitle("Find DeviceX SDK Install", true, "Directory");
                    if (result == null || result.length == 0) {
                        if(JOptionPane.showConfirmDialog(mainPanel, "Do you want to download the DeviceX SDK?", "Download SDK", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) ==
                                JOptionPane.YES_OPTION) {
                            try {
                                Desktop.getDesktop().browse(new URI("http://developer.deviceX.com/sdk/"));
                            } catch(Throwable ioErr) {
                                ioErr.printStackTrace();
                            }
                        }
                        return;
                    }
                    node = result[0].getAbsolutePath();
                    if (!new File(node, "platform-tools/dx.bat").exists()) {
                        JOptionPane.showMessageDialog(mainPanel, "No DeviceX SDK Instance", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Preferences.userNodeForPackage(ResourceEditorView.class).put("deviceXSDK", node);
                }
            }
            String antNode = Preferences.userNodeForPackage(ResourceEditorView.class).get("antLocation", null);
            if (antNode == null || !new File(antNode).exists()) {
                File[] result = ResourceEditorView.showOpenFileChooserWithTitle("Find Apache Ant Install", true, "Directory");
                if (result == null || result.length == 0) {
                    if(JOptionPane.showConfirmDialog(mainPanel, "Do you want to download Apache Ant?", "Download Ant", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) ==
                            JOptionPane.YES_OPTION) {
                        try {
                            Desktop.getDesktop().browse(new URI("http://ant.apache.org/bindownload.cgi"));
                        } catch(Throwable ioErr) {
                            ioErr.printStackTrace();
                        }
                    }
                    return;
                }
                antNode = result[0].getAbsolutePath();
                if (!new File(antNode, "bin/ant.bat").exists()) {
                    JOptionPane.showMessageDialog(mainPanel, "No Apache Ant Installation Found", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Preferences.userNodeForPackage(ResourceEditorView.class).put("antLocation", antNode);
            }
            String javaHomeNode = System.getProperty("java.home");
            if(javaHomeNode == null || !new  File(javaHomeNode + "/bin/javac.exe").exists()) {
                javaHomeNode = Preferences.userNodeForPackage(ResourceEditorView.class).get("javaHome", null);
                if (javaHomeNode == null || !new File(javaHomeNode).exists()) {
                    File[] result = ResourceEditorView.showOpenFileChooserWithTitle("Find The JDK Installation", true, "Directory");
                    if (result == null || result.length == 0) {
                        return;
                    }
                    javaHomeNode = result[0].getAbsolutePath();
                    if (!new File(javaHomeNode, "bin/javac.exe").exists()) {
                        JOptionPane.showMessageDialog(mainPanel, "No JDK Installation Found", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Preferences.userNodeForPackage(ResourceEditorView.class).put("javaHome", javaHomeNode);
                }
            }
            File tmp = File.createTempFile("resourceedit", "tmp");
            tmp.deleteOnExit();
            tmp = new File(tmp.getParentFile(), "resourceeditor");
            if(tmp.exists()) {
                ResourceEditorView.delTree(tmp);
            }
            tmp.mkdirs();
            final File tmpFile = tmp;
            final String finalNode = node;
            final String finalAntNode = antNode;
            final String finalJavaHome = javaHomeNode;
            final RunOnDevice rd = RunOnDevice.showRunDialog(mainPanel, "/help/xDeviceRunOnDeviceURL.html");
            new Thread() {
                public void run() {
                    if(generatePreviewActivity(mainPanel, loadedResources, rd, tmpFile, finalNode) != null) {
                        // run build
                        compileAndUploadToDeviceX(mainPanel, rd, finalJavaHome, finalAntNode, tmpFile, finalNode);
                    }
                }
            }.start();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel, "Error " + ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Try to find a JDE instance automatically
     */
    private String pickDeviceXSDK() {
        File baseDir = new File("C:\\Program Files (x86)\\DeviceX\\deviceX-sdk-windows");
        if(!baseDir.exists()) {
            baseDir = new File("C:\\Program Files\\DeviceX\\deviceX-sdk-windows");
            if(!baseDir.exists()) {
                baseDir = new File("C:\\Program Files (x86)\\DeviceX\\deviceX-sdk");
                if(!baseDir.exists()) {
                    baseDir = new File("C:\\Program Files\\DeviceX\\deviceX-sdk");
                     if(!baseDir.exists()) {
                        return null;
                    }
                }
            }
        }

        return baseDir.getAbsolutePath();
    }

    private void compileAndUploadToDeviceX(JComponent mainPanel, final RunOnDevice rd, final String jdkDir, final String antDir, final File previewMIDletDir, final String sdkDir) {
        try {
            ProcessBuilder builder = new ProcessBuilder(antDir + "\\bin\\ant.bat", "debug", "install").
                redirectErrorStream(true).
                directory(previewMIDletDir);
            builder.environment().put("JAVA_HOME", jdkDir);
            Process p = builder.start();
            rd.waitForProcess(p, false, "Compiling And Installing Activity");
            new ProcessBuilder(sdkDir + "\\platform-tools\\adb.exe", "-d", "shell",
                    "am start -n com.oracle.lwuit.preview/com.oracle.lwuit.preview.LWUITPreview").
                redirectErrorStream(true).start();
            rd.waitForProcess(p, true, "Running App");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel, "Error " + ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
