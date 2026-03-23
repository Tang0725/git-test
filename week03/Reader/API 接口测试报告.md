**API 接口测试报告**

**项目名称**：workWeek03

**测试日期**：2026-03-23

**测试环境**：Spring Boot 开发环境 (dev)

**基础 URL**：http://localhost:8080/api

**目录**

- HuTool 工具类接口
- 阿里云短信服务接口
- 配置查询接口
- 通义千问 AI 接口

**1\. HuTool 工具类接口**

**1.1 获取 UUID**

**接口基础信息**

- **请求方式**：GET
- **接口URL**：/hutool/id
- **完整地址**：http://localhost:8080/api/hutool/id
- **功能描述**：生成一个唯一的 UUID 标识符，用于业务唯一标识场景
- **请求参数**：无

**响应示例**

json  
"hutool-id:a1b2c3d4e5f6g7h8i9j0"

**响应说明**

返回字符串格式：hutool-id: + UUID，UUID 采用 Hutool 工具类 IdUtil.fastSimpleUUID() 生成，具备高效、唯一的特性。

**1.2 MD5 加密**

**接口基础信息**

- **请求方式**：GET
- **接口URL**：/hutool/md5
- **完整地址**：http://localhost:8080/api/hutool/md5?text={文本内容}
- **功能描述**：对输入的明文文本进行 MD5 哈希加密，适用于密码、敏感文本脱敏场景

**请求参数**

|     |     |     |     |
| --- | --- | --- | --- |
| 参数名 | 类型  | 必填  | 说明  |
| text | String | 是   | 需要进行MD5加密的原始文本内容 |

**请求示例**

http  
GET /hutool/md5?text=Hello12

**响应示例**

json  
"4297f6a81ee2f48e2f7dac5bc5cd7fe7"

**1.3 文件上传**

**接口基础信息**

- **请求方式**：POST
- **接口URL**：/hutool/upload
- **完整地址**：http://localhost:8080/api/hutool/upload
- **功能描述**：上传文件至服务器，自动生成唯一文件名存储，避免文件重名覆盖
- **请求头**：Content-Type: multipart/form-data

**请求参数**

|     |     |     |     |
| --- | --- | --- | --- |
| 参数名 | 类型  | 必填  | 说明  |
| file | MultipartFile | 是   | 待上传的本地文件，支持各类常规文件格式 |

**成功响应示例**

json  
{  
"success": true,  
"message": "上传成功",  
"originalFileName": "test.jpg",  
"uniqueFileName": "a1b2c3d4e5f6g7h8.jpg",  
"savePath": "./uploads/a1b2c3d4e5f6g7h8.jpg",  
"fileSize": 102400  
}

**响应字段说明**

- **success**：布尔值，标识文件上传是否成功
- **message**：响应提示信息
- **originalFileName**：用户上传的原始文件名
- **uniqueFileName**：系统生成的唯一文件名（UUID+文件扩展名）
- **savePath**：文件在服务器的完整存储路径
- **fileSize**：文件大小，单位为字节

**错误响应示例（文件为空）**

json  
{  
"success": false,  
"message": "文件上传失败：上传文件不能为空"  
}

<div class="joplin-table-wrapper"><table><tbody><tr><td><p><strong>注意事项</strong></p><ul><li>文件大小限制：10MB，参数在 application-dev.yml 配置文件中定义</li><li>文件存储目录：./uploads，系统会自动创建不存在的目录</li></ul></td></tr></tbody></table></div>

**2\. 阿里云短信服务接口**

**2.1 发送验证码**

**接口基础信息**

- **请求方式**：POST
- **接口URL**：/sms/send-code
- **完整地址**：http://localhost:8080/api/sms/send-code
- **功能描述**：调用阿里云短信服务，向指定手机号发送动态验证码
- **请求头**：Content-Type: application/json

**请求参数**

|     |     |     |     |
| --- | --- | --- | --- |
| 参数名 | 类型  | 必填  | 说明  |
| phone | String | 是   | 接收短信验证码的合法手机号 |

**请求体示例**

json  
{  
"phone": "13800138000"  
}

**成功响应示例**

json  
{  
"code": "200",  
"message": "成功",  
"data": {  
"message": "验证码已发送",  
"bizId": "1234567890",  
"outId": "abc123"  
}  
}

**响应字段说明**

- **code**：响应状态码，200代表成功
- **message**：响应状态描述
- **bizId**：阿里云短信业务ID，用于后续核验排查
- **outId**：外部业务ID，用于业务链路关联

