package co.jp.r.yomiagekbot;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RealOjisanConverterV2 {

    public enum Category {
        GREETING, SMALL_TALK, ENCOURAGEMENT, APOLOGY
    }

    private static final Map<Category, String[]> ENDINGS = Map.of(
            Category.GREETING, new String[]{"だよね", "だよ〜", "よろしくね", "今日もがんばろうね"},
            Category.SMALL_TALK, new String[]{"だよね", "なんだよ〜", "わかる〜", "って感じかな"},
            Category.ENCOURAGEMENT, new String[]{"無理しないでね", "応援してるよ", "元気出してね", "大丈夫だよ"},
            Category.APOLOGY, new String[]{"ほんとごめんね", "許してね", "気をつけるよ", "反省してるよ"}
    );

    private static final Map<Category, String[]> INTERJECTIONS = Map.of(
            Category.GREETING, new String[]{"やっほ", "おはよう", "こんちゃ〜"},
            Category.SMALL_TALK, new String[]{"うんうん", "へぇ〜", "ふふっ", "それな"},
            Category.ENCOURAGEMENT, new String[]{"うん", "そうそう", "うんうん", "よしよし"},
            Category.APOLOGY, new String[]{"ごめん", "ううっ", "ほんとに", "しょんぼり"}
    );

    private static final Random random = new Random();

    public String convert(String input, Category category) {
        StringBuilder sb = new StringBuilder();

        // 挨拶カテゴリなら冒頭に呼びかけ
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

            // 音声合成向けに「…」や「〜〜」は使わず自然語尾を追加
            sb.append("、").append(getRandom(ENDINGS.get(category))).append("。");

            sb.append("\n");
        }

        return sb.toString();
    }

    private String getRandom(String[] array) {
        return array[random.nextInt(array.length)];
    }
}