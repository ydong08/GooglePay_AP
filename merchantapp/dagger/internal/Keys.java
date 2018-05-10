package dagger.internal;

import dagger.Lazy;
import dagger.MembersInjector;
import java.lang.annotation.Annotation;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;
import javax.inject.Provider;
import javax.inject.Qualifier;

public final class Keys {
    private static final Memoizer<Class<? extends Annotation>, Boolean> IS_QUALIFIER_ANNOTATION = new Memoizer<Class<? extends Annotation>, Boolean>() {
        protected Boolean create(Class<? extends Annotation> cls) {
            return Boolean.valueOf(cls.isAnnotationPresent(Qualifier.class));
        }
    };
    private static final String LAZY_PREFIX = String.valueOf(Lazy.class.getCanonicalName()).concat("<");
    private static final String MEMBERS_INJECTOR_PREFIX = String.valueOf(MembersInjector.class.getCanonicalName()).concat("<");
    private static final String PROVIDER_PREFIX = String.valueOf(Provider.class.getCanonicalName()).concat("<");
    private static final String SET_PREFIX = String.valueOf(Set.class.getCanonicalName()).concat("<");

    Keys() {
    }

    public static String get(Type type) {
        return get(type, null);
    }

    public static String getMembersKey(Class<?> cls) {
        return "members/".concat(cls.getName());
    }

    private static String get(Type type, Annotation annotation) {
        Type boxIfPrimitive = boxIfPrimitive(type);
        if (annotation == null && (boxIfPrimitive instanceof Class) && !((Class) boxIfPrimitive).isArray()) {
            return ((Class) boxIfPrimitive).getName();
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (annotation != null) {
            stringBuilder.append(annotation).append("/");
        }
        typeToString(boxIfPrimitive, stringBuilder, true);
        return stringBuilder.toString();
    }

    public static String getSetKey(Type type, Annotation[] annotationArr, Object obj) {
        Annotation extractQualifier = extractQualifier(annotationArr, obj);
        Type boxIfPrimitive = boxIfPrimitive(type);
        StringBuilder stringBuilder = new StringBuilder();
        if (extractQualifier != null) {
            stringBuilder.append(extractQualifier).append("/");
        }
        stringBuilder.append(SET_PREFIX);
        typeToString(boxIfPrimitive, stringBuilder, true);
        stringBuilder.append(">");
        return stringBuilder.toString();
    }

    public static String get(Type type, Annotation[] annotationArr, Object obj) {
        return get(type, extractQualifier(annotationArr, obj));
    }

    private static Annotation extractQualifier(Annotation[] annotationArr, Object obj) {
        Annotation annotation = null;
        int length = annotationArr.length;
        int i = 0;
        while (i < length) {
            Annotation annotation2;
            Annotation annotation3 = annotationArr[i];
            if (!((Boolean) IS_QUALIFIER_ANNOTATION.get(annotation3.annotationType())).booleanValue()) {
                annotation2 = annotation;
            } else if (annotation != null) {
                String valueOf = String.valueOf(obj);
                throw new IllegalArgumentException(new StringBuilder(String.valueOf(valueOf).length() + 34).append("Too many qualifier annotations on ").append(valueOf).toString());
            } else {
                annotation2 = annotation3;
            }
            i++;
            annotation = annotation2;
        }
        return annotation;
    }

    private static void typeToString(Type type, StringBuilder stringBuilder, boolean z) {
        int i = 0;
        if (type instanceof Class) {
            Class cls = (Class) type;
            if (cls.isArray()) {
                typeToString(cls.getComponentType(), stringBuilder, false);
                stringBuilder.append("[]");
            } else if (!cls.isPrimitive()) {
                stringBuilder.append(cls.getName());
            } else if (z) {
                String str = "Uninjectable type ";
                String valueOf = String.valueOf(cls.getName());
                throw new UnsupportedOperationException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
            } else {
                stringBuilder.append(cls.getName());
            }
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            typeToString(parameterizedType.getRawType(), stringBuilder, true);
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            stringBuilder.append("<");
            while (i < actualTypeArguments.length) {
                if (i != 0) {
                    stringBuilder.append(", ");
                }
                typeToString(actualTypeArguments[i], stringBuilder, true);
                i++;
            }
            stringBuilder.append(">");
        } else if (type instanceof GenericArrayType) {
            typeToString(((GenericArrayType) type).getGenericComponentType(), stringBuilder, false);
            stringBuilder.append("[]");
        } else {
            String valueOf2 = String.valueOf(type);
            throw new UnsupportedOperationException(new StringBuilder(String.valueOf(valueOf2).length() + 18).append("Uninjectable type ").append(valueOf2).toString());
        }
    }

    static String getBuiltInBindingsKey(String str) {
        int startOfType = startOfType(str);
        if (substringStartsWith(str, startOfType, PROVIDER_PREFIX)) {
            return extractKey(str, startOfType, str.substring(0, startOfType), PROVIDER_PREFIX);
        }
        if (substringStartsWith(str, startOfType, MEMBERS_INJECTOR_PREFIX)) {
            return extractKey(str, startOfType, "members/", MEMBERS_INJECTOR_PREFIX);
        }
        return null;
    }

    static String getLazyKey(String str) {
        int startOfType = startOfType(str);
        if (substringStartsWith(str, startOfType, LAZY_PREFIX)) {
            return extractKey(str, startOfType, str.substring(0, startOfType), LAZY_PREFIX);
        }
        return null;
    }

    private static int startOfType(String str) {
        return str.startsWith("@") ? str.lastIndexOf(47) + 1 : 0;
    }

    private static String extractKey(String str, int i, String str2, String str3) {
        String valueOf = String.valueOf(str2);
        String valueOf2 = String.valueOf(str.substring(str3.length() + i, str.length() - 1));
        return valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
    }

    private static boolean substringStartsWith(String str, int i, String str2) {
        return str.regionMatches(i, str2, 0, str2.length());
    }

    public static boolean isAnnotated(String str) {
        return str.startsWith("@");
    }

    public static String getClassName(String str) {
        int i = 0;
        if (str.startsWith("@") || str.startsWith("members/")) {
            i = str.lastIndexOf(47) + 1;
        }
        if (str.indexOf(60, i) == -1 && str.indexOf(91, i) == -1) {
            return str.substring(i);
        }
        return null;
    }

    public static boolean isPlatformType(String str) {
        return str.startsWith("java.") || str.startsWith("javax.") || str.startsWith("android.");
    }

    private static Type boxIfPrimitive(Type type) {
        if (type == Byte.TYPE) {
            return Byte.class;
        }
        if (type == Short.TYPE) {
            return Short.class;
        }
        if (type == Integer.TYPE) {
            return Integer.class;
        }
        if (type == Long.TYPE) {
            return Long.class;
        }
        if (type == Character.TYPE) {
            return Character.class;
        }
        if (type == Boolean.TYPE) {
            return Boolean.class;
        }
        if (type == Float.TYPE) {
            return Float.class;
        }
        if (type == Double.TYPE) {
            return Double.class;
        }
        if (type == Void.TYPE) {
            return Void.class;
        }
        return type;
    }
}
