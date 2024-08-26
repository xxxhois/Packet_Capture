#### 1. 项目结构

```
PacketCapture/
├── src/
│   └── main/
│       └── java/
│           └── org/
│               └── example/
│                   ├── PacketCaptureGUI.java
│                   ├── PacketCaptureManager.java
│                   └── PacketParser.java
├── pom.xml
├── .gitignore
└── README.md
```

#### 2. 前置条件

- 安装Java Development Kit (JDK) 8或更高版本(本程序使用JDK21开发)。
- 安装IntelliJ IDEA。
- 安装Maven。

#### 3. 配置Maven

在项目根目录下创建一个`pom.xml`文件，并添加以下内容：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>CatchPacket</artifactId>
    <version>1.0-SNAPSHOT</version>
    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.pcap4j</groupId>
            <artifactId>pcap4j-core</artifactId>
            <version>1.8.0</version>
        </dependency>
        <dependency>
            <groupId>org.pcap4j</groupId>
            <artifactId>pcap4j-packetfactory-static</artifactId>
            <version>1.8.0</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.30</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <version>1.7.30</version>
        </dependency>
    </dependencies>
</project>
```

#### 4. 在IntelliJ IDEA中导入项目

1. 打开IntelliJ IDEA。
2. 点击`File` -> `Open`，选择项目根目录`PacketCapture`。
3. IDEA会自动检测到`pom.xml`文件，并提示你导入Maven项目，点击`Import`。
4. 如果IDEA没有自动下载依赖，可以在`pom.xml`文件上右键，选择`Maven` -> `Reload Project`。

#### 5. 运行程序

1. 确保你已经连接到网络，并且有权限访问网络接口。
2. 打开`PacketCaptureGUI.java`，在类名上右键，选择`Run 'PacketCaptureGUI.main()'`。
3. GUI窗口将会弹出，你可以选择网络接口，输入目标IP地址，然后点击“Start Capture”按钮开始捕获数据包。
   ***注意，如果不能完整看到按钮和IP地址输入框，可能是由于窗口大小问题，请横向拉动窗口以完整显示。通常选择WIFI 6 无线网卡进行测试。

#### 6.使用程序

- **选择网络接口**：在下拉菜单中选择要捕获数据包的网络接口。
- **输入目标IP地址**（可选）：在文本框中输入目标IP地址。如果不输入，将捕获所有流量。
- **开始捕获**：点击“Start Capture”按钮开始捕获数据包。
- **停止捕获**：点击“Stop Capture”按钮停止捕获数据包。
- **查看输出**：捕获到的数据包信息将显示在文本区域中，包括源地址、目的地址、源端口、目的端口以及负载数据。