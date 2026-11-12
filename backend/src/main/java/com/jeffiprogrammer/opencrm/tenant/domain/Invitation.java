package com.jeffiprogrammer.opencrm.tenant.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "invitations")
public class Invitation {
    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TenantRole role;

    @Column(name = "token_hash", nullable = false, unique = true)
    private String tokenHash;

    @Column(name = "invited_by", nullable = false)
    private UUID invitedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus status;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "accepted_at")
    private Instant acceptedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected Invitation() {}

    public Invitation(
        UUID tenantId,
        String email,
        TenantRole role,
        String tokenHash,
        UUID invitedBy,
        Instant expiresAt) {

        if (role == TenantRole.OWNER) {
            throw new IllegalArgumentException(
                "OWNER cannot be assigned by invitation"
            );
        }

        this.id = UUID.randomUUID();
        this.tenantId = tenantId;
        this.email = email;
        this.role = role;
        this.tokenHash = tokenHash;
        this.invitedBy = invitedBy;
        this.expiresAt = expiresAt;
        this.status = InvitationStatus.PENDING;
        this.createdAt = Instant.now();
    }

    public void accept(Instant now) {
        if (status != InvitationStatus.PENDING) {
            throw new IllegalStateException(
                "Invitation is no longer pending"
            );
        }

        if (!expiresAt.isAfter(now)) {
            throw new IllegalStateException(
                "Invitation has expired"
            );
        }

        status = InvitationStatus.ACCEPTED;
        acceptedAt = now;
    }

    public void revoke() {
        if (status != InvitationStatus.PENDING) {
            throw new IllegalStateException(
                "Only pending invitations can be revoked"
            );
        }

        status = InvitationStatus.REVOKED;
    }

    public UUID getId() { return id; }
    public UUID getTenantId() { return tenantId; }
    public String getEmail() { return email; }
    public TenantRole getRole() { return role; }
    public String getTokenHash() { return tokenHash; }
    public UUID getInvitedBy() { return invitedBy; }
    public InvitationStatus getStatus() { return status; }
    public Instant getExpiresAt() { return expiresAt; }
    public Instant getAcceptedAt() { return acceptedAt; }
}
