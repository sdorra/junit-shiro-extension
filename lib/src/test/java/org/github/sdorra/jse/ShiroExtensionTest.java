/*
 * The MIT License
 *
 * Copyright (c) 2021, Sebastian Sdorra
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.github.sdorra.jse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Nested;
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

  @Nested
  @SubjectAware(value = "marvin", roles = "thenestedone")
  class NestedInheritance {

    @Test
    void shouldReturnNestedPrincipal() {
      Subject subject = SecurityUtils.getSubject();
      assertThat(subject.getPrincipal()).isEqualTo("marvin");
    }

    @Test
    void shouldApplyRolesFromNestedAndClass() {
      Subject subject = SecurityUtils.getSubject();
      assertThat(subject.hasRole("user")).isTrue();
      assertThat(subject.hasRole("thenestedone")).isTrue();
    }
  }

  @Nested
  class NestedWithoutAnnotation {

    @Test
    void shouldReturnClassPrincipal() {
      Subject subject = SecurityUtils.getSubject();
      assertThat(subject.getPrincipal()).isEqualTo("trillian");
    }
  }
}
