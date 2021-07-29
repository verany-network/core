package net.verany.api.enumhelper;

public enum EnumHelper {

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

    public int getIdFromEnum(Class<?> enumClass, Object enumObject) {
        int toReturn = -1;
        Object[] possibleValues = enumClass.getEnumConstants();
        for (int i = 0; i < possibleValues.length; i++) {
            if (possibleValues[i].equals(enumObject)) {
                toReturn = i;
                break;
            }
        }
        return toReturn;
    }

    public String getNextEnumValue(Class<?> enumClass, Object enumObject) {
        Object[] possibleValues = enumClass.getEnumConstants();
        int enumSize = possibleValues.length - 1;
        int currentId = getIdFromEnum(enumClass, enumObject);
        for (Object possibleValue : possibleValues) {
            int newId = getIdFromEnum(enumClass, possibleValue);
            if (newId == currentId) {
                currentId = newId + 1;
                break;
            }
        }
        if (currentId > enumSize)
            currentId = 0;
        return possibleValues[currentId].toString();
    }

    public String getPreviousEnumValue(Class<?> enumClass, Object enumObject) {
        Object[] possibleValues = enumClass.getEnumConstants();
        int enumSize = possibleValues.length - 1;
        int currentId = getIdFromEnum(enumClass, enumObject);
        for (Object possibleValue : possibleValues) {
            int newId = getIdFromEnum(enumClass, possibleValue);
            if (newId == currentId) {
                currentId = newId - 1;
                break;
            }
        }
        if (currentId < 0)
            currentId = enumSize;
        return possibleValues[currentId].toString();
    }

}
