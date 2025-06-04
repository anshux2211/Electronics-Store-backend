package com.example.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    private String userId;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(length = 100)
    private String password;

    private String gender;

    @Column(unique = true)
    private String phone;

    @Column(length = 1000)
    private String about;

    private String imageName;

    @ManyToMany(fetch =FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Role> roleList=new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> collection=roleList.stream().map(curr_role->
            new SimpleGrantedAuthority(curr_role.getName())).collect(Collectors.toSet());
        return collection;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
