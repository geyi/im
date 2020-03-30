package com.airing.im.enums;

public enum Language {
    EN("en-in", "fts_game.t_quiz", "fts_game.t_quiz_option"),
    HI("hi-in", "fts_game.t_quiz_hi", "fts_game.t_quiz_option_hi");

    private String key;
    private String quizTable;
    private String quizOptionTable;

    public String getKey() {
        return key;
    }

    public String getQuizTable() {
        return quizTable;
    }

    public String getQuizOptionTable() {
        return quizOptionTable;
    }

    private Language(String key, String quizTable, String quizOptionTable) {
        this.key = key;
        this.quizTable = quizTable;
        this.quizOptionTable = quizOptionTable;
    }

    public static Language getLanguage(String key) {
        for (Language l : Language.values()) {
            if (l.getKey().equals(key)) {
                return l;
            }
        }

        return null;
    }
}
