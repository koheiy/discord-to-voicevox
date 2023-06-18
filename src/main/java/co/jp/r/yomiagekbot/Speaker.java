package co.jp.r.yomiagekbot;

public enum Speaker {

    Shikokumetan_Amaama(0,"Shikokumetan","あまあま"),
    Zundamon_Amaama(1,"Zundamon","あまあま"),
    Shikokumetan_Normal(2,"Shikokumetan","ノーマル"),
    Zundamon_Normal(3,"Zundamon","ノーマル"),
    Shikokumetan_Sexy(4,"Shikokumetan","セクシー"),
    Zundamon_Sexy(5,"Zundamon","セクシー"),
    Shikokumetan_Tsuntsun(6,"Shikokumetan","ツンツン"),
    Zundamon_Tsuntsun(7,"Zundamon","ツンツン"),
    Kasukabetsumugi_Normal(8,"Kasukabetsumugi","ノーマル"),
    Namineritsu_Normal(9,"Namineritsu","ノーマル"),
    Ameharehau_Normal(10,"Ameharehau","ノーマル"),
    Kuronotakehiro_Normal(11,"Kuronotakehiro","ノーマル"),
    Shirakamikotarou_Normal(12,"Shirakamikotarou","ふつう"),
    Aoyamaryusei_Normal(13,"Aoyamaryusei","ノーマル"),
    Meimeihimari_Normal(14,"Meimeihimari","ノーマル"),
    Kyushusora_Amaama(15,"Kyushusora","あまあま"),
    Kyushusora_Normal(16,"Kyushusora","ノーマル"),
    Kyushusora_Sexy(17,"Kyushusora","セクシー"),
    Kyushusora_Tsuntsun(18,"Kyushusora","ツンツン"),
    Kyushusora_Sasayaki(19,"Kyushusora","ささやき"),
    Mochikosan_Normal(20,"Mochikosan","ノーマル"),
    Kenzakimesuo_Normal(21,"Kenzakimesuo","ノーマル"),
    Zundamon_Sasayaki(22,"Zundamon","ささやき"),
    WhiteCUL_Normal(23,"WhiteCUL","ノーマル"),
    WhiteCUL_Tanoshii(24,"WhiteCUL","たのしい"),
    WhiteCUL_Kanashii(25,"WhiteCUL","かなしい"),
    WhiteCUL_Bieen(26,"WhiteCUL","びえーん"),
    Goki_NingenVer(27,"Goki","人間ver."),
    Goki_NuigurumiVer(28,"Goki","ぬいぐるみver."),
    No7_Normal(29,"No.7","ノーマル"),
    No7_Announce(30,"No.7","アナウンス"),
    No7_Yomikikase(31,"No.7","読み聞かせ"),
    Shirakamikotarou_Waaai(32,"Shirakamikotarou","わーい"),
    Shirakamikotarou_Bikubiku(33,"Shirakamikotarou","びくびく"),
    Shirakamikotarou_Oko(34,"Shirakamikotarou","おこ"),
    Shirakamikotarou_Bieen(35,"Shirakamikotarou","びえーん"),
    Shirakamikotarou_Sasayaki(36,"Shikokumetan","ささやき"),
    Shirakamikotarou_Hisohiso(37,"Shikokumetan","ヒソヒソ"),
    Zundamon_Hisohiso(38,"Zundamon","ヒソヒソ"),
    Kuronotakehiro_Yorokobi(39,"Kuronotakehiro","喜び"),
    Kuronotakehiro_Tsungire(40,"Kuronotakehiro","ツンギレ"),
    Kuronotakehiro_Kanashimi(41,"Kuronotakehiro","悲しみ"),
    Chibishikijii_Normal(42,"Chibishikijii","ノーマル"),
    Oukamiko_Normal(43,"Oukamiko","ノーマル"),
    Oukamiko_Dainikeitai(44,"Oukamiko","第二形態"),
    Oukamiko_Rori(45,"Oukamiko","ロリ"),
    Sayo_Normal(46,"Sayo/SAYO","ノーマル"),
    Nurse_lobo_Normal(47,"Nurse_lobo","ノーマル"),
    Nurse_lobo_Rakuraku(48,"Nurse_lobo","楽々"),
    Nurse_lobo_Kyoufu(49,"Nurse_lobo","恐怖"),
    Nurse_lobo_Naishobanashi(50,"Nurse_lobo","内緒話"),
    Holy_Knight_Benizakura_Normal(51,"Holy_Knight_Benizakura","ノーマル"),
    Wakamatsuakashi_Normal(52,"Wakamatsuakashi","ノーマル"),
    Kirigashimasourin_Normal(53,"Kirigashimasourin","ノーマル")
    ;

    private int speakerId;

    private String speaker;

    private String emotions;

    public int getSpeakerId() {
        return speakerId;
    }

    public String getSpeaker() {
        return speaker;
    }

    public String getEmotions() {
        return emotions;
    }

    Speaker(int speakerId, String speaker, String emotions) {
        this.speakerId = speakerId;
        this.speaker = speaker;
        this.emotions = emotions;
    }

    public static Speaker of(int targetSpeakerId) {
        for(Speaker speaker : values()) {
            if (speaker.getSpeakerId() == targetSpeakerId) {
                return speaker;
            }
        }
        throw new SpeakerNotFoundException();
    }
}
