package org.eclipse.jdt.ls.core.internal;

import ch.epfl.scala.bsp4j.BuildClient;
import ch.epfl.scala.bsp4j.DidChangeBuildTarget;
import ch.epfl.scala.bsp4j.LogMessageParams;
import ch.epfl.scala.bsp4j.PublishDiagnosticsParams;
import ch.epfl.scala.bsp4j.ShowMessageParams;
import ch.epfl.scala.bsp4j.TaskFinishParams;
import ch.epfl.scala.bsp4j.TaskProgressParams;
import ch.epfl.scala.bsp4j.TaskStartParams;

public class BspClient implements BuildClient {

	@Override
	public void onBuildShowMessage(ShowMessageParams params) {
		System.out.println(params.getMessage());
	}

	@Override
	public void onBuildLogMessage(LogMessageParams params) {
		System.out.println(params.getMessage());
	}

	@Override
	public void onBuildTaskStart(TaskStartParams params) {
		System.out.println(params.getMessage());
	}

	@Override
	public void onBuildTaskProgress(TaskProgressParams params) {
		System.out.println(params.getMessage());
	}

	@Override
	public void onBuildTaskFinish(TaskFinishParams params) {
		System.out.println(params.getMessage());
	}

	@Override
	public void onBuildPublishDiagnostics(PublishDiagnosticsParams params) {
		System.out.println(params.getTextDocument().getUri());
	}

	@Override
	public void onBuildTargetDidChange(DidChangeBuildTarget params) {
		System.out.println("DidChangeBuildTarget");
	}
}