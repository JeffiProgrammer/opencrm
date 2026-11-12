
CREATE TABLE app_users (
    id UUID PRIMARY KEY,
    identity_subject VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    display_name VARCHAR(150) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT chk_user_status
        CHECK (status IN ('ACTIVE', 'DISABLED'))
);

CREATE TABLE tenants (
    id UUID PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    slug VARCHAR(100) NOT NULL UNIQUE,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT chk_tenant_status
        CHECK (status IN ('ACTIVE', 'SUSPENDED'))
);

CREATE TABLE tenant_memberships (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    user_id UUID NOT NULL REFERENCES app_users(id),
    role VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    joined_at TIMESTAMPTZ NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT uk_tenant_membership
        UNIQUE (tenant_id, user_id),

    CONSTRAINT chk_membership_role
        CHECK (role IN ('OWNER', 'ADMIN', 'MANAGER', 'SALES', 'VIEWER')),

    CONSTRAINT chk_membership_status
        CHECK (status IN ('ACTIVE', 'SUSPENDED'))
);

CREATE INDEX idx_membership_user
    ON tenant_memberships(user_id);

CREATE TABLE invitations (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    email VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL,
    token_hash VARCHAR(255) NOT NULL UNIQUE,
    invited_by UUID NOT NULL REFERENCES app_users(id),
    status VARCHAR(30) NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    accepted_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT chk_invitation_role
        CHECK (role IN ('ADMIN', 'MANAGER', 'SALES', 'VIEWER')),

    CONSTRAINT chk_invitation_status
        CHECK (status IN ('PENDING', 'ACCEPTED', 'REVOKED', 'EXPIRED'))
);

CREATE INDEX idx_invitation_tenant_email
    ON invitations(tenant_id, email);
