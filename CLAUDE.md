# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build/Lint/Test Commands

### Backend (Java/Spring Boot)

- Build: `./mvnw package -DskipTests`
- Run: `./lifecycle/run.sh` or `SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run`
- Unit Tests: `./mvnw test` or `./lifecycle/unit-test.sh`
- Single Test: `./mvnw test -Dtest=ClassName#methodName`
- Integration Tests: `./mvnw test -P integration` or `./lifecycle/integration-test.sh`
- Code Style: `./mvnw checkstyle:check`

### Frontend (Next.js/TypeScript)

- Dev Server: `cd frontend && pnpm dev`
- Lint: `cd frontend && pnpm lint`
- Lint Fix: `cd frontend && pnpm lint:fix`
- Type Check: `cd frontend && pnpm typecheck`
- Tests: `cd frontend && pnpm test`
- Single Test: `cd frontend && pnpm test -- -t "test name"`

## Code Style Guidelines

### Java
- Google Java Style Guide (enforced via checkstyle)
- Lombok annotations for boilerplate reduction
- Clear class/method organization with proper separation of concerns
- Proper exception handling with custom exceptions
- Entity classes in domain/entity package
- Use Spring dependency injection
- JUnit for testing with descriptive test names

### TypeScript/React
- ESLint with Next.js and TypeScript configurations
- Prefer functional components with hooks
- Import sorting: external libraries first, then internal components
- Use TypeScript interfaces/types for all props and state
- Tailwind CSS for styling
- Proper error boundary usage
- Jest for testing components