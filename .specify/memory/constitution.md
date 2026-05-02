<!--
Sync Impact Report
Version change: template -> 1.0.0
Modified principles:
- Template principle 1 -> I. Type-Safe React Frontend
- Template principle 2 -> II. Layered Spring Boot Backend
- Template principle 3 -> III. Tests Are Required Quality Gates
- Template principle 4 -> IV. Stable Contracts And Validation
- Template principle 5 -> V. Operability, Security, And Maintainability
Added sections:
- Technology Standards
- Delivery Workflow And Reviews
Removed sections:
- None
Templates requiring updates:
- Updated: .specify/templates/plan-template.md
- Updated: .specify/templates/spec-template.md
- Updated: .specify/templates/tasks-template.md
- Checked: .specify/templates/commands/*.md (directory not present)
- Updated: AGENTS.md
Follow-up TODOs:
- None
-->
# booking-manager Constitution

## Core Principles

### I. Type-Safe React Frontend
React frontend code MUST use typed components, explicit state ownership, and predictable data
flow. Components MUST remain focused on rendering and user interaction; data fetching,
authorization decisions, and business rules MUST live in dedicated hooks, services, or shared
domain modules. Forms MUST use schema-backed validation, accessible labels, and deterministic
error states. UI changes MUST preserve responsive behavior and keyboard-accessible workflows.

Rationale: A booking manager frontend is workflow-heavy; type safety, clear component
boundaries, and accessible interactions reduce regressions in repeated operational use.

### II. Layered Spring Boot Backend
Spring Boot backend code MUST keep controllers, services, repositories, domain models, and DTOs
separated by responsibility. Controllers MUST handle transport concerns only; services MUST own
business rules and transaction boundaries; repositories MUST encapsulate persistence access. API
inputs MUST be validated before business logic runs, and persistence changes MUST be represented
through explicit migrations when storage is involved.

Rationale: Layer boundaries make booking rules testable and prevent web, persistence, and
business concerns from becoming coupled.

### III. Tests Are Required Quality Gates
Every behavior change MUST include automated tests at the lowest meaningful level. React changes
MUST include component, hook, or interaction tests for visible behavior and state transitions.
Spring Boot changes MUST include unit tests for business rules and integration or slice tests for
HTTP, persistence, security, or transaction behavior. Bug fixes MUST include a regression test
that fails without the fix. Test omissions require a documented reason in the plan and review.

Rationale: The system manages booking workflows where regressions can corrupt availability,
pricing, customer data, or operational decisions.

### IV. Stable Contracts And Validation
Frontend and backend changes MUST preserve explicit contracts for APIs, payloads, validation
rules, and error formats. Contract changes MUST be documented in the feature plan, reflected in
tests on both sides of the boundary, and treated as breaking unless backward compatibility is
shown. User-facing and API error responses MUST be actionable, consistent, and free of sensitive
implementation details.

Rationale: React and Spring Boot can evolve independently only when contracts are visible,
versioned by intent, and verified.

### V. Operability, Security, And Maintainability
Code MUST be observable, secure by default, and simple enough to maintain. Backend flows MUST log
important domain and failure events with structured context and without secrets or personal data
leakage. Frontend flows MUST expose loading, empty, success, and failure states for asynchronous
work. Dependencies MUST be justified by current need, actively maintained, and compatible with
the existing stack. Complexity that crosses module or service boundaries MUST be documented in
the plan.

Rationale: Booking operations need diagnosable failures, safe data handling, and code that can be
changed without broad rewrites.

## Technology Standards

The primary frontend stack is React with TypeScript. New frontend code MUST prefer function
components, typed props, reusable hooks for non-trivial stateful behavior, and colocated tests
that exercise user-visible outcomes. Styling and component composition MUST follow the existing
project conventions once the application structure is present.

The primary backend stack is Java with Spring Boot. New backend code MUST prefer constructor
injection, Bean Validation for request validation, explicit DTO mapping at API boundaries, and
transaction annotations at service-level boundaries when persistence changes are atomic.

Shared standards:

- Formatting, linting, and static analysis configured for a layer MUST pass before review.
- Public contracts MUST be documented in `contracts/`, OpenAPI, or an equivalent project artifact.
- Configuration MUST be environment-driven; secrets MUST NOT be committed.
- Database changes MUST be repeatable through migrations and covered by rollback or remediation
  notes when rollback is not practical.

## Delivery Workflow And Reviews

Every feature plan MUST include a Constitution Check that states how the change satisfies the
React, Spring Boot, testing, contract, and operability principles. If a principle does not apply,
the plan MUST say why.

Every task list MUST include quality-gate tasks for tests, linting or static analysis, contract
updates when APIs change, and documentation updates when developer or operational behavior
changes.

Reviews MUST block changes that violate this constitution unless the plan documents a temporary
exception, owner, expiration date, and remediation task. Exceptions MUST be rare and MUST NOT
allow known data loss, secret exposure, missing validation on external input, or untested booking
business logic.

## Governance

This constitution supersedes conflicting project practices. Amendments MUST be made through a
documented change to `.specify/memory/constitution.md`, include a Sync Impact Report, and update
dependent templates or runtime guidance in the same change when affected.

Versioning follows semantic versioning:

- MAJOR: Principle removals, incompatible governance changes, or redefinitions that invalidate
  previously compliant work.
- MINOR: New principles, new required sections, or materially expanded quality gates.
- PATCH: Clarifications, wording fixes, and non-semantic refinements.

Compliance MUST be reviewed during planning, task generation, code review, and before release.
Plans and reviews MUST explicitly call out violations, exceptions, and remediation work.

**Version**: 1.0.0 | **Ratified**: 2026-05-02 | **Last Amended**: 2026-05-02
