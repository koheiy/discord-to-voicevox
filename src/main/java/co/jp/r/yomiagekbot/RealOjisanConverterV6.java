
package co.jp.r.yomiagekbot;

public class RealOjisanConverterV6 {

    /**
     * 任意の文章をウザいおじさん構文に変換します。
     *
     * @param input 入力文章
     * @return おじさん化された文章
     */
    public String convert(String input) {
        return OjisanStyler.style(input);
    }

    // デモ用 main メソッド（開発テスト用）
    public static void main(String[] args) {
        RealOjisanConverterV6 converter = new RealOjisanConverterV6();
        String input = "今日はちょっと疲れたかも";
        String output = converter.convert(input);
        System.out.println(output);
    }
}
