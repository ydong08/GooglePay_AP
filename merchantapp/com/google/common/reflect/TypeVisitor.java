package com.google.common.reflect;

import com.google.common.collect.Sets;
import java.lang.reflect.Type;
import java.util.Set;

class TypeVisitor {
    private final Set<Type> visited = Sets.newHashSet();

    TypeVisitor() {
    }
}
