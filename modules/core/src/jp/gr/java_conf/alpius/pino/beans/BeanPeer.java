/*
 * Copyright [2021] [shiro]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.gr.java_conf.alpius.pino.beans;

import com.google.common.base.CaseFormat;
import jp.gr.java_conf.alpius.pino.disposable.Disposable;
import jp.gr.java_conf.alpius.pino.disposable.Disposer;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;

import static jp.gr.java_conf.alpius.pino.util.Strings.isNullOrEmpty;

/**
 * オブジェクトを解析し、そのオブジェクトに含まれる{@link Bind}でアノテートされたフィールドの{@link PropertyDescriptor}のリストを作成します。この解析は、スーパークラスにさかのぼって行われます。
 * <h3>プロパティの情報</h3>
 * <p>
 *     {@link jp.gr.java_conf.alpius.pino.annotation.ResourceBundle}を使用することで、各プロパティの追加情報を外部ファイルに分離することが出来ます。
 *     ResourceBundleを使用して外部ファイルに情報を分離する第一の目的は、異なる言語への対応です。そのため、基本的にユーザーに表示される情報がこの機能の対象となります。
 * </p>
 * <table>
 *     <tr>
 *         <th>キー</th><th>説明</th>
 *     </tr>
 *     <tr>
 *         <td>[propertyName].name</td><td>プロパティのDisplayNameをその値に設定します</td>
 *     </tr>
 * </table>
 *
 * <p>
 *     {@link Max}, {@link Min}, {@link Range}で指定された最小値、最大値は、{@link PropertyUtils#getDoubleElse(PropertyDescriptor, Object, double)}を使用することで取得可能です。
 *     この際、キーは{@link Max#KEY}および{@link Min#KEY}を使用してください。Rangeを使用して範囲を指定した場合もこれらのキーを使用します。
 * </p>
 *
 * <h3>セッターとゲッター</h3>
 * <p>
 *     セッターおよびゲッターの名前は基本的にキャメルケースで指定し、次のフォーマットに従う必要があります。
 * </p>
 * <table>
 *     <tr>
 *         <th>セッター</th> <td>setXXX (XXXはフィールドの名前)</td>
*      </tr>
 *     <tr>
 *         <th rowspan="2">ゲッター</th> <td>getXXX (XXXはフィールドの名前かつフィールドがbooleanでない場合)</td>
 *     </tr>
 *     <tr>
 *         <td>isXXX (XXXはフィールドの名前かつフィールドがbooleanの場合)</td>
 *     </tr>
 * </table>
 * しかし、場合によってはこれらのフォーマットに従うことが難しいことやメソッド名とフィールド名を対応させられないことがあります。その場合には、
 * {@link Accessor}を使用してセッター名およびゲッター名を明示してください。
 * @param <T> Beanの型
 */
public class BeanPeer<T> {
    private final T bean;

    private final List<PropertyDescriptor> descriptorList = new ArrayList<>();

    private final PropertyChangeSupport changeSupport;

    public void addListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void addListener(PropertyChangeListener listener, Disposable parent) {
        addListener(listener);
        Disposer.registerDisposable(parent, () -> removeListener(listener));
    }

    public void removeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    public <P> void firePropertyChange(String name, P oldValue, P newValue) {
        changeSupport.firePropertyChange(name, oldValue, newValue);
    }

    public BeanPeer(T bean) {
        this.bean = Objects.requireNonNull(bean);
        ResourceBundle bundle = Utils.findBundle(bean.getClass());

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

    private static PropertyDescriptor createDescriptor(Object bean, Field field, String id, ResourceBundle bundle) throws IntrospectionException {
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
        if (field.getType() == boolean.class || field.getType() == Boolean.class) {
            return "is" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, field.getName());
        }
        return "get" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, field.getName());
    }

    private static void initAttributes(PropertyDescriptor desc, Field field) {
        Range range = field.getAnnotation(Range.class);

        if (range != null) {
            PropertyUtils.putValue(desc, Min.KEY, range.min());
            PropertyUtils.putValue(desc, Max.KEY, range.max());
        }

        Max max = field.getAnnotation(Max.class);
        Min min = field.getAnnotation(Min.class);

        if (max != null) {
            PropertyUtils.putValue(desc, Max.KEY, max.value());
        }

        if (min != null) {
            PropertyUtils.putValue(desc, Min.KEY, min.value());
        }

        //View view = field.getAnnotation(View.class);
        //
        //if (view != null) {
        //    PropertyUtils.putValue(desc, ViewType.KEY, view.value());
        //}
        //
        //if (Utils.isIntType(desc.getPropertyType())) {
        //    PropertyUtils.putValue(desc, NumberAttribute.PRECISION, DEFAULT_INT_PRECISION);
        //} else {
        //    Precision precision = field.getAnnotation(Precision.class);
        //    if (precision != null) {
        //        PropertyUtils.putValue(desc, NumberAttribute.PRECISION, precision.value());
        //    } else {
        //        PropertyUtils.putValue(desc, NumberAttribute.PRECISION, DEFAULT_FLOATING_PRECISION);
        //    }
        //}
    }

    public T getBean() {
        return bean;
    }

    public List<PropertyDescriptor> getUnmodifiableProperties() {
        return Collections.unmodifiableList(descriptorList);
    }
}
