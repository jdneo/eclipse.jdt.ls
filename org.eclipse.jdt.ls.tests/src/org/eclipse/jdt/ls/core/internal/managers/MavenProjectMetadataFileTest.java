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

package org.eclipse.jdt.ls.core.internal.managers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.internal.preferences.EclipsePreferences;
import org.eclipse.core.internal.utils.FileUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.ls.core.internal.ResourceUtils;
import org.eclipse.jdt.ls.core.internal.WorkspaceHelper;
import org.eclipse.jdt.ls.core.internal.filesystem.JdtlsFsUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MavenProjectMetadataFileTest extends AbstractMavenBasedTest {
	@Parameter
	public String fsMode;

	@Parameters
	public static Collection<String> data(){
		return Arrays.asList(JdtlsFsUtils.FS_LEGACY_MODE, JdtlsFsUtils.FS_AUTO_MODE);
	}

	@Before
	public void setup() throws Exception {
		System.setProperty(JdtlsFsUtils.METADATA_LOCATION_KEY, fsMode);
	}

	@Test
	public void testMetadataFileLocation() throws Exception {
		String name = "salut";
		importProjects("maven/" + name);
		IProject project = WorkspaceHelper.getProject(name);
		assertTrue(project.exists());

		IFile projectDescription = project.getFile(IProjectDescription.DESCRIPTION_FILE_NAME);
		// workaround to get the correct path, see: https://github.com/eclipse/eclipse.jdt.ls/pull/1900
		IPath projectDescriptionPath = FileUtil.toPath(projectDescription.getLocationURI());
		assertTrue(projectDescriptionPath.toFile().exists());
		assertEquals(project.getLocation().isPrefixOf(projectDescriptionPath), !JdtlsFsUtils.isAutoMode());

		IFile classpath = project.getFile(IJavaProject.CLASSPATH_FILE_NAME);
		// workaround to get the correct path, see: https://github.com/eclipse/eclipse.jdt.ls/pull/1900
		IPath classpathPath = FileUtil.toPath(classpath.getLocationURI());
		assertTrue(classpathPath.toFile().exists());
		assertEquals(project.getLocation().isPrefixOf(classpathPath), !JdtlsFsUtils.isAutoMode());

		IFile preferencesFile = project.getFile(EclipsePreferences.DEFAULT_PREFERENCES_DIRNAME);
		// workaround to get the correct path, see: https://github.com/eclipse/eclipse.jdt.ls/pull/1900
		IPath preferencesPath = FileUtil.toPath(preferencesFile.getLocationURI());
		assertTrue(preferencesPath.toFile().exists());
		assertEquals(project.getLocation().isPrefixOf(preferencesPath), !JdtlsFsUtils.isAutoMode());
	}

	@Test
	public void testMetadataFileSync() throws Exception {
		String name = "quickstart2";
		importProjects("maven/" + name);
		IProject project = WorkspaceHelper.getProject(name);
		assertTrue(project.exists());

		// workaround to get the correct path, see: https://github.com/eclipse/eclipse.jdt.ls/pull/1900
		IFile pom = project.getFile("pom.xml");
		String content = ResourceUtils.getContent(pom);
		content = content.replaceAll(">11<", ">1.8<");
		content = content.replace(">11<", ">1.8<");
		//@formatter:on
		ResourceUtils.setContent(pom, content);
		projectsManager.updateProject(project, false);
		waitForBackgroundJobs();

		IFile classpath = project.getFile(IJavaProject.CLASSPATH_FILE_NAME);
		String classpathContent = ResourceUtils.getContent(classpath);
		assertTrue(classpathContent.contains("StandardVMType/JavaSE-1.8"));
	}
}