**错误响应示例**

json  
// 手机号为空  
{  
"code": "400",  
"message": "手机号不能为空",  
"data": null  
}  
// 发送失败  
{  
"code": "500",  
"message": "发送失败：连接超时",  
"data": null  
}

<div class="joplin-table-wrapper"><table><tbody><tr><td><p><strong>前置条件</strong></p><ul><li>需配置阿里云密钥：ALIYUN_ACCESS_KEY_ID、ALIYUN_ACCESS_KEY_SECRET</li><li>短信签名名称：速通互联验证码</li><li>短信模板CODE：100001</li></ul></td></tr></tbody></table></div>

**2.2 核验验证码**

**接口基础信息**

- **请求方式**：POST
- **接口URL**：/sms/verify
- **完整地址**：http://localhost:8080/api/sms/verify
- **功能描述**：核验用户提交的短信验证码有效性，完成身份校验
- **请求头**：Content-Type: application/json

**请求参数**

|     |     |     |     |
| --- | --- | --- | --- |
| 参数名 | 类型  | 必填  | 说明  |
| phone | String | 是   | 接收验证码的手机号 |
| code | String | 是   | 用户输入的短信验证码 |
| outId | String | 否   | 发送验证码返回的外部业务ID，用于精准核验 |

**请求体示例**

json  
{  
"phone": "13800138000",  
"code": "123456",  
"outId": "abc123"  
}

**成功响应示例（验证通过）**

json  
{  
"code": "200",  
"message": "成功",  
"data": true  
}

**错误响应示例（参数缺失）**

json  
{  
"code": "400",  
"message": "手机号和验证码不能为空",  
"data": null  
}

<div class="joplin-table-wrapper"><table><tbody><tr><td><p><strong>注意事项</strong></p><ul><li>验证码有效期：默认5分钟，超时自动失效</li><li>outId为可选参数，携带后可提升核验精准度</li></ul></td></tr></tbody></table></div>

**3\. 配置查询接口**

**3.1 应用信息查询**

**接口基础信息**

- **请求方式**：GET
- **接口URL**：/config/app-info
- **完整地址**：http://localhost:8080/api/config/app-info
- **功能描述**：获取当前Spring Boot应用的基础配置信息，便于运维排查
- **请求参数**：无

**成功响应示例**

json  
{  
"code": "200",  
"message": "成功",  
"data": {  
"name": "spring-boot-demo",  
"version": "1.0.0",  
"description": "Spring Boot Demo",  
"port": "8080",  
"contextPath": "/api",  
"applicationName": "workWeek03 开发环境"  
}  
}

**响应字段说明**

- **name**：应用项目名称
- **version**：应用版本号
- **description**：应用功能描述
- **port**：服务启动端口
- **contextPath**：接口上下文路径
- **applicationName**：Spring容器应用名称

**3.2 数据库信息查询**

**接口基础信息**

- **请求方式**：GET
- **接口URL**：/config/db-info
- **完整地址**：http://localhost:8080/api/config/db-info
- **功能描述**：查询数据库基础配置，仅脱敏返回非敏感信息
- **请求参数**：无

**成功响应示例**

json  
{  
"code": "200",  
"message": "成功",  
"data": {  
"host": "localhost"  
}  
}

<div class="joplin-table-wrapper"><table><tbody><tr><td><p><strong>安全说明</strong></p><ul><li>仅返回数据库主机地址，屏蔽用户名、密码等敏感信息</li><li>符合企业敏感信息安全管理规范</li><li>生产环境建议禁用该接口，或添加权限校验机制</li></ul></td></tr></tbody></table></div>

**4\. 通义千问 AI 接口**

**4.1 健康检查**

**接口基础信息**

- **请求方式**：GET
- **接口URL**：/qwen/health
- **完整地址**：http://localhost:8080/api/qwen/health
- **功能描述**：检查通义千问AI服务配置及运行状态
- **请求参数**：无

**成功响应示例**

json  
{  
"status": "ok",  
"model": "qwen-turbo",  
"apiKeyConfigured": true,  
"apiUrl": "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions"  
}

**响应字段说明**

- **status**：AI服务运行状态，ok代表正常
- **model**：当前调用的AI模型名称
- **apiKeyConfigured**：API密钥是否配置完成
- **apiUrl**：AI接口调用地址

**4.2 简单对话**

**接口基础信息**

- **请求方式**：POST
- **接口URL**：/qwen/chat
- **完整地址**：http://localhost:8080/api/qwen/chat
- **功能描述**：与通义千问AI进行单次无上下文对话
- **请求头**：Content-Type: application/json

