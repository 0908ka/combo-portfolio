# Combo Portfolio

React + Spring Boot + MySQL を使用した学習用ポートフォリオです。

## 技術スタック
- Frontend: React (JavaScript)
- Backend: Spring Boot (Java)
- Database: MySQL
- インフラ: Docker / Docker Compose

## 起動方法
```bash
docker-compose up --build
・ 起動後、ブラウザでhttp://localhost:3000 にアクセスしてください。

## 主な機能
・　キャラごとのコンボ登録・表示・編集・削除
・　フロントエンドからSpring Boot API への通信
・　バリデーション付きの登録フォーム
・　MySQLへの永続化とDocker連携

## ディレクト構成
combo_portfolio/
|-- comboapp/               # Spring Boot（バックエンド）
|-- frontend/               # React（フロントエンド）
|-- comboapp-mysql/         # MySQL 用設定（valume 除外済み）
|-- docker-compose.yml      # 環境構築用ファイル
|-- README.md               # 本ファイル

## 今後追加したい機能（予定）
・　検索・並び替え機能強化
・　キャラクター画像・動画の追加
・　認証（ログイン/サインアップ）機能
・　スマホ対応
・　AWSへのデプロイ


## 制作意図
JavaとReactの学習、APIとDB接続への理解、Dockerを使用した実践を目的に開発しています。