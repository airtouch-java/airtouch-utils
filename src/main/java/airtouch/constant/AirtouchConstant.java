package airtouch.constant;

import java.util.Arrays;

/**
 * Interface to mark an Enum that can be converted from Airtouch Version specific to Generic.
 * @param <G> is the non-version specific Enum. Typically lives in <code>airtouch</code> or <code>airtouch.constants</code> package.
 * @param <S> is the version specific Enum. Typically lives in <code>airtouch.vX.constants</code> package.
 */
public interface AirtouchConstant<G extends Enum, S extends Enum> {
    /**
     * Returns the non-specific Enum that is equivalent to the version specific Enum
     * @return The non-version specific Enum. Typically lives in <code>airtouch</code> or <code>airtouch.constants</code> package.
     */
    public G getGeneric();

    default S getSpecific(G generic) {
        return Arrays.stream(values()).filter(s -> s.name().equalsIgnoreCase(generic.name())).findFirst().orElse(getDefaultValue());
    }

    public S getDefaultValue();

}
