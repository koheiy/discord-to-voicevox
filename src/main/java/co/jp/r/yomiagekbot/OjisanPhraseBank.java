
package co.jp.r.yomiagekbot;

import java.util.List;
import java.util.Map;

public class OjisanPhraseBank {

    public static final List<String> EMOTICONS = List.of(
        "💕", "💘", "✨", "😳", "🥺", "👀", "😚", "💦", "🍵", "🎶", "🎀"
    );

    public static final List<String> INTERJECTIONS = List.of(
        "ねぇねぇ", "やっほ〜〜", "ふふっ", "あのさ〜〜", "そうそう", "あれってさ〜", "てゆーか", "え、マジで？"
    );

    public static final List<String> ENDINGS = List.of(
        "だよぉ〜〜〜〜〜〜〜💕", "なんだよぉぉ〜〜〜〜〜〜💘", "だってばぁぁ〜〜〜〜〜〜😚", "って感じぃぃ〜〜〜〜〜✨", 
        "なのさぁ〜〜〜〜〜〜〜🥺", "なんだからねぇぇ〜〜〜〜〜〜〜😳", "かもね〜〜〜〜〜〜〜🎶", "だよんっ💖"
    );

    public static final List<String> TAIL_PHRASES = List.of(
        "キミってほんと最高ぉぉ〜〜〜〜〜〜〜💕", "おじさん感動しちゃうぅぅ〜〜〜〜〜〜〜〜😭", 
        "その笑顔、反則だよぉぉ〜〜〜〜〜〜〜〜〜〜✨", "もっと話したいなぁぁ〜〜〜〜〜〜〜〜〜〜👀", 
        "ずっと一緒にいたいかもぉぉ〜〜〜〜〜〜〜〜〜〜💘"
    );

    public static final Map<String, List<String>> TIME_DECORATIONS = Map.of(
        "MORNING", List.of("おっはよ〜〜☀️", "朝からキミを思い出してニヤけちゃったよ〜〜😚"),
        "EVENING", List.of("こんばんは〜〜〜〜🌙", "今日も一日お疲れさまぁぁ〜〜〜〜〜🍵"),
        "NIGHT", List.of("もう夜だね〜〜〜〜〜🌃", "寝る前に…キミのこと考えてたんだ〜〜〜〜〜😳")
    );

    public static final Map<String, List<String>> WEEKDAY_MODIFIERS = Map.of(
        "MONDAY", List.of("週の始まりだね〜〜〜〜〜〜😤"),
        "FRIDAY", List.of("やっと金曜日ぃぃ〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜！！✨"),
        "SUNDAY", List.of("日曜の夜ってさ〜〜〜切ないよねぇぇ〜〜〜〜〜〜〜〜〜🥺")
    );

    public static String getRandom(List<String> list) {
        return list.get((int) (Math.random() * list.size()));
    }
}
