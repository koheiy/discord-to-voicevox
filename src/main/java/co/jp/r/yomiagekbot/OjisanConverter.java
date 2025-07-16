package co.jp.r.yomiagekbot;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OjisanConverter {

    private static final String[] OJISAN_ENDINGS = {
            "だよ〜", "だよね〜", "かな〜？", "って思っちゃった〜", "なのよね〜", "ね♪", "〜よ〜", "だよ〜ん"
    };

    private static final String[] EMOJIS = {
            "(*´∀｀*)", "（＾ω＾）", "(≧▽≦)", "(〃ω〃)", "(^з^)-☆", "(๑˃̵ᴗ˂̵)", "（笑）", "(๑>◡<๑)"
    };

    private static final String[] HEARTS = {
            "💕", "💖", "💓", "❤️", "💘"
    };

    public String convertToOjisan(String input) {
        Random rand = new Random();

        // 文章を文節で分ける（単純に句点で分割）
        String[] sentences = input.split("(?<=[。！？])");

        StringBuilder result = new StringBuilder();
        for (String sentence : sentences) {
            if (sentence.trim().isEmpty()) continue;

            // ランダムな語尾を追加
            String ending = OJISAN_ENDINGS[rand.nextInt(OJISAN_ENDINGS.length)];

            // ランダムな顔文字を追加
            String emoji = EMOJIS[rand.nextInt(EMOJIS.length)];

            // ランダムにハートを追加
            String heart = rand.nextBoolean() ? HEARTS[rand.nextInt(HEARTS.length)] : "";

            // 文字の一部を伸ばす（「すごい」→「すごいーー！」）
            String stretched = sentence.replaceAll("([ぁ-んァ-ン一-龥]{2,3})", "$1ー！");

            result.append(stretched.trim())
                    .append(ending)
                    .append(" ")
                    .append(emoji)
                    .append(" ")
                    .append(heart)
                    .append("\n");
        }

        return result.toString();
    }
}
