package org.eclipse.jdt.ls.core.internal.managers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ch.epfl.scala.bsp4j.BuildTarget;

public class BspUtils {
    private BspUtils() {}

    public static Map<String, List<BuildTarget>> mapBuildTargetsByBaseDir(List<BuildTarget> buildTargets) {
		// we assume all build targets will have a non-null base directory.
		return buildTargets.stream().collect(Collectors.groupingBy(BuildTarget::getBaseDirectory));
	}
}
