-- AI 智能图书资讯管理系统数据库结构
-- 请先连接到 .env 中配置的数据库，再执行本脚本；数据库名称不写死在 SQL 中。
CREATE TABLE IF NOT EXISTS users (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 username VARCHAR(50) NOT NULL UNIQUE,
 password VARCHAR(255) NOT NULL COMMENT '演示项目：SHA-256摘要，不适用于生产',
 role VARCHAR(20) NOT NULL DEFAULT 'USER',
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS books (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 title VARCHAR(200) NOT NULL, author VARCHAR(120) NOT NULL,
 category VARCHAR(80), summary TEXT, cover_url VARCHAR(500),
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 INDEX idx_books_title(title), INDEX idx_books_author(author), INDEX idx_books_category(category)
);
