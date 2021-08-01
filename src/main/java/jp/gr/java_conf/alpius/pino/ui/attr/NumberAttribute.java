package jp.gr.java_conf.alpius.pino.ui.attr;

public enum NumberAttribute implements Attribute {
    MIN("number-attribute:min", Double.TYPE),
    MAX("number-attribute:max", Double.TYPE),
    BLOCK_INCREMENT("number-attribute:block-increment", Double.TYPE),
    PRECISION("number-attribute:precision", Integer.TYPE);

    private final String key;
    private final Class<?> valueClass;

    NumberAttribute(String key, Class<?> valueClass) {
        this.key = key;
        this.valueClass = valueClass;
    }

    @Override
    public String getAttributeName() {
        return key;
    }

    @Override
    public Class<?> getValueClass() {
        return valueClass;
    }
}
