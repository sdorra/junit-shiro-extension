package org.github.sdorra.jse;

import org.github.sdorra.jse.internal.TestingRealm;
import org.github.sdorra.jse.internal.TestingToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.util.ThreadContext;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.ArrayList;
import java.util.List;

public class ShiroExtension implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback, AfterAllCallback {

  @Override
  public void beforeAll(ExtensionContext context) {
    ThreadContext.bind(new DefaultSecurityManager(new TestingRealm()));
  }

  @Override
  public void beforeEach(ExtensionContext context) {
    TestingToken token = createToken(context);
    String principal = token.getPrincipal();
    if (principal != null && !principal.isEmpty()) {
      SecurityUtils.getSubject().login(token);
    }
  }

  private TestingToken createToken(ExtensionContext context) {
    List<SubjectAware> annotations = new ArrayList<>();
    SubjectAware classAnnotation = context.getRequiredTestClass().getAnnotation(SubjectAware.class);
    if (classAnnotation != null) {
      annotations.add(classAnnotation);
    }
    SubjectAware methodAnnotation = context.getRequiredTestMethod().getAnnotation(SubjectAware.class);
    if (methodAnnotation != null) {
      annotations.add(methodAnnotation);
    }
    return TestingToken.from(annotations);
  }

  @Override
  public void afterEach(ExtensionContext context) {
    ThreadContext.unbindSubject();
  }

  @Override
  public void afterAll(ExtensionContext context) {
    ThreadContext.unbindSecurityManager();
  }
}
