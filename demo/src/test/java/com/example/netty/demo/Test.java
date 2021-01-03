package java.com.example.netty.demo;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

/**
 * @author: yanuo
 * @create: 20201224 1:58 PM
 */

public class Test {

    private static final Unsafe THE_UNSAFE;

    static {
        try {
            final PrivilegedExceptionAction<Unsafe> action = new PrivilegedExceptionAction<Unsafe>() {
                public Unsafe run() throws Exception {
                    Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
                    theUnsafe.setAccessible(true);
                    return (Unsafe) theUnsafe.get(null);
                }
            };

            THE_UNSAFE = AccessController.doPrivileged(action);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load unsafe", e);
        }
    }

    public static void main(String[] args) throws NoSuchFieldException {
        int bufferSize = 3;
        Integer.bitCount(bufferSize);

        long value = THE_UNSAFE.objectFieldOffset(Value.class.getDeclaredField("aChar"));
        long value1 = THE_UNSAFE.objectFieldOffset(Value.class.getDeclaredField("bChar"));
        long value2 = THE_UNSAFE.objectFieldOffset(Value.class.getDeclaredField("value"));
        long value3 = THE_UNSAFE.objectFieldOffset(Value.class.getDeclaredField("value2"));
        System.out.println("");
    }

    public class Value {
        public char bChar = 'a';
        public char aChar = 'a';
        public long value = 1;
        public long value2 = 1;
    }
}
