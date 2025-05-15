package selfit.selfit.global.security.springsecurity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import selfit.selfit.domain.user.entity.User;

import java.util.Collection;
import java.util.List;


public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getAccountId();
    }

    // 계정 만료 확인 ( true 만료X)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠겼는지 확인(true 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호가 만료되었는지 (true 만료 x)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화인지 (true : 활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId(){
        return user.getId();
    }

    public String getEmail(){
        return user.getEmail();
    }
}
