# ScipioFX
[![](https://jitpack.io/v/ScipioAM/scipio-fx.svg)](https://jitpack.io/#scipioam/scipio-fx)
[![GitHub Package Version](https://img.shields.io/github/v/tag/scipioam/scipio-fx?label=Github&color=blue)](https://github.com/scipioam/scipio-fx/packages)

### Description
JavaFX`s lightweight integration framework (Java 21+)

### Third-party Components
- (Optional) [ControlsFX](https://github.com/controlsfx/controlsfx)
- (Optional) [MaterialFX](https://github.com/palexdev/MaterialFX)

### Third-party Libraries
- (Optional) [MyBatis](https://mybatis.org/mybatis-3/)

### Usage

##### 1.Add repository to your pom.xml
Using [Jitpack repository](https://jitpack.io/#scipioam/scipio-fx)
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
Or [Github Packages](https://github.com/scipioam/scipio-fx/packages/) (Only support for 2.0.0+)
```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/scipioam/scipio-fx</url>
    </repository>
</repositories>
```

##### 2.Import maven dependency
```xml
<dependency>
    <groupId>com.github.scipioam</groupId>
    <artifactId>scipiofx</artifactId>
    <version>latest</version>
</dependency>
```