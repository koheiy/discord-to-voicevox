package co.jp.r.yomiagekbot;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class RealOjisanConverterWithKuromoji {

    public enum Category {
        GREETING, SMALL_TALK, ENCOURAGEMENT, APOLOGY
    }

    private static final Map<Category, List<String>> CATEGORY_BASE_FORMS = Map.of(
            Category.GREETING, List.of("おはよう", "こんにちは", "元気", "調子"),
            Category.SMALL_TALK, List.of("暑い", "寒い", "天気", "映画", "話", "最近", "仕事", "昼ごはん"),
            Category.ENCOURAGEMENT, List.of("頑張る", "応援", "大丈夫", "無理", "休む", "疲れる", "心配"),
            Category.APOLOGY, List.of("謝る", "ごめん", "すまない", "失礼", "反省", "許す")
    );

    private static final Map<Category, String[]> BASE_ENDINGS = Map.of(
            Category.GREETING, new String[]{"だよね", "だよ〜", "よろしくね", "今日もがんばろうね", "いい日になりそうだね"},
            Category.SMALL_TALK, new String[]{"だよね", "なんだよ〜", "わかる〜", "って感じかな", "ほんとそれ〜", "だよぉ〜ん"},
            Category.ENCOURAGEMENT, new String[]{"無理しないでね", "応援してるよ", "元気出してね", "大丈夫だよ", "いつでも味方だよ"},
            Category.APOLOGY, new String[]{"ほんとごめんね", "許してね", "気をつけるよ", "反省してるよ", "悪気はなかったんだよね"}
    );

    private static final Map<Category, String[]> INTERJECTIONS = Map.of(
            Category.GREETING, new String[]{"やっほ", "おはよう", "こんちゃ〜", "こんにちは"},
            Category.SMALL_TALK, new String[]{"うんうん", "へぇ〜", "ふふっ", "それな"},
            Category.ENCOURAGEMENT, new String[]{"うん", "そうそう", "うんうん", "よしよし"},
            Category.APOLOGY, new String[]{"ごめん", "ううっ", "ほんとに", "しょんぼり"}
    );

    private static final List<Category> PRIORITY_ORDER = List.of(
            Category.APOLOGY, Category.ENCOURAGEMENT, Category.GREETING, Category.SMALL_TALK
    );

    private static final Random random = new Random();

    public String convert(String input) {
        Category category = detectCategoryWithMorph(input);
        return convert(input, category);
    }

    public String convert(String input, Category category) {
        StringBuilder sb = new StringBuilder();

        if (category == Category.GREETING) {
            sb.append(getRandom(INTERJECTIONS.get(category))).append("！ ");
        }

        String[] sentences = input.split("(?<=[。！？])");
        String[] endings = getSeasonalOrTimeBasedEndings(category);

        for (String sentence : sentences) {
            if (sentence.trim().isEmpty()) continue;

            if (random.nextInt(100) < 40) {
                sb.append(getRandom(INTERJECTIONS.get(category))).append("、");
            }

            sb.append(sentence.trim());
            sb.append("、").append(getRandom(endings)).append("。\n");
        }

        return sb.toString();
    }

    private Category detectCategoryWithMorph(String input) {
        Tokenizer tokenizer = Tokenizer.builder().build();
        List<Token> tokens = tokenizer.tokenize(input);

        Map<Category, Integer> scoreMap = new EnumMap<>(Category.class);
        for (Category cat : Category.values()) {
            scoreMap.put(cat, 0);
        }

        for (Token token : tokens) {
            String base = token.getBaseForm();
            if (base == null) base = token.getSurfaceForm();

            for (Category cat : Category.values()) {
                if (CATEGORY_BASE_FORMS.get(cat).contains(base)) {
                    scoreMap.put(cat, scoreMap.get(cat) + 1);
                }
            }
        }

        int maxScore = scoreMap.values().stream().max(Integer::compareTo).orElse(0);
        if (maxScore == 0) return Category.SMALL_TALK;

        for (Category cat : PRIORITY_ORDER) {
            if (scoreMap.get(cat) == maxScore) return cat;
        }

        return Category.SMALL_TALK;
    }

    private String getRandom(String[] array) {
        return array[random.nextInt(array.length)];
    }

    private String[] getSeasonalOrTimeBasedEndings(Category category) {
        String[] base = BASE_ENDINGS.get(category);
        List<String> list = new ArrayList<>(Arrays.asList(base));

        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int month = now.getMonthValue();

        // 時間帯別追加語尾
        if (hour < 10) list.add("朝から元気だね〜");
        else if (hour < 18) list.add("午後もがんばろうね");
        else list.add("夜はゆっくりしてね");

        // 季節別語尾
        if (month >= 3 && month <= 5) list.add("春っていいね〜");
        else if (month >= 6 && month <= 8) list.add("夏バテしないようにね");
        else if (month >= 9 && month <= 11) list.add("秋ってなんか落ち着くよね");
        else list.add("寒いから風邪ひかないでね");

        return list.toArray(new String[0]);
    }
}