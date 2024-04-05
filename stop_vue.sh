cd vue

PID=$(pgrep -f 'vue-cli-service')

if [ -n "$PID" ]; then
    echo "Stopping vue-cli-service..."
    kill -SIGTERM $PID
    sleep 5
    if kill -0 $PID 2>/dev/null; then
        echo "vue-cli-service did not stop gracefully, sending SIGKILL..."
        kill -SIGKILL $PID
    fi
else
    echo "vue-cli-service is not running."
fi