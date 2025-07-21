
package co.jp.r.yomiagekbot;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

public class OjisanStyler {

    public static String style(String input) {
        StringBuilder styled = new StringBuilder();

        // 1. 時間帯による装飾
        styled.append(getTimeGreeting());

        // 2. 適当な間におじさん感あふれる語を挿入
        styled.append(" ").append(OjisanPhraseBank.getRandom(OjisanPhraseBank.INTERJECTIONS)).append(" ");

        // 3. メインメッセージ
        styled.append(input);

        // 4. 適度に語尾追加
        styled.append(" ").append(OjisanPhraseBank.getRandom(OjisanPhraseBank.ENDINGS));

        // 5. テールフレーズ追加（少ししつこく）
        styled.append(" ").append(OjisanPhraseBank.getRandom(OjisanPhraseBank.TAIL_PHRASES));

        // 6. 絵文字追加
        styled.append(" ").append(OjisanPhraseBank.getRandom(OjisanPhraseBank.EMOTICONS));

        return styled.toString();
    }

    private static String getTimeGreeting() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        String timeKey;

        if (hour >= 5 && hour < 11) {
            timeKey = "MORNING";
        } else if (hour >= 17 && hour < 21) {
            timeKey = "EVENING";
        } else {
            timeKey = "NIGHT";
        }

        DayOfWeek day = now.getDayOfWeek();
        String weekdayPhrase = OjisanPhraseBank.WEEKDAY_MODIFIERS.getOrDefault(day.name(), List.of("")).stream()
            .findAny()
            .orElse("");

        String base = OjisanPhraseBank.getRandom(OjisanPhraseBank.TIME_DECORATIONS.getOrDefault(timeKey, List.of("")));
        return base + (weekdayPhrase.isBlank() ? "" : " " + weekdayPhrase);
    }
}
