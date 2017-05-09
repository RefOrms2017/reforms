package com.reforms.orm.dao.proxy;

import static com.reforms.ann.TargetQuery.*;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import com.reforms.ann.TargetFilter;
import com.reforms.ann.TargetQuery;
import com.reforms.orm.OrmDao;
import com.reforms.orm.dao.bobj.IOrmDaoAdapter;

/**
 * Proxy implementations of dao
 * @author evgenie
 */
public class DaoProxy implements InvocationHandler {

    private Object connectionHolder;
    private Class<?> daoInterface;

    public DaoProxy(Object connectionHolder2, Class<?> daoInterface) {
        this.connectionHolder = connectionHolder2;
        this.daoInterface = daoInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        TargetQuery targetQuery = method.getAnnotation(TargetQuery.class);
        if (method.isDefault()) {
            return invokeDefaultMethod(proxy, method, args);
        }
        if (targetQuery != null) {
            if (ST_SELECT == targetQuery.type()) {
                return processSelectQuery(targetQuery, method, args);
            }
            if (ST_UPDATE == targetQuery.type()) {
                return processUpdateQuery(targetQuery, method, args);
            }
            if (ST_INSERT == targetQuery.type()) {
                return processInsertQuery(targetQuery, method, args);
            }
            if (ST_DELETE == targetQuery.type()) {
                return processDeleteQuery(targetQuery, method, args);
            }
        } else {
            if ("toString".equals(method.getName())) {
                return daoInterface.toString();
            }
            if ("equals".equals(method.getName())) {
                return proxy == args[0];
            }
            if ("hashCode".equals(method.getName())) {
                return System.identityHashCode(proxy);
            }
        }
        throw new IllegalStateException("Method '" + method + "' not implemented yet. Class '" + daoInterface + "'");
    }

    /**
     * TODO оптимизация.
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    private Object invokeDefaultMethod(Object proxy, Method method, Object[] args) throws Throwable {
        // Если не получить ссылку на этот лукапер,
        // вызывать метод возможно только для интерфейсов
        // которые объявленные в том же пакете что и вызов метода unreflectSpecial
        Field field = Lookup.class.getDeclaredField("IMPL_LOOKUP");
        field.setAccessible(true);
        Lookup lookup = (Lookup) field.get(null);
        Object result = lookup
                .in(method.getDeclaringClass())
                .unreflectSpecial(method, method.getDeclaringClass())
                .bindTo(proxy)
                .invokeWithArguments(args);
        return result;
    }

    private Object processSelectQuery(TargetQuery targetQuery, Method method, Object[] args) throws Exception {
        IOrmDaoAdapter daoAdapter = OrmDao.createDao(connectionHolder, targetQuery.query());
        configureSelectAdapter(daoAdapter, method, args);
        Class<?> ormType = getOrmClass(targetQuery, method);
        if (Iterable.class.isAssignableFrom(method.getReturnType())) {
            return daoAdapter.loads(ormType);
        }
        return daoAdapter.load(ormType);
    }

    @SuppressWarnings("unchecked")
    private void configureSelectAdapter(IOrmDaoAdapter daoAdapter, Method method, Object[] args) {
        if (args.length == 0) {
            return;
        }
        for (int index = 0; index < args.length; index++) {
            Object argValue = args[index];
            TargetFilter filter = findTargetFilter(index, method);
            if (filter != null) {
                if (filter.bobj()) {
                    daoAdapter.setFilterObject(argValue);
                    continue;
                }
                String filterName = filter.value();
                if (! filterName.isEmpty()) {
                    daoAdapter.addFilterPair(filterName, argValue);
                    continue;
                }
            }
            if (argValue instanceof Map) {
                daoAdapter.addFilterPairs((Map<String, Object>) argValue);
                continue;
            }
            daoAdapter.addSimpleFilterValues(argValue);
        }
    }

    private Object processUpdateQuery(TargetQuery targetQuery, Method method, Object[] args) throws Exception {
        IOrmDaoAdapter daoAdapter = OrmDao.createDao(connectionHolder, targetQuery.query());
        configureUpdateAdapter(daoAdapter, method, args);
        return daoAdapter.update();
    }

    @SuppressWarnings("unchecked")
    private void configureUpdateAdapter(IOrmDaoAdapter daoAdapter, Method method, Object[] args) {
        if (args.length == 0) {
            return;
        }
        for (int index = 0; index < args.length; index++) {
            Object argValue = args[index];
            TargetFilter filter = findTargetFilter(index, method);
            if (filter != null) {
                if (filter.bobj()) {
                    daoAdapter.setUpdateObject(argValue);
                    continue;
                }
                String filterName = filter.value();
                if (! filterName.isEmpty()) {
                    daoAdapter.addUpdatePair(filterName, argValue);
                    continue;
                }
            }
            if (argValue instanceof Map) {
                daoAdapter.addUpdatePairs((Map<String, Object>) argValue);
                continue;
            }
            daoAdapter.addUpdateValue(argValue);
        }
    }

    private Object processInsertQuery(TargetQuery targetQuery, Method method, Object[] args) throws Exception {
        IOrmDaoAdapter daoAdapter = OrmDao.createDao(connectionHolder, targetQuery.query());
        configureInsertAdapter(daoAdapter, method, args);
        daoAdapter.insert();
        return null;
    }

    @SuppressWarnings("unchecked")
    private void configureInsertAdapter(IOrmDaoAdapter daoAdapter, Method method, Object[] args) {
        if (args.length == 0) {
            return;
        }
        for (int index = 0; index < args.length; index++) {
            Object argValue = args[index];
            TargetFilter filter = findTargetFilter(index, method);
            if (filter != null) {
                if (filter.bobj()) {
                    daoAdapter.setInsertObject(argValue);
                    continue;
                }
                String filterName = filter.value();
                if (! filterName.isEmpty()) {
                    daoAdapter.addInsertPair(filterName, argValue);
                    continue;
                }
            }
            if (argValue instanceof Map) {
                daoAdapter.addInsertPairs((Map<String, Object>) argValue);
                continue;
            }
            daoAdapter.addInsertValue(argValue);
        }
    }

    private Object processDeleteQuery(TargetQuery targetQuery, Method method, Object[] args) throws Exception {
        IOrmDaoAdapter daoAdapter = OrmDao.createDao(connectionHolder, targetQuery.query());
        configureSelectAdapter(daoAdapter, method, args);
        return daoAdapter.delete();
    }

    private TargetFilter findTargetFilter(int index, Method method) {
        Annotation[][] paramsAnns = method.getParameterAnnotations();
        if (paramsAnns.length > index) {
            for (Annotation paramAnn : paramsAnns[index]) {
                if (paramAnn instanceof TargetFilter) {
                    return (TargetFilter) paramAnn;
                }
            }
        }
        return null;
    }

    private Class<?> getOrmClass(TargetQuery targetQuery, Method method) {
        Class<?> ormType = targetQuery.orm();
        if (ormType != Object.class) {
            return ormType;
        }
        Class<?> returnType = method.getReturnType();
        if (returnType.isArray()) {
            return returnType.getComponentType();
        }
        return returnType;
    }

}
