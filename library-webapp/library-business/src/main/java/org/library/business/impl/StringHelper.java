package org.library.business.impl;

import java.text.Normalizer;

class StringHelper {


    String cleanUpSpecialChars(String str) {
        str = str.toUpperCase();
        return Normalizer
                .normalize(str, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }
}
