# BessonClient移动端APP  📱
## 帮助文档
### 采用了Kotiln + RecyclerView + Retrokit的自由开源的安卓app, 后端技术栈为springboot + RESTful写法 (详情见studentmanager-for-springboot)

> 任何安卓软件开发者都能加入开发并提交代码，项目使用MIT开源协议。
> 另外参与开发必须切换至develop分支, 官方的新增代码都会提交到develop上, 如果有人提PR官方也会第一时间合并到develop
### 🛠️ 技术栈

| 技术 | 用途 |
|------|------|
| Kotlin | 主要开发语言 |
| Retrofit 2 | 网络请求 |
| Gson | JSON 解析 |
| RecyclerView | 列表展示 |
| Material Design | UI 组件 |
| ViewBinding | 视图绑定（可选） |
---

### 实现功能
- [+] 正在被开发中
---
### 更加商业化
- 虽然这个app是开源的, 我们为了能俘获更多用户的信任决定采用圆角设计, 并打算日后维护保持这种设计思路
- app免费, 用户使用的app都是这样。我们也理解用户对app的看法并根据用户反馈来调整app

### 启动项目
#### 环境要求
- Android Studio(我们最推荐的IDE，或者VSCode+Android SDK和IDEA + Android Development插件)
- SDK 16
- 真机或者模拟器(如果真需要运行app并打包, 但不能上架应用商店甚至无法存储数据, 因为资金不足我们买不起云服务器)
``` kotlin
private const val BASE_URL = "http://192.168.1.18:8080/api/students/"
```

### 如果你遇到了一些问题, 给开发者发邮件 (邮箱: student.feedback@zohomail.cn)