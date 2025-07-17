package co.jp.r.yomiagekbot;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RealOjisanConverterV2 {

    public enum Category {
        GREETING, SMALL_TALK, ENCOURAGEMENT, APOLOGY
    }

    // ========================
    // 🔹 カテゴリ別キーワード
    // ========================
    private static final Map<Category, List<String>> CATEGORY_KEYWORDS = Map.of(
            Category.GREETING, List.of("おはよう", "こんにちは", "こんばんは", "やっほ", "元気", "調子"),
            Category.SMALL_TALK, List.of("暑い", "寒い", "天気", "昼ごはん", "最近", "どう", "映画", "仕事"),
            Category.ENCOURAGEMENT, List.of("がんばって", "応援", "大丈夫", "無理", "休んで", "つらい", "心配", "疲れ"),
            Category.APOLOGY, List.of("ごめん", "すまない", "許して", "謝る", "失礼", "反省")
    );

    // ========================
    // 🔹 カテゴリ別語尾
    // ========================
    private static final Map<Category, String[]> ENDINGS = Map.of(
            Category.GREETING, new String[]{"だよね", "だよ〜", "よろしくね", "今日もがんばろうね"},
            Category.SMALL_TALK, new String[]{"だよね", "なんだよ〜", "わかる〜", "って感じかな"},
            Category.ENCOURAGEMENT, new String[]{"無理しないでね", "応援してるよ", "元気出してね", "大丈夫だよ"},
            Category.APOLOGY, new String[]{"ほんとごめんね", "許してね", "気をつけるよ", "反省してるよ"}
    );

    // ========================
    // 🔹 カテゴリ別相槌・呼びかけ
    // ========================
    private static final Map<Category, String[]> INTERJECTIONS = Map.of(
            Category.GREETING, new String[]{"やっほ", "おはよう", "こんちゃ〜", "こんにちは"},
            Category.SMALL_TALK, new String[]{"うんうん", "へぇ〜", "ふふっ", "それな"},
            Category.ENCOURAGEMENT, new String[]{"うん", "そうそう", "うんうん", "よしよし"},
            Category.APOLOGY, new String[]{"ごめん", "ううっ", "ほんとに", "しょんぼり"}
    );

    private static final Random random = new Random();

    // ========================
    // 🔹 自動カテゴリ推定付き変換
    // ========================
    public String convert(String input) {
        Category category = detectCategory(input);
        return convert(input, category);
    }

    // ========================
    // 🔹 指定カテゴリで変換
    // ========================
    public String convert(String input, Category category) {
        StringBuilder sb = new StringBuilder();

        // 挨拶カテゴリは冒頭に呼びかけ追加
        if (category == Category.GREETING) {
            sb.append(getRandom(INTERJECTIONS.get(category))).append("！ ");
        }

        String[] sentences = input.split("(?<=[。！？])");

        for (String sentence : sentences) {
            if (sentence.trim().isEmpty()) continue;

            if (random.nextInt(100) < 40) {
                sb.append(getRandom(INTERJECTIONS.get(category))).append("、");
            }

            sb.append(sentence.trim());
            sb.append("、").append(getRandom(ENDINGS.get(category))).append("。");
            sb.append("\n");
        }

        return sb.toString();
    }

    // ========================
    // 🔹 カテゴリ判定
    // ========================
    private Category detectCategory(String input) {
        Map<Category, Integer> scoreMap = new EnumMap<>(Category.class);

        for (Category cat : Category.values()) {
            int count = 0;
            for (String keyword : CATEGORY_KEYWORDS.get(cat)) {
                if (input.contains(keyword)) {
                    count++;
                }
            }
            scoreMap.put(cat, count);
        }

        return scoreMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .filter(entry -> entry.getValue() > 0)
                .map(Map.Entry::getKey)
                .orElse(Category.SMALL_TALK);

    }

    private String getRandom(String[] array) {
        return array[random.nextInt(array.length)];
    }
}
