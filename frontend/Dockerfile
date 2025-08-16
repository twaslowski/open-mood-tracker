FROM node:18-alpine

WORKDIR /app

# Install dependencies
COPY package.json pnpm-lock.yaml ./
RUN corepack enable pnpm && pnpm i --frozen-lockfile

# Copy source
COPY . .

ENV NODE_ENV=production
ENV NEXT_TELEMETRY_DISABLED=1

# Valid for both the suggested docker-compose setup as well as the default Helm setup
ENV BACKEND_HOST=http://mood-tracker:8000

RUN pnpm run build

EXPOSE 3000

CMD ["pnpm", "start"]
