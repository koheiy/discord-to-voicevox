# Discord to Voicevox(VC)
## はじめに
DiscordのチャットをVoicevoxに喋らせるBotアプリケーションです。
VCミュートの方がVoicevoxのキャラクターで喋ったら面白そうと思ってさっと作られたBotとなります。

※まだバグが多かったりのα版となります。


## 準備
DiscordのBotトークンの払い出しを行ってください（ここでは省略します） 。
払いだした後、Discordの個人サーバに作ったBotをInvite(テキストのRead権限は必ず付与しておいてください)しておいてください。

ローカル環境かVM上に[Voicevoxをダウンロード](https://voicevox.hiroshiba.jp/)して立ち上げておいてください。
立ち上げは以下のようにして行ってください。

`./run VMやローカルのIPアドレス --port 適当なポート番号`

※必要な場合ポート開放を忘れずに。

## 実行
application.ymlに以下設定を入れてください。
~~~yaml
voicevox:
  base-url: VoicevoxのURL,port(例:http://ipaddr:port)
  download-directory: Voicevoxからwavを落としてくるディレクトリ(例:C:\voicevox\wav)
discord:
  token: DiscordBotのトークン
~~~


ローカル上でSpringbootアプリケーションを立ち上げます。

自分自身がVCに入ったのち、#generalに!joinを打ち込みます。うまくいけばBotがVCに入ってくるはずです。
VCが入ってきた後は#generalにテキストを打ってみましょう。Botが喋れば完了です。