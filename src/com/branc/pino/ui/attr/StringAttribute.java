package com.branc.pino.ui.attr;

public enum StringAttribute implements Attribute {
    REGEXP("pino:string-attribute:regexp", String.class);

    private final String key;
    private final Class<?> klass;

    StringAttribute(String key, Class<?> klass) {
        this.key = key;
        this.klass = klass;
    }

    @Override
    public String getAttributeName() {
        return key;
    }

    @Override
    public Class<?> getValueClass() {
        return klass;
    }
}
