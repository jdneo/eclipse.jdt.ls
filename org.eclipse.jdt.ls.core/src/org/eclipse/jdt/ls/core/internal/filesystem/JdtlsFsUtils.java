/*******************************************************************************
 * Copyright (c) 2021 Red Hat Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Microsoft Corporation
 *******************************************************************************/

package org.eclipse.jdt.ls.core.internal.filesystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.eclipse.core.internal.preferences.EclipsePreferences;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;

/**
 * Utilities of the file system implementation.
 */
public class JdtlsFsUtils {
    private JdtlsFsUtils() {}

    // todo: any util to handle the path?
    public static final IPath METADATA_FOLDER_PATH = ResourcesPlugin.getWorkspace().getRoot().getLocation().append(".metadata").append(".plugins")
            .append(ResourcesPlugin.PI_RESOURCES).append(".projects");

    public static final String METADATA_LOCATION_KEY = "java.project.metadataLocation";

    /**
     * The metadata files
     */
    private static final Set<String> METADATA_NAMES = new HashSet<>(Arrays.asList(
        IProjectDescription.DESCRIPTION_FILE_NAME,
        EclipsePreferences.DEFAULT_PREFERENCES_DIRNAME,
        IJavaProject.CLASSPATH_FILE_NAME
    ));

    /**
     * The mode of the file system. If it's in auto mode, the metadata files will be stored
     * in the workspace metadata folder. See: {@link JdtlsFsUtils#shouldStoreInMetadataFolder}.
     */
    private static final String FS_AUTO_MODE = "auto";

    /**
     * Determine whether the resource should be stored in workspace's metadata folder.
     * <p>
     * The file will be stored in workspace's metadata folder when following conditions meet:
     * <ul>
     *   <li>The system property shows that it's allowed to store them in workspace.</li>
     *   <li>The file belongs to the metadata file defined in {@link JdtlsFsUtils#METADATA_NAMES}.</li>
     *   <li>The project's root path does not contain any of the metadata files.</li>
     * </ul>
     * </p>
     * 
     * @param location the path of the resource.
     * @return whether the resource needs to be stored in workspace's metadata folder.
     */
    static boolean shouldStoreInMetadataFolder(IPath location) { 
        if (!isAutoMode()) {
            return false;
        }

        if (location == null || location.segmentCount() < 2) {
            return false;
        }

        if (location.lastSegment().endsWith(EclipsePreferences.PREFS_FILE_EXTENSION)) {
            location = location.removeLastSegments(1);
        }

        if (location.segmentCount() < 2) {
            return false;
        }

        if (!METADATA_NAMES.contains(location.lastSegment())) {
            return false;
        }

        IPath projectLocation = location.removeLastSegments(1);
        boolean persistedAtRoot = METADATA_NAMES.stream().anyMatch(name -> {
            return projectLocation.append(name).toFile().exists();
        });

        return !persistedAtRoot;
    }

    /**
     * Get the name of the given resource's belonging project.
     * @param location the path of the resource.
     * @return the name of the given resource's belonging project.
     */
    static String getProjectName(IPath location) {
        int resultProjectPathSegments = 0;
        IProject belongingProject = null;
        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects(IContainer.INCLUDE_HIDDEN);
        for (IProject project : projects) {
            IPath projectLocation = project.getLocation();
            if (projectLocation != null && projectLocation.isPrefixOf(location)) {
                int segmentsToRemove = projectLocation.segmentCount();
                if (segmentsToRemove > resultProjectPathSegments) {
                    resultProjectPathSegments = segmentsToRemove;
                    belongingProject = project;
                }
            }
        }

        if (belongingProject == null) {
            return null;
        }

        return belongingProject.getName();
    }

    /**
     * Get the redirected path of the input path. The path will be redirected to
     * the workspace's metadata folder ({@link JdtlsFsUtils#METADATA_FOLDER_PATH}).
     * @param projectName name of the project.
     * @param path path needs to be redirected.
     * @return the redirected path.
     */
    static IPath getMetaDataFilePath(String projectName, IPath path) {
        if (path.segmentCount() == 1) {
            return METADATA_FOLDER_PATH.append(projectName).append(path);
        }

        if (Objects.equals(path.lastSegment(), IProjectDescription.DESCRIPTION_FILE_NAME) ||
                Objects.equals(path.lastSegment(), IJavaProject.CLASSPATH_FILE_NAME) ||
                Objects.equals(path.lastSegment(), EclipsePreferences.DEFAULT_PREFERENCES_DIRNAME)) {
            return METADATA_FOLDER_PATH.append(projectName).append(path.lastSegment());
        } else if (path.lastSegment().endsWith(EclipsePreferences.PREFS_FILE_EXTENSION)) {
            if (path.segmentCount() > 2) {
                return METADATA_FOLDER_PATH.append(projectName)
                        .append(EclipsePreferences.DEFAULT_PREFERENCES_DIRNAME)
                        .append(path.lastSegment());
            } else {
                return METADATA_FOLDER_PATH.append(projectName)
                        .append(EclipsePreferences.DEFAULT_PREFERENCES_DIRNAME)
                        .append(path.lastSegment());
            }
            
        }

        return null;
    }

    /**
     * Check the current file system working mode.
     * @return whether the file system is working in auto mode now.
     */
    static boolean isAutoMode() {
        String location = System.getProperty(METADATA_LOCATION_KEY);
        if (FS_AUTO_MODE.equals(location)) {
            return true;
        }
        return false;
    }
}
