import com.example.api.LogService;
import com.example.impl.LogServiceStdImpl;

module impl {
    requires api;
    exports com.example.impl;

    provides LogService with LogServiceStdImpl;
}