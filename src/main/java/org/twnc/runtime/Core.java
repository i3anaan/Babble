package org.twnc.runtime;

import java.lang.invoke.*;
import java.lang.invoke.MethodHandles.Lookup;

public class Core {
    private static final MethodType INVOKE_TYPE =
        MethodType.methodType(BObject.class, MutableCallSite.class, String.class, Object[].class);
    private static final MethodType CHECK_TYPE =
        MethodType.methodType(boolean.class, Class.class, Object.class);

    /**
     * invokedynamic bootstrap method
     *
     * This method is called by the Java VM when it is unsure what method it has
     * to call. We always tell it to call our special invoker method: at this
     * point, we do not yet know the runtime type of the receiver, and we have
     * no way of getting to it, so we dynamically call that invoker method
     * instead of dynamically calling the right method right away.
     *
     * @param lookup   The current caller's view of the world.
     * @param selector Method name (in our case the mangled method selector).
     * @param type     The (compile-time) type of the method we're looking for.
     *
     * @return a CallSite pointing at our invoker method.
     */
    public static CallSite bootstrap(Lookup lookup, String selector, MethodType type) {
        CallSite cs = new MutableCallSite(type);

        try {
            MethodHandle handle = lookup.findStatic(Core.class, "invoke", Core.INVOKE_TYPE);
            handle = handle.bindTo(cs);
            handle = handle.bindTo(selector);
            handle = handle.asCollector(BObject[].class, type.parameterCount());
            handle = handle.asType(type);

            cs.setTarget(handle);
            return cs;
        } catch (NoSuchMethodException | IllegalAccessException e) {
            // TODO Dress this exception up a little
            throw new RuntimeException(e);
        }
    }

    /**
     * Second stage of the invokedynamic dance. At this point, we do know our
     * arguments, which means we know the type of our receiver. We have enough
     * information to look up the correct method and call it.
     *
     * As an added bonus, we store the result of our search inside the CallSite
     * object so that the JVM can use it to optimize calls if it wants to.
     *
     * The cached method is only valid for values of the same class. If the
     * class changes (check returns false), we have to do the bootstrap/invoke
     * dance again.
     *
     * @param cs       The bootstrapped call site.
     * @param selector The mangled selector we use to find the method.
     * @param args     All arguments to the method (the receiver is the first).
     * @return the result of the call, whatever that is.
     * @throws Throwable as the method we're calling could throw anything.
     */
    public static BObject invoke(MutableCallSite cs, String selector, Object[] args) throws Throwable {
        Class<?> receiverClass = args[0].getClass();

        // Look for a matching method.
        MethodHandle target = MethodHandles.lookup().findVirtual(
            receiverClass,                      // ...on the receiver class
            selector,                           // ...named after the selector
            cs.type().dropParameterTypes(0, 1)  // ... and taking the right amount of BObjects.
        ).asType(cs.type());

        // Look for the check method.
        MethodHandle test = MethodHandles.lookup().findStatic(Core.class, "check", Core.CHECK_TYPE).bindTo(receiverClass);
        test = test.asType(test.type().changeParameterType(0, cs.type().parameterType(0)));

        // Bundle the two together.
        MethodHandle guard = MethodHandles.guardWithTest(test, target, cs.getTarget());

        // Set the guarded, tested, method as the result.
        cs.setTarget(guard);

        // Invoke the bare method right now, as well.
        return (BObject) target.invokeWithArguments(args);
    }

    public static boolean check(Class klass, Object receiver) {
        Class<?> receiverClass = receiver.getClass();
        return receiverClass.equals(klass);
    }
}
