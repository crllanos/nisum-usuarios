package cl.nisum.usuarios;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Util {
    private final Gson gsonUtil;

    /**
     * Represents an object in Json format
     *
     * @param o
     * @return String
     */
    public String obj2Json(Object o){
        return gsonUtil.toJson(o);
    }
}