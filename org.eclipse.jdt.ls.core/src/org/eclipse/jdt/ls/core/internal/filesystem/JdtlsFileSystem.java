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

import java.net.URI;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.internal.filesystem.local.LocalFileSystem;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.ls.core.internal.ResourceUtils;

public class JdtlsFileSystem extends LocalFileSystem {

    @Override
    public IFileStore getStore(IPath path) {
        if (JdtlsFsUtils.shouldStoreInMetadataFolder(path)) {
            return new JdtlsFile(JdtlsFsUtils.getMetaDataFilePath("", path).toFile()); 
        }

        return new JdtlsFile(path.toFile());
    }

    @Override
    public IFileStore getStore(URI uri) {
        IPath path = ResourceUtils.filePathFromURI(uri.toString());
        return getStore(path);
    }
}
