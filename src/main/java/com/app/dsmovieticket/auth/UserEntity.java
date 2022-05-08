package com.app.dsmovieticket.auth;

import com.app.dsmovieticket.common.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
//import javax.validation.constraints.Size;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "tbl_user")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username" , nullable = false , unique = true)
    private String username;

    @Column(name = "password" , nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "first_name" , nullable = false)
    private String firstName;

    @Column(name = "email" , nullable = false)
    private String email;

    @Column(name = "phone" , nullable = false)
//    @Size(min = 3, max = 15)
    private String phone;

    @Column(name = "address" , nullable = true)
    private String address;

    @Column(name = "role")
    private String role = Role.CUSTOMER;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return false;
    }

}