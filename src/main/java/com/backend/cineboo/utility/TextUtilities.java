package com.backend.cineboo.utility;

import java.text.Normalizer;

public class TextUtilities {

        public static String removeDiacritics(String input) {
            String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
            return normalized.replaceAll("\\p{M}", ""); // Remove diacritic marks
        }



}
