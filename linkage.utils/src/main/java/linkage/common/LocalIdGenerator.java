package linkage.common;

import java.util.UUID;

/**
 * Created by hzxuzhonglin on 2015/7/13.
 */
public class LocalIdGenerator {

    /**
     * ���Ψһid
     * @return
     */
    public static String generateId(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
