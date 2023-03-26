package edu.ssafy.punpun.security.oauth2;

import edu.ssafy.punpun.entity.Child;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
@ToString
public class PrincipalChildDetail implements OAuth2User, UserDetails {
    private Child child;
    private OAuth2Attributes oAuthAttributes;

    public PrincipalChildDetail(Child child) {
        this.child = child;
    }

    public PrincipalChildDetail(Child child, OAuth2Attributes oAuthAttributes) {
        this.child = child;
        this.oAuthAttributes = oAuthAttributes;
    }
    /**
     * UserDetails 구현
     * 비밀번호를 리턴
     */
    @Override
    public String getPassword() {
        return child.getEmail();
    }
    /**
     * UserDetails 구현
     * userName을 반환해준다
     */
    @Override
    public String getUsername() {
        return child.getName();
    }
    /**
     * UserDetails 구현
     * 계정 만료 여부
     *  true : 만료안됨
     *  false : 만료됨
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    /**
     * UserDetails 구현
     * 계정 잠김 여부
     *  true : 잠기지 않음
     *  false : 잠김
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    /**
     * UserDetails 구현
     * 계정 비밀번호 만료 여부
     *  true : 만료 안됨
     *  false : 만료됨
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    /**
     * UserDetails 구현
     * 계정 활성화 여부
     *  true : 활성화됨
     *  false : 활성화 안됨
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
    /**
     * OAuth2User 구현
     * @return
     */
    @Override
    public Map<String, Object> getAttributes() {
        return oAuthAttributes.getAttributes();
    }
    /**
     * UserDetails 구현
     * 해당 유저의 권한목록 리턴
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return child.getRole().toString();
            }
        });
        return collect;
    }
    /**
     * OAuth2User 구현
     * @return
     */
    @Override
    public String getName() {
        return child.getName();
    }
}
