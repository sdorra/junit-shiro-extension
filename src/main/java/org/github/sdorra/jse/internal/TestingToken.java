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

package org.github.sdorra.jse.internal;

import org.github.sdorra.jse.SubjectAware;
import org.apache.shiro.authc.AuthenticationToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TestingToken implements AuthenticationToken {

  private final String principal;
  private final Collection<String> roles;
  private final Collection<String> permissions;

  public TestingToken(String principal, Collection<String> roles, Collection<String> permissions) {
    this.principal = principal;
    this.roles = roles;
    this.permissions = permissions;
  }

  @Override
  public String getPrincipal() {
    return principal;
  }

  public Collection<String> getRoles() {
    return roles;
  }

  public Collection<String> getPermissions() {
    return permissions;
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  public static TestingToken from(Iterable<SubjectAware> annotations) {
    String principal = null;
    List<String> roles = new ArrayList<>();
    List<String> permissions = new ArrayList<>();

    for (SubjectAware annotation : annotations) {
      if (!annotation.value().isEmpty()) {
        principal = annotation.value();
      }
      roles.addAll(Arrays.asList(annotation.roles()));
      permissions.addAll(Arrays.asList(annotation.permissions()));
    }

    return new TestingToken(principal, roles, permissions);
  }
}
