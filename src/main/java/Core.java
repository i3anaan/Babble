/**
 * Core code for the Babble runtime system.
 */

import java.lang.invoke.*;
import java.math.BigInteger;
import java.util.function.BinaryOperator;

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
    public static CallSite bootstrap(MethodHandles.Lookup lookup, String selector, MethodType type) {
        CallSite cs = new MutableCallSite(type);

        try {
            MethodHandle handle = lookup.findStatic(Core.class, "invoke", INVOKE_TYPE);
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
        MethodHandle test = MethodHandles.lookup().findStatic(Core.class, "check", CHECK_TYPE).bindTo(receiverClass);
        test = test.asType(test.type().changeParameterType(0, cs.type().parameterType(0)));

        // Bundle the two together.
        MethodHandle guard = MethodHandles.guardWithTest(test, target, cs.getTarget());

        // Set the guarded, tested, method as the result.
        cs.setTarget(guard);

        // Invoke the bare method right now, as well.
        return (BObject) target.invokeWithArguments(args);
    }

    public static boolean check(Class klass, Object receiver) {
        return receiver.getClass() == klass;
    }

    public static class BObject extends Object {
        public BObject _print() {
            System.out.println(this.toString());
            return this;
        }

        public BObject _isNil() {
            return BBool.of(false);
        }

        public BObject _asString() {
            return new BStr(this.toString());
        }

        public BObject _asBool() {
            return BBool.of(true);
        }

        public BObject _asInt() {
            return new BInt(0);
        }

        public BObject _eqeq_(BObject that) {
            return BBool.of(this.equals(that));
        }

        public BObject _bangeq_(BObject that) {
            return BBool.of(!this.equals(that));
        }
    }

    /* TODO Port some of these to Babble */

    public static abstract class BBool extends BObject {
        public static BBool of(final boolean b) {
            return b ? new BTrue() : new BFalse();
        }

        public BObject _assert() {
            assert this instanceof BTrue;
            return this;
        }

        public BObject _asBool() {
            return this;
        }
    }

    public static class BTrue extends BBool {
        public BObject _not() {
            return BBool.of(false);
        }

        public BObject _and_(final BObject that) {
            return that._asBool();
        }

        public BObject _or_(final BObject that) {
            return this._asBool();
        }

        public BObject _asBool() {
            return this;
        }

        public BObject _asInt() {
            return new BInt(1);
        }

        public String toString() {
            return "true";
        }

        public boolean equals(Object that) {
            return that instanceof BTrue;
        }
    }

    public static class BFalse extends BBool {
        public BObject _not() {
            return BBool.of(true);
        }

        public BObject _and_(BObject that) {
            return this._asBool();
        }

        public BObject _or_(BObject that) {
            return that._asBool();
        }

        public String toString() {
            return "false";
        }

        public BObject _asInt() {
            return new BInt(0);
        }

        public boolean equals(Object that) {
            return that instanceof BFalse;
        }
    }

    public static class BNil extends BObject {
        public String toString() {
            return "nil";
        }

        public BObject _isNil() {
            return BBool.of(true);
        }

        public BObject _asBool() {
            return BBool.of(false);
        }
    }

    public static class BInt extends BObject {
        private BigInteger integer;

        public BInt(int i) {
            integer = BigInteger.valueOf(i);
        }

        public BInt(BigInteger bi) {
            integer = bi;
        }

        public BInt(String s) {
            integer = new BigInteger(s);
        }

        public BInt(BInt i) {
            integer = i.getInteger();
        }

        private static BObject op(BObject a, BObject b, BinaryOperator<BigInteger> lambda) {
            BigInteger ai = ((BInt)a._asInt()).getInteger();
            BigInteger bi = ((BInt)b._asInt()).getInteger();
            return new BInt(lambda.apply(ai, bi));
        }

        private static int cmp(BObject a, BObject b) {
            BigInteger ai = ((BInt)a._asInt()).getInteger();
            BigInteger bi = ((BInt)b._asInt()).getInteger();
            return ai.compareTo(bi);
        }

        public BObject _plus_(BObject that)   { return op(this, that, BigInteger::add); }
        public BObject _minus_(BObject that)  { return op(this, that, BigInteger::subtract); }
        public BObject _star_(BObject that)   { return op(this, that, BigInteger::multiply); }
        public BObject _slash_(BObject that)  { return op(this, that, BigInteger::divide); }
        public BObject _mod_(BObject that)    { return op(this, that, BigInteger::mod); }

        public BObject _lt_(BObject that)     { return BBool.of(cmp(this, that) < 0); }
        public BObject _eqeq_(BObject that)   { return BBool.of(cmp(this, that) == 0); }
        public BObject _gt_(BObject that)     { return BBool.of(cmp(this, that) > 0); }
        public BObject _gteq_(BObject that)   { return BBool.of(cmp(this, that) >= 0); }
        public BObject _bangeq_(BObject that) { return BBool.of(cmp(this, that) != 0); }
        public BObject _lteq_(BObject that)   { return BBool.of(cmp(this, that) <= 0); }

        public BObject _negate() { return new BInt(this.getInteger().negate()); }
        public BObject _abs() { return new BInt(this.getInteger().abs()); }

        public BObject _asBool() {
            return BBool.of(false);
        }

        public BInt _asInt() {
            return new BInt(this);
        }

        private BigInteger getInteger() {
            return integer;
        }

        public String toString() {
            return integer.toString();
        }
    }

    public static class BStr extends BObject {
        private String str;

        public BStr(String str) {
            this.str = str;
        }

        public BStr(BStr bstr) {
            this.str = bstr.toString();
        }

        public BObject _asBool() {
            return BBool.of(str.isEmpty());
        }

        public BObject _upper() {
            return new BStr(str.toUpperCase());
        }

        public BObject _lower() {
            return new BStr(str.toLowerCase());
        }

        public BObject _startsWith_(BObject that) {
            return BBool.of(str.startsWith(that.toString()));
        }

        public BObject _endsWith_(BObject that) {
            return BBool.of(str.endsWith(that.toString()));
        }

        public BObject _contains_(BObject that) {
            return BBool.of(str.contains(that.toString()));
        }

        public BObject _replace_with_(BObject search, BObject replace) {
            return new BStr(str.replace(search.toString(), replace.toString()));
        }

        public BObject _comma_(BObject that) {
            return new BStr(str.concat(that.toString()));
        }

        public String toString() {
            return str;
        }

        public boolean equals(Object that) {
            return that instanceof BStr && this.toString().equals(that.toString());
        }
    }

    public static class BSymbol extends BObject {
        private int num;
        private String symbol;

        public BSymbol(String symbol, int num) {
            this.num = num;
            this.symbol = symbol;
        }

        public BObject _asInt() {
            return new BInt(num);
        }

        public String toString() {
            return "#" + symbol;
        }
    }

    public static class BBlock extends BObject {
        public BObject _value() {
            return new BNil();
        }
    }
}
