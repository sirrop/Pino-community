package pino.beans;

import com.branc.pino.annotations.*;
import com.branc.pino.core.annotaion.Internal;
import com.branc.pino.ui.attr.NumberAttribute;
import com.branc.pino.ui.attr.ViewType;
import com.google.common.base.CaseFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ResourceBundle;
import java.util.*;

import static com.branc.pino.core.util.Strings.isNullOrEmpty;
import static pino.util.Utils.findBundle;

@Internal
public class BeenPeer<T> {
    @NotNull
    private final T bean;

    @NotNull
    private final List<PropertyDescriptor> descriptorList = new LinkedList<>();

    private final PropertyChangeSupport changeSupport;

    public void addListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    public <P> void firePropertyChange(String name, P oldValue, P newValue) {
        changeSupport.firePropertyChange(name, oldValue, newValue);
    }

    public BeenPeer(@NotNull T bean) {
        this.bean = Objects.requireNonNull(bean);
        @Nullable ResourceBundle bundle = findBundle(bean.getClass());

        Field[] fields = getAllFields(bean);

        for (Field field : fields) {
            var bind = field.getAnnotation(Bind.class);
            if (bind == null) continue;
            try {
                PropertyDescriptor desc = createDescriptor(bean, field, bind.id(), bundle);
                descriptorList.add(desc);
            } catch (IntrospectionException e) {
                throw new IllegalArgumentException("illegal binding", e);
            }
        }

        changeSupport = new PropertyChangeSupport(bean);
    }

    private static final Field[] EMPTY = new Field[0];

    private static Field[] getAllFields(Object bean) {
        Class<?> klass = bean.getClass();
        Field[] res = EMPTY;
        while (klass != Object.class) {
            Field[] theFields = klass.getDeclaredFields();
            Field[] newArray = new Field[res.length + theFields.length];
            System.arraycopy(res, 0, newArray, 0, res.length);
            System.arraycopy(theFields, 0, newArray, res.length, theFields.length);
            res = newArray;
            klass = klass.getSuperclass();
        }
        return res;
    }

    private static PropertyDescriptor createDescriptor(@NotNull Object bean, @NotNull Field field, @Nullable String id, @Nullable ResourceBundle bundle) throws IntrospectionException {
        Accessor accessor = field.getAnnotation(Accessor.class);
        String getter = "";
        String setter = "";
        if (accessor != null) {
            getter = accessor.getter();
            setter = accessor.setter();
        }
        if (isNullOrEmpty(getter)) {
            getter = defaultGetter(field);
        }
        if (isNullOrEmpty(setter)) {
            setter = "set" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, field.getName());
        }
        PropertyDescriptor res = new PropertyDescriptor(field.getName(), bean.getClass(), getter, setter);

        initAttributes(res, field);

        if (bundle != null) {
            id = isNullOrEmpty(id) ? field.getName() + ".name" : id;
            res.setDisplayName(bundle.getString(id));
        }
        return res;
    }

    private static String defaultGetter(Field field) {
        if (field.getType() == boolean.class || field.getType() == Boolean.class)
            return "is" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, field.getName());
        return "get" + field.getName().toUpperCase();
    }

    private static void initAttributes(PropertyDescriptor desc, Field field) {
        Range range = field.getAnnotation(Range.class);

        if (range != null) {
            PropertyUtils.putValue(desc, NumberAttribute.MIN, range.min());
            PropertyUtils.putValue(desc, NumberAttribute.MAX, range.max());
        }

        Max max = field.getAnnotation(Max.class);
        Min min = field.getAnnotation(Min.class);

        if (max != null) {
            PropertyUtils.putValue(desc, NumberAttribute.MAX, max.value());
        }

        if (min != null) {
            PropertyUtils.putValue(desc, NumberAttribute.MIN, min.value());
        }

        View view = field.getAnnotation(View.class);

        if (view != null) {
            PropertyUtils.putValue(desc, ViewType.KEY, view.value());
        }
    }

    @NotNull
    public T getBean() {
        return bean;
    }

    @NotNull
    public List<PropertyDescriptor> getUnmodifiableProperties() {
        return Collections.unmodifiableList(descriptorList);
    }
}
