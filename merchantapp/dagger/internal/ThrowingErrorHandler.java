package dagger.internal;

import dagger.internal.Linker.ErrorHandler;
import java.util.List;

public final class ThrowingErrorHandler implements ErrorHandler {
    public void handleErrors(List<String> list) {
        if (!list.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Errors creating object graph:");
            for (String append : list) {
                stringBuilder.append("\n  ").append(append);
            }
            throw new IllegalStateException(stringBuilder.toString());
        }
    }
}
