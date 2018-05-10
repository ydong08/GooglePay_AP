package dagger.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public final class Modules {

    public static class ModuleWithAdapter {
        private final Object module;
        private final ModuleAdapter<?> moduleAdapter;

        ModuleWithAdapter(ModuleAdapter<?> moduleAdapter, Object obj) {
            this.moduleAdapter = moduleAdapter;
            this.module = obj;
        }

        public ModuleAdapter<?> getModuleAdapter() {
            return this.moduleAdapter;
        }

        public Object getModule() {
            return this.module;
        }
    }

    private Modules() {
    }

    public static ArrayList<ModuleWithAdapter> loadModules(Loader loader, Object[] objArr) {
        int i;
        int length = objArr.length;
        Object arrayList = new ArrayList(length);
        HashSet hashSet = new HashSet(length);
        for (i = length - 1; i >= 0; i--) {
            Object obj = objArr[i];
            if (obj instanceof Class) {
                if (hashSet.add((Class) obj)) {
                    ModuleAdapter moduleAdapter = loader.getModuleAdapter((Class) obj);
                    arrayList.add(new ModuleWithAdapter(moduleAdapter, moduleAdapter.newModule()));
                }
            } else if (hashSet.add(obj.getClass())) {
                arrayList.add(new ModuleWithAdapter(loader.getModuleAdapter(obj.getClass()), obj));
            }
        }
        i = arrayList.size();
        for (int i2 = 0; i2 < i; i2++) {
            collectIncludedModulesRecursively(loader, ((ModuleWithAdapter) arrayList.get(i2)).getModuleAdapter(), arrayList, hashSet);
        }
        return arrayList;
    }

    private static void collectIncludedModulesRecursively(Loader loader, ModuleAdapter<?> moduleAdapter, List<ModuleWithAdapter> list, HashSet<Class<?>> hashSet) {
        for (Class cls : moduleAdapter.includes) {
            if (!hashSet.contains(cls)) {
                ModuleAdapter moduleAdapter2 = loader.getModuleAdapter(cls);
                list.add(new ModuleWithAdapter(moduleAdapter2, moduleAdapter2.newModule()));
                hashSet.add(cls);
                collectIncludedModulesRecursively(loader, moduleAdapter2, list, hashSet);
            }
        }
    }
}
