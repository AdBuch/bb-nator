FROM node:24.15.0-alpine AS build
WORKDIR /workspace
COPY package.json package-lock.json ./
RUN npm ci --no-audit --no-fund
COPY . .
RUN npm run build

# Unprivileged image: master and workers run as UID 101, listening on 8080.
FROM nginxinc/nginx-unprivileged:1.28-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /workspace/dist/bb-nator/browser/ /usr/share/nginx/html/
EXPOSE 8080
