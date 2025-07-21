
package co.jp.r.yomiagekbot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class RealOjisanConverterV5 {

    private final Map<String, List<String>> responsePatterns;
    private final OjisanStateManager stateManager;
    private final Random random;

    public enum Category {
        GREETING, SMALL_TALK, ENCOURAGEMENT, APOLOGY
    }

    private static final Map<Category, List<String>> CATEGORY_KEYWORDS = Map.of(
        Category.GREETING, List.of("おはよう", "こんにちは", "こんばんは", "やっほ", "元気", "調子"),
        Category.SMALL_TALK, List.of("最近", "今日", "何してる", "ランチ", "話", "推し", "空", "映画"),
        Category.ENCOURAGEMENT, List.of("がんば", "応援", "大丈夫", "無理", "休んで", "つら", "心配", "疲れ"),
        Category.APOLOGY, List.of("ごめ", "すま", "許し", "謝", "失礼", "反省")
    );

    public RealOjisanConverterV5() throws Exception {
        this.stateManager = new OjisanStateManager();
        this.random = new Random();

        // Load response pattern JSON
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("OjisanResponsePatterns_expanded.json")) {
            if (in == null) {
                throw new IllegalStateException("OjisanResponsePatterns.json not found in resources");
            }
            ObjectMapper mapper = new ObjectMapper();
            this.responsePatterns = mapper.readValue(in, new TypeReference<Map<String, List<String>>>() {});
        }
    }

    public String convert(String input) {
        Category category = detectCategory(input);
        stateManager.updateState(category.name());

        List<String> responses = responsePatterns.getOrDefault(category.name(), List.of("ふふっ、それって…どういうことぉ〜〜〜？"));
        String baseResponse = getRandom(responses);

        if (stateManager.isRapidPosting()) {
            baseResponse += " …えっ、たくさん話しかけてくれて嬉しすぎぃぃぃ〜〜〜〜〜〜〜〜〜〜〜〜〜〜！！💕💕💕";
        }

        return baseResponse;
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

    private String getRandom(List<String> list) {
        return list.get(random.nextInt(list.size()));
    }
}
