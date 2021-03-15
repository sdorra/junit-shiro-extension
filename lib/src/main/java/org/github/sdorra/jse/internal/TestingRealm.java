package org.github.sdorra.jse.internal;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

public class TestingRealm extends AuthorizingRealm {

  public TestingRealm() {
    super(new AllowAllCredentialsMatcher());
    this.setAuthenticationTokenClass(TestingToken.class);
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    TestingToken testingToken = (TestingToken) token;
    SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo();

    SimplePrincipalCollection principalCollection = new SimplePrincipalCollection();
    principalCollection.add(token.getPrincipal(), "testing");
    principalCollection.add(testingToken, "testing");
    authenticationInfo.setPrincipals(principalCollection);

    return authenticationInfo;
  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    TestingToken token = principals.oneByType(TestingToken.class);
    SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
    authorizationInfo.addRoles(token.getRoles());
    authorizationInfo.addStringPermissions(token.getPermissions());
    return authorizationInfo;
  }
}
