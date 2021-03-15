package org.github.sdorra.jse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@SubjectAware(value = "trillian", roles = "user", permissions = "one:*")
@ExtendWith(ShiroExtension.class)
class ShiroExtensionTest {

  @Test
  void shouldReturnClassPrincipal() {
    Subject subject = SecurityUtils.getSubject();
    assertThat(subject.getPrincipal()).isEqualTo("trillian");
  }

  @Test
  @SubjectAware("dent")
  void shouldReturnMethodPrincipal() {
    Subject subject = SecurityUtils.getSubject();
    assertThat(subject.getPrincipal()).isEqualTo("dent");
  }

  @Test
  @SubjectAware("dent")
  void shouldApplyClassRole() {
    Subject subject = SecurityUtils.getSubject();
    assertThat(subject.hasRole("user")).isTrue();
  }

  @Test
  @SubjectAware(value = "dent", roles = {"captain", "admin"})
  void shouldApplyClassAndMethodRoles() {
    Subject subject = SecurityUtils.getSubject();
    assertThat(subject.hasRole("user")).isTrue();
    assertThat(subject.hasRole("captain")).isTrue();
    assertThat(subject.hasRole("admin")).isTrue();
  }

  @Test
  void shouldApplyClassPermissions() {
    Subject subject = SecurityUtils.getSubject();
    assertThat(subject.isPermitted("one:dot:one")).isTrue();
  }

  @Test
  @SubjectAware(permissions = {"a:b", "a:c"})
  void shouldApplyClassAndMethodPermissions() {
    Subject subject = SecurityUtils.getSubject();
    assertThat(subject.isPermitted("one:dot:one")).isTrue();
    assertThat(subject.isPermitted("a:b")).isTrue();
    assertThat(subject.isPermitted("a:c")).isTrue();
  }
}
