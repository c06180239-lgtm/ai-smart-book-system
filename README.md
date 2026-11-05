# AI 智能图书资讯管理系统

仅供学习演示的全栈 Demo：MySQL + Spring Boot/MyBatis + Flask/DeepSeek + Vue3/Vite。

## 功能
- 用户注册、登录、角色区分（USER/ADMIN）；SHA-256 密码摘要及演示用 Bearer Token。
- 图书分页、书名/作者/分类筛选，管理员新增、编辑、删除、批量删除、图片上传。
- Flask 独立 AI 服务：图书解读、阅读偏好推荐、对话问答；请求超时与异常响应。
- Vue 页面：登录注册、图书查询、管理表单、分页、上传预览、AI 面板。

## 环境
JDK 17、Maven 3.9+、Python 3.10+、Node.js 20+、MySQL 8.0+。
DeepSeek 调用使用模型 `deepseek-v4-pro`。请自行申请 API Key，并通过本地 `.env` 提供。

## 配置
在项目根目录创建 `.env`（已忽略版本控制），填写：
```dotenv
DB_NAME=ai_smart_book_system
DB_USERNAME=你的MySQL用户名
DB_PASSWORD=你的MySQL密码
BACKEND_PORT=8080
AI_PORT=5001
FRONTEND_PORT=5173
AI_API_KEY=你的DeepSeek_API_Key
AI_BASE_URL=https://api.deepseek.com
```
先按 `.env` 中的 `DB_NAME` 在 MySQL 创建空数据库，再选中该库执行 `schema.sql`。

## Windows 启动
先在 MySQL 客户端执行 `schema.sql`。以下命令在 PowerShell 中分别执行；先从项目根目录载入变量：
```powershell
Get-Content .env | Where-Object { $_ -match '^[A-Za-z_][A-Za-z0-9_]*=' } | ForEach-Object { $k,$v=$_.Split('=',2); [Environment]::SetEnvironmentVariable($k,$v,'Process') }
```
后端：`cd 'D:\Git Box\AI-Smart-Book-System\springboot-backend'; mvn spring-boot:run`
AI 服务：`cd 'D:\Git Box\AI-Smart-Book-System\python-ai-service'; py -m venv .venv; .\.venv\Scripts\Activate.ps1; pip install -r requirements.txt; py app.py`
前端：`cd 'D:\Git Box\AI-Smart-Book-System\vue3-frontend'; npm install; npm run dev`
前端代理访问后端，Vite 端口默认 5173；后端、AI 服务默认端口分别为 8080、5001，可由 `.env` 调整。

## 演示账号
首次运行后通过注册创建普通用户。请在数据库中将指定用户的 `role` 更新为 `ADMIN`，例如：
`UPDATE users SET role='ADMIN' WHERE username='你的用户名';`

**安全说明：本项目仅为学习演示 Demo。** Token、密码处理、上传与 CORS 均未按生产环境加固；不要用于真实用户数据或公网部署。DeepSeek Key 仅放在本地 `.env`，勿提交版本库。
