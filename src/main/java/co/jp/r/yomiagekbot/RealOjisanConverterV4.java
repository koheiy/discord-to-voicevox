
package co.jp.r.yomiagekbot;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class RealOjisanConverterV4 {

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
        Category.GREETING, new String[]{
            "だよね〜", "だよ〜ん", "よろしくね〜〜", "今日もがんばろ〜ね〜", "今日もファイト〜〜💪", "元気にいこ〜〜〜〜〜✨"
        },
        Category.SMALL_TALK, new String[]{
            "だよねぇ〜〜〜", "なんだよね〜〜", "わかるぅ〜〜〜", "って感じ〜〜〜〜", "ほんとほんと〜〜", "それな〜〜〜〜〜"
        },
        Category.ENCOURAGEMENT, new String[]{
            "無理しないでね〜〜", "おじさん応援してるよぉぉ〜〜〜📣", "元気出してね〜〜", "きっと大丈夫だよぉ〜〜〜〜", "休んでいいんだよ〜〜〜〜"
        },
        Category.APOLOGY, new String[]{
            "ほんとごめんね〜〜〜〜", "許してね…🥺", "気をつけるからね〜〜〜", "しょんぼり…反省してるよぉぉ〜〜〜〜"
        }
    );

    private static final Map<Category, String[]> INTERJECTIONS = Map.of(
        Category.GREETING, new String[]{"やっほ〜〜", "おはよぉぉ〜〜☀", "こんちゃ〜〜〜", "ごきげんよう〜〜〜〜"},
        Category.SMALL_TALK, new String[]{"うんうん〜〜", "へぇ〜〜！", "ふふっ", "それな〜〜〜〜", "あれさ〜〜〜"},
        Category.ENCOURAGEMENT, new String[]{"うんうん", "そうそう〜〜", "よしよし〜〜〜", "ぎゅ〜〜〜っ🤗", "えらいえらい〜〜"},
        Category.APOLOGY, new String[]{"ごめんね〜〜", "ううっ…", "ほんとに…", "しょんぼり…🥺"}
    );

    private static final String[] SUFFIX_PATTERNS = {
        "ってばさ〜〜〜", "なのよ〜〜〜", "なんだけどぉ〜〜〜", "だってば〜〜〜〜", "だもんね〜〜〜", "なのにぃぃ〜〜〜〜"
    };

    private static final String[] MORNING_ADDITIONS = {"☀️", "🌞", "今日も一緒にがんばろ〜〜！"};
    private static final String[] NOON_ADDITIONS = {"🍔", "☕", "お昼何食べた〜〜？"};
    private static final String[] NIGHT_ADDITIONS = {"🌙", "🌇", "夜はさみしいねぇぇ〜〜〜〜"};
    private static final String[] MIDNIGHT_ADDITIONS = {"🌃", "眠れないのぉぉ〜〜〜？", "おじさんまだ起きてるよぉぉ〜〜〜〜"};
    private static final String[] WEEKEND_ADDITIONS = {"今日は週末だよ〜〜〜！", "一緒にまったりしよ〜〜〜〜💕", "週末テンション↑↑↑↑"};

    private static final String[] EMOJIS = {"💘", "💕", "😚", "😳", "✨", "🥺", "😆", "😎", "🤗"};

    private final Random random = new Random();

    public String convert(String input) {
        Category category = detectCategory(input);
        return convert(input, category);
    }

    public String convert(String input, Category category) {
        StringBuilder sb = new StringBuilder();
        sb.append(getRandom(INTERJECTIONS.getOrDefault(category, new String[]{"ねぇ〜〜"})));
        sb.append("、").append(input).append("、").append(getRandom(SUFFIX_PATTERNS));
        sb.append(" ").append(getRandom(ENDINGS.getOrDefault(category, new String[]{"だよ〜〜〜〜"})));

        // 時間帯・曜日による変調
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int day = now.getDayOfWeek().getValue(); // 1 = 月, 7 = 日

        if (day >= 6) { // 土日
            sb.append(" ").append(getRandom(WEEKEND_ADDITIONS));
        }
        if (hour >= 5 && hour <= 10) {
            sb.append(" ").append(getRandom(MORNING_ADDITIONS));
        } else if (hour >= 11 && hour <= 16) {
            sb.append(" ").append(getRandom(NOON_ADDITIONS));
        } else if (hour >= 17 && hour <= 23) {
            sb.append(" ").append(getRandom(NIGHT_ADDITIONS));
        } else {
            sb.append(" ").append(getRandom(MIDNIGHT_ADDITIONS));
        }

        sb.append(" ").append(getRandom(EMOJIS));
        return sb.toString();
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

    private String getRandom(String[] array) {
        return array[random.nextInt(array.length)];
    }
}
