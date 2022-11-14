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

import java.util.List;

import org.eclipse.jdt.core.CompletionContext;
import org.eclipse.jdt.core.CompletionProposal;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.ls.core.internal.contentassist.CompletionProposalRequestor;
import org.eclipse.lsp4j.CompletionItem;

/**
 * Interface that can be implemented to participate into the completion processing.
 * <p>
 * Note: Following APIs are in experimental stage which means they might be changed
 * in the future.
 * </p>
 */
public interface CompletionRankingProvider {

	/**
	 * Providers can rank the proposals by adding or subtracting the relevance filed of the proposals.
	 * <p>
	 * The method will be invoked before parsing completion proposals to completion items.
	 * </p>
	 * <p>
	 * Please do NOT modify other fields of the proposals.
	 * Only modify the ranking via <code>proposal.setRelevance(rating);</code>
	 * </p>
	 *
	 * @param proposals The completion proposals accepted by {@link CompletionProposalRequestor}.
	 * @param context The completion context accepted by {@link CompletionProposalRequestor}.
	 * @param unit The compilation unit where the completion happens.
	 */
	CompletionRankingResult[] rank(List<CompletionProposal> proposals, CompletionContext context, ICompilationUnit unit);

	/**
	 * This method will be invoked when the completion item is selected.
	 * Providers can use this method to do some post actions if needed.
	 * 
	 * @param completionData The data of the completion, includes:
	 * 
	 * <ul>
	 *   <li>elapsedTime - The time cost during the completion phase.</li>
	 *   <li>rawProposal - The raw proposal of the selected completion item.</li>
	 * </ul>
	 */
	void onDidCompletionItemSelect(CompletionItem item);
}
