package com.pucminas.commons.utils;

import opennlp.tools.tokenize.SimpleTokenizer;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class TextMatcherUtils {
    private static final SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;

    private TextMatcherUtils() {
    }

    public static String findBestMatch(final String reference, final String excludedToken, List<String> suggested) {
        final Map<String, String> suggestedNormalizedMap = new HashMap<>();
        suggested.forEach(title -> suggestedNormalizedMap.put(normalize(title), title));

        final List<String> referenceNormalized = Arrays.stream(tokenizer.tokenize(normalize(reference))).toList();
        final String[] excludedTokensNormalized = tokenizer.tokenize(normalize(excludedToken));

        final String bestMatch = suggestedNormalizedMap.keySet().stream()
                .map(title -> {
                    double similarity = calculateSimilarity(referenceNormalized, tokenizer.tokenize(title));
                    boolean isKeyOnly = excludeSimilarity(referenceNormalized, tokenizer.tokenize(title), excludedTokensNormalized);
                    return Map.entry(title, isKeyOnly ? 0.0 : similarity); // Zera se for só pela cidade
                })
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);

        return suggestedNormalizedMap.get(bestMatch);
    }

    /**
     * Utiliza-se da biblioteca do apache para normalizar palavras
     *
     * @return List<String>
     */
    private static String normalize(String word) {
        return Optional.of(word).map(StrUtils::removeNonTextNumbers).map(StrUtils::normalizeAccents)
            .map(StringUtils::normalizeSpace).map(StringUtils::trimToNull).map(String::toUpperCase)
            .orElse(null);
    }

    /**
     * @return Retorna a similaridade como a proporção de palavras em comum
     */
    private static double calculateSimilarity(List<String> referenceTokens, String[] suggestedTokens) {

        final List<String> suggestedTokensList = Arrays.asList(suggestedTokens);
        final long commonWordCount = suggestedTokensList.stream().filter(referenceTokens::contains).count();

        return (double) commonWordCount / referenceTokens.size();
    }

    private static boolean excludeSimilarity(List<String> referenceTokens, String[] suggestedTokens, String[] excludedTokens) {
        final List<String> suggestedTokensList = Arrays.asList(suggestedTokens);
        final List<String> excludeTokenList = Arrays.asList(excludedTokens);

        final long commonWordCount = suggestedTokensList.stream().filter(referenceTokens::contains).count();
        final long excludedTokenCount = suggestedTokensList.stream().filter(excludeTokenList::contains).count();

        return commonWordCount == excludedTokenCount; // se for considerado similiar apenas por causa de uma determinada palavra
    }

}
