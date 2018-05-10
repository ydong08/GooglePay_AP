package com.google.common.base;

import java.util.logging.Logger;

final class Platform {
    private static final Logger logger = Logger.getLogger(Platform.class.getName());
    private static final PatternCompiler patternCompiler = loadPatternCompiler();

    private Platform() {
    }

    static boolean stringIsNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private static PatternCompiler loadPatternCompiler() {
        return new PatternCompiler();
    }
}
