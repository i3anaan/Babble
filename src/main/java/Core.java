/**
 * Core code for the Babble runtime system.
 */

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.math.BigInteger;
import java.util.Arrays;

public class Core {
    public static BObject invoke(BObject receiver, String selector, BObject... args) {
        // Collect the *run-time*, dynamic class of the receiver and the args
        Class receiverClass = receiver.getClass();
        Class[] classes = new Class[args.length];
        for (int i = 0; i < args.length; i++) classes[i] = args[i].getClass();

        // Collect that information into a method signature
        MethodType mt = MethodType.methodType(BObject.class, classes);

        try {
            // Try to find a method matching that signature
            MethodHandle mh = MethodHandles.lookup().findVirtual(receiverClass, selector, mt);

            // Invoke it!
            return (BObject) mh.bindTo(receiver).invokeWithArguments(Arrays.asList(args));
        } catch (NoSuchMethodException e) {
            String complaint = "Class '%s' does not define a matching method named '%s'.";
            throw new RuntimeException(String.format(complaint, receiverClass.getSimpleName(), selector), e);
        } catch (Throwable e) {
            // We *have* to catch Throwable: the method we're invoking could throw anything.
            throw new RuntimeException(e);
        }
    }

    public static class BObject extends Object {
        public BObject _true() {
            return new BTrue();
        }

        public BObject _false() {
            return new BFalse();
        }

        public BObject _nil() {
            return new BNil();
        }

        public BObject _print() {
            System.out.println(this.toString());
            return this;
        }

        public BObject _isNil() {
            return new BFalse();
        }

        public BObject _asString() {
            return new BStr(this.toString());
        }

        public BObject _asBool() {
            return new BTrue();
        }

        /**
         * Invoke one of this BObject's methods. The selector must be in
         * mangled form.
         */
        public BObject invoke(String selector, BObject... args) {
            return Core.invoke(this, selector, args);
        }
    }

    /* TODO Port some of these to Babble */

    public static abstract class BBool extends BObject {
        public static BBool of(boolean b) {
            return b ? new BTrue() : new BFalse();
        }
    }

    public static class BTrue extends BBool {
        public BObject _not() { return new BFalse(); }

        public BObject _and_(BTrue t) { return new BTrue(); }
        public BObject _and_(BFalse f) { return new BFalse(); }

        public BObject _or_(BTrue b) { return new BTrue(); }
        public BObject _or_(BFalse b) { return new BTrue(); }

        public BObject _xor_(BTrue t) { return new BFalse(); }
        public BObject _xor_(BFalse t) { return new BTrue(); }

        public BObject _assert() {
            assert true;
            return this;
        }

        public BObject _asBool() {
            return new BTrue();
        }

        public String toString() {
            return "true";
        }
    }

    public static class BFalse extends BBool {
        public BObject _not() { return new BTrue(); }

        public BObject _and_(BTrue t) { return new BFalse(); }
        public BObject _and_(BFalse f) { return new BFalse(); }

        public BObject _or_(BTrue b) { return new BTrue(); }
        public BObject _or_(BFalse b) { return new BTrue(); }

        public BObject _xor_(BTrue t) { return new BTrue(); }
        public BObject _xor_(BFalse t) { return new BFalse(); }

        public BObject _assert() {
            assert false;
            return this;
        }

        public BObject _asBool() {
            return new BFalse();
        }

        public String toString() {
            return "false";
        }
    }

    public static class BNil extends BObject {
        public String toString() {
            return "nil";
        }

        public BObject _isNil() {
            return new BTrue();
        }

        public BObject _asBool() {
            return new BFalse();
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

        public BInt(BInt bi) {
            integer = bi.getInteger();
        }

        public BObject _plus_(BInt that) {
            return new BInt(this.getInteger().add(that.getInteger()));
        }

        public BObject _minus_(BInt that) {
            return new BInt(this.getInteger().subtract(that.getInteger()));
        }

        public BObject _star_(BInt that) {
            return new BInt(this.getInteger().multiply(that.getInteger()));
        }

        public BObject _slash_(BInt that) {
            return new BInt(this.getInteger().divide(that.getInteger()));
        }

        public BObject _negate() {
            return new BInt(this.getInteger().negate());
        }

        public BObject _abs() {
            return new BInt(this.getInteger().abs());
        }

        public BObject _mod(BInt that) {
            return new BInt(this.getInteger().mod(that.getInteger()));
        }

        public BObject _asBool() {
            return BBool.of(integer.equals(BigInteger.ZERO));
        }

        public BigInteger getInteger() {
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

        public BObject _startsWith_(BStr that) {
            return BBool.of(str.startsWith(that.toString()));
        }

        public BObject _endsWith_(BStr that) {
            return BBool.of(str.endsWith(that.toString()));
        }

        public BObject _contains_(BStr that) {
            return BBool.of(str.contains(that.toString()));
        }

        public BObject _replace_with_(BStr search, BStr replace) {
            return new BStr(str.replace(search.toString(), replace.toString()));
        }

        public BObject _comma_(BStr that) {
            return new BStr(str.concat(that.toString()));
        }

        public String toString() {
            return str;
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
