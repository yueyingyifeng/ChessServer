# ChessServer - 联机五子棋服务器

这是一个基于 Java 的联机五子棋服务器，使用 WebSocket 实现实时通信。

## 功能特点

- 多房间支持：玩家可以创建和加入不同的游戏房间
- 实时对战：基于 WebSocket 的实时通信
- 悔棋功能：支持玩家请求悔棋，并由对手确认
- 游戏重启：支持游戏结束后重新开始
- 心跳检测：自动检测客户端连接状态
- 聊天功能：支持玩家间的消息交流

## 技术栈

- Java
- WebSocket (Java-WebSocket)
- Gradle
- FastJSON

## 快速开始

### 前提条件

- JDK 8 或更高版本
- Gradle

### 构建与运行

1. 克隆仓库
```bash
git clone https://github.com/yourusername/ChessServer.git
cd ChessServer
```

2. 使用 Gradle 构建项目
```bash
gradle build
```

3. 运行服务器
```bash
java -jar build/libs/ChessServer.jar [IP地址]
```

如果不指定 IP 地址，服务器将默认使用 `192.168.1.161`。

### 服务器命令

服务器运行后，可以在控制台使用以下命令：

- `0`: 显示帮助信息
- `1`: 查看当前玩家列表
- `2`: 查看当前房间列表
- `3`: 查看指定房间的棋盘状态（需要输入房间 ID）
- `-1`: 停止服务器

## 通信协议

服务器使用 JSON 格式进行通信，主要消息类型包括：

- 玩家登录/登出
- 房间创建/加入/离开
- 落子操作
- 悔棋请求
- 游戏重启
- 玩家消息
- 心跳包

## 项目结构

- `org.yyyf.game.entity`: 游戏实体类（玩家、房间、棋盘、棋子）
- `org.yyyf.game.manager`: 管理类（玩家管理、房间管理）
- `org.yyyf.game.tool`: 工具类（日志、状态码）
- `org.yyyf.net`: 网络通信相关类
- `org.yyyf.game`: 游戏核心逻辑

## 贡献指南

欢迎提交 Pull Request 或 Issue 来帮助改进项目。
