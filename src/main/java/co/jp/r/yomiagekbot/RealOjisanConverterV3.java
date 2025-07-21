
package co.jp.r.yomiagekbot;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class RealOjisanConverterV3 {

    public enum Category {
        GREETING, SMALL_TALK, ENCOURAGEMENT, APOLOGY
    }

    private static final Map<Category, List<String>> CATEGORY_KEYWORDS = Map.of(
        Category.GREETING, List.of("おはよう", "こんにちは", "こんばんは", "やっほ", "元気", "調子"),
        Category.SMALL_TALK, List.of("暑い", "寒い", "天気", "昼ごはん", "最近", "どう", "映画", "仕事"),
        Category.ENCOURAGEMENT, List.of("がんばって", "応援", "大丈夫", "無理", "休んで", "つらい", "心配", "疲れ"),
        Category.APOLOGY, List.of("ごめん", "すまない", "許して", "謝る", "失礼", "反省")
    );

    private static final Map<Category, String[]> ENDINGS = Map.of(
        Category.GREETING, new String[]{"だよね〜", "だよ〜ん", "よろしくね〜〜", "今日もがんばろ〜ね〜", "今日もファイト〜〜💪"},
        Category.SMALL_TALK, new String[]{"だよねぇ〜〜〜", "なんだよね〜〜", "わかるぅ〜〜〜", "って感じ〜〜〜〜", "ほんとほんと〜〜"},
        Category.ENCOURAGEMENT, new String[]{"無理しないでね〜〜", "おじさん応援してるよぉぉ〜〜〜📣", "元気出してね〜〜", "きっと大丈夫だよぉ〜〜〜〜"},
        Category.APOLOGY, new String[]{"ほんとごめんね〜〜〜〜", "許してね…🥺", "気をつけるからね〜〜〜", "しょんぼり…反省してるよぉぉ〜〜〜〜"}
    );

    private static final Map<Category, String[]> INTERJECTIONS = Map.of(
        Category.GREETING, new String[]{"やっほ〜〜", "おはよぉぉ〜〜☀", "こんちゃ〜〜〜", "ごきげんよう〜〜〜〜"},
        Category.SMALL_TALK, new String[]{"うんうん〜〜", "へぇ〜〜！", "ふふっ", "それな〜〜〜〜"},
        Category.ENCOURAGEMENT, new String[]{"うんうん", "そうそう〜〜", "よしよし〜〜〜", "ぎゅ〜〜〜っ🤗"},
        Category.APOLOGY, new String[]{"ごめんね〜〜", "ううっ…", "ほんとに…", "しょんぼり…🥺"}
    );

    private static final String[] SUFFIX_PATTERNS = {
        "ってばさ〜〜〜", "なのよ〜〜〜", "なんだけどぉ〜〜〜", "だってば〜〜〜〜", "だもんね〜〜〜"
    };

    private static final Random random = new Random();

    public String convert(String input) {
        Category category = detectCategory(input);
        return convert(input, category);
    }

    public String convert(String input, Category category) {
        String interjection = getRandom(INTERJECTIONS.getOrDefault(category, new String[]{"ねぇ〜〜"}));
        String ending = getRandom(ENDINGS.getOrDefault(category, new String[]{"だよ〜〜〜〜"}));
        String suffix = getRandom(SUFFIX_PATTERNS);

        return interjection + "、" + input + "、" + suffix + " " + ending + getRandomEmoji();
    }

    private Category detectCategory(String input) {
        for (Category category : CATEGORY_KEYWORDS.keySet()) {
            for (String keyword : CATEGORY_KEYWORDS.get(category)) {
                if (input.contains(keyword)) {
                    return category;
                }
            }
        }
        return Category.SMALL_TALK;
    }

    private String getRandomEmoji() {
        String[] emojis = {"💘", "💕", "😚", "😳", "✨", "🥺", "😆", "😎", "🤗"};
        return emojis[random.nextInt(emojis.length)];
    }

    private String getRandom(String[] array) {
        return array[random.nextInt(array.length)];
    }
}
