"""独立 AI 服务：调用 DeepSeek OpenAI 兼容接口。"""
import os
from flask import Flask, request, jsonify
from flask_cors import CORS
from dotenv import load_dotenv
from openai import OpenAI

load_dotenv("../.env")
app = Flask(__name__)
CORS(app)
MODEL = "deepseek-v4-pro"

def ask(system, prompt):
    key = os.getenv("AI_API_KEY")
    if not key:
        raise RuntimeError("未配置 AI_API_KEY，请检查根目录 .env")
    client = OpenAI(api_key=key, base_url=os.environ["AI_BASE_URL"], timeout=30, max_retries=1)
    result = client.chat.completions.create(model=MODEL, messages=[{"role":"system","content":system},{"role":"user","content":prompt}], temperature=0.7)
    return result.choices[0].message.content

@app.post("/api/ai/<action>")
def ai(action):
    data=request.get_json(silent=True) or {}
    try:
        if action == "interpret":
            prompt=f"请解读以下图书内容，说明主题、亮点、适合读者：\n{data.get('text','') or data.get('title','')}"
            system="你是一位图书导读助手。"
        elif action == "recommend":
            prompt=f"依据阅读偏好推荐图书，给出理由：{data.get('preferences','')}"
            system="你是一位图书推荐助手；优先推荐经典与适配偏好的作品。"
        elif action == "chat":
            prompt=str(data.get("message", "")); system="你是友好的图书与阅读问答助手。回答清晰简洁。"
        else:
            return jsonify(error="未知 AI 操作"), 404
        return jsonify(answer=ask(system,prompt), model=MODEL)
    except Exception as exc:
        return jsonify(error=f"AI 请求失败：{exc}"), 502

if __name__ == "__main__":
    app.run(host="127.0.0.1", port=int(os.environ["AI_PORT"]), debug=True)
