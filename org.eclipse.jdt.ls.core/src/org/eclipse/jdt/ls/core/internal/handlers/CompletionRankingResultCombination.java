package org.eclipse.jdt.ls.core.internal.handlers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CompletionRankingResultCombination {
    private int score;

	private Set<Character> decorators;

    private Map<String, String> data;

    public CompletionRankingResultCombination() {
        this.score = 0;
        this.decorators = new HashSet<>();
        this.data = new HashMap<>();
    }

    public int getScore() {
        return score;
    }

    public void addScore(int score) {
        if (score <= 0) {
            return;
        }
        if (score > 100) {
            score = 100;
        }
        this.score += score;
    }

    public String getDecorators() {
        return String.valueOf(this.decorators.stream().sorted().toArray(Character[]::new));
    }

    public void appendDecorators(char decorator) {
        if (decorator != 0) {
            this.decorators.add(decorator);
        }
    }

    public Map<String, String> getData() {
        return data;
    }

    public void addData(Map<String, String> data) {
        if (data != null) {
            for (String key : data.keySet()) {
                this.data.put(key, data.get(key));
            }
            this.data = data;
        }
    }
}
