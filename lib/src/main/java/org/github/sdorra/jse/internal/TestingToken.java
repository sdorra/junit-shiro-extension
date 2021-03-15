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
