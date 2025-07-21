
package co.jp.r.yomiagekbot;

import org.springframework.stereotype.Component;

@Component
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
}
