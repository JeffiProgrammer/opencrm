package com.jeffiprogrammer.opencrm.tenant.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tenant_memberships", uniqueConstraints = @UniqueConstraint(name = "uk_tenant_membership", columnNames = {"tenant_id", "user_id"}))
public class TenantMembership {
    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TenantRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipStatus status;

    @Column(name = "joined_at", nullable = false)
    private Instant joinedAt;

    @Version
    private Long version;

    protected TenantMembership() {
    }

    public TenantMembership(UUID tenantId, UUID userId, TenantRole role) {

        this.id = UUID.randomUUID();
        this.tenantId = tenantId;
        this.userId = userId;
        this.role = role;
        this.status = MembershipStatus.ACTIVE;
        this.joinedAt = Instant.now();
    }

    public void changeRole(TenantRole newRole) {
        this.role = newRole;
    }

    public void suspend() {
        this.status = MembershipStatus.SUSPENDED;
    }

    public void activate() {
        this.status = MembershipStatus.ACTIVE;
    }

    public UUID getId() {
        return id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public UUID getUserId() {
        return userId;
    }

    public TenantRole getRole() {
        return role;
    }

    public MembershipStatus getStatus() {
        return status;
    }
}
