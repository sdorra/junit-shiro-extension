<p align="center">
    <img alt="JUnit Shiro Extension" src="images/title.jpg" width="640" height="275" />
</p>

# JUnit Shiro Extension

Simplify unit tests with JUnit 5 and Apache Shiro.

## Usage

```java
@ExtendWith(ShiroExtension.class)
@SubjectAware(
  value = "trillian", 
  roles = "user", 
  permissions = "one:*"
)
class ShiroExtensionTest {

  @Test
  void shouldHavePreparedSubject() {
    Subject subject = SecurityUtils.getSubject();
    assertThat(subject.getPrincipal()).isEqualTo("trillian");
    assertThat(subject.hasRole("user")).isTrue();
    assertThat(subject.isPermitted("one:dot:one")).isTrue();
  }

}
```

The `SubjectAware` annotation can be placed on classes, nested classes or on methods.
Roles and permissions are merged. If the `value` is of `SubjectAware` is empty, no subject is bound.

For more samples have a look at the [ShiroExtensionTest](lib/src/test/java/org/github/sdorra/jse/ShiroExtensionTest.java).

## Installation

Get the latest stable version from [![Maven Central](https://img.shields.io/maven-central/v/com.github.sdorra/junit-shiro-extension.svg)](https://search.maven.org/search?q=g:com.github.sdorra%20a:junit-shiro-extension)

### Gradle

```groovy
testImplementation 'com.github.sdorra:junit-shiro-extension:x.y.z'
```

### Maven

```xml
<dependency>
  <groupId>com.github.sdorra</groupId>
  <artifactId>junit-shiro-extension</artifactId>
  <version>x.y.z</version>
  <scope>test</scope>
</dependency>
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
