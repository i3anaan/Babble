package org.twnc;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;

public class Bootstrap {
    /* TODO Move this code to generated .class file */

    public class BObject {
        public BObject invoke(BObject receiver, String selector, BObject... args) {
            Class receiverClass = receiver.getClass();
            Class[] classes = new Class[args.length];

            for (int i = 0; i < args.length; i++) {
                classes[i] = args[i].getClass();
            }

            MethodType mt = MethodType.methodType(BObject.class, classes);

            try {
                MethodHandle mh = MethodHandles.lookup().findVirtual(receiverClass, selector, mt);
                return (BObject)mh.bindTo(receiver).invokeWithArguments(Arrays.asList(args));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("dunno bubs", e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("whoa iae", e);
            } catch (Throwable e) {
                throw new RuntimeException("runtime", e);
            }
        }

        public BTrue __true() {
            return new BTrue();
        }

        public BFalse __false() {
            return new BFalse();
        }

        public BNil __nil() {
            return new BNil();
        }
    }

    /* TODO Port some of these to Babble */

    public abstract class BBool extends BObject {
        public abstract BBool not();
    }

    public class BTrue extends BBool {
        public BBool not() { return new BFalse(); }
    }

    public class BFalse extends BBool {
        public BBool not() { return new BTrue(); }
    }

    public class BNil extends BObject {
    }
}
