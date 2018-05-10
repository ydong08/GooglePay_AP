package com.google.android.libraries.commerce.hce.ndef;

import com.google.common.base.Objects;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class Text {
    public static final Charset DEFAULT_TEXT_CHARSET = StandardCharsets.UTF_8;
    public static final Charset LANGUAGE_CHARSET = StandardCharsets.US_ASCII;
    public static final Format LANGUAGE_FORMAT = Format.get(LANGUAGE_CHARSET);
    private final Charset charset;
    private final String languageCode;
    private final String text;

    public Text(String str) {
        this(null, null, str);
    }

    public Text(Charset charset, String str, String str2) {
        if (charset == null) {
            charset = DEFAULT_TEXT_CHARSET;
        }
        this.charset = charset;
        if (str == null) {
            str = Locale.getDefault().getLanguage();
        }
        this.languageCode = str;
        this.text = str2;
    }

    public String getLanguageCode() {
        return this.languageCode;
    }

    public String getText() {
        return this.text;
    }

    public String toString() {
        return String.format("%s (%s)", new Object[]{this.text, this.languageCode});
    }

    public int hashCode() {
        return Objects.hashCode(getLanguageCode(), getText());
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Text text = (Text) obj;
        if (java.util.Objects.equals(getLanguageCode(), text.getLanguageCode()) && java.util.Objects.equals(getText(), text.getText())) {
            return true;
        }
        return false;
    }
}
