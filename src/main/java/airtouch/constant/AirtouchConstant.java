package airtouch.constant;

/**
 * Interface to mark an Enum that can be converted from Airtouch Version specific to Generic.
 * @param <G> is the non-version specific Enum. Typically lives in <code>airtouch</code> or <code>airtouch.constants</code> package.
 */
public interface AirtouchConstant<G extends Enum> { //  & SpecialEnum<S>
    /**
     * Returns the non-specific Enum that is equivalent to the version specific Enum
     * @return The non-version specific Enum. Typically lives in <code>airtouch</code> or <code>airtouch.constants</code> package.
     */
    public G getGeneric();

}
