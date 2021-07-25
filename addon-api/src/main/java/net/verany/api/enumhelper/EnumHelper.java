package net.verany.api.enumhelper;

public enum EnumHelper {// Du bist im Wald, wolf fragt wohin? Großmutter und deren Bauch fetzen

    INSTANCE;

    public <T extends IdentifierType<S>, S> T valueOf(S id, T[] values) {
        if (!values[0].getClass().isEnum())
            throw new IllegalArgumentException("Values provided to scan is not an Enum");

        T type = null;

        for (int i = 0; i < values.length && type == null; i++)
            if (values[i].getId().equals(id))
                type = values[i];

        return type;
    }

}
