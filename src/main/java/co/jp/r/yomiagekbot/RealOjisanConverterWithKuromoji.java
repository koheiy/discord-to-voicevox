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
            Category.GREETING, new String[]{"だよねぇ〜〜〜✨", "だよ〜ん💕", "よろしくねっ💖", "今日もがんばろうねっっ💪💪", "いい日になりそうだねぇ〜🌞"},
            Category.SMALL_TALK, new String[]{"だよねぇ〜〜ん😎", "なんだよぉぉ〜〜💦", "わかるぅぅ〜〜🥺", "って感じかなぁ〜〜？", "ほんとそれそれ〜〜😂", "だよぉぉ〜〜ん💫"},
            Category.ENCOURAGEMENT, new String[]{"無理しないでねぇ〜〜💦", "応援してるよぉぉぉ〜〜📣", "元気出してねっっっ💥", "大丈夫だよぉぉぉ〜〜ん💕", "いつでも味方だかんね〜〜💪"},
            Category.APOLOGY, new String[]{"ほんっとにごめんねぇぇ〜〜🙏", "許してぇぇぇ〜〜ん🥺", "気をつけるからぁ〜〜😭", "反省してるんだよぉぉ〜〜😢", "悪気はなかったのぉぉ〜〜ん😭"}
    );

    private static final Map<Category, String[]> INTERJECTIONS = Map.of(
            Category.GREETING, new String[]{"やっほ〜〜〜✨", "おっはよぉぉ〜〜☀️", "こんちゃぁ〜〜っっ🌈", "こんにちは〜〜っ💕"},
            Category.SMALL_TALK, new String[]{"うんうんっ！", "へぇ〜〜〜っ！？", "ふふふっっ💕", "それな〜〜〜😎"},
            Category.ENCOURAGEMENT, new String[]{"うんうんっ💪", "そうそうそう〜〜っ！", "よしよしよしぃぃ〜〜🥰", "がんばってぇぇ〜〜〜！！🔥"},
            Category.APOLOGY, new String[]{"ごめんちゃ〜〜〜ん💦", "ううぅっ…🥺", "ほんとにぃぃ〜〜🙏", "しょんぼりぃ〜〜〜😢"}
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
            sb.append(getRandom(INTERJECTIONS.get(category))).append("っ！ ");
        }

        String[] sentences = input.split("(?<=[。！？])");
        String[] endings = getSeasonalOrTimeBasedEndings(category);

        for (String sentence : sentences) {
            if (sentence.trim().isEmpty()) continue;

            if (random.nextInt(100) < 70) {
                sb.append(getRandom(INTERJECTIONS.get(category))).append("、");
            }

            sb.append(sentence.trim());
            sb.append("、").append(getRandom(endings)).append("\n");
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

        if (hour < 10) list.add("朝から元気もりもりだよぉぉ〜〜ん💥");
        else if (hour < 18) list.add("午後もはりきっていこうねぇぇ〜〜💪");
        else list.add("夜はムリせずゆっくりしよぉぉ〜〜ね〜〜💤");

        if (month >= 3 && month <= 5) list.add("春ってキモチい〜〜〜ねぇぇ〜〜🌸");
        else if (month >= 6 && month <= 8) list.add("夏はアツくてドキドキだよぉぉ〜〜🔥");
        else if (month >= 9 && month <= 11) list.add("秋ってなんか…センチメンタルだよねぇぇ〜〜🍁");
        else list.add("冬はくっついてあったまりた〜〜〜い🥶💕");

        return list.toArray(new String[0]);
    }
}
