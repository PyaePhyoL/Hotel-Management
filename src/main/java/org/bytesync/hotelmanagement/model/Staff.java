package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import org.bytesync.hotelmanagement.model.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "staffs")
public class Staff extends Auditable implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(50)")
    private String name;
    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(50)")
    private String email;
    @Column(columnDefinition = "VARCHAR(20)")
    private String phoneNumber;
    private String password;
    @Column(columnDefinition = "VARCHAR(50)")
    private String fatherName;
    @Column(columnDefinition = "VARCHAR(50)")
    private String position;
    @Enumerated(EnumType.STRING)@Column(columnDefinition = "VARCHAR(50)")
    private Role role;

    @Column(unique = true, columnDefinition = "VARCHAR(50)")
    private String nrc;
    private LocalDate birthDate;
    private LocalDate joinDate;
    private String address;

    @Column(columnDefinition = "TEXT")
    private String notes;
    private boolean enabled;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
