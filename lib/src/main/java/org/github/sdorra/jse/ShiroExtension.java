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
import java.util.Optional;

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
    appendParent(annotations, context);
    appendMethod(context, annotations);
    return TestingToken.from(annotations);
  }

  private void appendMethod(ExtensionContext context, List<SubjectAware> annotations) {
    SubjectAware methodAnnotation = context.getRequiredTestMethod().getAnnotation(SubjectAware.class);
    if (methodAnnotation != null) {
      annotations.add(methodAnnotation);
    }
  }

  private void appendParent( List<SubjectAware> annotations, ExtensionContext context) {
    Optional<ExtensionContext> parent = context.getParent();
    parent.ifPresent(extensionContext -> appendParent(annotations, extensionContext));
    Optional<Class<?>> testClass = context.getTestClass();
    if (testClass.isPresent()) {
      SubjectAware annotation = testClass.get().getAnnotation(SubjectAware.class);
      if (annotation != null) {
        annotations.add(annotation);
      }
    }
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
