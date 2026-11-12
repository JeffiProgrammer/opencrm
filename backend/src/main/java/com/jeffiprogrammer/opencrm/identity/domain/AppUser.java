package com.jeffiprogrammer.opencrm.identity.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "app_users")
public class AppUser {
    @Id
    private UUID id;

    @Column(name = "identity_subject",
        nullable = false, unique = true)
    private String identitySubject;

    @Column(nullable = false)
    private String email;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    private Long version;

    protected AppUser() {}

    public AppUser(
        String identitySubject,
        String email,
        String displayName) {

        this.id = UUID.randomUUID();
        this.identitySubject = identitySubject;
        this.email = email;
        this.displayName = displayName;
        this.status = UserStatus.ACTIVE;
    }

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }

    public void updateProfile(String name, String email) {
        this.displayName = name;
        this.email = email;
    }

    public void disable() {
        this.status = UserStatus.DISABLED;
    }

    public UUID getId() { return id; }
    public String getIdentitySubject() { return identitySubject; }
    public String getEmail() { return email; }
    public String getDisplayName() { return displayName; }
    public UserStatus getStatus() { return status; }
    public Long getVersion() { return version; }
}
