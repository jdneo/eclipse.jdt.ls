/*******************************************************************************
 * Copyright (c) 2022 Microsoft Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Microsoft Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.ls.core.internal.handlers;

import java.util.LinkedList;
import java.util.List;

public class CompletionRankingProviderStore {

	private List<CompletionRankingProvider> providers;

	private static class InstanceHolder {
		public static CompletionRankingProviderStore instance = new CompletionRankingProviderStore();
	}

	private CompletionRankingProviderStore() {
		this.providers = new LinkedList<>();
	}

	public static CompletionRankingProviderStore instance() { 
		return InstanceHolder.instance;
	}

	public List<CompletionRankingProvider> getRankingProviders() {
		return this.providers;
	}

	public void addRankingProvider(CompletionRankingProvider provider) {
		for (CompletionRankingProvider p : this.providers) {
			if (p.equals(provider)) {
				return;
			}
		}
		this.providers.add(provider);
	}
}
