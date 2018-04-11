package lab2;

import java.util.*;
import java.util.Map.Entry;


class RORtbl {

    private static final Hashtable<RemoteObjectRef, Object> table = new Hashtable<>();


    public static void addObj(RemoteObjectRef ror, Object o) {
        table.put(ror, o);
    }


    public static Object findObj(RemoteObjectRef ror) {
        if (table.containsKey(ror)) {
            return table.get(ror);
        }
        return null;
    }

    public static RemoteObjectRef findROR(Object o) {
        Iterator<Map.Entry<RemoteObjectRef, Object>> it = RORtbl.table.entrySet().iterator();
        Entry<RemoteObjectRef, Object> entry;

        while (it.hasNext()) {
            entry = it.next();
            if (entry.getValue() == o) {
                return entry.getKey();

            }
        }
        return null;
    }

    public static Object findObjByname(String name) {
        Iterator<Map.Entry<RemoteObjectRef, Object>> it = RORtbl.table.entrySet().iterator();
        Entry<RemoteObjectRef, Object> entry;
        while (it.hasNext()) {
            entry = it.next();
            if (entry.getKey().interfaceName.equals(name)) {
                return entry.getValue();

            }
        }
        return null;
    }
}
