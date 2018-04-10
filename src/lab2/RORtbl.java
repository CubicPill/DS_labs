package lab2;

import java.util.*;
import java.util.Map.Entry;

// This is simple. ROR needs a new object key for each remote object (or its skeleton).
// This can be done easily, for example by using a counter.
// We also assume a remote object implements only one interface, which is a remote interface.

class RORtbl {
    // I omit all instance variables. you can use hash table, for example.
    // The table would have a key by ROR.
    static final Hashtable<RemoteObjectRef, Object> table = new Hashtable<>();

    // make a new table.

    // add a remote object to the table.
    // Given an object, you can get its class, hence its remote interface.
    // Using it, you can construct a ROR.
    // The host and port are not used unless it is exported outside.
    // In any way, it is better to have it for uniformity.
    public static void addObj(RemoteObjectRef ror, Object o) {

        table.put(ror, o);
    }

    // given ror, find the corresponding object.
    public static Object findObj(RemoteObjectRef ror) {
        // if you use a hash table this is easy.
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
