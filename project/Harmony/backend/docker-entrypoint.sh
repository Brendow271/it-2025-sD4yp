#!/bin/sh
set -e

mkdir -p /app/uploads/images
chown -R appuser:appuser /app/uploads

JAVA_BIN=$(command -v java)
exec su -s /bin/sh appuser -c "$JAVA_BIN -Djava.security.egd=file:/dev/./urandom -jar /app/app.war"
