# タブレット端末を用いた仮想書道体験システム試作に関する研究

このシステムは、タブレット端末を用いて仮想的に書道表現を行うことができるシステムです。

八戸高専本科5年生の時に卒業研究として制作し、専攻科に入学した現在もこの研究を継続して行なっています。

ユーザーはタブレット端末と導電加工を施した書道筆を用いて仮想的に書道を行います。

開発にはAndroid Studioを用いています。

最終的なシステム目標は、書道パフォーマンスを行う際のパフォーマンス全体の設計ツールです。


## 研究背景や目的

書道は小中学校の教養において文字の由来や文化、書き方を学ぶ重要な授業です。

また、書道パフォーマンスも部活動として行なっている学校も多く、各地で大会も開催されています。

しかし、書道に用いる筆や墨汁は消耗品であり、ランニングコストがかかります。

また、書道パフォーマンスをする際には大きな紙や筆、大量の墨汁、広いスペースが必要となり練習が困難です。

そこで、この研究では書道パフォーマンスのように複数人で協調して文字を書き、共有するシステムを提案します。

このシステムは、実際の書道パフォーマンスのように大きな空間を必要とせず、各々が持つタブレット端末を用いて書いた文字を共有して大きなディスプレイに映し出すことができるものです。

このシステムの実現のために、5年生の卒業研究では、研究の初期段階として仮想的に書道を行うことができるシステムの試作に取り組みました。


## 開発環境

PC：MacBookPro 1.4GHz クアッドコアIntel Core i5, メモリ8GB

使用ソフトウェア：Andoroid Studio Flamingo | 2022.2.1 Patch2

使用タブレット：HUAWEI HDN-W09, NEC LAVIE Tab 102K1


## ハードウェアについて

使用するタブレット端末は、HUAWEIのタブレットとNECのタブレットの2つです。

2つ使用する理由は、タブレット端末の個体差を評価するためです。

<img width="500" alt="huawei" src="https://github.com/Take-Kai/Tablet-Calligraphy/assets/169955027/b8bce327-ad10-4a2e-aaec-b42bda95eba1">

<img width="500" alt="nec" src="https://github.com/Take-Kai/Tablet-Calligraphy/assets/169955027/98b59eb5-bda5-4c48-b5e3-ec3f94a8ba53">


文字の描画には導電性の布と糸で加工を施した書道筆を用います。

導電性の布は書道筆の穂先に取り付けられ、導電性の糸は書道筆の持ち手の部分に巻き付けられています。

<img width="500" alt="fude" src="https://github.com/Take-Kai/Tablet-Calligraphy/assets/169955027/3c0dd1a3-89ea-44c2-9ddb-ed3e1d63558f">



## ソフトウェア面について

## 基本機能の実装

基本機能として、作品の保存、削除、色選択の機能を実装しました。

これらの機能は、操作画面右上のメニューバーにあるアイコンをタップすることで実行できます。

<img width="201" alt="kihonn" src="https://github.com/Take-Kai/Tablet-Calligraphy/assets/169955027/b316cecf-3401-4717-8194-2ec7d3cb98f0">


作品の保存機能は、書いた作品をAndroidタブレットのファイルに保存します。この機能はBitmapを作成することで実装しています。

削除機能は、画面に書かれた線を全て削除します。

色選択機能は、色を赤、青、黒に変えることができます。この機能は、書道パフォーマンス特有のカラフルな色使いを表現するために実装しました。

<img width="500" alt="iro" src="https://github.com/Take-Kai/Tablet-Calligraphy/assets/169955027/909edba3-d9db-467d-a318-411b9c283be0">


## 書道のような表現の実装

実際の書道を行うと、書いている最中に筆跡が掠れていき、墨を付け足さなければなりません。

また、筆を強く半紙に押し付けると線が太くなり、穂先のみをつけると細い線になります。

このような書道特有の表現の実装に取り組みました。

段々と筆跡が掠れていく表現は、透明度のパラメータであるアルファ値を減少させることで実現します。

筆跡が掠れていく条件として、筆を速く動かした時、最初に筆を墨に付けて以降しばらくの間墨を付けずに書き進めた時の2つが考えられます。

1つ目の「筆を速く動かした時」はトラッキング速度を求めることで実現します。

タッチパネルをタップしたときはDOWN、指を動かした時はMOVE、話した時はUPの3つの場合で分けることができます。

このMOVEの際にトラッキング速度を求める処理を行います。

筆を押し付けた時の強さによる線の太さの実装には画面へのタッチ圧力を用います。

圧力が強ければ線を太く、弱ければ線を細くします。


## パターンの描画

筆跡を描画する際は通常、Pathを用いて線の状態で描画しますが、Pathは描画の途中で線の太さを変えることや筆跡が薄くなる表現を実装できません。

そのため、予め用意したパターンを筆跡に沿うように連続で描画していくことで実装します。

このパターン描画は、なぞっている時に座標を連続で取得し、取得した座標を保存してその保存した座標情報を基にパターンを描画します。

<img width="500" alt="image" src="https://github.com/Take-Kai/Tablet-Calligraphy/assets/169955027/b4f5df24-4afe-4082-96dc-7067d54afad6">

描画するパターンは円を用いており、タップ圧力が強ければ円の半径を大きくし、圧力が弱ければ円の半径を小さくします。

このような処理を指を動かしている間連続的に行います。


## 現在のシステム

<img width="500" alt="NowSystem" src="https://github.com/Take-Kai/Tablet-Calligraphy/assets/169955027/e3e065ac-7758-4b90-aa72-a3f6f66c1ec4">


<img width="500" alt="kaizenn" src="https://github.com/Take-Kai/Tablet-Calligraphy/assets/169955027/a3f6159a-06cf-48be-a509-73a2c5d78017">


## 課題と今後の展望

問題点として、パターンの半径が全て変わってしまうことや書いた線が全て薄くなることが挙げられます。

これらを解決するには、パターンを1つ描画したらその状態で画像として保存し、次のパターンを描画する際に保存した画像を再描画する処理を行う必要があります。

今後は、「とめ・はね・はらい」などの書道表現の実装、書道パフォーマンスのように複数人で行えるようにサーバ開設を行う必要があります。

現在は上記のことに取り組むためにMEANスタックの勉強を進めており、Webアプリケーションとして動作できるようにしたいと考えています。

また、システム完成後は書道部や専門家に評価をしてただく予定です。地元で書道パフォーマンスを部活動として行っている高校に訪問し、体験していただこうと考えています。

## 受賞・学会発表

この研究は卒業研究として、卒業研究優秀賞をいただきました。

また、本科5年次の研究活動などを評価していただき、電子情報通信学会東北支部から優秀学生賞もいただきました。

2024年3月2日には、青森県青森市にある県立美術館にて行われた芸術科学会東北支部研究会にてこの研究の発表を行いました。