**请求参数**

|     |     |     |     |
| --- | --- | --- | --- |
| 参数名 | 类型  | 必填  | 说明  |
| text | String | 是   | 用户提问或指令内容 |

**请求体示例**

json  
{  
"text": "你好，请介绍一下你自己"  
}

**成功响应示例**

json  
{  
"success": true,  
"content": "你好！我是通义千问，由阿里巴巴达摩院开发的超大规模语言模型...",  
"model": "qwen-turbo",  
"promptTokens": 15,  
"completionTokens": 128,  
"totalTokens": 143  
}

**响应字段说明**

- **success**：请求是否成功
- **content**：AI返回的对话内容
- **model**：调用的AI模型
- **promptTokens**：输入token消耗量
- **completionTokens**：输出token消耗量
- **totalTokens**：总token消耗量

**错误响应示例（API Key未配置）**

json  
{  
"success": false,  
"error": "API 调用失败：401",  
"errorMessage": "Invalid API key"  
}

**4.3 带系统提示词的对话**

**接口基础信息**

- **请求方式**：POST
- **接口URL**：/qwen/chat-with-system
- **完整地址**：http://localhost:8080/api/qwen/chat-with-system
- **功能描述**：自定义系统提示词，设定AI角色，实现定向对话
- **请求头**：Content-Type: application/json

**请求参数**

|     |     |     |     |     |
| --- | --- | --- | --- | --- |
| 参数名 | 类型  | 必填  | 默认值 | 说明  |
| text | String | 是   | \-  | 用户提问内容 |
| systemPrompt | String | 否   | 你是一个乐于助人的助手 | AI角色设定、对话规则提示 |
| temperature | Number | 否   | 0.7 | 回答创造性参数，取值0-1，数值越大越灵活 |
| maxTokens | Number | 否   | 1024 | AI最大输出字符长度 |

**请求体示例**

json  
{  
"text": "写一首关于春天的诗",  
"systemPrompt": "你是一位才华横溢的诗人，擅长创作古典诗词",  
"temperature": 0.8,  
"maxTokens": 512  
}

**成功响应示例**

json  
{  
"success": true,  
"content": "《春晓》\\n春风拂柳绿，\\n细雨润花香。\\n燕语穿帘幕，\\n莺歌绕画梁。...",  
"model": "qwen-turbo",  
"usage": {  
"prompt_tokens": 25,  
"completion_tokens": 156,  
"total_tokens": 181  
}  
}

**应用场景**

- 角色扮演：客服、教师、医生等专业角色模拟
- 风格写作：诗词、代码、公文、文案等定向创作
- 专业问答：法律、医疗、技术等领域精准解答

**4.4 多轮对话**

**接口基础信息**

- **请求方式**：POST
- **接口URL**：/qwen/conversation
- **完整地址**：http://localhost:8080/api/qwen/conversation
- **功能描述**：携带历史对话记录，实现上下文连贯的多轮对话
- **请求头**：Content-Type: application/json

**请求参数**

|     |     |     |     |
| --- | --- | --- | --- |
| 参数名 | 类型  | 必填  | 说明  |
| messages | Array | 否   | 历史对话记录数组，保留上下文 |
| currentMessage | String | 是   | 当前用户最新提问内容 |
| temperature | Number | 否   | 创造性参数，默认0.7 |

**messages数组格式**

json  
\[  
{"role": "user", "content": "你好"},  
{"role": "assistant", "content": "你好，有什么可以帮你的？"}  
\]

**请求体示例**

json  
{  
"messages": \[  
{"role": "user", "content": "你好"},  
{"role": "assistant", "content": "你好，有什么可以帮你的？"}  
\],  
"currentMessage": "帮我推荐几本好看的小说",  
"temperature": 0.7  
}

**成功响应示例**

json  
{  
"success": true,  
"content": "当然可以！以下是我为你推荐的几本不同类型的小说：\\n\\n1. 《三体》",  
"model": "qwen-turbo",  
"usage": {  
"prompt_tokens": 32,  
"completion_tokens": 200,  
"total_tokens": 232  
}  
}

<div class="joplin-table-wrapper"><table><tbody><tr><td><p><strong>前置条件</strong></p><ul><li>需配置环境变量：DASHSCOPE_API_KEY</li><li>默认调用模型：qwen-turbo，可在配置文件修改</li><li>接口超时时间：30秒</li></ul></td></tr></tbody></table></div>